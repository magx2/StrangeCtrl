package com.xafero.strangectrl.input;

import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.ControllerEvent;
import net.java.games.input.ControllerListener;
import pl.grzeslowski.strangectrl.config.Key;

import com.xafero.strangectrl.cmd.ConfigUtils;
import com.xafero.superloader.NativeLoader;

public class InputUtils {

    private static final String prefix = "VK_";
    private static ControllerEnvironment environment;
    private final Robot robot;
    private final Map<String, Integer> keyMap;
    private final Map<Key, Boolean> pressedKeys = new HashMap<>();
    private final Map<MouseButton, Boolean> pressedMouseButtons = new HashMap<>();

    static {
        NativeLoader.setupNative("jinput-platform-", "-natives-");

        environment = ControllerEnvironment
                .getDefaultEnvironment();
        environment.addControllerListener(new ControllerListener() {

            @Override
            public void controllerRemoved(final ControllerEvent ev) {
                System.out.println("removed");
            }

            @Override
            public void controllerAdded(final ControllerEvent ev) {
                System.out.println("add");
            }
        });
    }

    public static enum MouseButton {
        LEFT(InputEvent.BUTTON1_MASK), RIGHT(InputEvent.BUTTON3_MASK), CENTER(
                InputEvent.BUTTON2_MASK);

        private final int buttonMask;

        private MouseButton(final int buttonMask) {
            this.buttonMask = buttonMask;
        }

        public int getButtonMask() {
            return buttonMask;
        }

    }

    public InputUtils(final Robot robot) {
        this.robot = robot;
        keyMap = ConfigUtils.buildKeyMap(prefix);
    }

    public static Set<Controller> getControllers(final Type... types) {
        final Set<Controller> controllers = new HashSet<Controller>();
        final List<Type> typeList = Arrays.asList(types);
        for (final Controller ctrl : environment.getControllers()) {
            if (typeList.contains(ctrl.getType())) {
                controllers.add(ctrl);
            }
        }
        return controllers;
    }

    public void pressKey(final List<Key> keys) {
        for (final Key key : keys) {
            if (!pressedKeys.containsKey(key) || !pressedKeys.get(key)) {
                robot.keyPress(getCode(key));

                pressedKeys.put(key, Boolean.TRUE);
            }
        }
    }

    public void pressKey(final Key... keys) {
        pressKey(Arrays.asList(keys));
    }

    private int getCode(final Key key) {
        return keyMap.get(key.getKey().toLowerCase(Locale.US));
    }

    public void releaseKey(final List<Key> keys) {
        for (final Key key : keys) {
            robot.keyRelease(getCode(key));

            pressedKeys.put(key, Boolean.FALSE);
        }
    }

    public void releaseKey(final Key... keys) {
        releaseKey(Arrays.asList(keys));
    }

    public void pressKeyCombo(final List<Key> keys) {
        final Deque<Key> keyCodes = new ArrayDeque<>();
        for (final Key key : keys) {
            robot.keyPress(getCode(key));
            keyCodes.push(key);
        }
        for (final Key key : keyCodes) {
            robot.keyRelease(getCode(key));
        }
    }

    public void pressKeyCombo(final Key... keys) {
        pressKeyCombo(Arrays.asList(keys));
    }

    public void moveMouse(final Point point) {
        robot.mouseMove(point.x, point.y);
    }

    public void mousePress(final MouseButton button) {
        if (!pressedMouseButtons.containsKey(button)
                || !pressedMouseButtons.get(button)) {
            robot.mousePress(button.buttonMask);

            pressedMouseButtons.put(button, Boolean.TRUE);
        }
    }

    public void mousePressLeft() {
        mousePress(MouseButton.LEFT);
    }

    public void mousePressRight() {
        mousePress(MouseButton.RIGHT);
    }

    public void mousePressCenter() {
        mousePress(MouseButton.CENTER);
    }

    public void mouseRelease(final MouseButton button) {
        robot.mouseRelease(button.buttonMask);

        pressedMouseButtons.put(button, Boolean.FALSE);
    }

    public void mouseReleaseLeft() {
        mouseRelease(MouseButton.LEFT);
    }

    public void mouseReleaseRight() {
        mouseRelease(MouseButton.RIGHT);
    }

    public void mouseReleaseCenter() {
        mouseRelease(MouseButton.CENTER);
    }

    public void mouseWheel(final int value) {
        robot.mouseWheel(value);
    }
}