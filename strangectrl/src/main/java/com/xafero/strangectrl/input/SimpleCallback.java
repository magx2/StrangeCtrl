package com.xafero.strangectrl.input;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.GraphicsDevice;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import pl.grzeslowski.strangectrl.cmd.CommandFactory;

import com.xafero.strangectrl.cmd.ICommand;

public class SimpleCallback implements IControllerCallback {
    private static final String RELEASE_POV = "RELEASE_POV";
    private final CommandFactory commandFactory;
    private final GraphicsDevice graphicsDevice;
    private final Set<CommandLastValue> periodExecutionCommands = Collections
            .synchronizedSet(new HashSet<CommandLastValue>());
    private ICommand lastPovCommand;

    public SimpleCallback(final CommandFactory commandFactory,
            final GraphicsDevice graphicsDevice) {
        this.commandFactory = checkNotNull(commandFactory);
        this.graphicsDevice = checkNotNull(graphicsDevice);
    }

    @Override
    public void onNewEvent(final Controller controller, final Event event) {
        checkNotNull(event);

        System.out.println("period size = " + periodExecutionCommands.size());

        final Component component = event.getComponent();
        final String identifier = component.getIdentifier().getName();

        final String configName = transformIdentifier(
                identifier, event.getValue());
        final ICommand command = commandFactory.getCommand(configName);
        if (command != null) {
            double value = event.getValue();

            if ("pov".equalsIgnoreCase(identifier)) {
                value = value == 0.0 ? 0.0 : 1.0;
                lastPovCommand = command;
            }

            // execute command
            command.execute(graphicsDevice, value);

            // add period command
            if (command.isPeriodCommand()) {
                removeCommand(command);

                if (value != 0.0) {
                    periodExecutionCommands.add(new CommandLastValue(
                            value, command, controller));
                }
            }

        } else if (RELEASE_POV.equalsIgnoreCase(configName)) {
            if (lastPovCommand.isPeriodCommand()) {
                removeCommand(lastPovCommand);
            } else {
                lastPovCommand.execute(graphicsDevice, 0.0);
            }
            lastPovCommand = null;
        }
    }

    @Override
    public void doPeriodCommands() {
        for (final CommandLastValue commandLastValue : periodExecutionCommands) {
            commandLastValue.command.execute(graphicsDevice,
                    commandLastValue.value);
        }
    }

    @Override
    public void removeController(final Controller controller) {
        checkNotNull(controller);

        synchronized (periodExecutionCommands) {
            for (final Iterator<CommandLastValue> it = periodExecutionCommands
                    .iterator(); it.hasNext();) {
                final CommandLastValue next = it.next();

                if (controller.equals(next.controller)) {
                    it.remove();
                }
            }
        }
    }

    private void removeCommand(final ICommand command) {
        synchronized (periodExecutionCommands) {
            for (final Iterator<CommandLastValue> it = periodExecutionCommands
                    .iterator(); it.hasNext();) {
                final CommandLastValue next = it.next();

                if (next.command.equals(command)) {
                    command.execute(graphicsDevice, 0.0);
                    it.remove();
                    break;
                }
            }
        }
    }

    private String transformIdentifier(final String identifier,
            final float value) {
        switch (identifier) {
        case "0":
            return "A";
        case "1":
            return "B";
        case "2":
            return "X";
        case "3":
            return "Y";
        case "4":
            return "LB";
        case "5":
            return "RB";
        case "6":
            return "BACK";
        case "7":
            return "START";
        case "8":
            return "LS";
        case "9":
            return "RS";
        case "pov":
            return findPov(value);
        default:
            return identifier;
        }
    }

    private String findPov(final double value) {
        if (value == 0.25) {
            return "NP";
        } else if (value == 0.375) {
            return "NEP";
        } else if (value == 0.5) {
            return "EP";
        } else if (value == 0.625) {
            return "SEP";
        } else if (value == 0.75) {
            return "SP";
        } else if (value == 0.875) {
            return "SWP";
        } else if (value == 1) {
            return "WP";
        } else if (value == 0.125) {
            return "NWP";
        } else if (value == 0) {
            return RELEASE_POV;
        }

        throw new RuntimeException("Cannot find this value in POV : " + value);
    }

    private class CommandLastValue {
        private final double value;
        private final ICommand command;
        private final Controller controller;

        public CommandLastValue(final double value, final ICommand command,
                final Controller controller) {
            this.value = value;
            this.command = command;
            this.controller = controller;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result
                    + (command == null ? 0 : command.hashCode());
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final CommandLastValue other = (CommandLastValue) obj;
            if (!getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (command == null) {
                if (other.command != null) {
                    return false;
                }
            } else if (!command.equals(other.command)) {
                return false;
            }
            return true;
        }

        private SimpleCallback getOuterType() {
            return SimpleCallback.this;
        }

        @Override
        public String toString() {
            return "CommandLastValue [value=" + value + ", command="
                    + command.getClass().getSimpleName()
                    + "]";
        }

    }

    boolean containsCommandsFor(final Controller controller) {
        checkNotNull(controller);

        for (final CommandLastValue commandLastValue : periodExecutionCommands) {
            if (controller.equals(commandLastValue.controller)) {
                return true;
            }
        }

        return false;
    }

}
