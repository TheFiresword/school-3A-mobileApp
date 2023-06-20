/**
 * Import function triggers from their respective submodules:
 *
 * const {onCall} = require("firebase-functions/v2/https");
 * const {onDocumentWritten} = require("firebase-functions/v2/firestore");
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

// const {onRequest} = require("firebase-functions/v2/https");
// const logger = require("firebase-functions/logger");

// Create and deploy your first functions
// https://firebase.google.com/docs/functions/get-started

// exports.helloWorld = onRequest((request, response) => {
//   logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });

const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp()

exports.androidPushNotification = functions.firestore.document("Notifications/{docId}").onCreate((snapshot, context) =>{
    let registrationTokens = ["dZAVyEPBRuOJQoNoLblJHQ:APA91bF4Sk0qKy_gkODgpLUxX15Mh_cl3Mtx8O-9r1Rm2-DWSubj-AR9y37x6c9heSfNLaLxxs3EyWFI_AsWK_n5wh_C9Vze39LDSvmh9Y7rf66yEixweqQGTayAWfTn9Js8WmfJ8nO_"];
    admin.messaging().sendMulticast({
            tokens: registrationTokens,
            notification: {
                title: snapshot.data().title,
                body: snapshot.data().body
            }
        });
    }
);
