# AnimeWatcher Android

### Instalation
This project does not contains release nor provide apk for you.
(At least, yet.)
To use this you will have to checkout the code and compile it by yourself.

[Instructions](#instalation-instructions)

### Usage
The idea is to detect automatically the pages that the user is trying to reach and then the app will open with the content.

Just use your browser as usual, the app should detect those pages.
You can also share the page with the app, if it's decodable it will show you the right content otherwise the url will be sent to us adding it as issue for future release.

### Instalation Instructions
We're planning to do releases here on github, but by now you can follow these steps:
1. Clone the repo or download the source
1. Create a project on [firebase](https://firebase.google.com/)
1. Download it's `google-services.json` file on project folder
1. Plug your device on usb
1. Run `./gradlew app:installRelease`