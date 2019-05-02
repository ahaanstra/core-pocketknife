package com.semantica.pocketknife.mock;

import java.util.List;

public class AlternativeStubber<U> {
	private final List<Object> returnValues;
	private final StubType stubType;

	AlternativeStubber(StubType stubType, List<Object> returnValues) {
		super();
		this.stubType = stubType;
		this.returnValues = returnValues;
	}

	public AlternativeStubber<U> whenIntercepted(U dummy) {
		return this;
	}

	public <V> V when(V mock) {
		return mock;
	}

	List<Object> getReturnValues() {
		return this.returnValues;
	}

	StubType getStubType() { return this.stubType; }

}