const express = require("express");
const bodyParser = require('body-parser');
const { Pool } = require('pg');
const compression = require('compression');
const helmet = require('helmet');
const RateLimit = require('express-rate-limit');

const {db_connection_details, app_config_details}  = require('./config')


//Connection à la base de données mysql (en local pour le moment)
const connection_pool = new Pool({
    connectionString: `postgres://${db_connection_details.user}:${db_connection_details.password}@${db_connection_details.host}:${db_connection_details.port}/${db_connection_details.database}`,
    ssl: {
      rejectUnauthorized: false // Ignorer les erreurs de certificat SSL
    }
  });

async function connecToDatabase(){
    try{
        await connection_pool.connect();
        console.log('Connecté à la base de données');
    }catch(error){
        console.error('Erreur de connexion', error);
    }
}
connecToDatabase();

//Création de l'application express
const app = express()

//middleware pour parser le body d'une requête
app.use(bodyParser.json());

//middleware pour transmettre l'objet connection_pool
app.use((req, res, next)=>{
    req.socket = connection_pool;
    next();
})

// middleware pour compresser les réponses envoyées (performance)
app.use(compression());

// middleware contre les attaques css, xml, ...
app.use(helmet());

// middleware pour limiter le nombre de connexions
// Pour le moment on peut faire 97 requêtes simultanées à la BDD
// Estimation de 5 requetes sql simultanées par utilisateur
// => limite de 19 utilisateurs connectés simultanément
const limiter = RateLimit({
    windowMs: 1*60*1000,
    max: 19
})
app.use(limiter);


const rescuersRoutes = require('./routes/rescuersRoutes')
app.use('/rescuers', rescuersRoutes)

const authentificationRoutes = require('./routes/authentification');
app.use('/authentification', authentificationRoutes)

//Démarrage du serveur et gestion de la clôture
app.listen(app_config_details.port, ()=>console.log(`Serveur lancé sur le port ${app_config_details.port}`))
app.on('close', ()=>{
    connection_pool.end();
})