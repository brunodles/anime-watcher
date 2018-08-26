const functions = require('firebase-functions');
const gatewayModule = require("./gateway")
const searchModule = require("./search")

exports.gateway = functions.https.onRequest(gatewayModule.gateway);
exports.search = functions.https.onRequest(searchModule.search);
