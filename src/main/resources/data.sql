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

INSERT INTO audit_entity (entity_value, entity_class, audited_on)
VALUES ('Owner(super=BasicAudit(createdAt=2022-01-01T00:00:01, updatedAt=2022-01-01T00:00:01, createdBy=USER 1, updatedBy=USER 1, version=0),
id=1, firstName=firstName 1, lastName=lastName 1, age=21, petIds=[1]', 'com.df.entity.Owner', '2022-01-01T00:00:01.1');
INSERT INTO audit_entity (entity_value, entity_class, audited_on)
VALUES ('Owner(super=BasicAudit(createdAt=2022-01-01T00:00:02, updatedAt=2022-01-02T00:00:02, createdBy=USER 2, updatedBy=USER 2, version=0),
id=2, firstName=firstName 2, lastName=lastName 2, age=22, petIds=[2, 3]', 'com.df.entity.Owner', '2022-01-01T00:00:02.1');
INSERT INTO audit_entity (entity_value, entity_class, audited_on)
VALUES ('Owner(super=BasicAudit(createdAt=2022-01-01T00:00:03, updatedAt=2022-01-02T00:00:03, createdBy=USER 3, updatedBy=USER 3, version=0),
id=3, firstName=firstName 3, lastName=lastName 3, age=23, petIds=[4, 5, 6]', 'com.df.entity.Owner', '2022-01-01T00:00:03.1');
INSERT INTO audit_entity (entity_value, entity_class, audited_on)
VALUES ('Owner(super=BasicAudit(createdAt=2022-01-01T00:00:01, updatedAt=2022-01-01T00:00:01, createdBy=USER 1, updatedBy=USER 1, version=0),
id=1, name=petName 1, age=1, weight=3, length=4', 'com.df.entity.Pet', '2022-01-01T00:00:01.1');
INSERT INTO audit_entity (entity_value, entity_class, audited_on)
VALUES ('Owner(super=BasicAudit(createdAt=2022-01-01T00:00:02, updatedAt=2022-01-01T00:00:02, createdBy=USER 2, updatedBy=USER 2, version=0),
id=2, name=petName 2, age=2, weight=4, length=5', 'com.df.entity.Pet', '2022-01-01T00:00:02.1');
INSERT INTO audit_entity (entity_value, entity_class, audited_on)
VALUES ('Owner(super=BasicAudit(createdAt=2022-01-01T00:00:03, updatedAt=2022-01-01T00:00:03, createdBy=USER 2, updatedBy=USER 2, version=0),
id=3, name=petName 3, age=3, weight=5, length=6', 'com.df.entity.Pet', '2022-01-01T00:00:03.1');
INSERT INTO audit_entity (entity_value, entity_class, audited_on)
VALUES ('Owner(super=BasicAudit(createdAt=2022-01-01T00:00:04, updatedAt=2022-01-01T00:00:04, createdBy=USER 3, updatedBy=USER 3, version=0),
id=4, name=petName 4, age=4, weight=6, length=7', 'com.df.entity.Pet', '2022-01-01T00:00:04.1');
INSERT INTO audit_entity (entity_value, entity_class, audited_on)
VALUES ('Owner(super=BasicAudit(createdAt=2022-01-01T00:00:05, updatedAt=2022-01-01T00:00:05, createdBy=USER 3, updatedBy=USER 3, version=0),
id=5, name=petName 5, age=5, weight=7, length=8', 'com.df.entity.Pet', '2022-01-01T00:00:05.1');
INSERT INTO audit_entity (entity_value, entity_class, audited_on)
VALUES ('Owner(super=BasicAudit(createdAt=2022-01-01T00:00:06, updatedAt=2022-01-01T00:00:06, createdBy=USER 3, updatedBy=USER 3, version=0),
id=6, name=petName 6, age=6, weight=8, length=9', 'com.df.entity.Pet', '2022-01-01T00:00:06.1');