/**
 * This project is licensed under the Apache License, Version 2.0
 * if the following condition is met:
 * (otherwise it cannot be used by anyone but the author, Kevin, only)
 *
 * The original KommonLee project is owned by Lee, Seong Hyun (Kevin).
 *
 * -What does it mean to you?
 * Nothing, unless you want to take the ownership of
 * "the original project" (not yours or forked & modified one).
 * You are free to use it for both non-commercial and commercial projects
 * and free to modify it as the Apache License allows.
 *
 * -So why is this condition necessary?
 * It is only to protect the original project (See the case of Java).
 *
 *
 * Copyright 2009 Lee, Seong Hyun (Kevin)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.elixirian.kommonlee.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2010-02-03)
 * @version 0.0.2 (2010-02-23) commented...
 */
public final class CommonTestHelper
{
	public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];
	public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

	public static class ConstructorTester<T>
	{
		final Class<T> targetClass;
		final Object tester;
		Accessibility expectedAccessibility;
		boolean forceAccessibility;
		final List<Class<?>> parameterTypes;
		final List<Object> parameters;

		ConstructorTester(final Class<T> targetClass, final Object tester)
		{
			if (null == targetClass)
			{
				throw new NullPointerException("Class<?> targetClass must not be null!");
			}
			if (null == tester)
			{
				throw new NullPointerException("Object tester must not be null!");
			}

			this.targetClass = targetClass;
			this.tester = tester;
			this.expectedAccessibility = Accessibility.PRIVATE;
			this.forceAccessibility = false;
			this.parameterTypes = new ArrayList<Class<?>>();
			this.parameters = new ArrayList<Object>();
		}

		public ConstructorTester<T> mustBePrivate()
		{
			this.expectedAccessibility = Accessibility.PRIVATE;
			return this;
		}

		public ConstructorTester<T> mustBePackagePrivate()
		{
			this.expectedAccessibility = Accessibility.PACKAGE_PRIVATE;
			return this;
		}

		public ConstructorTester<T> mustBeProtected()
		{
			this.expectedAccessibility = Accessibility.PROTECTED;
			return this;
		}

		public ConstructorTester<T> mustBePublic()
		{
			this.expectedAccessibility = Accessibility.PUBLIC;
			return this;
		}

		public ConstructorTester<T> forceAccessibility()
		{
			this.forceAccessibility = true;
			return this;
		}

		public ConstructorTester<T> doNotForceAccessibility()
		{
			this.forceAccessibility = false;
			return this;
		}

		public ConstructorTester<T> parameterTypeAndValue(final Class<?> parameterType, final Object parameter)
		{
			this.parameterTypes.add(parameterType);
			this.parameters.add(parameter);
			return this;
		}

		public ConstructorTester<T> parameterTypes(final Class<?>... parameterTypes)
		{
			this.parameterTypes.addAll(Arrays.asList(parameterTypes));
			return this;
		}

		public ConstructorTester<T> parameterValues(final Object... parameters)
		{
			this.parameters.add(Arrays.asList(parameters));
			return this;
		}

		public void test() throws Exception
		{
			testNotAccessibleConstructor(targetClass, tester, expectedAccessibility, forceAccessibility,
					parameterTypes.toArray(new Class<?>[parameterTypes.size()]),
					parameters.toArray(new Object[parameters.size()]));
		}
	}

	public static <T> ConstructorTester<T> newConstructorTester(final Class<T> targetClass, final Object tester)
	{
		return new ConstructorTester<T>(targetClass, tester);
	}

	private CommonTestHelper() throws IllegalAccessException
	{
		throw new IllegalAccessException(getClass().getName() + " cannot be instantiated.");
	}

	public enum Accessibility
	{
		/* @formatter:off */
		PRIVATE(				Modifier.PRIVATE, 	"private"),
		PACKAGE_PRIVATE(0, 									"package-private"),
		PROTECTED(			Modifier.PROTECTED, "protected"),
		PUBLIC(					Modifier.PUBLIC, 		"public");
		/* @formatter:on */

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

	private static Accessibility getAccessibility(final int mod)
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
	 *          any type.
	 * @param targetClass
	 *          the target class the constructor of which is to be tested.
	 * @param tester
	 *          The object testing the target class.
	 * @param expectedAccessibility
	 *          expected accessibility.
	 * @param parameterTypes
	 *          the constructor parameter types.
	 * @param parameters
	 *          the constructor parameter values.
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
	 *          any type.
	 * @param targetClass
	 *          the target class the constructor of which is to be tested.
	 * @param tester
	 *          The object testing the target class.
	 * @param expectedAccessibility
	 *          expected accessibility.
	 * @param forceAccessibility
	 * @param parameterTypes
	 *          the constructor parameter types.
	 * @param parameters
	 *          the constructor parameter values.
	 * @throws Exception
	 */
	public static <T> void testNotAccessibleConstructor(final Class<T> targetClass, final Object tester,
			final Accessibility expectedAccessibility, final boolean forceAccessibility, final Class<?>[] parameterTypes,
			final Object[] parameters) throws Exception
	{
		Constructor<T> constructor = null;
		try
		{
			constructor = targetClass.getDeclaredConstructor(parameterTypes);
		}
		catch (final SecurityException e)
		{
			throw e;
		}
		catch (final NoSuchMethodException e)
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
		catch (final IllegalArgumentException e)
		{
			System.err.println("The given constructor parameters: " + arrayToString(parameters)
					+ " do not match with the constructor parameter types: " + arrayToString(parameterTypes));
			throw e;
		}
		catch (final InstantiationException e)
		{
			throw e;
		}
		catch (final IllegalAccessException e)
		{
			illegalAccessException = e;
		}

		if (null == illegalAccessException)
		{
			final Package targetPackage = targetClass.getPackage();
			final Class<?> testerClass = tester.getClass();
			final Package testerPackage = testerClass.getPackage();

			if ((Accessibility.PACKAGE_PRIVATE == accessibility && !targetPackage.getName()
					.equals(testerPackage.getName()))
					|| (Accessibility.PROTECTED == accessibility && !targetClass.isAssignableFrom(testerClass)))
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
			return "no parameter";
		}

		final StringBuilder stringBuilder = new StringBuilder("[");
		for (final T t : array)
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
