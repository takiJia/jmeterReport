package org.programmerplanet.ant.taskdefs.jmeter;

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
    private String suiteName;
    private AssertionResult assertionResult;

    public boolean isStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getResponseMessage() {
        return this.responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public AssertionResult getAssertionResult() {
        return this.assertionResult;
    }

    public void setAssertionResult(AssertionResult assertionResult) {
        this.assertionResult = assertionResult;
    }

    public String getCaseName() {
        return this.caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getResponseData() {
        return this.responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getQueryString() {
        return this.queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRunTime() {
        return this.runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getSuiteName() {
        return this.suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }
}