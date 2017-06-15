package org.programmerplanet.ant.taskdefs.jmeter;

/**
 * Created with IntelliJ IDEA.
 * User: jiaou
 * Date: 16-11-7
 * Time: 下午2:35
 * To change this template use File | Settings | File Templates.
 */
public class AssertionResult {
    private String name;
    private boolean failure;
    private boolean error;
    private String failureMessage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFailure() {
        return failure;
    }

    public void setFailure(boolean failure) {
        this.failure = failure;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }
}
