package pl.carrifyandroid.Utils;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class EventBus {
    private static final Bus bus = new Bus(ThreadEnforcer.ANY);

    private EventBus() {
    }

    public static Bus get() {
        return bus;
    }
}
