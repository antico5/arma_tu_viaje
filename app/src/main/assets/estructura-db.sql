CREATE TABLE VIAJE (_id INTEGER PRIMARY KEY AUTOINCREMENT,NOMBRE TEXT);
CREATE TABLE LUGAR (_id INTEGER PRIMARY KEY AUTOINCREMENT,NOMBRE TEXT, LATITUD DOUBLE, LONGITUD DOUBLE, DESCRIPCION TEXT, RATING FLOAT, ID_VIAJE INTEGER NOT NULL);
INSERT INTO VIAJE(_id,NOMBRE) VALUES(1,"Viaje a Cordoba");