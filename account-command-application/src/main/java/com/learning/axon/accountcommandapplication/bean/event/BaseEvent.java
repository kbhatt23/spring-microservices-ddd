package com.learning.axon.accountcommandapplication.bean.event;

public class BaseEvent<T> {

    public final T id;

    public BaseEvent(T id) {
        this.id = id;
    }
}
