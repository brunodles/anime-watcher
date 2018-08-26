# Anime Watcher - Firebase Cloud Functions

Cloud functions to serve as a gateway to multiple Heroku servers.

The main idea is to offer an auto fallback function for api clients,
with this we can check cached info on `firebase` and fetch data on
multiple servers.


## Issues
Unfortunately google cloud functions does not support networking on free plan.