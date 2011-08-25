/**
 * 
 */
package org.elixirian.kommonlee.test;


import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Constructor;

import org.elixirian.kommonlee.test.CommonTestHelper;
import org.elixirian.kommonlee.test.CommonTestHelper.Accessibility;
import org.elixirian.kommonlee.test.another.ClassWithPackagePrivateConstructor;
import org.elixirian.kommonlee.test.another.ClassWithProtectedConstructor;
import org.elixirian.kommonlee.test.another.SomeObjectForTesting;
import org.junit.Test;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2010-02-03)
 */
public class CommonTestHelperTest
{
	private static class TargetClass
	{
		private TargetClass()
		{
		}
	}

	private static class TestClassWithPackagePrivateConstructor
	{
		TestClassWithPackagePrivateConstructor()
		{
		}
	}

	private static class TestClassWithProtectedConstructor
	{
		protected TestClassWithProtectedConstructor()
		{
		}
	}

	private static class TargetClassWithPublicConstructor
	{
		public TargetClassWithPublicConstructor()
		{
		}
	}

	public static class TargetClassWithAccessibleConstructor
	{
		public TargetClassWithAccessibleConstructor()
		{
		}
	}

	private static abstract class TargetAbstractClass
	{
		public TargetAbstractClass()
		{
		}
	}

	private interface TargetInterface
	{
	}

	@Test(expected = IllegalStateException.class)
	public final void testCommonTestHelper() throws Exception
	{
		Class<CommonTestHelper> targetClass = CommonTestHelper.class;
		Class<?>[] parameterTypes = new Class<?>[] {};
		Object[] parameters = new Object[] {};

		Constructor<?> constructor = null;
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
			System.err.println("The constuctor with the given parameters does not exist in " + targetClass.getName());
			throw e;
		}
		assertThat("The constuctor with the given parameters does not exist in " + targetClass.getName(), constructor,
				notNullValue());
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
			System.err.println("The given constructor parameters do not match with the constructor parameter types.");
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

	@Test(expected = IllegalAccessException.class)
	public final void testTestNotAccessibleConstructor() throws Exception
	{
		CommonTestHelper.testNotAccessibleConstructor(TargetClass.class, this, Accessibility.PRIVATE,
				new Class<?>[] {}, new Object[] {});
	}

	@Test(expected = IllegalAccessException.class)
	public final void testTestWithPackagePrivateConstructor() throws Exception
	{
		CommonTestHelper.testNotAccessibleConstructor(ClassWithPackagePrivateConstructor.class, this,
				Accessibility.PACKAGE_PRIVATE, new Class<?>[] {}, new Object[] {});
	}

	@Test(expected = IllegalAccessException.class)
	public final void testTestWithPackagePrivateConstructor2() throws Exception
	{
		CommonTestHelper.testNotAccessibleConstructor(TestClassWithPackagePrivateConstructor.class,
				SomeObjectForTesting.INSTANCE, Accessibility.PACKAGE_PRIVATE, new Class<?>[] {}, new Object[] {});
	}

	@Test(expected = IllegalAccessException.class)
	public final void testTestWithProtectedConstructor() throws Exception
	{
		CommonTestHelper.testNotAccessibleConstructor(ClassWithProtectedConstructor.class, this,
				Accessibility.PROTECTED, new Class<?>[] {}, new Object[] {});
	}

	@Test(expected = IllegalAccessException.class)
	public final void testTestWithProtectedConstructor2() throws Exception
	{
		CommonTestHelper.testNotAccessibleConstructor(TestClassWithProtectedConstructor.class,
				SomeObjectForTesting.INSTANCE, Accessibility.PROTECTED, new Class<?>[] {}, new Object[] {});
	}

	@Test(expected = NoSuchMethodException.class)
	public final void testTestNotAccessibleConstructorToTestNoSuchMethodException() throws Exception
	{
		CommonTestHelper.testNotAccessibleConstructor(TargetClass.class, this, Accessibility.PUBLIC, new Class<?>[] {
				String.class, int.class }, new Object[] {});
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testTestAccessibleConstructorToTestIllegalArgumentException() throws Exception
	{
		CommonTestHelper.testNotAccessibleConstructor(TargetClassWithPublicConstructor.class, this,
				Accessibility.PUBLIC, new Class<?>[] {}, new Object[] { "test", 123 });
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testTestNotAccessibleConstructorToTestIllegalArgumentException() throws Exception
	{
		CommonTestHelper.testNotAccessibleConstructor(TargetClass.class, this, Accessibility.PRIVATE, true,
				new Class<?>[] {}, new Object[] { "test", 123 });
	}

	@Test(expected = AssertionError.class)
	public final void testTestNotAccessibleConstructorWithTargetClassWithAccessibleConstructor() throws Exception
	{
		try
		{
			CommonTestHelper.testNotAccessibleConstructor(TargetClassWithAccessibleConstructor.class, this,
					Accessibility.PUBLIC, new Class<?>[] {}, new Object[] {});
		}
		catch (AssertionError e)
		{
			assertThat(e.getMessage(), equalTo("The selected constructor is accessible."));
			throw e;
		}
	}

	@Test(expected = InstantiationException.class)
	public final void testTestNotAccessibleConstructorWithUninstantiableTargetClass() throws Exception
	{
		CommonTestHelper.testNotAccessibleConstructor(TargetAbstractClass.class, this, Accessibility.PUBLIC,
				new Class<?>[] {}, new Object[] {});
	}

	@Test(expected = NoSuchMethodException.class)
	public final void testTestNotAccessibleConstructorWithInterface() throws Exception
	{
		CommonTestHelper.testNotAccessibleConstructor(TargetInterface.class, this, Accessibility.PUBLIC,
				new Class<?>[] {}, new Object[] {});
	}

	@Test
	public final void testArrayToString()
	{
		@SuppressWarnings("boxing")
		final Object[] objectArray = { "Test", 123, true, 23.34D, (short) 10, 222L, 12.43f };
		assertThat(CommonTestHelper.arrayToString(objectArray), equalTo("[\"Test\", 123, true, 23.34, 10, 222, 12.43]"));
	}

	@SuppressWarnings("boxing")
	@Test
	public final void testClassArrayOf()
	{
		Class<?>[] classArray = CommonTestHelper.classArrayOf();
		assertThat(classArray.length, is(equalTo(CommonTestHelper.EMPTY_CLASS_ARRAY.length)));
		assertThat(classArray, is(equalTo(CommonTestHelper.EMPTY_CLASS_ARRAY)));

		classArray = CommonTestHelper.classArrayOf((Class<?>) null);
		assertThat(classArray.length, is(equalTo(1)));
		assertThat(classArray, is(equalTo(new Object[] { null })));

		final Class<?>[] expectedClassArray = new Class<?>[] { String.class, int.class, Integer.class };
		classArray = CommonTestHelper.classArrayOf(String.class, int.class, Integer.class);

		assertThat(classArray.length, is(equalTo(expectedClassArray.length)));

		for (int i = 0, size = expectedClassArray.length; i < size; i++)
		{
			assertEquals(expectedClassArray[i], classArray[i]);
		}
	}

	@SuppressWarnings("boxing")
	@Test
	public final void testObjectArrayOf()
	{
		Object[] objectArray = CommonTestHelper.objectArrayOf();
		assertThat(objectArray.length, is(equalTo(CommonTestHelper.EMPTY_OBJECT_ARRAY.length)));
		assertThat(objectArray, is(equalTo(CommonTestHelper.EMPTY_OBJECT_ARRAY)));

		objectArray = CommonTestHelper.objectArrayOf((Object) null);
		assertThat(objectArray.length, is(equalTo(1)));
		assertThat(objectArray, is(equalTo(new Object[] { null })));

		final Object[] expectedObjectArray = new Object[] { "Test", 123, true, 23.34D, (short) 10, 222L, 12.43f };
		objectArray = CommonTestHelper.objectArrayOf("Test", 123, true, 23.34D, (short) 10, 222L, 12.43f);

		assertThat(objectArray.length, is(equalTo(expectedObjectArray.length)));

		for (int i = 0, size = expectedObjectArray.length; i < size; i++)
		{
			assertEquals(expectedObjectArray[i], objectArray[i]);
		}
	}
}
