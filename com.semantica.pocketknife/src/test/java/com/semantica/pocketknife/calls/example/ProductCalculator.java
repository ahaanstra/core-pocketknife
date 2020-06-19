package com.semantica.pocketknife.calls.example;

import com.semantica.pocketknife.calls.example.Calculator.Multiplier;

public class ProductCalculator implements Multiplier {

	@Override
	public int multiply(int a, int b) {
		return a*b;
	}

}
