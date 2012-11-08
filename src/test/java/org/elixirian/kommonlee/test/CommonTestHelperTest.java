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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Constructor;

import org.elixirian.kommonlee.test.CommonTestHelper.Accessibility;
import org.elixirian.kommonlee.test.another.ClassWithPackagePrivateConstructor;
import org.elixirian.kommonlee.test.another.ClassWithProtectedConstructor;
import org.elixirian.kommonlee.test.another.SomeObjectForTesting;
import org.junit.Test;

/**
 * <pre>
 *     ___  _____                                              _____
 *    /   \/    / ______ __________________  ______ __ ______ /    /   ______  ______  
 *   /        / _/ __  // /  /   / /  /   /_/ __  // //     //    /   /  ___ \/  ___ \ 
 *  /        \ /  /_/ _/  _  _  /  _  _  //  /_/ _/   __   //    /___/  _____/  _____/
 * /____/\____\/_____//__//_//_/__//_//_/ /_____//___/ /__//________/\_____/ \_____/
 * </pre>
 * 
 * <pre>
 *     ___  _____                                _____
 *    /   \/    /_________  ___ ____ __ ______  /    /   ______  ______
 *   /        / /  ___ \  \/  //___// //     / /    /   /  ___ \/  ___ \
 *  /        \ /  _____/\    //   //   __   / /    /___/  _____/  _____/
 * /____/\____\\_____/   \__//___//___/ /__/ /________/\_____/ \_____/
 * </pre>
 * 
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

  @Test(expected = IllegalAccessException.class)
  public final void testCommonTestHelper() throws Exception
  {
    final Class<CommonTestHelper> targetClass = CommonTestHelper.class;
    final Class<?>[] parameterTypes = new Class<?>[] {};
    final Object[] parameters = new Object[] {};

    Constructor<?> constructor = null;
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
    catch (final IllegalAccessException e)
    {
      illegalAccessException = e;
    }
    assertThat(illegalAccessException, is(IllegalAccessException.class));

    constructor.setAccessible(true);
    try
    {
      constructor.newInstance(parameters);
    }
    catch (final IllegalArgumentException e)
    {
      System.err.println("The given constructor parameters do not match with the constructor parameter types.");
      throw e;
    }
    catch (final InstantiationException e)
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
    CommonTestHelper.testNotAccessibleConstructor(TargetClass.class, this, Accessibility.PRIVATE, new Class<?>[] {},
        new Object[] {});
  }

  @Test(expected = IllegalAccessException.class)
  public final void testTestNotAccessibleConstructorWithConstructorTester() throws Exception
  {
    CommonTestHelper.newConstructorTester(TargetClass.class, this)
        .mustBePrivate()
        .test();
  }

  @Test(expected = IllegalAccessException.class)
  public final void testTestWithPackagePrivateConstructor() throws Exception
  {
    CommonTestHelper.testNotAccessibleConstructor(ClassWithPackagePrivateConstructor.class, this,
        Accessibility.PACKAGE_PRIVATE, new Class<?>[] {}, new Object[] {});
  }

  @Test(expected = IllegalAccessException.class)
  public final void testTestWithPackagePrivateConstructorWithConstructorTester() throws Exception
  {
    CommonTestHelper.newConstructorTester(ClassWithPackagePrivateConstructor.class, this)
        .mustBePackagePrivate()
        .test();
  }

  @Test(expected = IllegalAccessException.class)
  public final void testTestWithPackagePrivateConstructor2() throws Exception
  {
    CommonTestHelper.testNotAccessibleConstructor(TestClassWithPackagePrivateConstructor.class,
        SomeObjectForTesting.INSTANCE, Accessibility.PACKAGE_PRIVATE, new Class<?>[] {}, new Object[] {});
  }

  @Test(expected = IllegalAccessException.class)
  public final void testTestWithPackagePrivateConstructor2WithConstructorTester() throws Exception
  {
    CommonTestHelper.newConstructorTester(TestClassWithPackagePrivateConstructor.class, SomeObjectForTesting.INSTANCE)
        .mustBePackagePrivate()
        .test();
  }

  @Test(expected = IllegalAccessException.class)
  public final void testTestWithProtectedConstructor() throws Exception
  {
    CommonTestHelper.testNotAccessibleConstructor(ClassWithProtectedConstructor.class, this, Accessibility.PROTECTED,
        new Class<?>[] {}, new Object[] {});
  }

  @Test(expected = IllegalAccessException.class)
  public final void testTestWithProtectedConstructorWithConstructorTester() throws Exception
  {
    CommonTestHelper.newConstructorTester(ClassWithProtectedConstructor.class, this)
        .mustBeProtected()
        .test();
  }

  @Test(expected = IllegalAccessException.class)
  public final void testTestWithProtectedConstructor2() throws Exception
  {
    CommonTestHelper.testNotAccessibleConstructor(TestClassWithProtectedConstructor.class,
        SomeObjectForTesting.INSTANCE, Accessibility.PROTECTED, new Class<?>[] {}, new Object[] {});
  }

  @Test(expected = IllegalAccessException.class)
  public final void testTestWithProtectedConstructor2WithConstructorTester() throws Exception
  {
    CommonTestHelper.newConstructorTester(TestClassWithProtectedConstructor.class, SomeObjectForTesting.INSTANCE)
        .mustBeProtected()
        .test();
  }

  @Test(expected = NoSuchMethodException.class)
  public final void testTestNotAccessibleConstructorToTestNoSuchMethodException() throws Exception
  {
    CommonTestHelper.testNotAccessibleConstructor(TargetClass.class, this, Accessibility.PUBLIC, new Class<?>[] {
        String.class, int.class }, new Object[] {});
  }

  @Test(expected = NoSuchMethodException.class)
  public final void testTestNotAccessibleConstructorToTestNoSuchMethodExceptionWithConstructorTester() throws Exception
  {
    CommonTestHelper.newConstructorTester(TargetClass.class, this)
        .mustBePublic()
        .parameterTypes(String.class, int.class)
        .test();
  }

  @Test(expected = IllegalArgumentException.class)
  public final void testTestAccessibleConstructorToTestIllegalArgumentException() throws Exception
  {
    CommonTestHelper.testNotAccessibleConstructor(TargetClassWithPublicConstructor.class, this, Accessibility.PUBLIC,
        new Class<?>[] {}, new Object[] { "test", 123 });
  }

  @Test(expected = IllegalArgumentException.class)
  public final void testTestAccessibleConstructorToTestIllegalArgumentExceptionWithConstructorTester() throws Exception
  {
    CommonTestHelper.newConstructorTester(TargetClassWithPublicConstructor.class, this)
        .mustBePublic()
        .parameterValues("test", 123)
        .test();
  }

  @Test(expected = IllegalArgumentException.class)
  public final void testTestNotAccessibleConstructorToTestIllegalArgumentException() throws Exception
  {
    CommonTestHelper.testNotAccessibleConstructor(TargetClass.class, this, Accessibility.PRIVATE, true,
        new Class<?>[] {}, new Object[] { "test", 123 });
  }

  @Test(expected = IllegalArgumentException.class)
  public final void testTestNotAccessibleConstructorToTestIllegalArgumentExceptionWithConstructorTester()
      throws Exception
  {
    CommonTestHelper.newConstructorTester(TargetClass.class, this)
        .mustBePrivate()
        .forceAccessibility()
        .parameterValues("test", 123)
        .test();
  }

  @Test(expected = AssertionError.class)
  public final void testTestNotAccessibleConstructorWithTargetClassWithAccessibleConstructor() throws Exception
  {
    try
    {
      CommonTestHelper.testNotAccessibleConstructor(TargetClassWithAccessibleConstructor.class, this,
          Accessibility.PUBLIC, new Class<?>[] {}, new Object[] {});
    }
    catch (final AssertionError e)
    {
      assertThat(e.getMessage(), equalTo("The selected constructor is accessible."));
      throw e;
    }
  }

  @Test(expected = AssertionError.class)
  public final void testTestNotAccessibleConstructorWithTargetClassWithAccessibleConstructorWithConstructorTester()
      throws Exception
  {
    try
    {
      CommonTestHelper.newConstructorTester(TargetClassWithAccessibleConstructor.class, this)
          .mustBePublic()
          .test();
    }
    catch (final AssertionError e)
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

  @Test(expected = InstantiationException.class)
  public final void testTestNotAccessibleConstructorWithUninstantiableTargetClassWithConstructorTester()
      throws Exception
  {
    CommonTestHelper.newConstructorTester(TargetAbstractClass.class, this)
        .mustBePublic()
        .test();
  }

  @Test(expected = NoSuchMethodException.class)
  public final void testTestNotAccessibleConstructorWithInterface() throws Exception
  {
    CommonTestHelper.testNotAccessibleConstructor(TargetInterface.class, this, Accessibility.PUBLIC, new Class<?>[] {},
        new Object[] {});
  }

  @Test(expected = NoSuchMethodException.class)
  public final void testTestNotAccessibleConstructorWithInterfaceWithConstructorTester() throws Exception
  {
    CommonTestHelper.newConstructorTester(TargetInterface.class, this)
        .mustBePublic()
        .test();
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
