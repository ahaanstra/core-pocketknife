package com.semantica.pocketknife.mock;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.semantica.pocketknife.Mock;
import com.semantica.pocketknife.MocksRegistry;
import com.semantica.pocketknife.calls.Calls;
import com.semantica.pocketknife.calls.CallsFactory.CallType;
import com.semantica.pocketknife.calls.Invoked;
import com.semantica.pocketknife.util.Assert;

public class InlineMockerTest {

    private static final int INT_TEST_ARGUMENT = 42;
    private static final String DRIVE_RETURN_VALUE = String.format("Received intParameter=%d in implementation.",
            INT_TEST_ARGUMENT);
    private static final String UNSTUBBED_METHOD_RETURN_VALUE = "Unstubbed return value";

    private MockedInterface implementation;

    private class ExpectedException extends Exception {

    }

    @BeforeEach
    public void setup() {
        implementation = new MockedInterfaceImplementation();
    }

    @Test
    public void shouldVerifyMethodInvocationsStrictly() throws Exception {
        InlineMocker mocker = InlineMockers.get(CallType.STRICT);
        MockedInterface carMock = mocker.mock(MockedInterface.class);
        mocker.whenIntercepted(carMock.stubbedMethod(INT_TEST_ARGUMENT)).thenReturn(DRIVE_RETURN_VALUE);

        Assert.actual(carMock.stubbedMethod(INT_TEST_ARGUMENT)).equalsExpected(DRIVE_RETURN_VALUE);

        mocker.assertCalled(carMock).stubbedMethod(INT_TEST_ARGUMENT);
        mocker.assertNoMoreMethodInvocations(carMock);
        mocker.assertNoMoreMethodInvocationsAnywhere();
    }

    @Test
    public void shouldVerifyMethodInvocationsStrictlyWithMatcher() throws Exception {
        InlineMocker mocker = InlineMockers.get(CallType.STRICT);
        MockedInterface carMock = mocker.mock(MockedInterface.class);
        mocker.whenIntercepted(carMock.stubbedMethod(mocker.matchArgWith(Matchers.any(Integer.class), Integer.class)))
                .thenReturn(DRIVE_RETURN_VALUE);

        Assert.actual(carMock.stubbedMethod(INT_TEST_ARGUMENT)).equalsExpected(DRIVE_RETURN_VALUE);

        mocker.assertCalled(carMock).stubbedMethod(INT_TEST_ARGUMENT);
        mocker.assertNoMoreMethodInvocations(carMock);
        mocker.assertNoMoreMethodInvocationsAnywhere();
    }

    @Test
    public void shouldReturnStubWhenArgumentMatchesMatcher() throws Exception {
        InlineMocker mocker = InlineMockers.get(CallType.STRICT);
        MockedInterface carMock = mocker.mock(MockedInterface.class);
        mocker.whenIntercepted(carMock.stubbedMethod(INT_TEST_ARGUMENT)).thenReturn(DRIVE_RETURN_VALUE);

        Assert.actual(carMock.stubbedMethod(INT_TEST_ARGUMENT)).equalsExpected(DRIVE_RETURN_VALUE);

        mocker.assertCalled(carMock)
                .stubbedMethod(mocker.matchArgWith(distance -> distance == INT_TEST_ARGUMENT, int.class));
        mocker.assertNoMoreMethodInvocations(carMock);
        mocker.assertNoMoreMethodInvocationsAnywhere();
    }

    @Test
    public void unverifiedmockShouldCauseVerificationToFail() {
        InlineMocker mocker = InlineMockers.get(CallType.DEFAULT);
        Calls<?> callsMock = mocker.mock(Calls.class);
        Mock unverifiedmockMock = mocker.mock(Mock.class);

        mocker.doReturn(callsMock).when(unverifiedmockMock).getCalls();
        mocker.whenIntercepted(callsMock.verifyNoMoreMethodInvocations(false)).thenReturn(false);

        MocksRegistry mocksRegistry = new MocksRegistry();
        mocksRegistry.registerMock(unverifiedmockMock);
        assert mocksRegistry.verifyNoMoreMethodInvocationsAnywhere() == false;

        mocker.assertCalled(Invoked.ONCE, callsMock).verifyNoMoreMethodInvocations(false);
        mocker.assertCalled(Invoked.ONCE, unverifiedmockMock).getCalls();
        mocker.assertNoMoreMethodInvocations(callsMock, unverifiedmockMock);
    }

    @Test
    public void shouldThrowException() throws Exception {
        InlineMocker mocker = InlineMockers.get(CallType.DEFAULT);
        MockedInterface carMock = mocker.mock(MockedInterface.class);

        mocker.whenIntercepted(carMock.stubbedMethod(INT_TEST_ARGUMENT)).thenThrow(new ExpectedException());
        Assertions.assertThrows(ExpectedException.class, () -> carMock.stubbedMethod(INT_TEST_ARGUMENT));
        mocker.assertCalled(carMock).stubbedMethod(INT_TEST_ARGUMENT);
        mocker.assertNoMoreMethodInvocations(carMock);
        mocker.assertNoMoreMethodInvocationsAnywhere();
    }

    @Test
    public void shouldThrowExceptionWithAlternativeSyntax() throws Exception {
        InlineMocker mocker = InlineMockers.get(CallType.DEFAULT);
        MockedInterface carMock = mocker.mock(MockedInterface.class);

        mocker.doThrow(new ExpectedException()).when(carMock).stubbedMethod(INT_TEST_ARGUMENT);
        Assertions.assertThrows(ExpectedException.class, () -> carMock.stubbedMethod(INT_TEST_ARGUMENT));
        mocker.assertCalled(carMock).stubbedMethod(INT_TEST_ARGUMENT);
        mocker.assertNoMoreMethodInvocations(carMock);
        mocker.assertNoMoreMethodInvocationsAnywhere();
    }

    @Test
    public void shouldThrowExceptionAndThenReturnValue() throws Exception {
        InlineMocker mocker = InlineMockers.get(CallType.DEFAULT);
        MockedInterface carMock = mocker.mock(MockedInterface.class);

        mocker.whenIntercepted(carMock.stubbedMethod(INT_TEST_ARGUMENT)).thenThrow(new ExpectedException()).thenReturn("Return value after throw");
        Assertions.assertThrows(ExpectedException.class, () -> carMock.stubbedMethod(INT_TEST_ARGUMENT));
        Assert.actual(carMock.stubbedMethod(INT_TEST_ARGUMENT)).equalsExpected("Return value after throw");


        mocker.assertCalled(Invoked.TWICE, carMock).stubbedMethod(INT_TEST_ARGUMENT);
        mocker.assertNoMoreMethodInvocations(carMock);
        mocker.assertNoMoreMethodInvocationsAnywhere();
    }

    @Test
    public void shouldReturnValueAndThenThrowException() throws Exception {
        InlineMocker mocker = InlineMockers.get(CallType.DEFAULT);
        MockedInterface carMock = mocker.mock(MockedInterface.class);

        mocker.whenIntercepted(carMock.stubbedMethod(INT_TEST_ARGUMENT)).thenReturn("Return value after throw").thenThrow(new ExpectedException());
        Assert.actual(carMock.stubbedMethod(INT_TEST_ARGUMENT)).equalsExpected("Return value after throw");
        Assertions.assertThrows(ExpectedException.class, () -> carMock.stubbedMethod(INT_TEST_ARGUMENT));

        mocker.assertCalled(Invoked.TWICE, carMock).stubbedMethod(INT_TEST_ARGUMENT);
        mocker.assertNoMoreMethodInvocations(carMock);
        mocker.assertNoMoreMethodInvocationsAnywhere();
    }
}
