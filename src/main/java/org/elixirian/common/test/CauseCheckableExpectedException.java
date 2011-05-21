package org.elixirian.common.test;

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
	 * @return a Rule that expects no exception to be thrown (identical to behaviour without this Rule)
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
	 * Adds {@code matcher} to the list of requirements for any thrown exception.
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
	 * Adds to the list of requirements for any thrown exception that it should be an instance of {@code type}
	 */
	public <T extends Throwable> CauseCheckableExpectedException expect(final Class<T> type)
	{
		return expect0(instanceOf(type));
	}

	/**
	 * Adds to the list of requirements for any thrown exception that it should <em>contain</em> string
	 * {@code substring}
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
	 */
	public CauseCheckableExpectedException expectMessage(final Matcher<String> matcher)
	{
		return expectMessage0(matcher);
	}

	private CauseCheckableExpectedException expectMessage0(final Matcher<String> matcher)
	{
		return expect0(hasMessage(matcher));
	}

	public <T> CauseCheckableExpectedException expectCause(final Matcher<T> matcher)
	{
		return expectCause0(matcher);
	}

	private <T> CauseCheckableExpectedException expectCause0(final Matcher<T> matcher)
	{
		@SuppressWarnings("unchecked")
		final Matcher<Object> castedMatcher = (Matcher<Object>) matcher;
		causeMatcher = null == causeMatcher ? castedMatcher : both(causeMatcher).and(castedMatcher);
		return this;
	}

	/**
	 * Adds to the list of requirements for any thrown exception that it should be an instance of {@code type}
	 */
	public <T extends Throwable> CauseCheckableExpectedException expectCause(final Class<T> type)
	{
		return expectCause0(instanceOf(type));
	}

	/**
	 * Adds to the list of requirements for any thrown exception that it should <em>contain</em> string
	 * {@code substring}
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
	 * Adds {@code matcher} to the list of requirements for the message returned from any thrown exception.
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
			catch (Throwable e)
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