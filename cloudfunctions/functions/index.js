// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

exports.addImage = functions.database.ref('{environment}/videos/{pageUrl}')
    .onWrite(event => {
      // Only edit data when it is first created.
      if (event.data.previous.exists()) {
        return null;
      }
      // Exit when the data is deleted.
      if (!event.data.exists()) {
        return null;
      }

      const episode = event.data.val();
      const imageRef = event.data.ref.child("image");

      if (imageRef.val() === undefined) {
        return imageRef.set("https://i.ytimg.com/vi/h1ctARmpOlw/maxresdefault.jpg");
      } else {
        return null
      }
    });
