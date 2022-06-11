package com.df.columns;

public enum PetColumns {

    ID("id"),
    NAME("name"),
    AGE("age"),
    WEIGHT("weight"),
    LENGTH("length"),
    CREATED_AT("created_at"),
    CREATED_BY("created_by"),
    UPDATED_AT("updated_at"),
    UPDATED_BY("updated_by"),
    VERSION("version");

    private final String value;

    PetColumns(String value) {
        this.value = "pet_" + value;
    }

    public String toString() {
        return this.value;
    }

}
