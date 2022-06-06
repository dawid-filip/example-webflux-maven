INSERT INTO pet (name, age, weight, length, created_at, created_by, updated_at, updated_by, version)
VALUES ('petName 1', 1, 3, 4, '2022-01-01T00:00:01', 'USER 1', '2022-01-01T00:00:01', 'USER 1', 0);
INSERT INTO pet (name, age, weight, length, created_at, created_by, updated_at, updated_by, version)
VALUES ('petName 2', 2, 4, 5, '2022-01-01T00:00:02', 'USER 2', '2022-01-01T00:00:02', 'USER 2', 0);
INSERT INTO pet (name, age, weight, length, created_at, created_by, updated_at, updated_by, version)
VALUES ('petName 3', 3, 5, 6, '2022-01-01T00:00:03', 'USER 2', '2022-01-01T00:00:03', 'USER 2', 0);
INSERT INTO pet (name, age, weight, length, created_at, created_by, updated_at, updated_by, version)
VALUES ('petName 4', 4, 6, 7, '2022-01-01T00:00:04', 'USER 3', '2022-01-01T00:00:04', 'USER 3', 0);
INSERT INTO pet (name, age, weight, length, created_at, created_by, updated_at, updated_by, version)
VALUES ('petName 5', 5, 7, 8, '2022-01-01T00:00:05', 'USER 3', '2022-01-01T00:00:05', 'USER 3', 0);
INSERT INTO pet (name, age, weight, length, created_at, created_by, updated_at, updated_by, version)
VALUES ('petName 6', 6, 8, 9, '2022-01-01T00:00:06', 'USER 3', '2022-01-01T00:00:06', 'USER 3', 0);

INSERT INTO owner (first_name, last_name, age, id_pet, created_at, created_by, updated_at, updated_by, version)
VALUES ('firstName 1', 'lastName 1', 21, ARRAY[1], '2022-01-01T00:00:01', 'USER 1', '2022-01-01T00:00:01', 'USER 1', 0);
INSERT INTO owner (first_name, last_name, age, id_pet, created_at, created_by, updated_at, updated_by, version)
VALUES ('firstName 2', 'lastName 2', 22, ARRAY[2, 3], '2022-01-01T00:00:02', 'USER 2', '2022-01-01T00:00:01', 'USER 2', 0);
INSERT INTO owner (first_name, last_name, age, id_pet, created_at, created_by, updated_at, updated_by, version)
VALUES ('firstName 3', 'lastName 3', 23, ARRAY[4, 5, 6], '2022-01-01T00:00:02', 'USER 3', '2022-01-01T00:00:01', 'USER 3', 0);
