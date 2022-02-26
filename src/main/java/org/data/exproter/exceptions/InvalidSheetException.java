package org.data.exproter.exceptions;

public class InvalidSheetException extends RuntimeException {
    private String s;
    public InvalidSheetException(String s) {
        this.s = s;
    }
}
