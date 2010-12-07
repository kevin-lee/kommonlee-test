/**
 * 
 */
package org.elixirian.common.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2010-02-03)
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

	public enum Accessibility
	{
		PRIVATE(Modifier.PRIVATE, "private"), PACKAGE_PRIVATE(0, "package-private"), PROTECTED(Modifier.PROTECTED,
				"protected"), PUBLIC(Modifier.PUBLIC, "public");

		private final int modifier;
		private final String name;

		private Accessibility(final int modifier, final String name)
		{
			this.modifier = modifier;
			this.name = name;
		}

		public int modifier()
		{
			return modifier;
		}

		public String getName()
		{
			return name;
		}

		public static Accessibility accessibilityOf(final int mod)
		{
			for (final Accessibility accessibility : values())
			{
				if (accessibility.modifier == mod)
				{
					return accessibility;
				}
			}
			return null;
		}
	}

	private static Accessibility getAccessibility(int mod)
	{
		final Accessibility accessibility = Accessibility.accessibilityOf(mod);
		if (null == accessibility)
		{
			return Accessibility.PACKAGE_PRIVATE;
		}
		return accessibility;
	}

	/**
	 * This method throws IllegalStateException if the target class passed the test.
	 * 
	 * @param <T>
	 *            any type.
	 * @param targetClass
	 *            the target class the constructor of which is to be tested.
	 * @param tester
	 *            The object testing the target class.
	 * @param expectedAccessibility
	 *            expected accessibility.
	 * @param parameterTypes
	 *            the constructor parameter types.
	 * @param parameters
	 *            the constructor parameter values.
	 * @throws Exception
	 */
	public static <T> void testNotAccessibleConstructor(final Class<T> targetClass, final Object tester,
			final Accessibility expectedAccessibility, final Class<?>[] parameterTypes, final Object[] parameters)
			throws Exception
	{
		testNotAccessibleConstructor(targetClass, tester, expectedAccessibility, false, parameterTypes, parameters);
	}

	/**
	 * This method throws IllegalStateException if the target class passed the test.
	 * 
	 * @param <T>
	 *            any type.
	 * @param targetClass
	 *            the target class the constructor of which is to be tested.
	 * @param tester
	 *            The object testing the target class.
	 * @param expectedAccessibility
	 *            expected accessibility.
	 * @param forceAccessibility
	 * @param parameterTypes
	 *            the constructor parameter types.
	 * @param parameters
	 *            the constructor parameter values.
	 * @throws Exception
	 */
	public static <T> void testNotAccessibleConstructor(final Class<T> targetClass, final Object tester,
			final Accessibility expectedAccessibility, final boolean forceAccessibility,
			final Class<?>[] parameterTypes, final Object[] parameters) throws Exception
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
				+ targetClass.getName(), constructor, is(notNullValue()));

		final Accessibility accessibility = getAccessibility(constructor.getModifiers());
		assertThat("Accessibility of the constructor does not match with the expected one.\n[expected: "
				+ expectedAccessibility.getName() + "][actual: " + accessibility.getName() + "]", accessibility,
				is(equalTo(expectedAccessibility)));

		// if (!Accessibility.PUBLIC.equals(expectedAccessibility))
		// {
		// if (Accessibility.PUBLIC.equals(accessibility)
		// || (Accessibility.PACKAGE_PRIVATE.equals(accessibility) && targetPackage.getName()
		// .equals(testerPackage.getName())))
		// {
		// throw new AssertionError("The selected constructor is accessible. \n[tester's package: "
		// + testerPackage + "]\n[package: " + targetPackage + "]\n[accessibility: "
		// + accessibility.getName() + "]");
		// }
		// }

		IllegalAccessException illegalAccessException = null;
		try
		{
			if (forceAccessibility)
			{
				constructor.setAccessible(true);
			}
			else if (constructor.isAccessible())
			{
				throw new AssertionError("The selected constructor is accessible.");
			}
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
		catch (IllegalAccessException e)
		{
			illegalAccessException = e;
		}

		if (null == illegalAccessException)
		{
			final Package targetPackage = targetClass.getPackage();
			final Class<?> testerClass = tester.getClass();
			final Package testerPackage = testerClass.getPackage();

			if ((Accessibility.PACKAGE_PRIVATE.equals(accessibility) && !targetPackage.getName()
					.equals(testerPackage.getName()))
					|| (Accessibility.PROTECTED.equals(accessibility) && !targetClass.isAssignableFrom(testerClass)))
			{
				throw new IllegalAccessException("[tester: " + testerClass + "]\n[target: " + targetClass
						+ "]\n[accessibility: " + accessibility.getName() + "]");
			}
			throw new AssertionError("The selected constructor is accessible.\n[tester's package: " + testerPackage
					+ "]\n[package: " + targetPackage + "]\n[accessibility: " + accessibility.getName() + "]");
		}
		throw illegalAccessException;
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

	public static Class<?>[] classArrayOf()
	{
		return EMPTY_CLASS_ARRAY;
	}

	public static Class<?>[] classArrayOf(final Class<?>... classes)
	{
		if (null == classes || 0 == classes.length)
		{
			return EMPTY_CLASS_ARRAY;
		}
		return classes;
	}

	public static Object[] objectArrayOf()
	{
		return EMPTY_OBJECT_ARRAY;
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
