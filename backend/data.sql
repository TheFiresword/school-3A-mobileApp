DROP TABLE rescuers;
CREATE TABLE rescuers (
  id INT(11) NOT NULL AUTO_INCREMENT,
  firstname VARCHAR(255) NOT NULL,
  lastname VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  telephone VARCHAR(255) DEFAULT "NaN",
  disponibility BOOLEAN DEFAULT 1,
  description TEXT DEFAULT "NaN",
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
)  DEFAULT COLLATE utf8_general_ci
   DEFAULT CHARACTER SET utf8;

INSERT INTO rescuers (firstname, lastname, email, password) VALUES
("admin", "admin", "sstou.insa.projet@gmail.com", "$2b$10$ss/OyI7ZyNM.Hn67rRQVwORoo5x1.Gh1jEz4aY6NxpiAGDHm8SPWq"), 
("Junior", "SEDOGBO", "junior.sedogbo@insa-cvl.fr", "$2b$10$6aFT1hcjQnc0nMk6qJrsvu7oVReRQfqU7kVc17WkM0c8H2yU0zvoC"),
("Siméon", "GRAVIS", "simeon.gravis@insa-cvl.fr", "$2b$10$6aFT1hcjQnc0nMk6qJrsvu7oVReRQfqU7kVc17WkM0c8H2yU0zvoC"),
("François", "PARACHE", "francois.parache@insa-cvl.fr", "$2b$10$6aFT1hcjQnc0nMk6qJrsvu7oVReRQfqU7kVc17WkM0c8H2yU0zvoC"),
("Yann", "GAUDICHAUD", "yann.gaudichaud@insa-cvl.fr", "$2b$10$6aFT1hcjQnc0nMk6qJrsvu7oVReRQfqU7kVc17WkM0c8H2yU0zvoC"),
("Antoine", "GRAND", "antoine.grand@insa-cvl.fr", "$2b$10$6aFT1hcjQnc0nMk6qJrsvu7oVReRQfqU7kVc17WkM0c8H2yU0zvoC"),
("Pierre-Gilles", "LEBOTLAN", "pierre-gilles.le_botlan@insa-cvl.fr", "$2b$10$6aFT1hcjQnc0nMk6qJrsvu7oVReRQfqU7kVc17WkM0c8H2yU0zvoC");