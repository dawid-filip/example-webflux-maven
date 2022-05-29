CREATE TABLE pet (
   id LONG NOT NULL AUTO_INCREMENT,
   name CHARACTER VARYING(100) NOT NULL,
   age SMALLINT,
   weight SMALLINT,
   length SMALLINT
);

INSERT INTO pet VALUES (1, 'petName 1', 2, 4, 15);
INSERT INTO pet VALUES (2, 'petName 2', 3, 5, 17);
INSERT INTO pet VALUES (3, 'petName 3', 4, 6, 19);
INSERT INTO pet VALUES (4, 'petName 4', 5, 7, 22);
INSERT INTO pet VALUES (5, 'petName 5', 6, 7, 23);