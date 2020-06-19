package com.semantica.pocketknife.calls.example;

import java.lang.reflect.Method;

import com.semantica.pocketknife.Mock;
import com.semantica.pocketknife.calls.CallsFactory;
import com.semantica.pocketknife.calls.DefaultCalls;

public class ProductCalculatorMock extends ProductCalculator implements Mock {

	DefaultCalls<Method> calls = CallsFactory.getDefaultCalls();

	@Override
	public int multiply(int a, int b) {
		calls.registerCall(new Object() {
		}.getClass().getEnclosingMethod(), a, b); // register this method call (can be copied)
		return 49; // method stubbing
	}

	@Override
	public DefaultCalls<Method> getCalls() {
		return calls; // provide access to verify calls
	}

	@Override
	public void reset() {
		calls.reset(); // provide access to reset this mock (its recorded calls)
	}

}
