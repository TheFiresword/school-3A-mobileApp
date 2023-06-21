
const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp()

exports.androidPushNotification = functions.firestore.document("Notifications/{docId}").onCreate((snapshot, context) =>{
        var registrationTokens = ["dZAVyEPBRuOJQoNoLblJHQ:APA91bF4Sk0qKy_gkODgpLUxX15Mh_cl3Mtx8O-9r1Rm2-DWSubj-AR9y37x6c9heSfNLaLxxs3EyWFI_AsWK_n5wh_C9Vze39LDSvmh9Y7rf66yEixweqQGTayAWfTn9Js8WmfJ8nO_"]
        admin.messaging().sendAll({
            tokens: registrationTokens,
            notification: {
                title: snapshot.data().title,
                body: snapshot.data().body
            }
        });
    }
);
