package org.programmerplanet.ant.taskdefs.jmeter;

/**
 * Created with IntelliJ IDEA.
 * User: jiaou
 * Date: 16-11-7
 * Time: 下午2:30
 * To change this template use File | Settings | File Templates.
 */
public class ReportCase {
    private String caseName;
    private String responseData;
    private String methodName;
    private String queryString;
    private String url;
    private String runTime;
    private String responseCode;
    private String responseMessage;
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    private AssertionResult assertionResult;

    public AssertionResult getAssertionResult() {
        return assertionResult;
    }

    public void setAssertionResult(AssertionResult assertionResult) {
        this.assertionResult = assertionResult;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
}
