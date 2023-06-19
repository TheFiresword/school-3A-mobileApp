const express = require('express')
const router = express.Router()
const mysql = require('mysql2')
const bcrypt = require('bcrypt');

const config_values = require('./../config')

// Middleware pour vérifier l'existence d'un secouriste en fonction de l'id
function get_rescuer(req, res, next){
    const connection = req.socket;
    const rescuer_id = parseInt(req.params.id);
    
    const sql = `SELECT * from ${config_values.rescuersTable} WHERE id = ?`;
    const params = [rescuer_id];
    connection.query(sql, [params], (err, results)=>{
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
            res.rescuer = results[0];       
        }
        next();
    })    
}

// Fonction de validation des données reçues d'un formulaire
const validate_fields = (data)=>{
    return ("firstname" in data && "lastname" in data && "email" in data
    && "password" in data && "telephone" in data && "disponibility" in data);
}

// Fonction de hachage des mots de passe
const hash_passwd = async (password) => {
    try {
      const hash = await bcrypt.hash(password, 10);
      return hash;
    } catch (error) {
      console.error('Erreur lors de la génération du hash :', error);
      return null;
    }
  };

// Fonction pour récupérer la liste de tous les secouristes
router.get('/', (req, res)=>{
    const connection = req.socket;
    connection.query('SELECT * from rescuers', (err, results, fields)=>{
        if(err){
            //console.error('Erreur lors de l\'exécution de la requête', err);
            res.status(500).json(
                {message: 'Echec', details:'Erreur de serveur'}
                );
        }
        else{
            res.status(200).json({message: 'Succès', details:results});
        }
    })
})

// Fonction pour récupérer la liste des secouristes disponibles
router.get('/available', (req, res)=>{
    const connection = req.socket; 
    const sql = `SELECT * from ${config_values.rescuersTable} WHERE DISPONIBILITY = ?`;
    const params = [1];

    connection.query(sql, [params], (err, results, fields) =>{
        if(err){
            //console.error('Erreur lors de l\'exécution de la requête', err);
            res.status(500).json({message: 'Echec', details:'Erreur de serveur'});
        }
        else{
            res.status(200).json({message: 'Succès', details:results});
        }
    } )
})

// Fonction pour récupérer un secouriste en particulier
router.get('/:id', get_rescuer, (req, res)=>{
    res.status(200).json({message: 'Succès', details:res.rescuer});    
})

// Fonction pour ajouter un secouriste dans la base
router.post('/', (req, res)=>{
    const connection = req.socket;
    const informations = req.body;

    // Vérifier que la data envoyée est au bon format (bons champs)
    //console.log(informations);
    const valid = validate_fields(informations);

    if(!valid){
        res.status(200).json(
            {message: 'Echec', 
            details: 'Data corrompue, le formulaire ne contient pas les bons champs'
        });
        return;
    }

    // Vérifier qu'il n'y a pas déja un compte associé à cet email
    if(informations.email){
        const sql = `SELECT firstname, lastname from ${config_values.rescuersTable} where email = ?`;
        const params = [informations.email]
        connection.query(sql, [params],
        (err, results)=>{
            if(err){
                res.status(500).json(
                    {message: 'Echec', 
                    details: err.message
                });
            }
            else{
                //console.log('Checking status', results);

                if(results.length != 0){
                    //Compte existant
                    res.status(200).json(
                        {message: 'Echec', 
                        details: 'Un compte associé à cette adresse mail existe déja'
                    });
                }
                else{
                    // Vérifier que le mot de passe fait au moins 8 caractères 
                    // (cette contrainte doit être préfaite par le frontend)
                    if(informations.password.length < 8){
                        res.status(201).json(
                            {message: "Echec", details: 'Mot de passe trop court'}
                            );
                        return;
                    }

                    // Hasher le mot de passe avant de continuer
                    hash_passwd(informations.password).then(
                        hash =>{
                            const hashed_password = hash;
                            //console.log('Hashed généré', hashed_password);
                            
                            if(!hashed_password){
                                res.status(500).json({message: 'Echec', details: 'Erreur interne au serveur'});
                                return;
                            }

                            // Traitement des champs optionnels
                            informations.telephone = informations.telephone || "NaN";
                            informations.disponibility = parseInt(informations.disponibility) || true;  
        
                            const params = [informations.firstname, informations.lastname, informations.email, hashed_password, 
                                informations.telephone, informations.disponibility
                            ];
                            console.log('Parametres de requete', params);
                            
                            const sql = `INSERT INTO ${config_values.rescuersTable} (firstname, lastname, email, password, telephone, disponibility) VALUES (?)`;
                            connection.query(sql, [params], (err, results) =>{
                                if(err){
                                    res.status(400).json(
                                        {message: 'Echec', details:err.message}
                                    );
                                    return;
                                }
                                // ne pas envoyer tous les champs
                                else{
                                    res.status(201).json(
                                        {message: "Succès", details: results}
                                        );
                                    }
                                })                        
                        }                    
                    ).catch(err => {res.status(500).json(
                        {message: 'Echec', details: 'Erreur interne au serveur'});});                    
                                                                                                 
                }
            }
        })
    }
            
})

    
// Fonction pour modifier les informations d'un secouriste
router.patch('/:id', get_rescuer, (req, res)=>{
    const connection = req.socket;
    const rescuer_id = parseInt(req.params.id);
    
    const informations = req.body;
    const fields_sent = Object.keys(informations);
   
    let sql = `UPDATE ${config_values.rescuersTable} SET `;
    const params = [];
    fields_sent.forEach((field, index)=>{
        sql += ` ${field} = ?`;

        let normalized_field = informations[field];
        if(field != "disponibility"){
            normalized_field = informations[field] || "Nan";
        }
        else{
            normalized_field = informations[field] || true;
        }

        if(field == "password"){
            if(informations.password.length < 8){
                res.status(201).json(
                    {message: "Echec", details: 'Mot de passe trop court'}
                    );
                return;
            }
            hash_passwd(informations['password']).then(
                hash =>{
                    normalized_field = hash;
                }).catch(err => {res.status(500).json(
                    {message: 'Echec', details: 'Erreur interne au serveur'});}); 
        }

        params.push(normalized_field);
        if(index < fields_sent.length-1){
            sql += ',';
        }
    })
    sql += ' WHERE id = ?';
    params.push(rescuer_id);
    console.log('La requête', sql, params);

    connection.query(sql, params, (err, results, fields) =>{
        if(err){
            //console.error('Erreur lors de l\'exécution de la requête', err);
            res.status(500).json({message: 'Echec', details: 'Erreur interne au serveur'});
        }
        else{
            res.status(200).json({message: 'Succès', details:results});
        }
    } )
})


// Fonction pour supprimer un secouriste de la bdd
router.delete('/:id', get_rescuer, (req, res)=>{
    const connection = req.socket;
    const rescuer_id = parseInt(req.params.id);

    const sql = `DELETE FROM ${config_values.rescuersTable} WHERE id = ?`;
    const params = [rescuer_id];

    connection.query(sql, [params],(err, results, fields) =>{
        if(err){
            res.status(500).json({message: 'Echec', details: 'Erreur interne au serveur'});
        }
        else{
            res.status(204).json({message: 'Succès', details:results});
        }
    } )
})

module.exports = router