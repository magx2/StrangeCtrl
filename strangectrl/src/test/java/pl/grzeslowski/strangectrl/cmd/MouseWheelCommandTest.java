package pl.grzeslowski.strangectrl.cmd;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.mockito.Mockito;

import com.xafero.strangectrl.input.InputUtils;

public class MouseWheelCommandTest {

    @Test
    public void do_not_execute_when_value_is_lower_or_equals_than_delta()
            throws Exception {

        // given
        final InputUtils inputUtils = mock(InputUtils.class);
        final int maxMove = 100;
        final double delta = 0.5;
        final MouseWheelCommand command = new MouseWheelCommand(inputUtils,
                maxMove, delta);

        final double value = 0.5;

        // when
        command.execute(null, value);

        // then
        verify(inputUtils, never()).mouseWheel(Mockito.any(Integer.class));
    }

    @Test
    public void execute_when_value_is_greater_than_delta() throws Exception {

        // given
        final InputUtils inputUtils = mock(InputUtils.class);
        final int maxMove = 100;
        final double delta = 0.5;
        final MouseWheelCommand command = new MouseWheelCommand(inputUtils,
                maxMove, delta);

        final double value = 0.7;

        // expected
        final int expected = maxMove;

        // when
        command.execute(null, value);

        // then
        verify(inputUtils).mouseWheel(expected);
    }

    @Test
    public void execute_when_value_is_greater_than_delta_minus_value()
            throws Exception {

        // given
        final InputUtils inputUtils = mock(InputUtils.class);
        final int maxMove = 100;
        final double delta = 0.5;
        final MouseWheelCommand command = new MouseWheelCommand(inputUtils,
                maxMove, delta);

        final double value = -0.7;

        // expected
        final int expected = -maxMove;

        // when
        command.execute(null, value);

        // then
        verify(inputUtils).mouseWheel(expected);
    }

    @Test
    public void execute_once_if_second_value_is_lower_than_first()
            throws Exception {

        // given
        final InputUtils inputUtils = mock(InputUtils.class);
        final int maxMove = 100;
        final double delta = 0.5;
        final MouseWheelCommand command = new MouseWheelCommand(inputUtils,
                maxMove, delta);

        final double value1 = 0.7;
        final double value2 = 0.6;

        // expected
        final int expected = maxMove;

        // when
        command.execute(null, value1);
        command.execute(null, value2);

        // then
        verify(inputUtils).mouseWheel(expected);
        verifyNoMoreInteractions(inputUtils);
    }

    @Test
    public void execute_once_if_second_value_is_equals_first() throws Exception {

        // given
        final InputUtils inputUtils = mock(InputUtils.class);
        final int maxMove = 100;
        final double delta = 0.5;
        final MouseWheelCommand command = new MouseWheelCommand(inputUtils,
                maxMove, delta);

        final double value = 0.7;

        // expected
        final int expected = maxMove;

        // when
        command.execute(null, value);

        // then
        verify(inputUtils).mouseWheel(expected);
        verifyNoMoreInteractions(inputUtils);
    }

    @Test
    public void execute_once_if_second_value_is_greater_than_first()
            throws Exception {

        // given
        final InputUtils inputUtils = mock(InputUtils.class);
        final int maxMove = 100;
        final double delta = 0.5;
        final MouseWheelCommand command = new MouseWheelCommand(inputUtils,
                maxMove, delta);

        final double value1 = 0.7;
        final double value2 = 0.8;

        // expected
        final int expected = maxMove;

        // when
        command.execute(null, value1);
        command.execute(null, value2);

        // then
        verify(inputUtils).mouseWheel(expected);
        verifyNoMoreInteractions(inputUtils);
    }

    @Test
    public void execute_twice_if_second_value_is_below_delta() throws Exception {

        // given
        final InputUtils inputUtils = mock(InputUtils.class);
        final int maxMove = 100;
        final double delta = 0.5;
        final MouseWheelCommand command = new MouseWheelCommand(inputUtils,
                maxMove, delta);

        final double value1 = 0.7;
        final double value2 = 0.3;
        final double value3 = 0.8;

        // expected
        final int expected = maxMove;

        // when
        command.execute(null, value1);
        command.execute(null, value2);
        command.execute(null, value3);

        // then
        verify(inputUtils, times(2)).mouseWheel(expected);
        verifyNoMoreInteractions(inputUtils);
    }

    @Test
    public void execute_once_if_value_didnt_get_below_delta() throws Exception {

        // given
        final InputUtils inputUtils = mock(InputUtils.class);
        final int maxMove = 100;
        final double delta = 0.5;
        final MouseWheelCommand command = new MouseWheelCommand(inputUtils,
                maxMove, delta);

        final double value1 = 0.7;
        final double value2 = 0.8;
        final double value3 = 0.8;
        final double value4 = 0.9;
        final double value5 = 1.0;

        // expected
        final int expected = maxMove;

        // when
        command.execute(null, value1);
        command.execute(null, value2);
        command.execute(null, value3);
        command.execute(null, value4);
        command.execute(null, value5);

        // then
        verify(inputUtils).mouseWheel(expected);
        verifyNoMoreInteractions(inputUtils);
    }
}
