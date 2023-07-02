package com.josevabo.exception;

public class AssinaturaExpiradaException extends RuntimeException{

    public AssinaturaExpiradaException() {
    }

    public AssinaturaExpiradaException(String message) {
        super(message);
    }
}
