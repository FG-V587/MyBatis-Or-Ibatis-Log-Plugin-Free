//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.mt.mybatislog.enums;

public enum JavaTypeEnum {
    String(false, (String)null),
    Date(true, "date"),
    Time(true, "time"),
    Timestamp(true, "timestamp"),
    LocalDate(true, "date"),
    LocalTime(true, "time"),
    LocalDateTime(true, "timestamp");

    private final boolean showLiteral;
    private final String literalName;

    private JavaTypeEnum(boolean showLiteral, String literalName) {
        this.showLiteral = showLiteral;
        this.literalName = literalName;
    }

    public boolean isShowLiteral() {
        return this.showLiteral;
    }

    public String getLiteralName() {
        return this.literalName;
    }
}
