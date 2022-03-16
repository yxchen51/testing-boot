package com.open.qa.junit.rule;

import com.open.qa.domain.StepType;
import com.open.qa.junit.log.StepLogger;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIn;
import org.junit.Assert;
import org.junit.rules.Verifier;
import org.junit.runners.model.MultipleFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * 自定义的断言工具类
 * @author : liang.chen
 * create in 2018/7/18 下午2:40
 */
public class Assertor extends Verifier {


    private static final Logger logger = LoggerFactory.getLogger(Assertor.class);

    private List<Throwable> errors = new ArrayList();
    private Boolean hasError = Boolean.valueOf(false);
    private Boolean isBreak = Boolean.valueOf(false);

    protected void verify()
            throws Throwable
    {
        MultipleFailureException.assertEmpty(this.errors);
    }

    private void addError(Throwable error)
    {
        this.errors.add(error);
    }

    private <T> void checkThat(final String reason, final T value, final Matcher<T> matcher)
    {
//    if (getIsSkip().booleanValue()) {
//      return;
//    }
        this.hasError = Boolean.valueOf(false);
        checkSucceeds(new Callable()
        {
            public Object call()
                    throws Exception
            {
                Assert.assertThat(reason, value, matcher);
                return value;
            }
        });
    }

    private Object checkSucceeds(Callable<Object> callable) {
        try {
            return callable.call();
        }
        catch (Throwable e) {
            this.hasError = Boolean.valueOf(true);
            addError(e);
        }
        return null;
    }

    public void assertTrue(boolean actual)
    {
        assertTrue(actual, "");
    }

    //  public void assertTrue(boolean actual, String message)
//  {
//    assertTrue(actual, message);
//  }
//
    public void assertTrue(boolean actual, String message)
    {
        String methodInfo = "assertTrue(" + String.valueOf(actual) + "," + message + ")";
        checkThat(message, Boolean.valueOf(actual), Matchers.is(Boolean.valueOf(true)));
        assertLog(methodInfo, message);
    }

    public void assertFalse(boolean actual)
    {
        assertFalse(actual, "");
    }



    public void assertFalse(boolean actual, String message)
    {
        String methodInfo = "assertFalse(" + String.valueOf(actual) + "," + message + ")";
        checkThat(message, Boolean.valueOf(actual), Matchers.is(Boolean.valueOf(false)));
        assertLog(methodInfo, message);
    }

    public <T> void assertEqual(T actual, T expect)
    {
        assertEqual(actual, expect, "");
    }



    public <T> void assertEqual(T actual, T expect, String message )
    {
        String methodInfo = "assertEqual(" + String.valueOf(actual) + "," + String.valueOf(expect) + "," + message + ")";
        checkThat(message, actual, Matchers.equalTo(expect));
        assertLog(methodInfo, message);
    }

    public void assertMatch(String actual, String regxp)
    {
        assertMatch(actual, regxp, "");
    }



    public void assertMatch(String actual, String regxp, String message)
    {
        checkThat(message, Boolean.valueOf(actual.matches(regxp)), Matchers.is(Boolean.valueOf(true)));
        String methodInfo = "assertMatch(" + String.valueOf(actual) + "," + String.valueOf(regxp) + "," + message + ")";
        assertLog(methodInfo, message);
    }

    public void assertContains(String actual, String expect)
    {
        assertContains(actual, expect, "");
    }



    public void assertContains(String actual, String expect, String message)
    {
        checkThat(message, actual, Matchers.is(Matchers.containsString(expect)));
        String methodInfo = "assertContains(" + String.valueOf(actual) + "," + String.valueOf(expect) + "," + message + ")";
        assertLog(methodInfo, message);
    }

    public <T> void assertContains(Collection<T> actual, T expect)
    {
        assertContains(actual, expect, "");
    }



    public <T> void assertContains(Collection<T> actual, T expect, String message) {
        checkThat(message, actual, Matchers.hasItem(expect));
        String methodInfo = "assertContains(" + String.valueOf(actual) + "," + String.valueOf(expect) + "," + message + ")";
        assertLog(methodInfo, message);
    }

    public void assertIn(String actual, String expect)
    {
        assertIn(actual, expect, "");
    }



    public void assertIn(String actual, String expect, String message) {
        checkThat(message, expect, Matchers.is(Matchers.containsString(actual)));
        String methodInfo = "assertIn(" + String.valueOf(actual) + "," + String.valueOf(expect) + "," + message + ")";
        assertLog(methodInfo, message);
    }

    public <T> void assertIn(T actual, Collection<T> expect)
    {
        assertIn(actual, expect, "");
    }



    public <T> void assertIn(T actual, Collection<T> expect, String message ) {
        checkThat(message, actual, IsIn.isIn(expect));
        String methodInfo = "assertIn(" + String.valueOf(actual) + "," + String.valueOf(expect) + "," + message + ")";
        assertLog(methodInfo, message);
    }

    public <T> void assertOut(T actual, Collection<T> expect)
    {
        assertOut(actual, expect, "");
    }



    public <T> void assertOut(T actual, Collection<T> expect, String message) {
        checkThat(message, actual, Matchers.not(IsIn.isIn(expect)));
        String methodInfo = "assertOut(" + String.valueOf(actual) + "," + String.valueOf(expect) + "," + message + ")";
        assertLog(methodInfo, message);
    }

    private void assertLog(String methodInfo, String message) {
//    if (getIsSkip().booleanValue()) {
//      return;
//    }
        if (this.hasError.booleanValue()) {
            String errorMessage = ((Throwable)this.errors.get(this.errors.size() - 1)).getMessage();
//      errorMessage = HtmlUtils.htmlEscape(errorMessage);
//      System.err.println(methodInfo);
            StepLogger.logStepFail(StepType.ASSEROT, methodInfo, errorMessage);
            if (this.isBreak.booleanValue()) {
                throw new RuntimeException(errorMessage);
            }
        } else {
            StepLogger.logStepPass(StepType.ASSEROT,methodInfo);
//      System.out.println(methodInfo);
        }
    }

    public void setIsBreak(Boolean isBreak)
    {
        this.isBreak = isBreak;
    }

    public Boolean getIsBreak()
    {
        return this.isBreak;
    }


}
