const express = require('express')
const router = express.Router()
const mysql = require('mysql2')

const Rescuer = require('./../models/rescuersModel')



// Fonction pour récupérer la liste de tous les secouristes
router.get('/', async (req, res)=>{
    const connection = req.connection;
    connection.query('SELECT * from rescuers', (err, results, fields)=>{
        if(err){
            console.error('Erreur lors de l\'exécution de la requête', err);
            res.status(500).send('Erreur de serveur');
        }
        else{
            res.json(results);
        }
    })

})

// Fonction pour récupérer la liste des secouristes disponibles
router.get('/available', (req, res)=>{
    const connection = req.connection;
    connection.query('SELECT * from rescuers WHERE DISPONIBILITY = 1', (err, results, fields) =>{
        if(err){
            console.error('Erreur lors de l\'exécution de la requête', err);
            res.status(500).send('Erreur de serveur');
        }
        else{
            res.json(results);
        }
    } )
})

// Fonction pour récupérer un secouriste en particulier
router.get('/:id', (req, res)=>{
    const connection = req.connection;
    connection.query('SELECT * from rescuers WHERE id='+ mysql.escape(req.params.id), (err, results, fields) =>{
        if(err){
            console.error('Erreur lors de l\'exécution de la requête', err);
            res.status(500).send('Erreur de serveur');
        }
        else{
            res.json(results);
        }
    } )
})

// Fonction pour ajouter un secouriste dans la base
router.post('/', (req, res)=>{

})

// Fonction pour modifier les informations d'un secouriste
router.patch('/', (req, res)=>{

})

// Fonction pour supprimer un secouriste de la bdd
router.delete('/:id', (req, res)=>{

})
module.exports = router