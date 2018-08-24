const functions = require('firebase-functions');

const Client = require('node-rest-client').Client;
const client = new Client();
client.on('error', function (err) {
    console.error('Something went wrong on the client', err);
})

const SERVER_URLS = ["https://anime-watcher-ktor.herokuapp.com/decoder",
    "https://anime-watcher-spark.herokuapp.com/decoder"];

exports.helloWorld = functions.https.onRequest((req, res) => {
    var url = Object.keys(req.body)[0];
    console.log(url);
    client.post(SERVER_URLS[0], url,  function (data, response) {
        return res.status(200)
            .type('application/json')
            .send(data);
    }).on('error', function (error) {
        console.log("First server errored, trying next")
        client.post(SERVER_URLS[1], url,  function (data, response) {
                return res.status(200)
                    .type('application/json')
                    .send(data);
            })
    })
});
