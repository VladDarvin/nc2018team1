-- add object
INSERT INTO NC_AIRPORT.OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (20, null, 15, 'admin', null);

-- login = admin
INSERT INTO NC_AIRPORT.ATTRIBUTES (ATTR_ID, OBJECT_ID, VALUE, DATE_VALUE, LIST_VALUE_ID) VALUES (44, 20, 'admin', null, null);
-- password = admin
INSERT INTO NC_AIRPORT.ATTRIBUTES (ATTR_ID, OBJECT_ID, VALUE, DATE_VALUE, LIST_VALUE_ID) VALUES (45, 20, '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', null, null);
-- email
INSERT INTO NC_AIRPORT.ATTRIBUTES (ATTR_ID, OBJECT_ID, VALUE, DATE_VALUE, LIST_VALUE_ID) VALUES (46, 20, 'admin@admin.com', null, null);
-- phone
INSERT INTO NC_AIRPORT.ATTRIBUTES (ATTR_ID, OBJECT_ID, VALUE, DATE_VALUE, LIST_VALUE_ID) VALUES (47, 20, '+380 66 666 6663', null, null);
-- nickname
INSERT INTO NC_AIRPORT.ATTRIBUTES (ATTR_ID, OBJECT_ID, VALUE, DATE_VALUE, LIST_VALUE_ID) VALUES (48, 20, 'Admin', null, null);
-- activated
INSERT INTO NC_AIRPORT.ATTRIBUTES (ATTR_ID, OBJECT_ID, VALUE, DATE_VALUE, LIST_VALUE_ID) VALUES (59, 20, 'true', null, null);
-- ROLE_ADMIN
INSERT INTO NC_AIRPORT.ATTRIBUTES (ATTR_ID, OBJECT_ID, VALUE, DATE_VALUE, LIST_VALUE_ID) VALUES (49, 20, null, null, 1);
