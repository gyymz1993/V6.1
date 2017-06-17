package com.lsjr.zizisteward.http;

/**
 * Created by lcq on 2017/1/18.
 */
public class MyError {
    private ErrorType errorType;
    private String errorMessage;

    public MyError(ErrorType errorType, String errorMessage) {
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
