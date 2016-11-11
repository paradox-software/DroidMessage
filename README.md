![DroidMessage Logo][1]
> Desktop Text Message Platform 

__DroidMessage__ is a program born out of the desire 
for an open source android equivalent of Apple's 
iMessage desktop compatibility.

This program lets you read, send, and manage your text messages and multimedia content from your desktop
without interacting with your phone.

__DroidMessage__ consists of two parts; the desktop application and the Android application.
Instructions for installing and setting up the programs can be found below.

## Principles ##
*   All private information sent over the wire encrypted
*   All user interfaces follow Google's Material Design
*   User-friendly and secure

## Installation ##
### Prerequisites ###
*   Java 8
*   Android Phone 5+

For now 


## Developer Information ##

Until the ALPHA release, the application and server will extremely unstable, but if you would like to see what is currently implemented,
clone down this repository and use the gradle wrapper to run the following tasks in the root directory:

```
gradlew android:build
gradlew server:launch4j
```

An android apk file will be placed in: __android/build/outputs/apk__

The server binary will be placed in: __server/build/launch4j__


## Contribution ##
TBD - Will update in BETA release

## Versioning and Libraries##
We use [SemVer](http://semver.org/) for versioning. 
All major and minor versions will be appropriately tagged.

__NOTE FOR ALPHA:__ During the Alpha releases, we will 
be using a revision versioning system. 
Every commit will increase the revision number by one. 
The project started at R1. After the Alpha release, the versioning system will revert back to SemVer.

## License ##
MIT Standard License

See [license.txt](../blob/master/LICENSE.txt)

[1]: www/res/droidmessage_logo.png
