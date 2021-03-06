package pl.grzeslowski.strangectrl.cmd;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Point;
import java.awt.Rectangle;

import org.junit.Test;
import org.mockito.Mockito;

import com.xafero.strangectrl.awt.DesktopUtils;
import com.xafero.strangectrl.input.InputUtils;

public class MouseMoveCommandTest {
    @Test
    public void move_mouse_y() throws Exception {

        // given
        final DesktopUtils desktopUtils = mock(DesktopUtils.class);
        final Point mousePosition = new Point(30, 60);
        when(desktopUtils.getMousePos(Mockito.any(GraphicsDevice.class)))
                .thenReturn(mousePosition);

        final InputUtils inputUtils = mock(InputUtils.class);
        final int maxMove = 10;
        
        final MouseMoveCommand command = new MouseMoveYCommand(inputUtils,
                maxMove, desktopUtils);
        final GraphicsDevice graphicsDevice = mock(GraphicsDevice.class);
        final GraphicsConfiguration graphicsConfiguration = mock(GraphicsConfiguration.class);

        when(graphicsDevice.getDefaultConfiguration()).thenReturn(
                graphicsConfiguration);
        when(graphicsConfiguration.getBounds()).thenReturn(
                new Rectangle(0, 0, 100, 200));

        // expected
        final Point expected = new Point(mousePosition.x,
                mousePosition.y + 10);

        // when
        command.execute(graphicsDevice, 1.0f);

        // then
        verify(inputUtils).moveMouse(expected);
    }

    @Test
    public void move_mouse_x() throws Exception {

        // given
        final DesktopUtils desktopUtils = mock(DesktopUtils.class);
        final Point mousePosition = new Point(30, 60);
        when(desktopUtils.getMousePos(Mockito.any(GraphicsDevice.class)))
                .thenReturn(mousePosition);
        
        final InputUtils inputUtils = mock(InputUtils.class);
        final int maxMove = 10;
        final MouseMoveCommand command = new MouseMoveXCommand(inputUtils,
                maxMove, desktopUtils);
        final GraphicsDevice graphicsDevice = mock(GraphicsDevice.class);
        final GraphicsConfiguration graphicsConfiguration = mock(GraphicsConfiguration.class);

        when(graphicsDevice.getDefaultConfiguration()).thenReturn(
                graphicsConfiguration);
        when(graphicsConfiguration.getBounds()).thenReturn(
                new Rectangle(0, 0, 100, 200));

        // expected
        final Point expected = new Point(mousePosition.x + 10,
                mousePosition.y);

        // when
        command.execute(graphicsDevice, 1.0f);

        // then
        verify(inputUtils).moveMouse(expected);
    }

    @Test
    public void do_not_move_when_value_is_smaller_tha_delta() throws Exception {

        // given
        final InputUtils inputUtils = mock(InputUtils.class);
        final int maxMove = 10;
        final double delta = 0.7f;
        final MouseMoveCommand command = new MouseMoveYCommand(inputUtils,
                maxMove, delta, mock(DesktopUtils.class));
        final GraphicsDevice graphicsDevice = mock(GraphicsDevice.class);
        final GraphicsConfiguration graphicsConfiguration = mock(GraphicsConfiguration.class);

        when(graphicsDevice.getDefaultConfiguration()).thenReturn(
                graphicsConfiguration);
        when(graphicsConfiguration.getBounds()).thenReturn(
                new Rectangle(0, 0, 100, 200));

        // when
        command.execute(graphicsDevice, 0.6f);

        // then
        verify(inputUtils, never()).moveMouse(Mockito.any(Point.class));
    }
}
