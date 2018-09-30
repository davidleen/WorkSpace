package com.giants3.hd.domain.exception;

/**
 * Created by david on 2015/9/14.
 */
public interface ErrorBundle {


    Exception getException();
    String getErrorMessage();
}
