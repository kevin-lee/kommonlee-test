package org.elixirian.kommonlee.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.elixirian.kommonlee.test.CauseCheckableExpectedException;
import org.elixirian.kommonlee.test.CauseCheckableExpectedException.CauseCheckableExpectedExceptionStatement;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.model.Statement;

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
    assertThat(causeCheckableExpectedException.getMatcher(), is(nullValue()));
    assertThat(causeCheckableExpectedException.getCauseMatcher(), is(nullValue()));
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
    assertThat(causeCheckableExpectedException.getMatcher(), is(nullValue()));
    assertThat(causeCheckableExpectedException.getCauseMatcher(), is(nullValue()));
  }

  @Test
  public final void testApply() throws Throwable
  {
    /* given */
    final Statement base = mock(Statement.class);

    final CauseCheckableExpectedException causeCheckableExpectedException = CauseCheckableExpectedException.none();

    /* when */
    final Statement result = causeCheckableExpectedException.apply(base, null, null);

    /* then */
    assertThat(result, is(instanceOf(CauseCheckableExpectedExceptionStatement.class)));

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
		final Statement statement = result.apply(base, null, null);
		statement.evaluate();

		/* then */
		assertThat(result, is(equalTo(causeCheckableExpectedException)));
		assertThat(statement, is(instanceOf(CauseCheckableExpectedExceptionStatement.class)));
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
    final Statement statement = result.apply(base, null, null);
    statement.evaluate();

    /* then */
    assertThat(result, is(equalTo(causeCheckableExpectedException)));
    assertThat(statement, is(instanceOf(CauseCheckableExpectedExceptionStatement.class)));
    verify(base)
        .evaluate();
    assertThat(new UnsupportedOperationException(), result.getMatcher());
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
    final Statement statement = result.apply(base, null, null);
    statement.evaluate();

    /* then */
    assertThat(result, is(equalTo(causeCheckableExpectedException)));
    assertThat(statement, is(instanceOf(CauseCheckableExpectedExceptionStatement.class)));
    verify(base).evaluate();
    assertThat(new NullPointerException("This is a test exception."), result.getMatcher());
    assertThat(new NullPointerException("This is not an expected exception."), is(not(result.getMatcher())));

    /* when */
    result.expect(UnsupportedOperationException.class);
    assertThat(new NullPointerException("This is a test exception."), is(not(result.getMatcher())));
    assertThat(new UnsupportedOperationException("This is a test exception."), result.getMatcher());
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
      causeCheckableExpectedException.expectMessage(is(equalTo(expectedMessage)));
    final Statement statement = result.apply(base, null, null);
    statement.evaluate();

    /* then */
    assertThat(result, is(equalTo(causeCheckableExpectedException)));
    assertThat(statement, is(instanceOf(CauseCheckableExpectedExceptionStatement.class)));
    verify(base)
        .evaluate();
    assertThat(new NullPointerException(expectedMessage), result.getMatcher());
    assertThat(new NullPointerException(expectedMessage + "blah blah"), is(not(result.getMatcher())));
    assertThat(new NullPointerException("expected"), is(not(result.getMatcher())));

    /* when */
    result.expect(UnsupportedOperationException.class);
    assertThat(new NullPointerException(expectedMessage), is(not(result.getMatcher())));
    assertThat(new UnsupportedOperationException(expectedMessage), result.getMatcher());
    /* @formatter:on */
  }

  private static class NestedException extends RuntimeException
  {
    private static final long serialVersionUID = 1L;

    public NestedException()
    {
    }

    public NestedException(String message, Throwable cause)
    {
      super(message, cause);
    }

    public NestedException(String message)
    {
      super(message);
    }

    public NestedException(Throwable cause)
    {
      super(cause);
    }
  }

  private static class RootException extends RuntimeException
  {
    public RootException()
    {
    }

    public RootException(String message, Throwable cause)
    {
      super(message, cause);
    }

    public RootException(String message)
    {
      super(message);
    }

    public RootException(Throwable cause)
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
    final Statement statement = result.apply(base, null, null);
    statement.evaluate();

    /* then */
    assertThat(result, is(equalTo(causeCheckableExpectedException)));
    assertThat(statement, is(instanceOf(CauseCheckableExpectedExceptionStatement.class)));
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
    final Statement statement = result.apply(base, null, null);
    statement.evaluate();

    /* then */
    assertThat(result, is(equalTo(causeCheckableExpectedException)));
    assertThat(statement, is(instanceOf(CauseCheckableExpectedExceptionStatement.class)));
    verify(base)
        .evaluate();
    assertThat(new RootException(), result.getCauseMatcher());
    assertThat(new UnsupportedOperationException(), is(not(result.getCauseMatcher())));
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
    final Statement statement = result.apply(base, null, null);
    statement.evaluate();

    /* then */
    assertThat(result, is(equalTo(causeCheckableExpectedException)));
    assertThat(statement, is(instanceOf(CauseCheckableExpectedExceptionStatement.class)));
    verify(base)
        .evaluate();
    assertThat(new NullPointerException("This is a test exception."), result.getCauseMatcher());
    assertThat(new UnsupportedOperationException("This is a test exception."), result.getCauseMatcher());
    assertThat(new RootException("This is a test exception."), result.getCauseMatcher());
    assertThat(new NullPointerException("This is not an expected exception."), is(not(result.getMatcher())));
    assertThat(new UnsupportedOperationException("This is not an expected exception."),
        is(not(result.getCauseMatcher())));
    assertThat(new RootException("This is not an expected exception."), is(not(result.getCauseMatcher())));

    /* when */
    result.expectCause(RootException.class);
    assertThat(new NullPointerException("This is a test exception."), is(not(result.getCauseMatcher())));
    assertThat(new UnsupportedOperationException("This is a test exception."), is(not(result.getCauseMatcher())));
    assertThat(new RootException("This is a test exception."), result.getCauseMatcher());
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
        .expectCauseMessage(is(equalTo(expectedMessage)));
    final Statement statement = result.apply(base, null, null);
    statement.evaluate();

    /* then */
    assertThat(result, is(equalTo(causeCheckableExpectedException)));
    assertThat(statement, is(instanceOf(CauseCheckableExpectedExceptionStatement.class)));
    verify(base)
        .evaluate();
    assertThat(new NullPointerException(expectedMessage), result.getCauseMatcher());
    assertThat(new UnsupportedOperationException(expectedMessage), result.getCauseMatcher());
    assertThat(new RootException(expectedMessage), result.getCauseMatcher());
    assertThat(new NullPointerException(expectedMessage + "blah blah"), is(not(result.getCauseMatcher())));
    assertThat(new NullPointerException("expected"), is(not(result.getCauseMatcher())));
    assertThat(new UnsupportedOperationException(expectedMessage + "blah blah"), is(not(result.getCauseMatcher())));
    assertThat(new UnsupportedOperationException("expected"), is(not(result.getCauseMatcher())));
    assertThat(new RootException(expectedMessage + "blah blah"), is(not(result.getCauseMatcher())));
    assertThat(new RootException("expected"), is(not(result.getCauseMatcher())));

    /* when */
    result.expectCause(RootException.class);
    assertThat(new NullPointerException(expectedMessage), is(not(result.getCauseMatcher())));
    assertThat(new UnsupportedOperationException(expectedMessage), is(not(result.getCauseMatcher())));
    assertThat(new RootException(expectedMessage), result.getCauseMatcher());
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
    final Statement statement = result1.apply(base, null, null);
    try
    {
      statement.evaluate();
    }
    catch (Exception e)
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
    final Statement statement2 = result2.apply(base, null, null);

    /* when */

    try
    {
      statement2.evaluate();
    }
    catch (Error e)
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
    final Statement statement3 = result3.apply(base, null, null);

    /* when */

    try
    {
      statement3.evaluate();
    }
    catch (Error e)
    {
      /* then */
      if (!AssertionError.class.equals(e.getClass()))
      {
        throw e;
      }
    }
  }
}
