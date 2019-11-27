package pl.carrifyandroid.Utils;


import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import lombok.Getter;

public class EventBus {

    @Getter
    private static final Bus bus = new Bus(ThreadEnforcer.ANY);

}
