package com.semantica.pocketknife.calls;

/**
 * The base Calls interface with methods common to both the {@link DefaultCalls}
 * and {@link StrictCalls} interfaces.
 *
 * @author A. Haanstra
 *
 * @param <T>
 */
public interface Calls<T> {

	/**
	 * Register a method call. The method name is inferred from the stack trace.
	 * <b>This method is only supported when T is declared {@link String}</b>, i.e.
	 * when {@code String.class} is used as the methodClass in the constructor for
	 * this Calls instance.
	 *
	 * This method should be invoked from a mock method to register the mock method
	 * call.
	 *
	 * Typical use:
	 *
	 * <pre>
	 * <code>
	 * public class MyMock extends MockedClass implements Mock {
	 *    {@code Calls<String> calls} = CallsFactory.getDefaultCallsUsingStrings();
	 *    ...
	 *    {@literal @}Override
	 *    public int someMockedMethod(Object arg1, Object arg2){
	 *       calls.registerCall(arg1, arg2);
	 *       return 42;
	 *    }
	 *    ...
	 * }
	 * </code>
	 * </pre>
	 *
	 * @param args The arguments that have been used in the call to the method being
	 *             registered (cannot be extracted from the stack trace).
	 */
	public void registerCall(Object... args);

	/**
	 * Register a method call explicitely. Works for instances initialized to both
	 * {@code String.class} or {@code java.lang.reflect.Method.class} as
	 * methodClass.
	 *
	 * Typical use:
	 *
	 * <pre>
	 * <code>
	 * public class MyMock extends MockedClass implements Mock {
	 *    {@code Calls<Method> calls} = CallsFactory.getDefaultCalls();
	 *    ...
	 *    {@literal @}Override
	 *    public int someMockedMethod(Object arg1, Object arg2){
	 *       calls.registerCall(new Object(){}.getClass().getEnclosingMethod(), arg1, arg2);
	 *       return 42;
	 *    }
	 *    ...
	 * }
	 * </code>
	 * </pre>
	 *
	 * @param method The identifier for the method.
	 *
	 * @param args   The arguments that have been used in the call to the method
	 *               being registered.
	 */
	public void registerCall(T method, Object... args);

	/**
	 * Convenience method that won't be necessary for most use cases. Registers a
	 * method call explicitely. Works for instances initialized to both
	 * {@code String.class} or {@code java.lang.reflect.Method.class} as
	 * methodClass.
	 *
	 * @param methodCall The {@link MethodCall} identifier for the method call.
	 */
	public void registerCall(MethodCall<T> methodCall);

	/**
	 * Verifies whether there were no more registered method invocations on the mock
	 * than the ones that have been verified and removed.
	 *
	 * @return True if there were no more method invocations, false otherwise.
	 */
	public boolean verifyNoMoreMethodInvocations();

	/**
	 * Verifies whether there were no more registered method invocations on the mock
	 * than the ones that have been verified and removed.
	 *
	 * @param printStackTrace If true, a stack trace will be printed for the method
	 *                        invocations that have not been verified and removed.
	 * @return True if there were no more method invocations, false otherwise.
	 */
	public boolean verifyNoMoreMethodInvocations(boolean printStackTrace);

	/**
	 * Clears all registered method invocations.
	 */
	public void reset();

	/**
	 * Removes a registered method call from the method calls store without any
	 * verification. In the absence of other changes, invocation causes this
	 * instance to return to the same state as before given call was registered.
	 * This method should only be used internally in this library.
	 *
	 * @param methodCall The method call to remove
	 */
	public void removeCall(MethodCall<T> methodCall);

}
