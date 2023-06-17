const express = require('express')
const router = express.Router()

// Fonction pour récupérer la liste de tous les secouristes
router.get('/', (req, res)=>{
    res.send('Hello dear student')
})

// Fonction pour récupérer la liste des secouristes disponibles
router.get('/available', (req, res)=>{

})

// Fonction pour récupérer un secouriste en particulier
router.get('/:id', (req, res)=>{

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