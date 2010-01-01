/**
 * 
 */
package com.elixirian.common.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2010-01-01)
 */
public class CommonTestHelperTest
{
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

	private static class TargetClass
	{
		private TargetClass()
		{
			throw new IllegalStateException(getClass().getName() + " cannot be instantiated.");
		}
	}
	
	private static class TargetClassWithAccessibleConstructor
	{
		public TargetClassWithAccessibleConstructor()
		{
		}
	}
	
	private static abstract class TargetAbstractClass
	{
		private TargetAbstractClass()
		{
		}
	}
	
	private interface TargetInterface
	{
	}

	/**
	 * Test method for {@link com.elixirian.common.test.CommonTestHelper#CommonTestHelper()}.
	 * 
	 * @throws Exception
	 */
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
			e.printStackTrace();
			throw e;
		}
		catch (NoSuchMethodException e)
		{
			System.err.println("The constuctor with the given parameters does not exist in " + targetClass.getName());
			e.printStackTrace();
			throw e;
		}
		assertThat("The constuctor with the given parameters does not exist in " + targetClass.getName(), constructor, notNullValue());
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

	/**
	 * Test method for {@link com.elixirian.common.test.CommonTestHelper#testNotAccessibleConstructor(java.lang.Class, java.lang.Class<?>[],
	 * java.lang.Object[])}.
	 */
	@Test(expected = IllegalStateException.class)
	public final void testTestNotAccessibleConstructor() throws Exception
	{
		CommonTestHelper.testNotAccessibleConstructor(TargetClass.class, new Class<?>[] {}, new Object[] {});
	}
	
	/**
	 * Test method for {@link com.elixirian.common.test.CommonTestHelper#testNotAccessibleConstructor(java.lang.Class, java.lang.Class<?>[],
	 * java.lang.Object[])}.
	 */
	@Test(expected = NoSuchMethodException.class)
	public final void testTestNotAccessibleConstructorToTestNoSuchMethodException() throws Exception
	{
		CommonTestHelper.testNotAccessibleConstructor(TargetClass.class, new Class<?>[] {String.class, int.class}, new Object[] {});
	}
	
	/**
	 * Test method for {@link com.elixirian.common.test.CommonTestHelper#testNotAccessibleConstructor(java.lang.Class, java.lang.Class<?>[],
	 * java.lang.Object[])}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testTestNotAccessibleConstructorToTestIllegalArgumentException() throws Exception
	{
		CommonTestHelper.testNotAccessibleConstructor(TargetClass.class, new Class<?>[] {}, new Object[] {"test", 123});
	}

	/**
	 * Test method for {@link com.elixirian.common.test.CommonTestHelper#testNotAccessibleConstructor(java.lang.Class, java.lang.Class<?>[],
	 * java.lang.Object[])}.
	 */
	@Test(expected = AssertionError.class)
	public final void testTestNotAccessibleConstructorWithTargetClassWithAccessibleConstructor() throws Exception
	{
		try
		{
			CommonTestHelper.testNotAccessibleConstructor(TargetClassWithAccessibleConstructor.class, new Class<?>[] {}, new Object[] {});
		}
		catch (AssertionError e)
		{
			assertThat(e.getMessage(), equalTo("The selected constructor is accessible."));
			throw e;
		}
	}
	
	/**
	 * Test method for {@link com.elixirian.common.test.CommonTestHelper#testNotAccessibleConstructor(java.lang.Class, java.lang.Class<?>[],
	 * java.lang.Object[])}.
	 */
	@Test(expected = InstantiationException.class)
	public final void testTestNotAccessibleConstructorWithUninstantiableTargetClass() throws Exception
	{
		CommonTestHelper.testNotAccessibleConstructor(TargetAbstractClass.class, new Class<?>[] {}, new Object[] {});
	}
	
	/**
	 * Test method for {@link com.elixirian.common.test.CommonTestHelper#testNotAccessibleConstructor(java.lang.Class, java.lang.Class<?>[],
	 * java.lang.Object[])}.
	 */
	@Test(expected = NoSuchMethodException.class)
	public final void testTestNotAccessibleConstructorWithInterface() throws Exception
	{
		CommonTestHelper.testNotAccessibleConstructor(TargetInterface.class, new Class<?>[] {}, new Object[] {});
	}
	
	/**
	 * Test method for {@link com.elixirian.common.test.CommonTestHelper#arrayToString(T[])}.
	 */
	@Test
	public final void testArrayToString()
	{
		Object[] objectArray = { "Test", 123, true, 23.34D, (short) 10, 222L, 12.43f };
		assertThat(CommonTestHelper.arrayToString(objectArray), equalTo("[\"Test\", 123, true, 23.34, 10, 222, 12.43]"));
	}

	/**
	 * Test method for {@link com.elixirian.common.test.CommonTestHelper#classArrayOf(java.lang.Class<?>[])}.
	 */
	@Test
	public final void testClassArrayOf()
	{
		Class<?>[] classArray = CommonTestHelper.classArrayOf(null);
		assertThat(classArray.length, equalTo(CommonTestHelper.EMPTY_CLASS_ARRAY.length));
		assertThat(classArray, equalTo(CommonTestHelper.EMPTY_CLASS_ARRAY));

		classArray = CommonTestHelper.classArrayOf();
		assertThat(classArray.length, equalTo(CommonTestHelper.EMPTY_CLASS_ARRAY.length));
		assertThat(classArray, equalTo(CommonTestHelper.EMPTY_CLASS_ARRAY));

		final Class<?>[] expectedClassArray = new Class<?>[] { String.class, int.class, Integer.class };
		classArray = CommonTestHelper.classArrayOf(String.class, int.class, Integer.class);

		assertThat(classArray.length, equalTo(expectedClassArray.length));

		for (int i = 0, size = expectedClassArray.length; i < size; i++)
		{
			assertEquals(expectedClassArray[i], classArray[i]);
		}
	}

	/**
	 * Test method for {@link com.elixirian.common.test.CommonTestHelper#objectArrayOf(java.lang.Object[])}.
	 */
	@Test
	public final void testObjectArrayOf()
	{
		Object[] objectArray = CommonTestHelper.objectArrayOf(null);
		assertThat(objectArray.length, equalTo(CommonTestHelper.EMPTY_OBJECT_ARRAY.length));
		assertThat(objectArray, equalTo(CommonTestHelper.EMPTY_OBJECT_ARRAY));

		objectArray = CommonTestHelper.objectArrayOf();
		assertThat(objectArray.length, equalTo(CommonTestHelper.EMPTY_OBJECT_ARRAY.length));
		assertThat(objectArray, equalTo(CommonTestHelper.EMPTY_OBJECT_ARRAY));

		final Object[] expectedObjectArray = new Object[] { "Test", 123, true, 23.34D, (short) 10, 222L, 12.43f };
		objectArray = CommonTestHelper.objectArrayOf("Test", 123, true, 23.34D, (short) 10, 222L, 12.43f);

		assertThat(objectArray.length, equalTo(expectedObjectArray.length));

		for (int i = 0, size = expectedObjectArray.length; i < size; i++)
		{
			assertEquals(expectedObjectArray[i], objectArray[i]);
		}
	}
}