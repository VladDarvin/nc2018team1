CREATE OR REPLACE TRIGGER generate_objtype_id
  BEFORE INSERT
  ON OBJTYPE
  FOR EACH ROW
DECLARE
  CURSOR c_id IS (SELECT nvl(max(OBJECT_TYPE_ID), 0)
                    FROM OBJTYPE);
  last_id NUMBER;
BEGIN
  OPEN c_id;
  FETCH c_id INTO last_id;
  IF :NEW.OBJECT_TYPE_ID IS NULL THEN
    :NEW.OBJECT_TYPE_ID := last_id + 1;
  END IF;
END;

CREATE OR REPLACE TRIGGER generate_attrtype_id
  BEFORE INSERT
  ON ATTRTYPE
  FOR EACH ROW
DECLARE
  CURSOR c_id IS (SELECT nvl(max(ATTR_ID), 0)
                    FROM ATTRTYPE);
  last_id NUMBER;
BEGIN
  OPEN c_id;
  FETCH c_id INTO last_id;
  IF :NEW.ATTR_ID IS NULL THEN
    :NEW.ATTR_ID := last_id + 1;
  END IF;
END;

CREATE OR REPLACE TRIGGER generate_obj_id
  BEFORE INSERT
  ON OBJECTS
  FOR EACH ROW
DECLARE
  CURSOR c_id IS (SELECT nvl(max(OBJECT_ID), 0)
                    FROM OBJECTS);
  last_id NUMBER;
BEGIN
  OPEN c_id;
  FETCH c_id INTO last_id;
  IF :NEW.OBJECT_ID IS NULL THEN
    :NEW.OBJECT_ID := last_id + 1;
  END IF;
END;
