# anime-watcher
Initially this repo was to make a AnimePlayer that grab data from anime pages.  
The idea was to provide a way to reach information and video url.

Now the idea changed to decode any page, avoiding ads.

## Why
Many pages have **tons of ads** before the user be able to reach out the content.
Some of these pages are stealing content from the others, so the idea is to let any one reach the content avoiding ads.
Then we won't send monny ( through ads) to those pages.

### Objectives
* Decode Page contents
* Avoid ads
* Provide a good experience when watch videos fron pages
* Provide a offline support for online videos
* Fight piracy?

## How to Use?

### Instalation
This project does not contains release nor provide apk for you.
(At least, yet.)
To use this you will have to checkout the code and compile it by yourself.

[Instructions](#instalation-instructions)

### Usage
The idea is to detect automatically the pages that the user is trying to reach and then the app will open with the content.

Just use your browser as usual, the app should detect those pages.  
You can also share the page with the app, if it's decodable it will show you the right content otherwise the url will be sent to us adding it as issue for future release.

## Instalation Instructions
We're planning to do releases here on github, but by now you can follow these steps:
1. Clone the repo or download the source
1. Create a project on [firebase](https://firebase.google.com/)
1. Download it's `google-services.json` file on project folder
1. Plug your device on usb
1. Run `./gradlew app:installRelease`

## Contributing
We have a lot of issues and ideas to implement.
You can help us implementing new decodes, fixing bugs, suggesting new pages and features.
Just create a issue.

If you're doing one of those issues pleas leave a comment so we will wait for you MR.
