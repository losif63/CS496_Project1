# DataViewer
> 2022 Summer MadCamp Week1 Class 2 

***

## 1. Teammates ##

* 숙명여대 컴퓨터과학부 [정채현](https://github.com/chaehyuns)
* KAIST 전산학부 [서재덕](https://github.com/losif63)

***

## 2. About DataViewer ##

* DataViewer is a simple Android app that can access and display your device's data - `Contacts`, `Images`, and `Music`.
* Displays all contact information stored in device and provides utility to modify them
* Displays all images stored in device and provides zoom-in page where the user can swipe between images
* Provides utility to create personal music playlist from all audio contents stored in the device
* Provides simple music player with visualizer to play music in personal playlist

***

### Tab 1 - Contacts ###

#### Features
* In the Contacts Tab, the DataViewer app accesses and retrieves all contact information stored in the device and loads it onto the activity screen.
<p align="center">
<img src="https://user-images.githubusercontent.com/77673334/177255873-eb0d6ce0-13bf-4223-855b-c2dea6f43ad2.jpg" width="30%" height="30%">
</p>

* By selecting one of these items, you can retrieve the detailed information page of the selected contact.

<p align="center">
<img src="https://user-images.githubusercontent.com/77673334/177256156-afd4e229-357a-4c3e-aa2f-e6f398a509d0.jpg" width="30%" height="30%">
</p>

* You can call, send a text message, or video call the person on the contact.

* You can even edit, share, or even delete the contact info on this page as well.

***

#### Implementation Methods
* Uses [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview) to display Contact items
* Uses [contacts-android](https://github.com/vestrel00/contacts-android) library to retrieve all contacts information in the device (thereby utilizing [Content Providers](https://developer.android.com/guide/topics/providers/content-providers))
* Starts android contacts app activity for detailed information page by using [Intent](https://developer.android.com/guide/components/intents-filters)

***

### Tab 2 - Images ###
#### Features
* In the Images Tab, the DataViewer app accesses and retrieves all images stored in the device and loads it onto the activity screen.

<p align="center">
<img src="https://user-images.githubusercontent.com/77673334/177256452-abfbda24-4df0-4280-a598-1b46e707325c.jpg" width="30%" height="30%">
</p>

* You can zoom in onto one of these images by simply tapping them.

<p align="center">
<img src="https://user-images.githubusercontent.com/77673334/177256567-1caf9c36-92d5-47f8-8214-3f4c3c5c8f32.jpg" width="30%" height="30%">
</p>

* You can share, edit or delete the image afterwards in the zoomed-in page. 

* You can also swipe between images as well.

***
#### Implementation Methods
* Uses [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview) to display Images
* Uses [MediaFacer](https://github.com/CodeBoy722/MediaFacer) library to retrieve all images stored in the device (thereby utilizing [Content Providers](https://developer.android.com/guide/topics/providers/content-providers))
* Starts android gallery app activity for zoom-in page by using [Intent](https://developer.android.com/guide/components/intents-filters)

***

### Tab 3 - Music ###
#### Features
* In the Music tab, the DataViewer app will display your personal music playlist!! (Initially, it will appear as empty) You can add music onto this playlist by tapping the '+' button in the downright corner of the screen.

<p align="center">
<img src="https://user-images.githubusercontent.com/77673334/177256906-8f9a8272-8984-47c0-a320-2abd494361b0.jpg" width="30%" height="30%">
</p>


* Tapping the '+' button will yield the following page where all audio files stored in the device is sorted & listed by names. Simply tap one of these files to add it onto your playlist.

<p align="center">
<img src="https://user-images.githubusercontent.com/77673334/177257097-4cd50eea-2231-4ead-a379-ae546759bce2.jpg" width="30%" height="30%">
</p>

* Back to the playlist, you can play any music in your playlist by just simply tapping the item. A new musicplayer screen with an exciting visualier will appear afterwards as follows.

<p align="center">
<img src="https://user-images.githubusercontent.com/77673334/177257278-ba8762c1-8773-4bd1-9281-942d13c559d7.jpg" width="30%" height="30%">
</p>

* You can pause & resume the music by pressing the pause/resume button. You can also move through the playlist via the PREV and NEXT buttons as well.

***
#### Implementation Methods
* Uses [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview) to display Playlist and Music list
* Uses [MediaFacer](https://github.com/CodeBoy722/MediaFacer) library to retrieve all audio files stored in the device (thereby utilizing [Content Providers](https://developer.android.com/guide/topics/providers/content-providers))
* Starts new music player activity by using [Intent](https://developer.android.com/guide/components/intents-filters)
* Conveniently update information displayed in music player activity via [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
* Uses modified version of [Nier-Visualizer](https://github.com/bogerchan/Nier-Visualizer) for visualizer
* Uses [SurfaceView](https://developer.android.com/reference/android/view/SurfaceView) for the visualizer
