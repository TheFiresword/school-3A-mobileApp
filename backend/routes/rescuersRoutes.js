const express = require('express')
const router = express.Router()
const mysql = require('mysql2')

const Rescuer = require('./../models/rescuersModel')



// Fonction pour récupérer la liste de tous les secouristes
router.get('/', async (req, res)=>{
    const connection = req.socket;
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
    const connection = req.socket;
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
    const connection = req.socket;
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
    const connection = req.socket;
    connection.query(`INSERT INTO rescuers (firstname, lastname, email, password, telephone, description) VALUES (${mysql.escape(req.params.firstname)}, ${mysql.escape(req.params.lastname)}, ${mysql.escape(req.params.email)}, ${mysql.escape(req.params.password)}, ${mysql.escape(req.params.telephone)}, ${mysql.escape(req.params.telephone)}, ${mysql.escape(req.params.description)});`,(err, results, fields) =>{
        if(err){
            console.error('Erreur lors de l\'exécution de la requête', err);
            res.status(500).send('Erreur de serveur');
        }
        else{
            res.json(results);
        }
    } )
})

// Fonction pour modifier les informations d'un secouriste
router.patch('/', (req, res)=>{
    const connection = req.socket;
    connection.query(`UPDATE rescuers (firstname, lastname, email, password, telephone, description) SET (${mysql.escape(req.params.firstname)}, ${mysql.escape(req.params.lastname)}, ${mysql.escape(req.params.email)}, ${mysql.escape(req.params.password)}, ${mysql.escape(req.params.telephone)}, ${mysql.escape(req.params.description)});`,(err, results, fields) =>{
        if(err){
            console.error('Erreur lors de l\'exécution de la requête', err);
            res.status(500).send('Erreur de serveur');
        }
        else{
            res.json(results);
        }
    } )
})

// Fonction pour modifier la disponibilité d'un secouriste
router.patch('/', (req, res)=>{
    const connection = req.socket;
    connection.query(`UPDATE rescuers SET disponibility=${mysql.escape(req.params.disponibility)};`,(err, results, fields) =>{
        if(err){
            console.error('Erreur lors de l\'exécution de la requête', err);
            res.status(500).send('Erreur de serveur');
        }
        else{
            res.json(results);
        }
    } )
})

// Fonction pour supprimer un secouriste de la bdd
router.delete('/:id', (req, res)=>{
    const connection = req.socket;
    connection.query(`DELETE FROM rescuers WHERE id=${mysql.escape(req.params.id)};`,(err, results, fields) =>{
        if(err){
            console.error('Erreur lors de l\'exécution de la requête', err);
            res.status(500).send('Erreur de serveur');
        }
        else{
            res.json(results);
        }
    } )
})
module.exports = router