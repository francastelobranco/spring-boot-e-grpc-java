package br.com.content4devs.exception;

import io.grpc.Status;

public abstract class BaseBusinessException extends RuntimeException {

    public BaseBusinessException(String message){
        super(message);
    }

    public abstract Status getStatusCode();
    public abstract String getErrorMessage();
}
