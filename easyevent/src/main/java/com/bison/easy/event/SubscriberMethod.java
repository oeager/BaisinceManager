package com.bison.easy.event;

import java.lang.reflect.Method;

/**
 * Created by oeager on 2015/5/22.
 */
final class SubscriberMethod {

    final Method method;

    final Class<?> eventType;

    final String action;

    String methodString;

    SubscriberMethod(Method method,Class<?> eventType,String action) {
        this.method = method;
        this.eventType = eventType;
        this.action = action;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof SubscriberMethod) {
            checkMethodString();
            SubscriberMethod otherSubscriberMethod = (SubscriberMethod)other;
            otherSubscriberMethod.checkMethodString();
            // Don't use method.equals because of http://code.google.com/p/android/issues/detail?id=7811#c6
            return methodString.equals(otherSubscriberMethod.methodString)&&EasyEvent.equal(action, otherSubscriberMethod.action);
        } else {
            return false;
        }
    }

    private synchronized void checkMethodString() {
        if (methodString == null) {
            // Method.toString has more overhead, just take relevant parts of the method
            StringBuilder builder = new StringBuilder(64);
            builder.append(method.getDeclaringClass().getName());
            builder.append('#').append(method.getName());
            builder.append('(').append(eventType.getName());
            methodString = builder.toString();
        }
    }

    @Override
    public int hashCode() {
        return method.hashCode();
    }


}
