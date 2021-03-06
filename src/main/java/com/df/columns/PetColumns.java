package com.df.columns;

public enum PetColumns {

    ID("id"),
    NAME("name"),
    AGE("age"),
    WEIGHT("weight"),
    LENGTH("length"),
    CREATED_AT(BasicAuditColumn.CREATED_AT.toString()),
    CREATED_BY(BasicAuditColumn.CREATED_BY.toString()),
    UPDATED_AT(BasicAuditColumn.UPDATED_AT.toString()),
    UPDATED_BY(BasicAuditColumn.UPDATED_BY.toString()),
    VERSION(BasicAuditColumn.VERSION.toString());

    private final String value;

    PetColumns(String value) {
        this.value = ClassEntity.PET.toString() + value;
    }

    @Override
    public String toString() {
        return this.value;
    }

}
