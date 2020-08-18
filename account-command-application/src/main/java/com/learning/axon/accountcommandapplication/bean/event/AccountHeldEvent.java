package com.learning.axon.accountcommandapplication.bean.event;

import com.learning.axon.accountcommandapplication.bean.aggregate.Status;

public class AccountHeldEvent extends BaseEvent<String> {

    public final Status status;

    public AccountHeldEvent(String id, Status status) {
        super(id);
        this.status = status;
    }
}
