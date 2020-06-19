package com.semantica.pocketknife.calls.example;

public class Calculator {
	
	public interface Multiplier {
		public int multiply(int a, int b);
	}

	public static final int MAX = (int) Math.sqrt(Integer.MAX_VALUE);

	private final Multiplier multiplier;

	public Calculator(Multiplier multiplier) {
		super();
		this.multiplier = multiplier;
	}

	public int squared(int a) {
		if (a <= MAX) {
			return multiplier.multiply(a, a);
		} else {
			throw new IllegalArgumentException("Value is too large");
		}
	}

}
