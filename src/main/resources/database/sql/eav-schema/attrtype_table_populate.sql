DELETE FROM NC_AIRPORT.ATTRTYPE;

INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (1, 1, null, 'NAME', 'Country name');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (2, 2, null, 'NAME', 'Airport name');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (3, 2, 1, 'CITY_ID', 'Reference on teh city');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (4, 2, null, 'ADDRESS', 'Address of the airport');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (6, 3, null, 'DEPARTURE_DATETIME', 'Departure time');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (7, 3, null, 'ARRIVAL_DATETIME', 'Arrival time');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (8, 3, 5, 'AIRPLANE_ID', 'Reference on the airplane');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (9, 3, null, 'BASE_COST', 'Base cost');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (10, 3, 2, 'ARRIVAL_AIRPORT', 'Arrival airport');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (11, 3, 2, 'DEPARTURE_AIRPORT', 'Departure airport');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (12, 4, null, 'NAME', 'Name of the airline');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (13, 4, null, 'DESCR', 'Description of the airline');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (14, 4, null, 'PHONE', 'Phone number of the airline');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (15, 4, null, 'EMAIL', 'Email of the airline');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (16, 5, null, 'MODEL', 'Model of the airplane');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (17, 5, 4, 'AIRLINE_ID', 'Reference on the airline of the airplane');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (18, 6, 7, 'EXTRA_TYPE_ID', 'Reference on the extra type');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (19, 6, 5, 'AIRPLANE_ID', 'Reference on the airplane');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (20, 7, null, 'NAME', 'Name of the extra type');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (21, 7, null, 'DESCR', 'Description of the extra type');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (22, 7, null, 'BASE_COST', 'Base cost of the extra type');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (23, 8, 5, 'AIRPLANE_ID', 'Reference on the airplane');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (24, 8, 9, 'SEAT_TYPE_ID', 'Reference on the seat type');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (25, 8, null, 'ROW', 'Row of the seat');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (26, 8, null, 'COL', 'Column of the seat');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (27, 9, null, 'NAME', 'Name of the seat type');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (28, 9, null, 'DESCR', 'Description of the seat type');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (29, 9, null, 'BASECOST', 'Base cost of the seat type');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (30, 10, 3, 'FLIGHT_ID', 'Reference on the flight');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (31, 10, 8, 'SEAT_ID', 'Reference on the seat');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (32, 10, 13, 'PASSENGER_ID', 'Reference on the passenger');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (33, 10, null, 'TICKET_STATUS', 'Ticket status');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (34, 11, 10, 'TICKET_ID', 'Reference on the ticket');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (35, 11, 6, 'EXTRA_ID', 'Reference on the extra');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (36, 11, null, 'QUANTITY', 'Quantity of the extras in the ticket');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (38, 13, null, 'FIRST_NAME', 'First name of the passenger');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (39, 13, null, 'LAST_NAME', 'Last name of the passenger');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (40, 13, 14, 'PASSPORT_ID', 'Reference on the passenger''s passport');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (41, 14, null, 'SERIAL_NUMBER', 'SERIAL NUMBER of the passport');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (42, 14, null, 'COUNTRY', 'Country name of the passport');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (43, 14, null, 'BIRTH_DATE', 'Birth date in the passport');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (44, 15, null, 'LOGIN', 'Login name of the user');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (45, 15, null, 'PASSWORD', 'Password of the user');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (46, 15, null, 'EMAIL', 'Email of the user');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (47, 15, null, 'PHONE', 'Phone of the user');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (48, 15, null, 'NICKNAME', 'Nickname of the user');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (49, 15, null, 'AUTHORITY', 'Authority enum');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (50, 16, null, 'NUMBER', 'Number of the credit card');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (51, 16, null, 'MONTH', 'Month of expiration date of the credit card');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (52, 16, null, 'YEAR', 'Year of expiration date of the credit card');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (53, 16, null, 'CVV', 'CVV code of the credit card');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (54, 16, null, 'NICKNAME', 'Nickname of the credit card');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (55, 16, 15, 'USER_ID', 'Reference on the user of card');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (57, 18, 10, 'ID_TICKET', 'Reference on the ticket');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (58, 18, 15, 'USER_ID', 'Reference on the user');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (59, 15, null, 'ENABLED', 'Is user enabled');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (60, 19, null, 'NAME', 'City name');
INSERT INTO NC_AIRPORT.ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (61, 19, 1, 'COUNTRY_ID', 'Reference on the country');
