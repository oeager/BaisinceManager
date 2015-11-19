package com.bison.easy.event;

/**
 * Created by oeager on 2015/5/22.
 * email: oeager@foxmail.com
 */
final class Subscription {


    final Object subscriber;
    final SubscriberMethod subscriberMethod;
    final int priority;

    /**
     * Becomes false as soon as {@link EasyEvent#unregister(Object)} is called, which is checked by queued event delivery
     * {@link EasyEvent#invokeSubscriber(Subscription, Object)} (PendingPost)} to prevent race conditions.
     */
    volatile boolean active;

    Subscription(Object subscriber, SubscriberMethod subscriberMethod, int priority) {
        this.subscriber = subscriber;
        this.subscriberMethod = subscriberMethod;
        this.priority = priority;
        active = true;

    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Subscription) {
            Subscription otherSubscription = (Subscription) other;
            return subscriber == otherSubscription.subscriber
                    && subscriberMethod.equals(otherSubscription.subscriberMethod);
        } else {
            return false;
        }
    }



    @Override
    public int hashCode() {
        return subscriber.hashCode() + subscriberMethod.methodString.hashCode();
    }

}
