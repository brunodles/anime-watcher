const functions = require('firebase-functions');

const Client = require('node-rest-client').Client;
const client = new Client();
client.on('error', function (err) {
    console.error('Something went wrong on the client', err);
})

const SERVER_URLS = [
    "https://anime-watcher-spark.herokuapp.com/decoder",
    "https://anime-watcher-ktor.herokuapp.com/decoder",
];

exports.decoder = functions.https.onRequest((req, res) => {
    var url = Object.keys(req.body)[0];
    console.log("Requested url: ", url);
    return decode(0, url, res)
});

function decode(serverIndex, url, response)  {

    console.log("Server("+serverIndex+") - Request data: ", url);
    client.post(SERVER_URLS[serverIndex], {data:url}, function (data, res) {
        console.log("Server("+serverIndex+")");
        console.log("\tStatusCode: ",res.statusCode);
        console.log("\tData: ", data)

        if (res.statusCode === 200) {
            return response.status(200)
                .type('application/json')
                .send(data);
        } else {
            return decode(serverIndex+1, url, response);
        }
    }).on('error', function (error) {
        if (serverIndex === SERVER_URLS.length){
            return response.status(500)
                .type('application/json')
                .send(error)
        } else {
            console.log("Server("+serverIndex+") erred, trying next")
            return decode(serverIndex+1, url, response);
        }
    })
}