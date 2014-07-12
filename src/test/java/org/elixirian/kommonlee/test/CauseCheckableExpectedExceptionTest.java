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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.elixirian.kommonlee.test.CauseCheckableExpectedException.CauseCheckableExpectedExceptionStatement;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.model.Statement;

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
 * @version 0.0.1 (2011-05-01)
 */
public class CauseCheckableExpectedExceptionTest
{

  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
  }

  @Before
  public void setUp() throws Exception
  {
  }

  @After
  public void tearDown() throws Exception
  {
  }

  @Test
  public final void testNone()
  {
    /* given */
    final CauseCheckableExpectedException causeCheckableExpectedException = CauseCheckableExpectedException.none();

    /* when */
    /* then */
    assertThat(causeCheckableExpectedException.getMatcher()).isNull();
    assertThat(causeCheckableExpectedException.getCauseMatcher()).isNull();
  }

  @Test
  public final void testCauseCheckableExpectedException()
  {
    class SubCauseCheckableExpectedException extends CauseCheckableExpectedException
    {
      public SubCauseCheckableExpectedException()
      {
      }
    }

    /* given */
    final CauseCheckableExpectedException causeCheckableExpectedException = new SubCauseCheckableExpectedException();

    /* when */
    /* then */
    assertThat(causeCheckableExpectedException.getMatcher()).isNull();
    assertThat(causeCheckableExpectedException.getCauseMatcher()).isNull();
  }

  @Test
  public final void testApply() throws Throwable
  {
    /* given */
    final Statement base = mock(Statement.class);

    final CauseCheckableExpectedException causeCheckableExpectedException = CauseCheckableExpectedException.none();

    /* when */
    // final Statement result = causeCheckableExpectedException.apply(base, null, null);
    final Statement result = causeCheckableExpectedException.apply(base, null);

    /* then */
    assertThat(result).isInstanceOf(CauseCheckableExpectedExceptionStatement.class);

    /* when */
    result.evaluate();

    /* then */
    verify(base).evaluate();
  }

  @Test
  public final void testExpectMatcherOfT() throws Throwable
  {
    /* @formatter:off */
		/* given */
		@SuppressWarnings("unchecked")
		final Matcher<Object> matcher = mock(Matcher.class);
		final NullPointerException nullPointerException = new NullPointerException();
		when(matcher.matches(any()))
		    .thenReturn(Boolean.FALSE);
		when(matcher.matches(nullPointerException))
		    .thenReturn(Boolean.TRUE);
		final Statement base = mock(Statement.class);
		doThrow(nullPointerException)
		    .when(base)
				.evaluate();

		final CauseCheckableExpectedException causeCheckableExpectedException = CauseCheckableExpectedException.none();

		/* when */
		final CauseCheckableExpectedException result = causeCheckableExpectedException.expect(matcher);
//		final Statement statement = result.apply(base, null, null);
		final Statement statement = result.apply(base, null);
		statement.evaluate();

		/* then */
		assertThat(result).isEqualTo(causeCheckableExpectedException);
		assertThat(statement).isInstanceOf(CauseCheckableExpectedExceptionStatement.class);
		verify(base)
		    .evaluate();
		verify(matcher)
		    .matches(nullPointerException);
		/* @formatter:on */
  }

  @Test
  public final void testExpectClassOfT() throws Throwable
  {
    /* @formatter:off */
    /* given */
    final Statement base = mock(Statement.class);
    doThrow(new UnsupportedOperationException())
        .when(base)
        .evaluate();

    final CauseCheckableExpectedException causeCheckableExpectedException = CauseCheckableExpectedException.none();

    /* when */
    final CauseCheckableExpectedException result =
      causeCheckableExpectedException.expect(UnsupportedOperationException.class);
//    final Statement statement = result.apply(base, null, null);
    final Statement statement = result.apply(base, null);
    statement.evaluate();

    /* then */
    assertThat(result).isEqualTo(causeCheckableExpectedException);
    assertThat(statement).isInstanceOf(CauseCheckableExpectedExceptionStatement.class);
    verify(base)
        .evaluate();
    assertThat(result.getMatcher().matches(new UnsupportedOperationException())).isTrue();
    assertThat(result.getMatcher().matches(new RuntimeException())).isFalse();
    /* @formatter:on */
  }

  @Test
  public final void testExpectMessageContains() throws Throwable
  {
    /* given */
    final Statement base = mock(Statement.class);
    doThrow(new UnsupportedOperationException("This is a test exception.")).when(base)
        .evaluate();

    final CauseCheckableExpectedException causeCheckableExpectedException = CauseCheckableExpectedException.none();

    /* when */
    final CauseCheckableExpectedException result = causeCheckableExpectedException.expectMessageContains("test");
    // final Statement statement = result.apply(base, null, null);
    final Statement statement = result.apply(base, null);
    statement.evaluate();

    /* then */
    assertThat(result).isEqualTo(causeCheckableExpectedException);
    assertThat(statement).isInstanceOf(CauseCheckableExpectedExceptionStatement.class);
    verify(base).evaluate();
    assertThat(result.getMatcher()
        .matches(new NullPointerException("This is a test exception."))).isTrue();
    assertThat(result.getMatcher()
        .matches(new NullPointerException("This is not an expected exception."))).isFalse();

    /* when */
    result.expect(UnsupportedOperationException.class);
    assertThat(result.getMatcher()
        .matches(new NullPointerException("This is a test exception."))).isFalse();
    assertThat(result.getMatcher()
        .matches(new UnsupportedOperationException("This is a test exception."))).isTrue();
  }

  @Test
  public final void testExpectMessage() throws Throwable
  {
    /* @formatter:off */
    /* given */
    final String expectedMessage = "This is the expected message.";
    @SuppressWarnings("unchecked")
    final Matcher<Object> matcher = mock(Matcher.class);
		when(matcher
		    .matches(any()))
		  .thenReturn(Boolean.FALSE);
		when(matcher
		    .matches(expectedMessage))
		  .thenReturn(Boolean.TRUE);
    final Statement base = mock(Statement.class);
    doThrow(new UnsupportedOperationException(expectedMessage))
        .when(base)
        .evaluate();

    final CauseCheckableExpectedException causeCheckableExpectedException = CauseCheckableExpectedException.none();

    /* when */
    final CauseCheckableExpectedException result =
      causeCheckableExpectedException.expectMessage(org.hamcrest.CoreMatchers.is(org.hamcrest.CoreMatchers.equalTo(expectedMessage)));
//    final Statement statement = result.apply(base, null, null);
    final Statement statement = result.apply(base, null);
    statement.evaluate();

    /* then */
    assertThat(result).isEqualTo(causeCheckableExpectedException);
    assertThat(statement).isInstanceOf(CauseCheckableExpectedExceptionStatement.class);
    verify(base)
        .evaluate();
    assertThat(result.getMatcher().matches(new NullPointerException(expectedMessage))).isTrue();
    assertThat(result.getMatcher().matches(new NullPointerException(expectedMessage + "blah blah"))).isFalse();
    assertThat(result.getMatcher().matches(new NullPointerException("expected"))).isFalse();

    /* when */
    result.expect(UnsupportedOperationException.class);
    assertThat(result.getMatcher().matches(new NullPointerException(expectedMessage))).isFalse();
    assertThat(result.getMatcher().matches(new UnsupportedOperationException(expectedMessage))).isTrue();
    /* @formatter:on */
  }

  private static class NestedException extends RuntimeException
  {
    private static final long serialVersionUID = 1L;

    public NestedException()
    {
    }

    public NestedException(final String message, final Throwable cause)
    {
      super(message, cause);
    }

    public NestedException(final String message)
    {
      super(message);
    }

    public NestedException(final Throwable cause)
    {
      super(cause);
    }
  }

  private static class RootException extends RuntimeException
  {
    public RootException()
    {
    }

    public RootException(final String message, final Throwable cause)
    {
      super(message, cause);
    }

    public RootException(final String message)
    {
      super(message);
    }

    public RootException(final Throwable cause)
    {
      super(cause);
    }
  }

  @Test
  public final void testExpectCauseMatcherOfT() throws Throwable
  {
    /* @formatter:off */
    /* given */
    @SuppressWarnings("unchecked")
    final Matcher<Object> matcher = mock(Matcher.class);
    final RootException rootException = new RootException();
    final NestedException nestedException = new NestedException(rootException);
    when(matcher.matches(rootException))
        .thenReturn(Boolean.TRUE);
    final Statement base = mock(Statement.class);
    doThrow(nestedException)
        .when(base)
        .evaluate();

    final CauseCheckableExpectedException causeCheckableExpectedException = CauseCheckableExpectedException.none();

    /* when */
    final CauseCheckableExpectedException result = causeCheckableExpectedException.expect(NestedException.class)
        .expectCause(matcher);
//    final Statement statement = result.apply(base, null, null);
    final Statement statement = result.apply(base, null);
    statement.evaluate();

    /* then */
    assertThat(result).isEqualTo(causeCheckableExpectedException);
    assertThat(statement).isInstanceOf(CauseCheckableExpectedExceptionStatement.class);
    verify(base)
        .evaluate();
    verify(matcher)
        .matches(nestedException.getCause());
    /* @formatter:on */
  }

  @Test
  public final void testExpectCauseClassOfT() throws Throwable
  {
    /* @formatter:off */
    /* given */
    final RootException rootException = new RootException();
    final NestedException nestedException = new NestedException(rootException);
    final Statement base = mock(Statement.class);
    doThrow(nestedException)
        .when(base)
        .evaluate();

    final CauseCheckableExpectedException causeCheckableExpectedException = CauseCheckableExpectedException.none();

    /* when */
    final CauseCheckableExpectedException result = causeCheckableExpectedException.expect(NestedException.class)
        .expectCause(RootException.class);
//    final Statement statement = result.apply(base, null, null);
    final Statement statement = result.apply(base, null);
    statement.evaluate();

    /* then */
    assertThat(result).isEqualTo(causeCheckableExpectedException);
    assertThat(statement).isInstanceOf(CauseCheckableExpectedExceptionStatement.class);
    verify(base)
        .evaluate();
    assertThat(result.getCauseMatcher().matches(new RootException() )).isTrue();
    assertThat(result.getCauseMatcher().matches(new UnsupportedOperationException())).isFalse();
    /* @formatter:on */
  }

  @Test
  public final void testExpectCauseMessageContains() throws Throwable
  {
    /* @formatter:off */
    /* given */
    final Statement base = mock(Statement.class);
    doThrow(new NestedException(new RootException("This is a test exception.")))
        .when(base)
        .evaluate();

    final CauseCheckableExpectedException causeCheckableExpectedException = CauseCheckableExpectedException.none();

    /* when */
    final CauseCheckableExpectedException result = causeCheckableExpectedException.expect(NestedException.class)
        .expectCauseMessageContains("test");
//    final Statement statement = result.apply(base, null, null);
    final Statement statement = result.apply(base, null);
    statement.evaluate();

    /* then */
    assertThat(result).isEqualTo(causeCheckableExpectedException);
    assertThat(statement).isInstanceOf(CauseCheckableExpectedExceptionStatement.class);
    verify(base)
        .evaluate();
    assertThat(result.getCauseMatcher().matches(new NullPointerException("This is a test exception."))).isTrue();
    assertThat(result.getCauseMatcher().matches(new UnsupportedOperationException("This is a test exception."))).isTrue();
    assertThat(result.getCauseMatcher().matches(new RootException("This is a test exception."))).isTrue();
    assertThat(result.getMatcher().matches(new NullPointerException("This is not an expected exception."))).isFalse();
    assertThat(result.getCauseMatcher().matches(new UnsupportedOperationException("This is not an expected exception."))).isFalse();
    assertThat(result.getCauseMatcher().matches(new RootException("This is not an expected exception."))).isFalse();

    /* when */
    result.expectCause(RootException.class);
    assertThat(result.getCauseMatcher().matches(new NullPointerException("This is a test exception."))).isFalse();
    assertThat(result.getCauseMatcher().matches(new UnsupportedOperationException("This is a test exception."))).isFalse();
    assertThat(result.getCauseMatcher().matches(new RootException("This is a test exception."))).isTrue();
    /* @formatter:on */
  }

  @Test
  public final void testExpectCauseMessage() throws Throwable
  {
    /* @formatter:off */
    /* given */
    final String expectedMessage = "This is the expected message.";
    final Matcher<Object> matcher = mock(Matcher.class);
    when(matcher.matches(any()))
        .thenReturn(Boolean.FALSE);
    when(matcher.matches(expectedMessage))
        .thenReturn(Boolean.TRUE);
    final Statement base = mock(Statement.class);
    doThrow(new NestedException(new RootException(expectedMessage)))
        .when(base)
        .evaluate();

    final CauseCheckableExpectedException causeCheckableExpectedException = CauseCheckableExpectedException.none();

    /* when */
    final CauseCheckableExpectedException result = causeCheckableExpectedException.expect(NestedException.class)
        .expectCauseMessage(org.hamcrest.CoreMatchers.is(org.hamcrest.CoreMatchers.equalTo(expectedMessage)));
//    final Statement statement = result.apply(base, null, null);
    final Statement statement = result.apply(base, null);
    statement.evaluate();

    /* then */
    assertThat(result).isEqualTo(causeCheckableExpectedException);
    assertThat(statement).isInstanceOf(CauseCheckableExpectedExceptionStatement.class);
    verify(base)
        .evaluate();
    assertThat(result.getCauseMatcher().matches(new NullPointerException(expectedMessage))).isTrue();
    assertThat(result.getCauseMatcher().matches(new UnsupportedOperationException(expectedMessage))).isTrue();
    assertThat(result.getCauseMatcher().matches(new RootException(expectedMessage))).isTrue();
    assertThat(result.getCauseMatcher().matches(new NullPointerException(expectedMessage + "blah blah"))).isFalse();
    assertThat(result.getCauseMatcher().matches(new NullPointerException("expected"))).isFalse();
    assertThat(result.getCauseMatcher().matches(new UnsupportedOperationException(expectedMessage + "blah blah"))).isFalse();
    assertThat(result.getCauseMatcher().matches(new UnsupportedOperationException("expected"))).isFalse();
    assertThat(result.getCauseMatcher().matches(new RootException(expectedMessage + "blah blah"))).isFalse();
    assertThat(result.getCauseMatcher().matches(new RootException("expected"))).isFalse();

    /* when */
    result.expectCause(RootException.class);
    assertThat(result.getCauseMatcher().matches(new NullPointerException(expectedMessage))).isFalse();
    assertThat(result.getCauseMatcher().matches(new UnsupportedOperationException(expectedMessage))).isFalse();
    assertThat(result.getCauseMatcher().matches(new RootException(expectedMessage))).isTrue();
    /* @formatter:on */
  }

  @Test
  public void testCauseCheckableExpectedExceptionStatement() throws Throwable
  {
    /* given */
    final Statement base = mock(Statement.class);
    doThrow(new UnsupportedOperationException()).when(base)
        .evaluate();
    final CauseCheckableExpectedException result1 = CauseCheckableExpectedException.none();

    /* when */
    // final Statement statement = result1.apply(base, null, null);
    final Statement statement = result1.apply(base, null);
    try
    {
      statement.evaluate();
    }
    catch (final Exception e)
    {
      /* then */
      if (!UnsupportedOperationException.class.equals(e.getClass()))
      {
        throw e;
      }
    }

    /* given */
    reset(base);
    final CauseCheckableExpectedException result2 = CauseCheckableExpectedException.none()
        .expect(RuntimeException.class);
    // final Statement statement2 = result2.apply(base, null, null);
    final Statement statement2 = result2.apply(base, null);

    /* when */

    try
    {
      statement2.evaluate();
    }
    catch (final Error e)
    {
      /* then */
      if (!AssertionError.class.equals(e.getClass()))
      {
        throw e;
      }
    }

    /* given */
    reset(base);
    final CauseCheckableExpectedException result3 = CauseCheckableExpectedException.none()
        .expectCause(RuntimeException.class);
    // final Statement statement3 = result3.apply(base, null, null);
    final Statement statement3 = result3.apply(base, null);

    /* when */

    try
    {
      statement3.evaluate();
    }
    catch (final Error e)
    {
      /* then */
      if (!AssertionError.class.equals(e.getClass()))
      {
        throw e;
      }
    }
  }
}
