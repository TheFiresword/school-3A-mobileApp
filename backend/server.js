const express = require("express")
const ibmdb = require("ibm_db")
const connection_details = require('./config')
/**
 * Connection à la base de données sur mon cloud ibm db2
 */
const connString = `DATABASE=${connection_details.database};HOSTNAME=${connection_details.host};
PORT=${connection_details.port};PROTOCOL=TCPIP;UID=${connection_details.user};PWD=${connection_details.password};`;
const connectionString = 'DATABASE=BLUDB;HOSTNAME=21fecfd8-47b7-4937-840d-d791d0218660.bs2io90l08kqb1od8lcg.databases.appdomain.cloud;PORT=31864;PROTOCOL=TCPIP;UID=qmx13331;PWD=GgrJI1i1G7GVSD2t;security=SSL';
                                                    
ibmdb.open(connectionString, (err, conn)=>{
    if(err){
        console.error(`Erreur de connexion à la base de données: ${err}`);
        return;
    }
    console.log('Connecté à la base de données')
})

/**
 * Création de l'application express
 */
const app = express()
app.listen(3000, ()=>console.log('Server started'))