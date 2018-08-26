const functions = require('firebase-functions');


// GATEWAY
const Client = require('node-rest-client').Client;
const client = new Client();
client.on('error', (err) => {
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
    var url = SERVER_URLS[serverIndex]+path;
    console.log("Server("+serverIndex+") = ", url);

    var errorHandler = (error) => {
        if (serverIndex + 1 >= SERVER_URLS.length){
            return response.status(500)
                .type('application/json')
                .send(error)
        } else {
            console.log("Server("+serverIndex+") erred, trying next")
            return decode(serverIndex+1, method, path, body, response);
        }
    }

    var successHandler = (data, res) => {
        console.log("Server("+serverIndex+")");
        console.log("\tStatusCode: ",res.statusCode);
        console.log("\tData: ", data)

        if (res.statusCode === 200) {
            return response.status(200)
                .type('application/json')
                .send(data);
        } else {
            return errorHandler(data)
        }
    }

    var call;
    if (method === "POST") {
        call = client.post(url, body, successHandler)
    } else {
        call = client.get(url, successHandler)
    }
    call.on('error', errorHandler)
}

// Google Query
const SUBDOMAINS = ["", "www."]
const PAGES = [
        // Anime kai
        "animekaionline.com",
        "animeskai.com",
        "animakai.info",
        // Animes Online Br
        "animesonlinebr.com.br",
        //Animes Orion
        "animesorion.site",
        "animesorion.tv",
        "animesorion.video",
        // Anitube brasil
        "animetubebrasil.com",
        // Anitube Br
        "anitubebr.com",
        // Anitube site
        "anitube.site",
        // One Piece Ex
        "onepiece-ex.com.br",
        "one-piece-x.com.br",
        // Anima/tv Curse
        "tvcurse.com",
        "animacurse.moe",
        "animacurse.tv",
        // XVideos
        "xvideos.com"
]
exports.search = functions.https.onRequest((req, res) => {
    var query = req.query.query + " "+ PAGES.map( (p) => SUBDOMAINS.map( (s) => s+p ))
        .reduce((acc, val) => acc.concat(val), [])
        .map( (p) => "site:"+p)
        .join(" OR ");

    res.redirect("http://google.com/search?q="+encodeURI(query))
});