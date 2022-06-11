package com.df.repository;

public class SqlQuery {

    public static final String SELECT_JOIN_AS_OWNER_DTO =
            "SELECT owner.id as owner_id, owner.first_name as owner_first_name, owner.last_name as owner_last_name, owner.age as owner_age, owner.id_pet as owner_id_pet, " +
            "owner.created_at as owner_created_at, owner.created_by as owner_created_by, owner.updated_at as owner_updated_at, owner.updated_by as owner_updated_by, owner.version as owner_version, " +
            "pet.id as pet_id, pet.name as pet_name, pet.age as pet_age, pet.weight as pet_weight, pet.length as pet_length, " +
            "pet.created_at as pet_created_at, pet.created_by as pet_created_by, pet.updated_at as pet_updated_at, pet.updated_by as pet_updated_by, pet.version as pet_version " +
            "FROM owner LEFT JOIN pet ON ARRAY_CONTAINS(owner.id_pet, pet.id); ";

    public static final String SELECT_PET =
            "SELECT pet.id as pet_id, pet.name as pet_name, pet.age as pet_age, pet.weight as pet_weight, pet.length as pet_length, " +
            "pet.created_at as pet_created_at, pet.created_by as pet_created_by, pet.updated_at as pet_updated_at, pet.updated_by as pet_updated_by, pet.version as pet_version " +
            "FROM pet; ";

}
