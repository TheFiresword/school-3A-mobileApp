DROP TABLE rescuers;
CREATE TABLE rescuers (
  id INT(11) NOT NULL AUTO_INCREMENT,
  firstname VARCHAR(255) NOT NULL,
  lastname VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL CHECK (LENGTH(password) >= 8),
  telephone VARCHAR(255) DEFAULT "",
  disponibility BOOLEAN DEFAULT 1,
  description TEXT DEFAULT "",
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
)DEFAULT CHARSET=utf8;

SET NAMES utf8;

INSERT INTO rescuers (firstname, lastname, email, password) VALUES 
("Junior", "SEDOGBO", "junior.sedogbo@insa-cvl.fr", "mhuzblwhgiOKf"),
("Siméon", "GRAVIS", "simeon.gravis@insa-cvl.fr", "mhuzblwhgiOKf"),
("François", "PARACHE", "francois.parache@insa-cvl.fr", "mhuzblwhgiOKf"),
("Yann", "GAUDICHAUD", "yann.gaudichaud@insa-cvl.fr", "mhuzblwhgiOKf"),
("Antoine", "GRAND", "antoine.grand@insa-cvl.fr", "mhuzblwhgiOKf"),
("Pierre-Gilles", "LEBOTLAN", "pierre-gilles.le_botlan@insa-cvl.fr", "mhuzblwhgiOKf")