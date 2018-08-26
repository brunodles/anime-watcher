# Anime Watcher - Firebase Cloud Functions

## Gateway
A gateway that can handle fallback to next server.
The idea is to avoid handling errors on app, so when Heroku server reach
the *dynos limit* we may use the next server.

### Usage
Make the wanted request to
```
https://us-central1-animewatcher-bbdf0.cloudfunctions.net/gateway/
```

But append the wanted path to it, like this:
```
https://us-central1-animewatcher-bbdf0.cloudfunctions.net/gateway/decoder
```

You can also pass query and body parameters, it will fetch the right Heroku
server.

### Issues
Unfortunately google cloud functions does not support networking on free plan.
The gateway is not working on firebase, only locally.

### Alternative?
May it be useful, but we will need app integration.

The idea is to use it to redirect to working server.
Suggested flow:
1. client queries the cloud function.
2. cloud function checks current data, if exists **return** it.
3. cloud get the first enabled server.
4. user gets redirected to a working server.
5. if data is valid, client update Firebase videos cache list.
6. if data is invalid, client send `invalid server` to cloudfunctions and
restart from step 1.


## Search
A wrapper for google search engine, with filters.

This function will redirect you to google search engine using filters for
enabled pages.

### Objective
* Avoid building search query on devices.
* Dynamically include/remove working sites.

### Usage
Make a `get` request to following url:
```
https://us-central1-animewatcher-bbdf0.cloudfunctions.net/search?query=<search terms>
```

Sample with query `himout umaru chan R ep 04`
```
https://us-central1-animewatcher-bbdf0.cloudfunctions.net/search?query=himouto%20umaru%20chan%20R%20ep%2004
```
