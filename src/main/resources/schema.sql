CREATE TABLE pet (
   id LONG AUTO_INCREMENT NOT NULL,
   name CHARACTER VARYING(100) NOT NULL,
   age SMALLINT,
   weight SMALLINT,
   length SMALLINT,
   created_at TIMESTAMP,
   created_by CHARACTER VARYING(100),
   updated_at TIMESTAMP,
   updated_by CHARACTER VARYING(100),
   version LONG,
   PRIMARY KEY (id)
);

CREATE TABLE owner (
   id LONG AUTO_INCREMENT NOT NULL,
   first_name CHARACTER VARYING(100) NOT NULL,
   last_name CHARACTER VARYING(100) NOT NULL,
   age SMALLINT,
   id_pet INTEGER ARRAY,
   created_at TIMESTAMP,
   created_by CHARACTER VARYING(100),
   updated_at TIMESTAMP,
   updated_by CHARACTER VARYING(100),
   version LONG,
   PRIMARY KEY (id)
);