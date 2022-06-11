package com.df.columns;

public enum OwnerColumn {

    ID("id"),
    FIRST_NAME("first_name"),
    LAST_NAME("last_name"),
    AGE("age"),
    ID_PET("id_pet"),
    CREATED_AT(BasicAuditColumn.CREATED_AT.toString()),
    CREATED_BY(BasicAuditColumn.CREATED_BY.toString()),
    UPDATED_AT(BasicAuditColumn.UPDATED_AT.toString()),
    UPDATED_BY(BasicAuditColumn.UPDATED_BY.toString()),
    VERSION(BasicAuditColumn.VERSION.toString());

    private final String value;

    OwnerColumn(String value) {
        this.value = ClassEntity.OWNER.toString() + value;
    }

    @Override
    public String toString() {
        return this.value;
    }

}
