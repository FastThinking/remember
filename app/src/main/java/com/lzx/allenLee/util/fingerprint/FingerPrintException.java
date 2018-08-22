package com.lzx.allenLee.util.fingerprint;


public class FingerPrintException extends RuntimeException {
    public FingerPrintException(String message) {
        super(message);
    }

    public FingerPrintException(Throwable e) {
        super(e);
    }
}
