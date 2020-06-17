<p align="center">
  <img src="https://github.com/eaemenkkstudios/overtracker-mobile/blob/master/readme/logo.png?raw=true">
</p>

# Welcome!
Welcome to the Overtracker project! Overtracker is an Android app that helps you to keep track of yours and many other player's competitive scores on Overwatch! Feel free to look around and contribute!

# Setting up
If you want to make your own fork/improvement, you'll need to set up some stuff before. First, you'll need the [latest Android Studio release](https://developer.android.com/studio).

You can clone this repo by using this command:
```
git clone https://github.com/eaemenkkstudios/overtracker-mobile
```

After that, you'll have to create a file called `api_keys.xml`, where your personal keys will be stored (don't worry, the `.gitignore` file is already covering this one).

This is the default model for this file:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="ad_app_id">ca-app-pub-XXX~XXX</string>
    <string name="ad_banner_id">ca-app-pub-XXX/XXX</string>
    <string name="ad_native_id">ca-app-pub-XXX/XXX</string>
    <string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">XXX</string>
</resources>
```

This file goes into `/app/src/main/res/values`. As you could guess, the first three tokens are related to the AdMob API, which you can get [by signing up here](https://admob.google.com/home/). The last one is related to Google Maps API, which [you can get here](https://developers.google.com/maps/documentation/javascript/get-api-key).

You'll also need to setup a basic emulator (or run on your own Android smartphone), which is also explained on [this documentation](https://developer.android.com/studio/run/emulator). You can select the device that you want to run on this menu:

![Emulator view](https://github.com/eaemenkkstudios/overtracker-mobile/blob/master/readme/emulator.png?raw=true)

Run the app by pressing on the play button or by pressing `Shift + F10`. Aaaaaand that's it! Happy hacking!

If you want to properly contribute to the development of this app, check out [CONTRIBUTING.md](CONTRIBUTING.md).

# Change Log
## v2.0.0
### Features
- Blizzard OAuth-based log-in system;
- Heroes details;
- Most played heroes by region;
- Helping Dialogflow chatbot;
- Player profile screen refactoring.
### Bug fixes
- Minor bugs with log-in sessions.
## v1.1.1
### Bug fixes
- Decreases ads frequency.
## v1.1.0
### Features
- Ads support.
## v1.0.0
### Features
- Firebase Realtime log-in and sign-up system;
- Global and local feeds.