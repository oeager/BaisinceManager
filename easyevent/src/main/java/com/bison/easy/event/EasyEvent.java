package com.bison.easy.event;


import com.developer.bsince.log.GOL;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by oeager on 2015/5/22.
 */
public class EasyEvent {


    private final static String ON_EVENT_METHOD_PREFIX = "onEvent";
    private static final int BRIDGE = 0x40;
    private static final int SYNTHETIC = 0x1000;

    private static final int MODIFIERS_IGNORE = Modifier.ABSTRACT | Modifier.STATIC | BRIDGE | SYNTHETIC;
    //用于存储对应类名的所有Event方法
    private final Map<String, List<SubscriberMethod>> methodCache = new HashMap<>();
    //用于存储对应Event的所有Subscription
    private final Map<Class<?>,CopyOnWriteArrayList<Subscription>> subscriptionsByEvent = new HashMap<>();
    //用于存储订阅者对应的所有Event事件类型集合
    private final Map<Object,List<Class<?>>> subscriberEventTypes = new HashMap<>();


    private final ThreadLocal<PostingThreadState> currentPostingThreadState = new ThreadLocal<PostingThreadState>() {
        @Override
        protected PostingThreadState initialValue() {
            return new PostingThreadState();
        }
    };

    volatile static EasyEvent INSTANCE;

    private EasyEvent (){}
    public static EasyEvent getDefault(){
        if (INSTANCE==null){
            synchronized (EasyEvent.class){
                if(INSTANCE==null){
                    INSTANCE = new EasyEvent();
                }
            }
        }
        return INSTANCE;
    }

    public void register(Object subscriber){
        register(subscriber,0);
    }

    public synchronized void register(Object subscriber,int priority){

        Class<?> subscriberClass = subscriber.getClass();
        List<SubscriberMethod> subscriberMethods = findSubscriberMethods(subscriberClass);

        for (int i =0,size = subscriberMethods.size();i<size;i++){
            subscribe(subscriber,subscriberMethods.get(i),priority);
        }

    }
    private List<SubscriberMethod> findSubscriberMethods(Class<?> subscriberClass) {
        String key = subscriberClass.getName();
        List<SubscriberMethod> subscriberMethods;
        synchronized (methodCache) {
            subscriberMethods = methodCache.get(key);
        }
        if (subscriberMethods != null) {
            return subscriberMethods;
        }
        subscriberMethods = new ArrayList<>();
        Class<?> clazz = subscriberClass;
        HashSet<String> eventTypesFound = new HashSet<>();
        StringBuilder methodKeyBuilder = new StringBuilder();
        while (clazz != null) {
            String name = clazz.getName();
            if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")) {
                // Skip system classes, this just degrades performance
                break;
            }

            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                String methodName = method.getName();
                if (methodName.startsWith(ON_EVENT_METHOD_PREFIX)) {
                    int modifiers = method.getModifiers();
                    if ((modifiers & Modifier.PUBLIC) != 0 && (modifiers & MODIFIERS_IGNORE) == 0) {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        if (parameterTypes.length == 1) {
                            Class<?> eventType = parameterTypes[0];
                            methodKeyBuilder.setLength(0);
                            methodKeyBuilder.append(methodName);
                            methodKeyBuilder.append('>').append(eventType.getName());
                            Action action = method.getAnnotation(Action.class);
                            String actionValue = action==null?null:action.value();
                            String methodKey = methodKeyBuilder.toString();
                            if (eventTypesFound.add(methodKey)) {
                                // Only add if not already found in a sub class
                                subscriberMethods.add(new SubscriberMethod(method, eventType,actionValue));

                            }
                        }
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        if (subscriberMethods.isEmpty()) {
            throw new NullPointerException("Subscriber " + subscriberClass + " has no public methods called "
                    + ON_EVENT_METHOD_PREFIX);
        } else {
            synchronized (methodCache) {
                methodCache.put(key, subscriberMethods);
            }
            return subscriberMethods;
        }
    }

    private void subscribe(Object subscriber, SubscriberMethod subscriberMethod,int priority){

        Class<?> eventType = subscriberMethod.eventType;
        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEvent.get(eventType);
        Subscription newSubscription = new Subscription(subscriber, subscriberMethod, priority);
        if (subscriptions == null) {
            subscriptions = new CopyOnWriteArrayList<>();
            subscriptionsByEvent.put(eventType, subscriptions);
        } else {
            if (subscriptions.contains(newSubscription)) {
                throw new IllegalArgumentException("Subscriber " + subscriber.getClass() + " already registered to event "
                        + eventType);
            }
        }


        int size = subscriptions.size();
        for (int i = 0; i <= size; i++) {
            if (i == size || newSubscription.priority > subscriptions.get(i).priority) {
                subscriptions.add(i, newSubscription);
                break;
            }
        }

        List<Class<?>> subscribedEvents = subscriberEventTypes.get(subscriber);
        if (subscribedEvents == null) {
            subscribedEvents = new ArrayList<>();
            subscriberEventTypes.put(subscriber, subscribedEvents);
        }
        subscribedEvents.add(eventType);
    }

    public void publishEvent(Object event){
        publishEvent(null, event);
    }

    public void publishEvent(String action,Object event){
        PostingThreadState postingState = currentPostingThreadState.get();
        List<Object> eventQueue = postingState.eventQueue;
        eventQueue.add(event);

        if (!postingState.isPosting) {
            postingState.isPosting = true;

            try {
                while (!eventQueue.isEmpty()) {
                    postSingleEvent(eventQueue.remove(0),action);
                }
            } finally {
                postingState.isPosting = false;

            }
        }
    }

    public static boolean initOrNot(){
      synchronized (EasyEvent.class){
          return INSTANCE!=null;
      }
    }

    public void release(){
        methodCache.clear();
        subscriberEventTypes.clear();
        subscriptionsByEvent.clear();
    }



    private void postSingleEvent(Object event,String action) throws Error {
        Class<?> eventClass = event.getClass();
        boolean  subscriptionFound = false;
        CopyOnWriteArrayList<Subscription> subscriptions;
        synchronized (this) {
            subscriptions = subscriptionsByEvent.get(eventClass);
        }
        if (subscriptions != null && !subscriptions.isEmpty()) {
            for (Subscription subscription : subscriptions) {

                if(equal(action,subscription.subscriberMethod.action)){
                    try {
                        invokeSubscriber(subscription,event);
                    } finally {
                        subscriptionFound = true;
                    }
                }

            }

        }
        if (!subscriptionFound) {
                GOL.d("No subscribers registered for event " + eventClass);

        }
    }

    public void unregister(Object subscriber){
        List<Class<?>> subscribedTypes = subscriberEventTypes.get(subscriber);
        if (subscribedTypes != null) {
            for (Class<?> eventType : subscribedTypes) {
                unSubscribeByEventType(subscriber, eventType);
            }
            subscriberEventTypes.remove(subscriber);
        } else {
            GOL.w("Subscriber to unregister was not registered before: " + subscriber.getClass());
        }
    }
    public static boolean equal(String action1,String action2){
        GOL.e("%s == %s",action1,action2);
        if(action1==null)
            return action2==null;

        return action1.equals(action2);
    }
    private void unSubscribeByEventType(Object subscriber, Class<?> eventType) {
        List<Subscription> subscriptions = subscriptionsByEvent.get(eventType);
        if (subscriptions != null) {
            int size = subscriptions.size();
            for (int i = 0; i < size; i++) {
                Subscription subscription = subscriptions.get(i);
                if (subscription.subscriber == subscriber) {
                    subscription.active = false;
                    subscriptions.remove(i);
                    i--;
                    size--;
                }
            }
        }
    }

    void invokeSubscriber(Subscription subscription, Object event) {
        try {
            subscription.subscriberMethod.method.invoke(subscription.subscriber, event);
        } catch (InvocationTargetException e) {
           GOL.e(e,"Invoking subscriber failed");
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unexpected exception", e);
        }
    }


    final static class PostingThreadState {
        final List<Object> eventQueue = new ArrayList<>();
        boolean isPosting;
    }
}
