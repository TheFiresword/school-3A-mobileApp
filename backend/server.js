const express = require("express")
const mysql = require('mysql2')
const bodyParser = require('body-parser')
const connection_details = require('./config')

//Connection à la base de données mysql (en local pour le moment)

const connection = mysql.createConnection({
    host: connection_details.host,
    user: connection_details.user,
    password: connection_details.password,
    database: connection_details.database,
    charset: 'utf8'
})

async function connecToDatabase(){
    try{
        await connection.connect();
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

//middleware pour transmettre l'objet connection
app.use((req, res, next)=>{
    req.socket = connection;
    next();
})

const rescuersRoutes = require('./routes/rescuersRoutes')
app.use('/rescuers', rescuersRoutes)

const authentificationRoutes = require('./routes/authentification')
app.use('/authentification', authentificationRoutes)

//Démarrage du serveur et gestion de la clôture
const app_port = 3000;
app.listen(app_port, ()=>console.log(`Serveur lancé sur le port ${app_port}`))
app.on('close', ()=>{
    connection.end();
})