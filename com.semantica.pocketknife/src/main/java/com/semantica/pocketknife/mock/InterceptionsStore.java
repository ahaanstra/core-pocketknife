package com.semantica.pocketknife.mock;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import com.semantica.pocketknife.mock.dto.QualifiedMethodCall;

public interface InterceptionsStore {
	public void addInterceptions(QualifiedMethodCall<Method> qualifiedMethodCall, StubType stubType, List<Object> returnValues);

	public Optional<Object> matchExactMethodCallToStoredMatchingMethodCalls(
			QualifiedMethodCall<Method> qualifiedMethodCall) throws Throwable;
}