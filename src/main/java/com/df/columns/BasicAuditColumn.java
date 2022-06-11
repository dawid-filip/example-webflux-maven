package com.df.columns;

public enum BasicAuditColumn {

    CREATED_AT("created_at"),
    CREATED_BY("created_by"),
    UPDATED_AT("updated_at"),
    UPDATED_BY("updated_by"),
    VERSION("version");

    private final String value;

    BasicAuditColumn(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

}
