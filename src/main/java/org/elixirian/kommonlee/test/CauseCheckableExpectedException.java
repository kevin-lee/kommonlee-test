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
import static org.junit.matchers.JUnitMatchers.*;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Assert;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2011-05-01)
 */
public class CauseCheckableExpectedException implements MethodRule
{
  /**
   * @return a Rule that expects no exception to be thrown (identical to behaviour without this Rule).
   */
  public static CauseCheckableExpectedException none()
  {
    return new CauseCheckableExpectedException();
  }

  private Matcher<Object> matcher;
  private Matcher<Object> causeMatcher;

  protected CauseCheckableExpectedException()
  {
  }

  Matcher<Object> getMatcher()
  {
    return matcher;
  }

  Matcher<Object> getCauseMatcher()
  {
    return causeMatcher;
  }

  @Override
  public Statement apply(final Statement base, @SuppressWarnings("unused") final FrameworkMethod method,
      @SuppressWarnings("unused") final Object target)
  {
    return new CauseCheckableExpectedExceptionStatement(base);
  }

  /**
   * Adds {@link Matcher} to the list of requirements for any thrown exception.
   *
   * @param matcher
   *          the given {@link Matcher}.
   * @return this {@link CauseCheckableExpectedException} instance itself for method chaining.
   */
  public <T> CauseCheckableExpectedException expect(final Matcher<T> matcher)
  {
    return expect0(matcher);
  }

  private <T> CauseCheckableExpectedException expect0(final Matcher<T> matcher)
  {
    @SuppressWarnings("unchecked")
    final Matcher<Object> castedMatcher = (Matcher<Object>) matcher;
    this.matcher = null == this.matcher ? castedMatcher : both(this.matcher).and(matcher);
    return this;
  }

  /**
   * Adds the given type of {@link Throwable} to check if it is thrown during test.
   *
   * @param type
   *          an expected type of {@link Throwable}.
   * @return this {@link CauseCheckableExpectedException} instance itself for method chaining.
   */
  public <T extends Throwable> CauseCheckableExpectedException expect(final Class<T> type)
  {
    return expect0(instanceOf(type));
  }

  /**
   * Adds a part of message which the thrown exception must contain in its message.
   *
   * @param substring
   *          a substring of expected message.
   * @return this {@link CauseCheckableExpectedException} instance itself for method chaining.
   */
  public CauseCheckableExpectedException expectMessageContains(final String substring)
  {
    return expectMessageContains0(substring);
  }

  private CauseCheckableExpectedException expectMessageContains0(final String substring)
  {
    return expectMessage0(containsString(substring));
  }

  /**
   * Adds {@code matcher} to the list of requirements for the message returned from any thrown exception.
   *
   * @param matcher
   *          a matcher to check the exception message.
   * @return this {@link CauseCheckableExpectedException} instance itself for method chaining.
   */
  public CauseCheckableExpectedException expectMessage(final Matcher<String> matcher)
  {
    return expectMessage0(matcher);
  }

  private CauseCheckableExpectedException expectMessage0(final Matcher<String> matcher)
  {
    return expect0(hasMessage(matcher));
  }

  /**
   * Adds {@link Matcher} to the list of requirements for the cause of any thrown exception.
   *
   * @param matcher
   *          the given {@link Matcher} to check the cause of exception.
   * @return this {@link CauseCheckableExpectedException} instance itself for method chaining.
   */
  public <T> CauseCheckableExpectedException expectCause(final Matcher<T> matcher)
  {
    return expectCause0(matcher);
  }

  private <T> CauseCheckableExpectedException expectCause0(final Matcher<T> matcher)
  {
    @SuppressWarnings("unchecked")
    final Matcher<Object> castedMatcher = (Matcher<Object>) matcher;
    /* @formatter:off */
    causeMatcher = null == causeMatcher ?
        castedMatcher :
        both(causeMatcher).and(castedMatcher);
    /* @formatter:on */
    return this;
  }

  /**
   * Adds the given type of {@link Throwable} to check if it is the cause of the thrown exception during test.
   *
   * @param type
   *          an expected type of the cause {@link Throwable}.
   * @return this {@link CauseCheckableExpectedException} instance itself for method chaining.
   */
  public <T extends Throwable> CauseCheckableExpectedException expectCause(final Class<T> type)
  {
    return expectCause0(instanceOf(type));
  }

  /**
   * Adds a part of message which the cause of thrown exception must contain in its message.
   *
   * @param substring
   *          a part of the cause of thrown exception.
   * @return this {@link CauseCheckableExpectedException} instance itself for method chaining.
   */
  public CauseCheckableExpectedException expectCauseMessageContains(final String substring)
  {
    return expectCauseMessageContains0(substring);
  }

  private CauseCheckableExpectedException expectCauseMessageContains0(final String substring)
  {
    return expectCauseMessage0(containsString(substring));
  }

  /**
   * Adds {@code matcher} to the list of requirements for the message returned from the cause of any thrown exception.
   *
   * @param matcher
   *          a matcher to check the cause exception message.
   * @return this {@link CauseCheckableExpectedException} instance itself for method chaining.
   */
  public CauseCheckableExpectedException expectCauseMessage(final Matcher<String> matcher)
  {
    return expectCauseMessage0(matcher);
  }

  private CauseCheckableExpectedException expectCauseMessage0(final Matcher<String> matcher)
  {
    return expectCause0(hasMessage(matcher));
  }

  class CauseCheckableExpectedExceptionStatement extends Statement
  {
    private final Statement nextStatement;

    public CauseCheckableExpectedExceptionStatement(final Statement base)
    {
      nextStatement = base;
    }

    @Override
    public void evaluate() throws Throwable
    {
      try
      {
        nextStatement.evaluate();
      }
      catch (final Throwable e)
      {
        /* checking cause is optional so it does not need to be checked if causeMatcher is null. */
        if (null != causeMatcher)
        {
          Assert.assertThat(e.getCause(), causeMatcher);
        }
        if (null == matcher)
        {
          throw e;
        }
        Assert.assertThat(e, matcher);
        return;
      }
      if (null != matcher)
      {
        throw new AssertionError("Expected test to throw " + StringDescription.toString(matcher));
      }
      if (null != causeMatcher)
      {
        throw new AssertionError("Expected test to throw a Throwable caused by "
            + StringDescription.toString(causeMatcher));
      }
    }
  }

  private Matcher<Throwable> hasMessage(final Matcher<String> matcher)
  {
    return new TypeSafeMatcher<Throwable>() {
      @Override
      public void describeTo(final Description description)
      {
        description.appendText("exception with message ");
        description.appendDescriptionOf(matcher);
      }

      @Override
      public boolean matchesSafely(final Throwable item)
      {
        return matcher.matches(item.getMessage());
      }
    };
  }
}