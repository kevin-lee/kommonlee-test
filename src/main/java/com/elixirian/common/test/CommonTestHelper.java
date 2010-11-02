/**
 * 
 */
package com.elixirian.common.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Constructor;

/**
 * This class is to test a private constructor.
 * 
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2010-01-01)
 * @version 0.0.2 (2010-02-23) commented...
 */
public final class CommonTestHelper
{
	public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];
	public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

	private CommonTestHelper()
	{
		throw new IllegalStateException(getClass().getName() + " cannot be instantiated.");
	}

	/**
	 * This method throws IllegalStateException if the target class passed the test.
	 * 
	 * @param <T>
	 *            any type.
	 * @param targetClass
	 *            the target class the constructor of which is to be tested.
	 * @param parameterTypes
	 *            the constructor parameter types.
	 * @param parameters
	 *            the constructor parameter values.
	 * @throws Exception
	 */
	public static <T> void testNotAccessibleConstructor(final Class<T> targetClass, final Class<?>[] parameterTypes,
			final Object[] parameters) throws Exception
	{
		Constructor<T> constructor = null;
		try
		{
			constructor = targetClass.getDeclaredConstructor(parameterTypes);
		}
		catch (SecurityException e)
		{
			throw e;
		}
		catch (NoSuchMethodException e)
		{
			System.err.println("The constuctor with the given parameters: " + arrayToString(parameterTypes)
					+ " does not exist in " + targetClass.getName());
			throw e;
		}
		assertThat("The constuctor with the given parameters: " + arrayToString(parameterTypes) + " does not exist in "
				+ targetClass.getName(), constructor, notNullValue());

		IllegalAccessException illegalAccessException = null;
		try
		{
			constructor.newInstance(parameters);
		}
		catch (IllegalAccessException e)
		{
			illegalAccessException = e;
		}

		if (null == illegalAccessException)
		{
			throw new AssertionError("The selected constructor is accessible.");
		}

		try
		{
			constructor.setAccessible(true);
			constructor.newInstance(parameters);
		}
		catch (IllegalArgumentException e)
		{
			System.err.println("The given constructor parameters: " + arrayToString(parameters)
					+ " do not match with the constructor parameter types: " + arrayToString(parameterTypes));
			throw e;
		}
		catch (InstantiationException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			e = (Exception) e.getCause();
			throw e;
		}
	}

	public static <T> String arrayToString(final T[] array)
	{
		if (null == array || 0 == array.length)
		{
			return "no parameters";
		}

		final StringBuilder stringBuilder = new StringBuilder("[");
		for (T t : array)
		{
			if (t instanceof String)
			{
				stringBuilder.append("\"")
						.append(t)
						.append("\"");
			}
			else
			{
				stringBuilder.append(t.toString());
			}
			stringBuilder.append(", ");
		}
		if (2 < stringBuilder.length())
		{
			stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
		}
		return stringBuilder.append("]")
				.toString();
	}

	public static Class<?>[] classArrayOf(final Class<?>... classes)
	{
		if (null == classes || 0 == classes.length)
		{
			return EMPTY_CLASS_ARRAY;
		}
		return classes;
	}

	public static Object[] objectArrayOf(final Object... objects)
	{
		if (null == objects || 0 == objects.length)
		{
			return EMPTY_OBJECT_ARRAY;
		}
		return objects;
	}
}
