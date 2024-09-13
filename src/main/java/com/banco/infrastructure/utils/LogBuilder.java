package com.banco.infrastructure.utils;

public class LogBuilder {
    
    private static final String HEADER = "\n => ";
    private static final String ROW_PREFIX = "\n - ";
    private static final String ROW_DEVIDER = ": ";

    private StringBuilder stringBuilder;

    private LogBuilder() {
        this.stringBuilder = new StringBuilder();
    }

    public static LogBuilder of() {
        return new LogBuilder();
    }

    public LogBuilder header(String header) {
        this.stringBuilder.append(HEADER);
        this.stringBuilder.append(header);

        return this;
    }

    public LogBuilder row(String row, Object value) {
        this.stringBuilder.append(ROW_PREFIX);
        this.stringBuilder.append(row);
        this.stringBuilder.append(ROW_DEVIDER);
        this.stringBuilder.append(value);

        return this;
    }

    public String build() {
        return this.stringBuilder.toString();
    }

}
