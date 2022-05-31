-- CREATE SEQUENCE pet_seq AS INTEGER START WITH 1 INCREMENT BY 1;

CREATE TABLE pet (
   id LONG AUTO_INCREMENT NOT NULL,
   name CHARACTER VARYING(100) NOT NULL,
   age SMALLINT,
   weight SMALLINT,
   length SMALLINT
);

CREATE TABLE owner (
    id LONG AUTO_INCREMENT  NOT NULL,
    first_name CHARACTER VARYING(100) NOT NULL,
    last_name CHARACTER VARYING(100) NOT NULL,
    age SMALLINT,
    id_pet INTEGER ARRAY
);
