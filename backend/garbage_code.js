/**
let jsonData;
// Lecture du fichier json
const fs = require('fs')
fs.readFile('./database.json', 'utf-8', (err, data)=>{
    if(err){
        console.error('Erreur de lecture du fichier JSON', err);
        return;
    }
    // Récupération des infos secouristes
    jsonData = JSON.parse(data);
})
*/