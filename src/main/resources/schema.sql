-- CREATE SEQUENCE pet_seq AS INTEGER START WITH 1 INCREMENT BY 1;

CREATE TABLE pet (
   id LONG AUTO_INCREMENT NOT NULL,
   name CHARACTER VARYING(100) NOT NULL,
   age SMALLINT,
   weight SMALLINT,
   length SMALLINT
);