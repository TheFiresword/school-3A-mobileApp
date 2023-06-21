const functions = require("firebase-functions");
const admin = require("firebase-admin");
const {credential} = require("firebase-admin");
const {getMessaging} = require("firebase-admin/messaging");
// admin.initializeApp({credential: admin.credential.cert({
//     projectId:"sstou-f765c"
// })})
admin.initializeApp()

exports.androidPushNotification = functions.firestore.document("Notifications/{docId}").onCreate(
    (snapshot, context) =>{
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

// const registrationTokens = [
//     'dZAVyEPBRuOJQoNoLblJHQ:APA91bF4Sk0qKy_gkODgpLUxX15Mh_cl3Mtx8O-9r1Rm2-DWSubj-AR9y37x6c9heSfNLaLxxs3EyWFI_AsWK_n5wh_C9Vze39LDSvmh9Y7rf66yEixweqQGTayAWfTn9Js8WmfJ8nO_'
// ];
// //
// // // Define a condition which will send to devices which are subscribed
// // // to either the Google stock or the tech industry topics.
// // // const condition = '\'stock-GOOG\' in topics || \'industry-tech\' in topics';
// //
// // // See documentation on defining a message payload.
// const messages = [];
// messages.push({
//     notification: { title: 'Price drop', body: '5% off all electronics' },
//     token: registrationTokens,
// });
// //
// getMessaging().sendAll(messages)
// //     .then((response) => {
// //         console.log(response.successCount + ' messages were sent successfully');
// //     });
