package com.df.columns;

public enum ClassEntity {

    PET("pet_"),
    OWNER("owner_"),
    AUDIT("audit_");

    private final String value;

    ClassEntity(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

}
