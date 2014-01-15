package com.xafero.strangectrl.input;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class ControllerPoller extends TimerTask {
    private final Set<Controller> controllers;
    private final long period;
    private final IControllerCallback callback;
    private Timer daemon;

    public ControllerPoller(final Set<Controller> controllers,
            final long period,
            final IControllerCallback callback) {
        this.controllers = Collections.synchronizedSet(new HashSet<Controller>(
                controllers));
        this.period = period;
        this.callback = callback;
    }

    public void start() {
        daemon = new Timer(true);
        daemon.scheduleAtFixedRate(this, period, period);
    }

    @Override
    public void run() {
        synchronized (controllers) {
            System.out.println("RUN poller");
            callback.doPeriodCommands();

            for (final Iterator<Controller> it = controllers.iterator(); it
                    .hasNext();) {
                final Controller controller = it.next();

                if (controller.poll()) {
                    final EventQueue queue = controller.getEventQueue();
                    final Event event = new Event();
                    while (queue.getNextEvent(event)) {
                        callback.onNewEvent(controller, event);
                    }
                } else {

                    // controller is no longer available
                    it.remove();
                    callback.removeController(controller);
                }
            }
        }
    }

    public void updateControllers(final Set<Controller> newControllers) {
        synchronized (controllers) {
            System.out.println("Adding controllers");

            // removing old controllers
            for (final Controller controller : newControllers) {
                callback.removeController(controller);
            }
            controllers.clear();
            controllers.addAll(newControllers);
        }
    }

    public void stop() {
        daemon.cancel();
        daemon.purge();
    }
}