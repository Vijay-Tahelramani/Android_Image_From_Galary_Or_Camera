# Android_Image_From_Galary_Or_Camera
This application shows how to pic image from galary or camera.

To select an image from galary or camera in android, it requires permission, to allow below line of code in android menifest file.
```
<uses-permission android:name="android.permission.CAMERA"></uses-permission>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

Then in Activity_main.xml of clicking image to shows options to be selected by user that either to select image from galary or camera or cancel.

```MainActivity.java``` describe the code for selecting image in android.
