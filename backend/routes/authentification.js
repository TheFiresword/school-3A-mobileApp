const express = require('express');
const router = express.Router();
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

const secretKey = require('./../tokenGeneration')

const safeInfos = "id, firstname, lastname, email, telephone, disponibility, tokenFirebase";


console.log('je suis appelé')
// Route de connexion
router.post('/login', (req, res)=>{
    const email = req.body.email;
    const password_sent = req.body.password;
    // Contrôles de types
    if (!email || !password_sent || typeof email != "string" || typeof password_sent !="string"){
        res.status(400).json(
            {message: 'Echec', 
            details: 'Data corrompue, le formulaire ne contient pas les bons champs'
        });
        return;
    }

    // Vérification de la validité de l'email
    const connection_pool = req.socket;
    
    const sql = `SELECT  * from rescuers WHERE email = $1`;
    const params = [email];
    connection_pool.query(sql, params, async (err, results)=>{
        if(err){
            res.status(500).json({message: 'Echec', details:'Erreur interne au serveur'});
            return;
        }
        results = results.rows;
        if(results.length === 0){
            // Le secouriste n'existe pas
            res.status(401).json({message: 'Echec', details:'Le secouriste demandé n\'existe pas'});
            return;
        }
        else{
            const rescuer = results[0];
            // Le secouriste existe -- On vérifie l'email
            const match = await bcrypt.compare(password_sent, rescuer.password);
            if(!match){
                res.status(401).json({message: 'Echec', details:'Le mot de passe est incorrect'});
                return;
            }
            // Authentification réussie
            // Génération d'un token d'identification pour la session
            const token = jwt.sign({email}, secretKey);
            infos = {
                "id" : rescuer.id, 
                "firstname" : rescuer.firstname, 
                "lastname": rescuer.lastname, 
                "email" : rescuer.email, 
                "telephone" : rescuer.telephone,
                "disponibility": rescuer.disponibility, 
                "tokenFirebase": rescuer.tokenFirebase
            }
            res.status(200).json(
                {
                    message: 'Succès', 
                    details: {token:token, userData: infos}
            });
            return;
        }
    })
})

module.exports = router;