package org.data.exproter.enums;

public enum SheetType {
    XLS(".xls");
    private final String type;
    SheetType(String type) {
        this.type = type;
    }
}
