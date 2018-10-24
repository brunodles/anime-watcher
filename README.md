[![Build Status](https://travis-ci.org/brunodles/anime-watcher.svg?branch=master)](https://travis-ci.org/brunodles/anime-watcher)
# Anime Watcher
An video player / browser to navigate through unsafe pages.

Initially this repo was to make a AnimePlayer that grab data from anime pages.  
The idea was to provide a way to reach information and video url.
Now the idea changed to decode any page, avoiding ads.

## Why
Many pages have **tons of ads** before the user be able to reach out the content.
Some of these pages are stealing content from the others, so the idea is to let any one reach the content avoiding ads.
Then we won't send money ( through ads) to those pages.

### Objectives
* Decode Page contents
* Avoid ads
* Provide a good experience when watch videos from pages
* Provide a offline support for online videos
* Fight piracy?

## About the project

This project is divided into these modules:
* [Explorer](/explorer) : a module to decode pages.
* [Server Spark](/server-spark) : a web server that uses Explorer modules.
* [Cli](/cli) : simple command to interact with Explorer on terminal.
* [App](app) : an android app that uses communicates with Server-Spark to
play decoded pages.
* [Cloud Functions](/cloudfunctions) : firebase cloud functions, work as a middleware.
* [Firebase Structure](/Firebase.md) : how data are stored.
* [Decoders](/decoders) : were used for decoders, some pages still there.
* [Cache](/cache) : just a shared cache folder.
* [gradle](/gradle) : useful script, some will be extracted to a plugin.

## Contributing
We have a lot of issues and ideas to implement.
You can help us implementing new decodes, fixing bugs, suggesting new pages and features.
Just create a issue.

If you're doing one of those issues pleas leave a comment so we will wait for you MR.
