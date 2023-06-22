const express = require('express');
const router = express.Router();
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

const secretKey = require('./../tokenGeneration')

const safeInfos = "id, firstname, lastname, email, telephone, disponibility, tokenFirebase";
const changeable_fields = ["firstname", "lastname", "email", "telephone", "disponibility",
"description", "password", "tokenFirebase"];

// Middleware pour vérifier l'existence d'un secouriste en fonction de l'id
function get_rescuer(req, res, next){
    const connection_pool = req.socket;
    const rescuer_id = parseInt(req.params.id);
    
    const sql = `SELECT ${safeInfos} from rescuers WHERE id = $1`;
    const params = [rescuer_id];
    connection_pool.query(sql, params, (err, results)=>{
        if(err){
            res.status(500).json({message: 'Echec', details:'Erreur interne au serveur'});
            return;
        }
        results = results.rows;
        if(results.length === 0){
            // Le secouriste n'existe pas
            res.status(404).json({message: 'Echec', details:'Le secouriste demandé n\'existe pas'});
            return;
        }
        else{
            res.rescuer = results[0];       
        }
        next();
    })    
}

// Fonction findByEmail()
const email_exists = (pooldb, email) => {
    return new Promise((resolve, reject) => {
      const sql = `SELECT firstname, lastname FROM rescuers WHERE email = $1`;
      const params = [email];
  
      pooldb.query(sql, params, (err, results) => {
        if (err) {
          reject(err);
        } else {
          results = results.rows;
          if(results.length !== 0){
            resolve(results[0]);
          }
          else{
            resolve(null);
          }
        }
      });
    });
};
  

// Middleware pour vérifier la validité de la session (pour les requêtes de modification de données)
const check_session = (req, res, next)=>{
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1];
    //console.log(token);
    if(token == null){
        res.status(401).json({message: 'Echec', details:'La session est fermée'});
        return;
    }
    jwt.verify(token, secretKey, (err, user)=>{
        if(err){
            res.status(401).json({message: 'Echec', details:'Token d\'authentification invalide'});
            return;
        }
        req.rescuer = user;
        next();
    })
}

// Fonction de validation des données reçues d'un formulaire
const validate_fields = (data)=>{
    return ("firstname" in data && "lastname" in data && "email" in data && "password" in data);
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
    const connection_pool = req.socket;

    const sql = `SELECT ${safeInfos} from rescuers`;
    connection_pool.query(sql,  (err, results, fields)=>{
        if(err){
            //console.error('Erreur lors de l\'exécution de la requête', err);
            res.status(500).json(
                {message: 'Echec', details:'Erreur de serveur'}
                );
        }
        else{
            results = results.rows;
            res.status(200).json({message: 'Succès', details:results});
        }
    })
})

// Fonction pour récupérer la liste des secouristes disponibles
router.get('/available', (req, res)=>{
    const connection_pool = req.socket; 
    const sql = `SELECT ${safeInfos} from rescuers WHERE DISPONIBILITY = $1`;
    const params = [1];

    connection_pool.query(sql, params, (err, results, fields) =>{
        if(err){
            //console.error('Erreur lors de l\'exécution de la requête', err);
            res.status(500).json({message: 'Echec', details:'Erreur de serveur'});
        }
        else{
            results = results.rows;
            res.status(200).json({message: 'Succès', details:results});
        }
    } )
})

// Fonction pour ajouter un secouriste dans la base
router.post('/',  check_session, async (req, res)=>{
    const connection_pool = req.socket;
    const informations = req.body;

    // Vérifier que la data envoyée est au bon format (bons champs)
    //console.log(informations);
    const valid = validate_fields(informations);

    if(!valid){
        res.status(400).json(
            {message: 'Echec', 
            details: 'Data corrompue, le formulaire ne contient pas les bons champs'
        });
        return;
    }

    // Vérifier qu'il n'y a pas déja un compte associé à cet email
    if(informations.email){
        try{
            const exists = await email_exists(connection_pool,informations.email);
            if(exists != null){
                res.status(409).json(
                    {message: 'Echec', 
                    details: 'Un compte associé à cette adresse mail existe déja'
                });
                return;
            }
            else{
                // Vérifier que le mot de passe fait au moins 8 caractères 
                // (cette contrainte doit être préfaite par le frontend)
                if(informations.password.length < 8){
                    res.status(400).json(
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
                        //console.log('Parametres de requete', params);
                                
                        const sql = `INSERT INTO rescuers (firstname, lastname, email, password, telephone, 
                            disponibility) VALUES ($1, $2, $3, $4, $5, $6)`;
                        connection_pool.query(sql, params, (err, results) =>{
                            if(err){
                                res.status(500).json(
                                    {message: 'Echec', details: 'Erreur interne au serveur'}
                                );
                                return;
                            }
                            // ne pas envoyer tous les champs
                            else{
                                results = results.rows;
                                res.status(201).json(
                                    {message: "Succès", details: results}
                                    );
                                }
                            })                        
                    }                    
                ).catch(err => {res.status(500).json(
                    {message: 'Echec', details: 'Erreur interne au serveur'});});                    
                                                                                                     
            }
        }catch(err){
            res.status(500).json(
                {message: 'Echec', 
                details: 'Erreur interne au serveur'
            });
            return;
        }               
    }
})

// Fonction pour récupérer un secouriste en particulier
router.get('/:id', get_rescuer, (req, res)=>{
    res.status(200).json({message: 'Succès', details:res.rescuer});    
})

    
// Fonction pour modifier les informations d'un secouriste
router.patch('/:id',  check_session, get_rescuer, (req, res)=>{
    const connection_pool = req.socket;
    const rescuer_id = parseInt(req.params.id);
    
    const informations = req.body;
    if(Object.keys(informations).length === 0){
        res.status(400).json({message: 'Echec', details:'Aucun champ à modifier'});
        return;
    }

    const fields_sent = Object.keys(informations);
   
    let sql = `UPDATE rescuers SET `;
    const params = [];

    const updateFields = async () => {
        let replicated_index = 0;
        for(; replicated_index < fields_sent.length; replicated_index++){
            const field = fields_sent[replicated_index];
            if(changeable_fields.includes(field)){
                // Pas de Champ customisé renvoyé par le client
                
                sql += ` ${field} = $${replicated_index+1}`;
                let normalized_field = informations[field];
                if(field != "disponibility"){
                    normalized_field = informations[field] || "Nan";
                }
                else{
                    normalized_field = informations[field] || true;
                }
                
                // On vérifie que le mot de passe n'est pas trop court
                if(field == "password"){
                    if(informations.password.length < 8){
                        res.status(400).json(
                            {message: "Echec", details: 'Mot de passe trop court'}
                            );
                        return;
                    }
                    const hash = await hash_passwd(informations['password']);
                    //console.log('The hash is', hash);
                    normalized_field = hash; 
                }
                
                // On vérifie que l'email n'est pas déja utilisé
                //----TRAITER LE CAS OU IL RENVOIE SON PROPRE EMAIL----------
                if(field == "email"){
                    try{
                        const exists = await email_exists(connection_pool, normalized_field);
                        if(exists != null){
                            res.status(409).json(
                                {message: 'Echec', 
                                details: 'Un compte associé à cette adresse mail existe déja'
                            });
                            return;
                        }
                    }catch(err){
                        res.status(500).json(
                            {message: 'Echec', 
                            details: 'Erreur interne au serveur'
                        });
                        return;
                    }
                }

                params.push(normalized_field);
                if(replicated_index < fields_sent.length-1){
                    sql += ',';
                }
            }                            
        }
        
        sql += ` WHERE id = $${replicated_index+1}`;
        params.push(rescuer_id);

        connection_pool.query(sql, params, (err, results, fields) =>{
            if(err){
                //console.error('Erreur lors de l\'exécution de la requête', err);
                res.status(500).json({message: 'Echec', details: 'Erreur interne au serveur'});
            }
            else{
                results = results.rows;
                res.status(200).json({message: 'Succès', details:results});
            }
        } )
    }
    updateFields();    
})


// Fonction pour supprimer un secouriste de la bdd
router.delete('/:id', check_session, get_rescuer, (req, res)=>{
    const connection_pool = req.socket;
    const rescuer_id = parseInt(req.params.id);

    const sql = `DELETE FROM rescuers WHERE id = $1`;
    const params = [rescuer_id];

    connection_pool.query(sql, params,(err, results, fields) =>{
        if(err){
            res.status(500).json({message: 'Echec', details: 'Erreur interne au serveur'});
        }
        else{
            results = results.rows;
            res.status(200).json({message: 'Succès', details:results});
        }
    } )
})

module.exports = router