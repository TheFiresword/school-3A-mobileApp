const express = require('express');
const router = express.Router();
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

const secretKey = require('./../tokenGeneration')
const config_values = require('./../config')

console.log('je suis appelé')
// Route de connexion
router.post('/login', (req, res)=>{
    const email = req.body.email;
    const password_sent = req.body.password;
    // Contrôles de types
    if (typeof email != "string" || typeof password_sent !="string"){
        res.status(200).json(
            {message: 'Echec', 
            details: 'Data corrompue, le formulaire ne contient pas les bons champs'
        });
        return;
    }

    // Vérification de la validité de l'email
    const connection = req.socket;
    
    const sql = `SELECT * from ${config_values.rescuersTable} WHERE email = ?`;
    const params = [email];
    connection.query(sql, [params], async (err, results)=>{
        if(err){
            res.status(500).json({message: 'Echec', details:'Erreur interne au serveur'});
            return;
        }
        if(results.length === 0){
            // Le secouriste n'existe pas
            res.status(200).json({message: 'Echec', details:'Le secouriste demandé n\'existe pas'});
            return;
        }
        else{
            const rescuer = results[0];
            // Le secouriste existe -- On vérifie l'email
            const match = await bcrypt.compare(password_sent, rescuer.password);
            if(!match){
                res.status(200).json({message: 'Echec', details:'Le mot de passe est incorrect'});
                return;
            }
            // Authentification réussie
            // Génération d'un token d'identification pour la session
            const token = jwt.sign({email}, secretKey);
            res.status(200).json({message: 'Succès', details:token});
            return;
        }
    })
})

module.exports = router;