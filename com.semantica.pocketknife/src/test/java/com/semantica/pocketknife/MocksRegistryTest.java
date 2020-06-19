package com.semantica.pocketknife;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.semantica.pocketknife.calls.Calls;

public class MocksRegistryTest {

    @Test
    public void verifiedMockShouldCauseVerificationToSucceed() {
        Calls<?> calls = Mockito.mock(Calls.class);
        Mock verifiedMock = Mockito.mock(Mock.class);
        Mockito.doReturn(calls).when(verifiedMock).getCalls();
        Mockito.when(calls.verifyNoMoreMethodInvocations(false)).thenReturn(true);

        MocksRegistry mocksRegistry = new MocksRegistry();
        mocksRegistry.registerMock(verifiedMock);
        assert mocksRegistry.verifyNoMoreMethodInvocationsAnywhere();

        Mockito.verify(calls, Mockito.times(1)).verifyNoMoreMethodInvocations(false);
        Mockito.verify(verifiedMock, Mockito.times(1)).getCalls();
        Mockito.verifyNoMoreInteractions(calls, verifiedMock);
    }

    @Test
    public void unverifiedMockShouldCauseVerificationToFail() {
        Calls<?> calls = Mockito.mock(Calls.class);
        Mock unverifiedMock = Mockito.mock(Mock.class);
        Mockito.doReturn(calls).when(unverifiedMock).getCalls();
        Mockito.when(calls.verifyNoMoreMethodInvocations(false)).thenReturn(false);

        MocksRegistry mocksRegistry = new MocksRegistry();
        mocksRegistry.registerMock(unverifiedMock);
        assert mocksRegistry.verifyNoMoreMethodInvocationsAnywhere() == false;

        Mockito.verify(calls, Mockito.times(1)).verifyNoMoreMethodInvocations(false);
        Mockito.verify(unverifiedMock, Mockito.times(1)).getCalls();
        Mockito.verifyNoMoreInteractions(calls, unverifiedMock);
    }

    @Test
    public void providerWithVerifiedMockShouldCauseVerificationToSucceed() {
        Calls<?> calls = Mockito.mock(Calls.class);
        Mock verifiedMock = Mockito.mock(Mock.class);
        Mockito.doReturn(calls).when(verifiedMock).getCalls();
        Mockito.when(calls.verifyNoMoreMethodInvocations(false)).thenReturn(true);

        MocksRegistry mocksRegistry = new MocksRegistry();
        mocksRegistry.registerMock(() -> verifiedMock);
        assert mocksRegistry.verifyNoMoreMethodInvocationsAnywhere();

        Mockito.verify(calls, Mockito.times(1)).verifyNoMoreMethodInvocations(false);
        Mockito.verify(verifiedMock, Mockito.times(1)).getCalls();
        Mockito.verifyNoMoreInteractions(calls, verifiedMock);
    }

    @Test
    public void providerWithUnverifiedMockShouldCauseVerificationToSucceed() {
        Calls<?> calls = Mockito.mock(Calls.class);
        Mock unverifiedMock = Mockito.mock(Mock.class);
        Mockito.doReturn(calls).when(unverifiedMock).getCalls();
        Mockito.when(calls.verifyNoMoreMethodInvocations(false)).thenReturn(false);

        MocksRegistry mocksRegistry = new MocksRegistry();
        mocksRegistry.registerMock(() -> unverifiedMock);
        assert mocksRegistry.verifyNoMoreMethodInvocationsAnywhere() == false;

        Mockito.verify(calls, Mockito.times(1)).verifyNoMoreMethodInvocations(false);
        Mockito.verify(unverifiedMock, Mockito.times(1)).getCalls();
        Mockito.verifyNoMoreInteractions(calls, unverifiedMock);
    }

    @Test
    public void removedUnverifiedMockShouldNotCauseVerificationToFail() {
        Calls<?> calls = Mockito.mock(Calls.class);
        Mock unverifiedMock = Mockito.mock(Mock.class);
        Mockito.doReturn(calls).when(unverifiedMock).getCalls();
        Mockito.when(calls.verifyNoMoreMethodInvocations(false)).thenReturn(false);

        MocksRegistry mocksRegistry = new MocksRegistry();
        mocksRegistry.registerMock(unverifiedMock);
        mocksRegistry.deregisterMock(unverifiedMock);
        assert mocksRegistry.verifyNoMoreMethodInvocationsAnywhere();

        Mockito.verifyNoMoreInteractions(calls, unverifiedMock);
    }

    @Test
    public void removedUnverifiedMockFromProviderShouldNotCauseVerificationToFail() {
        Calls<?> calls = Mockito.mock(Calls.class);
        Mock unverifiedMock = Mockito.mock(Mock.class);
        Mockito.doReturn(calls).when(unverifiedMock).getCalls();
        Mockito.when(calls.verifyNoMoreMethodInvocations(false)).thenReturn(false);

        MocksRegistry mocksRegistry = new MocksRegistry();
        mocksRegistry.registerMock(() -> unverifiedMock);
        mocksRegistry.deregisterMock(unverifiedMock);
        assert mocksRegistry.verifyNoMoreMethodInvocationsAnywhere();

        Mockito.verifyNoMoreInteractions(calls, unverifiedMock);
    }

    @Test
    public void deregisteringUnregisteredMockShouldThrowIllegalStateException() {
        Mock mock = Mockito.mock(Mock.class);

        MocksRegistry mocksRegistry = new MocksRegistry();

        Assertions.assertThrows(IllegalStateException.class, () -> mocksRegistry.deregisterMock(mock));
    }

    @Test
    public void verifiedMocksShouldCauseStaticVerificationToSucceed() {
        Calls<?> calls1 = Mockito.mock(Calls.class);
        Mock verifiedMock1 = Mockito.mock(Mock.class);
        Mockito.doReturn(calls1).when(verifiedMock1).getCalls();
        Mockito.when(calls1.verifyNoMoreMethodInvocations(false)).thenReturn(true);

        Calls<?> calls2 = Mockito.mock(Calls.class);
        Mock verifiedMock2 = Mockito.mock(Mock.class);
        Mockito.doReturn(calls2).when(verifiedMock2).getCalls();
        Mockito.when(calls2.verifyNoMoreMethodInvocations(false)).thenReturn(true);
        assert MocksRegistry.verifyNoMoreMethodInvocations(verifiedMock1, verifiedMock2);

        Mockito.verify(calls1, Mockito.times(1)).verifyNoMoreMethodInvocations(false);
        Mockito.verify(verifiedMock1, Mockito.times(1)).getCalls();
        Mockito.verify(calls2, Mockito.times(1)).verifyNoMoreMethodInvocations(false);
        Mockito.verify(verifiedMock2, Mockito.times(1)).getCalls();
        Mockito.verifyNoMoreInteractions(calls1, calls2, verifiedMock1, verifiedMock2);
    }

    @Test
    public void verifiedMocksShouldCauseVerificationToSucceed() {
        Calls<?> calls1 = Mockito.mock(Calls.class);
        Mock verifiedMock1 = Mockito.mock(Mock.class);
        Mockito.doReturn(calls1).when(verifiedMock1).getCalls();
        Mockito.when(calls1.verifyNoMoreMethodInvocations(false)).thenReturn(true);

        Calls<?> calls2 = Mockito.mock(Calls.class);
        Mock verifiedMock2 = Mockito.mock(Mock.class);
        Mockito.doReturn(calls2).when(verifiedMock2).getCalls();
        Mockito.when(calls2.verifyNoMoreMethodInvocations(false)).thenReturn(true);

        MocksRegistry mocksRegistry = new MocksRegistry(Arrays.asList(verifiedMock1, verifiedMock2));
        assert mocksRegistry.verifyNoMoreMethodInvocationsAnywhere();

        Mockito.verify(calls1, Mockito.times(1)).verifyNoMoreMethodInvocations(false);
        Mockito.verify(verifiedMock1, Mockito.times(1)).getCalls();
        Mockito.verify(calls2, Mockito.times(1)).verifyNoMoreMethodInvocations(false);
        Mockito.verify(verifiedMock2, Mockito.times(1)).getCalls();
        Mockito.verifyNoMoreInteractions(calls1, calls2, verifiedMock1, verifiedMock2);
    }

}
