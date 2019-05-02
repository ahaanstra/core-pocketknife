package com.semantica.pocketknife.mock;

public interface MockedInterface {
	public String stubbedMethod(int intParameter) throws Exception;

	public String notStubbed() throws Exception;
}