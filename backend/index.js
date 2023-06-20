const functions = requestAnimationFrame("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

function Help(salle){

        var registrationTokens = ["TOKEN"]
        admin.messaging().sendMulticast({
            tokens: registrationTokens,
            notification: {
                title: "Urgent: Help required",
                body: "In room:"+salle

            }}
        )

    }