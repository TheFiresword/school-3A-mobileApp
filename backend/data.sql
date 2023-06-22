DROP TABLE IF EXISTS rescuers;

CREATE TABLE rescuers (
  id SERIAL PRIMARY KEY,
  firstname VARCHAR(255) NOT NULL,
  lastname VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  telephone VARCHAR(255) DEFAULT 'NaN',
  disponibility BOOLEAN DEFAULT true,
  description TEXT DEFAULT 'NaN',
  tokenFirebase Varchar(255) DEFAULT 'NaN',
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Création de la fonction pour mettre à jour automatiquement le champ updated_at
CREATE OR REPLACE FUNCTION update_rescuers_updated_at()
  RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = now();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Création du déclencheur (trigger) pour appeler la fonction lors de la mise à jour des enregistrements
CREATE TRIGGER rescuers_update_updated_at
BEFORE UPDATE ON rescuers
FOR EACH ROW
EXECUTE FUNCTION update_rescuers_updated_at();

INSERT INTO rescuers (firstname, lastname, email, password)
VALUES
  ('admin', 'admin', 'sstou.insa.projet@gmail.com', '$2b$10$ss/OyI7ZyNM.Hn67rRQVwORoo5x1.Gh1jEz4aY6NxpiAGDHm8SPWq'), 
  ('Junior', 'SEDOGBO', 'junior.sedogbo@insa-cvl.fr', '$2b$10$6aFT1hcjQnc0nMk6qJrsvu7oVReRQfqU7kVc17WkM0c8H2yU0zvoC'),
  ('Siméon', 'GRAVIS', 'simeon.gravis@insa-cvl.fr', '$2b$10$6aFT1hcjQnc0nMk6qJrsvu7oVReRQfqU7kVc17WkM0c8H2yU0zvoC'),
  ('François', 'PARACHE', 'francois.parache@insa-cvl.fr', '$2b$10$6aFT1hcjQnc0nMk6qJrsvu7oVReRQfqU7kVc17WkM0c8H2yU0zvoC'),
  ('Yann', 'GAUDICHAUD', 'yann.gaudichaud@insa-cvl.fr', '$2b$10$6aFT1hcjQnc0nMk6qJrsvu7oVReRQfqU7kVc17WkM0c8H2yU0zvoC'),
  ('Antoine', 'GRAND', 'antoine.grand@insa-cvl.fr', '$2b$10$6aFT1hcjQnc0nMk6qJrsvu7oVReRQfqU7kVc17WkM0c8H2yU0zvoC'),
  ('Pierre-Gilles', 'LEBOTLAN', 'pierre-gilles.le_botlan@insa-cvl.fr', '$2b$10$6aFT1hcjQnc0nMk6qJrsvu7oVReRQfqU7kVc17WkM0c8H2yU0zvoC');