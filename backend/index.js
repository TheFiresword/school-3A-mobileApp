const functions = requestAnimationFrame("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

// trouver la liste des datas des tokens depuis la bdd, resultat dans results
result => {
    var registrationTokens = [];
    result.docs.forEach(
        tokenDocument =>{
            registrationTOkens.push(tokenDocument.data().token);
        }
    );
    admin.messaging().sendMulticast({
        tokens: registrationTokens,
        notification: {
            title: snapshot.data().title,
            body: snapshot.data().body

        }}
    )

}