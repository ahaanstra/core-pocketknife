package com.semantica.pocketknife.calls.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.semantica.pocketknife.calls.Invoked;
import com.semantica.pocketknife.methodrecorder.MethodRecorder;

public class CalculatorTest {

	@Test
	public void yieldsSquare() {
		ProductCalculatorMock mock = new ProductCalculatorMock();
		Calculator calculator = new Calculator(mock);
		MethodRecorder<ProductCalculatorMock> methodRecorder = new MethodRecorder<>(ProductCalculatorMock.class);

		int squared = calculator.squared(7);
		assert squared == 49;

		assert mock.getCalls().verifyAndRemoveCall(Invoked.ONCE,
				methodRecorder.getMethodCall(methodRecorder.getProxy().multiply(7, 7)));
		assert mock.getCalls().verifyNoMoreMethodInvocations();
	}
	
	@Test
	public void throwsIllegalArgumentExceptionExceptionForTooLargeAFactor() {
		ProductCalculatorMock mock = new ProductCalculatorMock();
		Calculator calculator = new Calculator(mock);

		int tooLargeAFactor = Calculator.MAX + 1;
		Assertions.assertThrows(IllegalArgumentException.class, () -> calculator.squared(tooLargeAFactor));

		assert mock.getCalls().verifyNoMoreMethodInvocations();
	}

}
