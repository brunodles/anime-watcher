const functions = require('firebase-functions');

const Client = require('node-rest-client').Client;
const client = new Client();
client.on('error', function (err) {
    console.error('Something went wrong on the client', err);
})

const SERVER_URLS = [
    "https://anime-watcher-spark.herokuapp.com",
    "https://anime-watcher-ktor.herokuapp.com",
];

exports.gateway = functions.https.onRequest((req, res) => {
    var url = Object.keys(req.body)[0];
    console.log("Requested body: ", url);
    console.log("Requested url, ", req.url)
    return decode(0, "POST", req.url, {data:url}, res)
});

function decode(serverIndex, method, path, body, response)  {
    console.log("Server("+serverIndex+") - Request data: ", body);

    var successHandler = function (data, res) {
        console.log("Server("+serverIndex+")");
        console.log("\tStatusCode: ",res.statusCode);
        console.log("\tData: ", data)
    
        if (res.statusCode === 200) {
            return response.status(200)
                .type('application/json')
                .send(data);
        } else {
            return decode(serverIndex+1, method, path, body, response);
        }
    }

    var call;
    if (method === "POST") {
        call = client.post(SERVER_URLS[serverIndex]+path, body, successHandler)
    } else {
        call = client.get(SERVER_URLS[serverIndex]+path, successHandler)
    }

    call.on('error', function (error) {
        if (serverIndex === SERVER_URLS.length){
            return response.status(500)
                .type('application/json')
                .send(error)
        } else {
            console.log("Server("+serverIndex+") erred, trying next")
            return decode(serverIndex+1, method, path, body, response);
        }
    })
}