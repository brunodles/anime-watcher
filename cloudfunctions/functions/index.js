const functions = require('firebase-functions');

//const searchModule = require("./search")

const admin = require('firebase-admin');
admin.initializeApp()

const invalidTextRegex = /[^\d\w]+/g;


//exports.search = functions.https.onRequest(searchModule.search);

exports.onAddToHistory = functions.database
    .ref("/users/{uid}/history/{pushId}")
    .onCreate((snapshot, context) => {

    const userId = context.auth.uid;
    const episodeUrl = snapshot.val();
    const episodeId = episodeUrl.replace(invalidTextRegex, "");

    console.log("user: "+userId +", episodeUrl: "+episodeUrl+", episodeId: "+episodeId);

    return admin.database().ref("/videos/"+episodeId).once("value").then( episodeRef => {

        const episode = episodeRef.val();

        console.log("episode: ", JSON.stringify(episode));

        return admin.database().ref("/users/"+userId+"/historyLast").set(episode)
            .then(res => {
                if (episode.nextEpisodes !== undefined && episode.nextEpisodes.length > 0)
                    return admin.database()
                        .ref("/users/" + userId + "/next/" + episode.animeName)
                        .set(episode.nextEpisodes[0]);
                return res;
        });
    })
});
