package com.upgrad.reddit.service.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * InvalidPostException is thrown when the question is not found in the database.
 */
public class InvalidPostException extends Exception {
    private final String code;
    private final String errorMessage;

    public InvalidPostException(final String code, final String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }

    @Override
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
    }

    public String getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}

