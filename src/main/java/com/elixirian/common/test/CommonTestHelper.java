/**
 * 
 */
package com.elixirian.common.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Constructor;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2010-01-01)
 */
public final class CommonTestHelper
{
	public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];
	public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

	private CommonTestHelper()
	{
		throw new IllegalStateException(getClass().getName() + " cannot be instantiated.");
	}

	public static <T> void testNotAccessibleConstructor(Class<T> targetClass, Class<?>[] parameterTypes, Object[] parameters)
			throws Exception
	{
		Constructor<T> constructor = null;
		try
		{
			constructor = targetClass.getDeclaredConstructor(parameterTypes);
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
			throw e;
		}
		catch (NoSuchMethodException e)
		{
			System.err.println("The constuctor with the given parameters: " + arrayToString(parameterTypes) + " does not exist in "
					+ targetClass.getName());
			e.printStackTrace();
			throw e;
		}
		assertThat("The constuctor with the given parameters: " + arrayToString(parameterTypes) + " does not exist in "
				+ targetClass.getName(), constructor, notNullValue());
		assertFalse(constructor.isAccessible());
		IllegalAccessException illegalAccessException = null;
		try
		{
			constructor.newInstance(parameters);
		}
		catch (IllegalAccessException e)
		{
			illegalAccessException = e;
		}
		assertThat(illegalAccessException, is(IllegalAccessException.class));

		constructor.setAccessible(true);
		try
		{
			constructor.newInstance(parameters);
		}
		catch (IllegalArgumentException e)
		{
			System.err.println("The given constructor parameters: " + arrayToString(parameters)
					+ " do not match with the constructor parameter types: " + arrayToString(parameterTypes));
			e.printStackTrace();
			throw e;
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
			throw e;
		}
		catch (Exception e)
		{
			e = (Exception) e.getCause();
			e.printStackTrace();
			throw e;
		}
	}

	public static <T> String arrayToString(T[] array)
	{
		if (null == array || 0 == array.length)
		{
			return "no parameters";
		}

		StringBuilder stringBuilder = new StringBuilder("[");
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

	public static Class<?>[] classArrayOf(Class<?>... classes)
	{
		if (null == classes || 0 == classes.length)
		{
			return EMPTY_CLASS_ARRAY;
		}
		return classes;
	}

	public static Object[] objectArrayOf(Object... objects)
	{
		if (null == objects || 0 == objects.length)
		{
			return EMPTY_OBJECT_ARRAY;
		}
		return objects;
	}
}
