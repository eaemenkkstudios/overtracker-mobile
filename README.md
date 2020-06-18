<p align="center">
  <img src="https://github.com/eaemenkkstudios/overtracker-mobile/blob/master/readme/logo.png?raw=true">
</p>

[![API](https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=23) [![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/dwyl/esta/issues) [![HitCount](http://hits.dwyl.com/eaemenkkstudios/overtracker-mobile.svg)](http://hits.dwyl.com/eaemenkkstudios/overtracker-mobile)

# Welcome!
Welcome to the Overtracker project! Overtracker is an Android app that helps you to keep track of yours and many other player's competitive scores on Overwatch! Feel free to look around and contribute!

You can download the latest release [here](https://github.com/eaemenkkstudios/overtracker-mobile/releases).

# Features

In order to use the features present in the Overtracker app, you'll need to create a [Blizzard](https://www.blizzard.com/) account, as it's the only login method of the app. (Make to sure to also create your battletag before attempting to login to the app for the first time)

Clicking in the icons in the bottom menu you can navigate through the main screens of the app.

The first screen after you login is the Global Feed, where the latest SR, Main Hero, Win Rate and Endorsement Level updates of the top registered players are shown in different cards. There you can also click on any card to show more information about the player in a popup window. In this popup window you can also follow the player by clicking on the button in the bottom right corner of his profile icon.

![Global Feed gif](https://github.com/eaemenkkstudios/overtracker-mobile/blob/master/readme/global_feed.gif?raw=true)

The second screen is the Local Feed. This screen is just like the Global Feed, except it only shows updates from the players you're following. There you can also search an especific player by clicking in the floating button in the bottom right corner and typing their battletag.

![Follow Player gif](https://github.com/eaemenkkstudios/overtracker-mobile/blob/master/readme/follow_player.gif?raw=true)

The button above the "Add Player" button takes you to the Following screen, where you can see a list of all the players you've followed.

![Following Players gifs](https://github.com/eaemenkkstudios/overtracker-mobile/blob/master/readme/following.gif?raw=true)

The third screen is the Hero List screen, where you can see a list of all the heroes playable in Overwatch. This list is updated based on the [game's website](https://playoverwatch.com/heroes). Clicking on any hero will take you to the Hero Details screen, where there is more information about the hero and a map showing the region where this hero is played the most. Note that the location is calculated based on the average location of ours users, so as the number of users increases, the accuracy of this location will increase too.

![Hero Details gif](https://github.com/eaemenkkstudios/overtracker-mobile/blob/master/readme/hero_details.gif?raw=true)

The fourth screen is the Chatbot screen. You can ask the bot about overwatch heroes, latest patch notes, ask him to give you a random workshop code, etc. This Chatbot was made using [DialogFlow](https://dialogflow.com/).

![Chatbot conversation](https://github.com/eaemenkkstudios/overtracker-mobile/blob/master/readme/chatbot.png?raw=true)
![Chatbot answer about patch notes gif](https://github.com/eaemenkkstudios/overtracker-mobile/blob/master/readme/patch_notes.gif?raw=true)

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

# Contributing

If you want to properly contribute to the development of this app, check out [CONTRIBUTING.md](CONTRIBUTING.md).

# Change Log

To see the full change log, check out [CHANGELOG.MD](CHANGELOG.md)