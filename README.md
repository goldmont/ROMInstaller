# ROM Installer

![Screenshots](http://i.imgur.com/Byv8NED.jpg)

# Table of contents
1. [Introduction](https://github.com/peppe130/ROMInstaller#introduction)
2. [How to import the project?](https://github.com/peppe130/ROMInstaller#how-to-import-the-project)
3. [Project structure](https://github.com/peppe130/ROMInstaller#project-structure)
4. [What is Control Center?](https://github.com/peppe130/ROMInstaller#what-is-control-center)
5. [What is test mode?](https://github.com/peppe130/ROMInstaller#what-is-test-mode)
6. [What is trial mode?](https://github.com/peppe130/ROMInstaller#what-is-trial-mode)
7. [How to add a device to compatibility list?](https://github.com/peppe130/ROMInstaller#how-to-add-a-device-to-compatibility-list)
8. [How to add MD5 to its list?](https://github.com/peppe130/ROMInstaller#how-to-add-md5-to-its-list)
9. [How to set which UI to use?](https://github.com/peppe130/ROMInstaller#how-to-set-which-ui-to-use)
10. [How to add Fragments?](https://github.com/peppe130/ROMInstaller#how-to-add-fragments)
11. [How to enable Home Fragment?](https://github.com/peppe130/ROMInstaller#how-to-enable-home-fragment)
12. [How to add a download button?](https://github.com/peppe130/ROMInstaller#how-to-add-a-download-button)
13. [How to download files?](https://github.com/peppe130/ROMInstaller#how-to-download-files)
14. [How to flash recoveries?](https://github.com/peppe130/ROMInstaller#how-to-flash-recoveries)
15. [How to read preferences from updater-script?](https://github.com/peppe130/ROMInstaller#how-to-read-preferences-from-updater-script)
16. [How to change App colors?](https://github.com/peppe130/ROMInstaller#how-to-change-app-colors)
17. [How to add your own changelog or agreement?](https://github.com/peppe130/ROMInstaller#how-to-add-your-own-changelog-or-agreement)
18. [How to add your own info in Settings?](https://github.com/peppe130/ROMInstaller#how-to-add-your-own-info-in-settings)
19. [How to publish the App?](https://github.com/peppe130/ROMInstaller#how-to-publish-the-app)
20. [Project changelog](https://github.com/peppe130/ROMInstaller/blob/master/CHANGELOG.md)


# Introduction
ROM Installer is a source code project. This means that you have to download the project and to 
[import](https://github.com/peppe130/ROMInstaller#how-to-import-the-project) it into your Android Studio. 
This project requires at least a basic knowledge of Android Studio. Although the project is made in Java, a high knowledge 
of the language is not required. ROM Installer is the new revolutionary way to flash Custom ROM on every Android device (5.0+). 
What makes ROM Installer unique is its characteristic of being easily reprogrammable by the developer who adopts it. 
The App has two user interfaces (ButtonUI and SwipeUI), offers the possibility to add a splash and a disclaimer screen, 
to verify the integrity of the ROM before installing, to download any type of file in many different modes, to download 
and install a recovery. This project is distributed under GNU General Public License. This means that you must respect the 
freedom of Open Source. I'm giving you this revolutionary project for free and I expect that you keep this public and 
Open Source. Moreover you must upload your custom version of this project on GitHub because your work could act as an 
example for other developers interested in this project and also because I can help you in case you need my help. The 
most important benefit for users is that they can choose what features to install on their devices. Instead, the most 
important benefit for developers is that they don't have to edit their updater-script in order to remove mods or features 
due to users requests. The developer has just to configure the App with a preference for each feature he wants to include 
in his ROM. Then is on the own of each user to choose whether to install that feature or not.

Get the latest sample APK on Google Play:
<dl> <a href="https://play.google.com/store/apps/details?id=com.peppe130.rominstaller" target="_blank">
  <img alt="Get it on Google Play"
       src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" height="60"/>
</a></dl>


# How to import the project?

Make sure that your Android Studio and your SDK are updated to the latest version available.

1. Head over the root directory of the ROM Installer repository and fork it.
2. Now ROM Installer repository has been imported into your GitHub profile.
3. Head over the root directory of **your** ROM Installer repository, click on **_"Clone or download"_** and copy the URI.
4. Launch Android Studio, click on **_"Check out project from Version Control"_** and then on **_"Git"._**
5. In the **_"Git repository URL"_** field paste the repository URI that you have previously copied.
6. Once the project is successfully imported, you can commit your changes to your GitHub repository by using the **_green arrow_** high in the centre.
7. From the side panel on the left, select the **_"Android"_** view.
8. Expand the **_"app"_** menu and then the **_"java"_** one. The package name should now be visible. (**_com.peppe130.rominstaller_**).

![Screenshots](https://raw.githubusercontent.com/peppe130/ROMInstaller/master/art/ScreenshotStep8.png) <dl />
9. Right click on the package name > **_"Refactor"_** > **_"Rename"._**

![Screenshots](https://raw.githubusercontent.com/peppe130/ROMInstaller/master/art/ScreenshotStep9.png) <dl />
10. From the new window that opens, click on **_"Rename Package"_**, enter a new name in **_lowercase_**
<dl /> (for example: **_newrominstaller_**) and click on **_"Refactor"._**

![Screenshots](https://raw.githubusercontent.com/peppe130/ROMInstaller/master/art/ScreenshotStep10.png) <dl />
11. From the new window that appears at the bottom, click on **_"Do Refactor"_** and wait until the process is completed.

![Screenshots](https://raw.githubusercontent.com/peppe130/ROMInstaller/master/art/ScreenshotStep11.png) <dl />
12. Expand the **_"Gradle Scripts"_** menu and open **_"build.gradle(Module: app)"_**. Replace the old package name defined in **_"applicationId"_** with the new one. For example: **_applicationId "com.peppe130.newrominstaller"._**

![Screenshots](https://raw.githubusercontent.com/peppe130/ROMInstaller/master/art/ScreenshotStep13.png)

**NB:**
* If you are experiencing rendering problems, from the toolbar, open the **_"Build"_** menu, click on 
**_"Clean Project"_** and then on **_"Rebuild Project"._**

* If the **_"Activity class {…} does not exist"_** error occurs while the App is being compiled by Android Studio, just sync again the project. Look at the picture below:

![Screenshots](https://raw.githubusercontent.com/peppe130/ROMInstaller/master/art/ScreenshotStep14.png)

* You may run into **_"Activity class {…} does not exist"_** error also due to the internal complex theme chooser code. 
The App is provided with a theme chooser which allows users to change both the App theme and the App launcher icon. 
When you debug your App, Android Studio automatically installs it on your device and launches the activity that has 
the `<category android:name="android.intent.category.LAUNCHER" />` category in the Manifest. 
Each activity has its own name that distinguishes it from the others. In order to change the App launcher icon, the App has to
disable the activity that has that category in its properties and has to enable an activity alias which has the same properties 
of its parent activity but has a different icon and a different name. Android Studio can not know whether the activity alias is 
enabled or not and so it will always try to launch the alias parent activity. To solve this problem, you have to create two
different debug configuration, one for each theme (Light and Dark). From the toolbar, open the **_"Run"_** menu and click on 
**_"Edit Configurations..."._** Highlight the **_"Android Application"_** menu and click on the green **+** in the upper left 
and again click on **_"Android Application"._** Give a new name to the configuration. Click on the **_"Module"_** field and 
choose **_"app."_** Click on the **_"Launch"_** field and choose **_"Specified Activity"._** A new field called **_"Activity"_** will 
appear and you will have to write there the **complete** name of the activity that Android Studio should launch when debugging. 
Once you have filled up the fields, click the **_OK_** button to save the configuration. See the below screenshots:

![Screenshots](https://raw.githubusercontent.com/peppe130/ROMInstaller/master/art/ScreenshotStep15Part1.png)

![Screenshots](https://raw.githubusercontent.com/peppe130/ROMInstaller/master/art/ScreenshotStep15Part2.png)

# Project structure
Project is composed of two packages and two classes:
* **_activities:_** it contains all the App's activities.
* **_core:_** it contains the "heart" of the App.
* **_Control Center:_** it is the control center of the App 
(Look [HERE](https://github.com/peppe130/ROMInstaller#what-is-control-center)).

**NB:**
* I will not provide any support for changes made to the **_core_** package.

# What is Control Center?

As written in the [Introduction](https://github.com/peppe130/ROMInstaller#introduction), the main feature of ROM Installer is 
its characteristic of being easily reprogrammable by the developer who adopts it. _Control Center_ allows the developer 
to adjust the App behavior by changing a few lines of code.

The following items belong to _Control Center_:
* _DEVICE\_COMPATIBILITY\_LIST_ = List of devices compatible with the ROM.
* _ROM\_MD5\_LIST_ = List of ROM MD5 and any of its add-ons.
* _RECOVERY\_MD5\_LIST_ = List of recoveries MD5s.
* _TEST\_MODE_ = If set to **_true_**, it enables the [test mode](https://github.com/peppe130/ROMInstaller#what-is-test-mode).
* _TRIAL\_MODE_ = If set to **_true_**, it enables the [trial mode](https://github.com/peppe130/ROMInstaller#what-is-trial-mode).
* _BUTTON\_UI_ = If set to **_true_**, it enables the user interface with buttons.
* _SHOULD\_SHOW\_SPLASH\_SCREEN_ = If set to **_true_**, it enables the splash screen.
* _SHOULD\_SHOW\_DISCLAIMER\_SCREEN_ = If set to **_true_**, it enables the disclaimer screen.
* _SHOULD\_SHOW\_HOME\_FRAGMENT_ = If set to **_true_**, it enables the home fragment.
* _AVAILABLE\_DOWNLOADS\_NUMBER_ = Number of download buttons.
* _SPLASH\_SCREEN\_DELAY_ = Duration time of splash screen (value in milliseconds).
* _SPLASH\_SCREEN\_IMAGE\_LIGHT_ = Image to display in the splash screen when light theme is enabled.
* _SPLASH\_SCREEN\_IMAGE\_DARK_ = Image to display in the splash screen when dark theme is enabled.
* _SETTINGS\_ICON_ = Toolbar Settings icon.
* _CHANGELOG\_ICON_ = Toolbar Changelog icon.
* _DEFAULT\_OPTIONS\_ICON_ = Toolbar "Default options" icon.
* _CLEAR\_DOWNLOADS\_ICON_ = Toolbar "Clear downloads" icon.

# What is test mode?
Test mode is a special mode of the App that disables each control that would normally be required. It was created to allow 
the developer to quickly test the new compiled code. Remember to disable it in your release version.

# What is trial mode?
Trial mode is another special mode that allows the developer to safely test the App on every device, no matter if it 
is compatible or not. It was created to allow the developer to test the App on multiple devices without the need to update 
the compatibility list. Remember to disable it in your release version.

# How to add a device to compatibility list?
To add a device to the compatibility list, open the _Control Center_ class, enter the device model in quotation marks on the 
list and separate each other with a comma.

**Example:** <dl />
```java
public static String[] DEVICE_COMPATIBILITY_LIST = new String[] {"Device", "Device2", "Device3"};
```

# How to add MD5 to its list?
As for the compatibility list, even for the MD5 just open the _Control Center_ class, enter the alphanumeric code in 
quotation marks on the list and separate each other with a comma.

**Example:** <dl />
```java
public static String[] ROM_MD5_LIST = new String[] {"YourFirstMD5", "YourSecondMD5", "YourThirdMD5"};
```

**or**

```java
public static String[] RECOVERY_MD5_LIST = new String[] {"YourFirstMD5", "YourSecondMD5", "YourThirdMD5"};
```

# How to set which UI to use?
To change **UI**, you have to open the _Control Center_ class and change the value to the boolean **_"BUTTON_UI":_** <dl />
* **_TRUE:_** Enables the UI with _"Next"_ and _"Back"_ buttons. <dl />
* **_FALSE:_** It enables the sensitive UI to the sliding of the finger on the screen.

# How to add Fragments?

1. Expand _"res"_ menu, right click on _"xml"_ menu and create a new **_XML_** file.
2. Expand _"xml"_ menu and open the newly created file. Inside it you have to add the preference:
  * To CheckBoxes just add the preference:
  
    ```xml
        <PreferenceCategory
          android:title="CheckBox">
          <CheckBoxPreference
              android:key="testCheckBox"
              android:defaultValue="false"
              android:title="CheckBox Title"
              android:summary="CheckBox Summary" />
        </PreferenceCategory>
        
        <!--
        
        android:key = Preference ID.
        android:defaultValue = If set to true, the CheckBox is automatically checked.
        
        -->
    ```
  * To Lists, you need first to define the number of items that will make up the list and their respective values. 
  To do this, we will use the **_"arrays.xml"_** file which is located in _"values"_ menu:
  
    ```xml
        <string-array name="your_list">
            <item>Item 1</item>
            <item>Item 2</item>
            <item>Item 3</item>
            <item>Item 4</item>
        </string-array>
        <string-array name="your_list_values">
            <item>1</item>
            <item>2</item>
            <item>3</item>
            <item>4</item>
        </string-array>
          
        <!--
            
          Each item has its own value:
          "Item 1" has value 1.
          "Item 2" has value 2.
          "Item 3" has value 3.
          "Item 4" has value 4.
            
        -->
    ```
    * After defining the items, you will have to create the preference:
    
      ``` xml
          <PreferenceCategory
            android:title="List">
            <ListPreference
                android:title="List title"
                android:summary="List summary"
                android:key="testList"
                android:defaultValue="1"
                android:entries="@array/your_list"
                android:entryValues="@array/your_list_values" />
          </PreferenceCategory>
          
          <!--
            
            android:key = Preference ID.
            android:defaultValue = Set which item should be selected by default.
            android:entries = Declare the group of items that should be set in the List.
            android:entryValues = Declare the items values.
            
            P.s. If you don't want to set a default value for a certain List, 
                 just leave its default value empty.
                 
                 For example: android:defaultValue=""
            
          -->
          
      ```

Once you have set up your XML file, you have to declare it in Java. Open the _Control Center_ class, 
look for **_"FRAGMENTS\_RESOURCES"_** Array and add your XML file name. Remember to separate each other with a comma.

**For example:**

```java

public static Integer[] FRAGMENTS_RESOURCES = new Integer[] {
    R.xml.first_fragment, R.xml.second_fragment, R.xml.third_fragment
};

```

# How to enable Home Fragment?

Firstly open _Control Center_ and look for **_"SHOULD\_SHOW\_HOME\_FRAGMENT"_** variable and set its value to **_true._** 
Open the `home_fragment_layout.xml` file under _"res/layout"_ and create a new layout for your Home Fragment. 
Then open the _Control Center_ class, look for the `HomeFragment()` method and inside it write the code in order to make 
your layout views work.

# How to add a download button?

Open _Control Center_ class, look for **_"AVAILABLE\_DOWNLOADS\_NUMBER"_** variable and write the number of buttons you want to 
set up in the **_Download Center._** To set the text of each button, look for `DownloadNameGetter` method. 
For each button you have to add a new `case` and, next to it, you have to write the button's ID.

**For example:**

```java

@Nullable
@Contract(pure = true)
public static String DownloadNameGetter(Integer ID) {

    switch (ID) {
    
        case 0: // ID = 0;
        
            return "Text of the first button";
            
        case 1: // ID = 1;
        
            return "Text of the second button";
            
    }
        
    return null;

}

```

# How to download files?
The App is provided with an internal code that lets you download any type of file in different modes in 
order to satisfy every need.

After setting up the buttons, you will have to set up the action to run when they are clicked. 
Look for `DownloadActionGetter` method in _Control Center_ class. As for the button's text, for each `case`, 
you will have to write your own download code.

**For example:**

```java

public static void DownloadActionGetter(Integer ID) {

    String mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mDownloadedFileMD5, mRecoveryPartition;

    switch (ID) {
    
        case 0: // Single download without MD5 check
        
            mDownloadLink = "YourDownloadLink";
            mDownloadDirectory = getString(R.string.rom_folder);
            mDownloadedFileFinalName = "File.zip";
            mDownloadedFileMD5 = null;

            Utils.StartSingleDownload(mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mDownloadedFileMD5);
            
            break;
            
        case 1: // Single download with MD5 check
        
            mDownloadLink = "YourDownloadLink";
            mDownloadDirectory = getString(R.string.rom_folder);
            mDownloadedFileFinalName = "File.zip";
            mDownloadedFileMD5 = "3a416cafb312cb15ce6b3b09249fe6e6";

            Utils.StartSingleDownload(mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mDownloadedFileMD5);
            
            break;
            
    }

}

```

## **Legend**

```

String mDownloadLink = It's the download link of your file.
String mDownloadDirectory = It's the destination folder.
String mDownloadedFileFinalName = It's the name that the downloaded file should assume.
String mDownloadedFileMD5 = It's the correct MD5 of the file.

```

**NB:**

* You should use hosting services which provide you direct download links such as Mediafire or Dropbox 
(on Dropbox, to download a file rather than display it, you can use **_"dl=1"_** as a query parameter in your URL).

* The environment is already placed in the internal storage. So, in **_"mDownloadDirectory"_**, you will have to store only 
the path from the internal storage to your destination folder and not the full path from root directory.

* If you don't have a direct link, you can redirect users to a website in order to let them download the ROM via their browser 
using the following syntax:

  ```java

  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("WebsiteDownloadLink")));

  ```

### The available modes are as follows:

1. Simple download without MD5 check

  ```java
  
String mDownloadLink = "YourDownloadLink";
String mDownloadDirectory = getString(R.string.rom_folder);
String mDownloadedFileFinalName = "File.zip";
String mDownloadedFileMD5 = null;

Utils.StartSingleDownload(mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mDownloadedFileMD5);
  
  ```
  
2. Simple download with MD5 check

  ```java
  
String mDownloadLink = "YourDownloadLink";
String mDownloadDirectory = getString(R.string.rom_folder);
String mDownloadedFileFinalName = "File.zip";
String mDownloadedFileMD5 = "YourMD5";

Utils.StartSingleDownload(mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mDownloadedFileMD5);
  
  ```
  
3. Download ROM

	**NB:**

	* In _Control Center_ there is a method called `DownloadROM()`. You can configure it to download the ROM and you can 
	call it by typing `ControlCenter.DownloadROM();` wherever you want.
		
	* Remember to add ROM MD5 to [ROM\_MD5\_LIST](https://github.com/peppe130/ROMInstaller#how-to-add-md5-to-its-list).
  
4. Multiple downloads without MD5 check

  ```java
  
// Download N°1
	Utils.EnqueueDownload(
		"YourDownloadLink",
		getString(R.string.rom_folder),
		"File.zip", null);

// Download N°2
	Utils.EnqueueDownload(
	    "YourDownloadLink",
		getString(R.string.rom_folder),
		"File2.zip", null);

// Download N°3
	Utils.EnqueueDownload(
		"YourDownloadLink",
		getString(R.string.rom_folder),
		"File3.zip", null);

Utils.StartMultipleDownloads();
  
  ```
  
5. Multiple downloads with MD5 check

  ```java
  
// Download N°1
	Utils.EnqueueDownload(
		"YourDownloadLink",
		getString(R.string.rom_folder),
		"File.zip",
		"YourMD5");

// Download N°2
	Utils.EnqueueDownload(
		"YourDownloadLink",
		getString(R.string.rom_folder),
		"File2.zip",
		"YourMD5");

// Download N°3
	Utils.EnqueueDownload(
		"YourDownloadLink",
		getString(R.string.rom_folder),
		"File3.zip",
		"YourMD5");

Utils.StartMultipleDownloads();
  
  ```
  
6. Multiple downloads mixed

  In case you want to disable the MD5 check for a particular file, simply set **_"null"_** value as MD5 of that file.

  ```java
  
// Download N°1 - MD5 check enabled
	Utils.EnqueueDownload(
		"YourDownloadLink",
		getString(R.string.rom_folder),
		"File.zip",
		"YourMD5");

// Download N°2 - MD5 check disabled
	Utils.EnqueueDownload(
		"YourDownloadLink",
		getString(R.string.rom_folder),
		"File2.zip", null);

// Download N°3 - MD5 check enabled
	Utils.EnqueueDownload(
		"YourDownloadLink",
		getString(R.string.rom_folder),
		"File3.zip",
		"YourMD5");
  
Utils.StartMultipleDownloads();
  
  ```

# How to flash recoveries?

After setting up the buttons, you will have to set up the action to run when they are clicked. 
Look for `DownloadActionGetter` method in _Control Center_ class. As for the button's text, for each `case`, 
you will have to write your own download code.

**For example:**

```java

public static void DownloadActionGetter(Integer ID) {

    String mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mDownloadedFileMD5, mRecoveryPartition;

    switch (ID) {
    
        case 0: // Download Recovery
        
            mDownloadLink = "YourDownloadLink";
            mDownloadDirectory = Utils.ACTIVITY.getString(R.string.rom_folder);
            mDownloadedFileFinalName = "Recovery.zip";
            mRecoveryPartition = "YourDeviceRecoveryPartition";

            Utils.StartFlashRecovery(mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mRecoveryPartition);
            
            break;
            
        case 1: // Download Recovery with Add-Ons
        
            // Download Recovery
            mDownloadLink = "YourDownloadLink";
            mDownloadDirectory = Utils.ACTIVITY.getString(R.string.rom_folder);
            mDownloadedFileFinalName = "Recovery.zip";
            mRecoveryPartition = "YourDeviceRecoveryPartition";

            // Download Add-On N°1
            Utils.EnqueueDownload(
            	"YourDownloadLink",
            	Utils.ACTIVITY.getString(R.string.rom_folder),
            	"Add-On.zip",
            	"YourMD5");

            Utils.StartFlashRecoveryWithAddons(mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mRecoveryPartition);
            
            break;

    }

}

```

## **Legend**

```

String mDownloadLink = It's the download link of your file.
String mDownloadDirectory = It's the destination folder.
String mDownloadedFileFinalName = It's the name that the downloaded file should assume.
String mRecoveryPartition = It's the recovery partition of your device.

```

**NB:**

* You must use hosting services which provide you direct download links such as Mediafire or Dropbox 
(on Dropbox, to download a file rather than display it, you can use **_"dl=1"_** as a query parameter in your URL).

* The environment is already placed in the internal storage. So, in **_"mDownloadDirectory"_**, 
you will have to store only the path from the internal storage to your destination folder and not the full path 
from root directory.

* Remember to add Recovery MD5 to [RECOVERY\_MD5\_LIST](https://github.com/peppe130/ROMInstaller#how-to-add-md5-to-its-list).


### The available modes are as follows:

1. Download Recovery
  ```java
  
String mDownloadLink = "YourDownloadLink";
String mDownloadDirectory = getString(R.string.rom_folder);
String mDownloadedFileFinalName = "Recovery.zip";
String mRecoveryPartition = "YourDeviceRecoveryPartition";

Utils.StartFlashRecovery(mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mRecoveryPartition);
  
  ```
  
2. Download Recovery with Add-Ons

  ```java
// Download Recovery
String mDownloadLink = "YourDownloadLink";
String mDownloadDirectory = getString(R.string.rom_folder);
String mDownloadedFileFinalName = "Recovery.zip";
String mRecoveryPartition = "YourDeviceRecoveryPartition";

// Download Add-On N°1
	Utils.EnqueueDownload(
		"YourDownloadLink",
		getString(R.string.rom_folder),
		"Add-On.zip",
		"YourMD5");

// Download Add-On N°2
	Utils.EnqueueDownload(
		"YourDownloadLink",
		getString(R.string.rom_folder),
		"Add-On2.zip",
		"YourMD5");

// Download Add-On N°3
	Utils.EnqueueDownload(
		"YourDownloadLink",
		getString(R.string.rom_folder),
		"Add-On3.zip",
		"YourMD5");

Utils.StartFlashRecoveryWithAddons(mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mRecoveryPartition);
  
  ```
  
# How to read preferences from updater-script?
**NB:**
* In the META-INF folder you have to include only the original _**updater-binary**_ for your 
device and the _**updater-script.**_

* If you are using Aroma Installer, just remove all stuff in the META-INF folder except for _**update-binary-installer**_ 
and _**updater-script**_ files. Then rename the _**update-binary-installer**_ into _**update-binary.**_

Firstly you need to mount `/data` partition. Once the `/data` partition is mounted, you will have access to the internal 
memory and therefore to the preferences. To read the preferences, just use the standard syntax to retrieve files with `.prop` 
extension:

```C

if file_getprop("/sdcard/YourROMFolder/preferences.prop", "YourPreferenceID") == "PreferenceValue" then
	# Do something
endif;

```


**Example:**

```C
# Mount /data partition
run_program("/sbin/mount", "-t", "auto", "/data");

# Read from preferences
if file_getprop("/sdcard/ROMFolder/preferences.prop", "wipe_data") == "true" then
	ui_print("- Wiping data");
else 
	ui_print("- Skipping wipe data");
endif;

# Unmount /data partition
unmount("/data");

```

## How to perform a full wipe without loosing ROM Installer folder?

To wipe the /data partition you can run the following **Bash** script from your updater-script:

```bash

#!/sbin/sh

# This command mounts all partitions.
/sbin/mount -a

# Write here the path to your /data partition.
DATA_PARTITION_PATH=/data

# Write here the path to your internal storage.
INTERNAL_STORAGE_PATH=/data/media/0

# Write here the name of the Android System "Download" folder.
ANDROID_SYSTEM_DOWNLOAD_FOLDER_NAME=Download

# Write here the name of your ROM Installer folder in the internal storage.
ROM_FOLDER_NAME=ROMInstaller

# Moving to /data partition.
cd $DATA_PARTITION_PATH

FILES=(*)

# Deleting everything in the /data partition except for the "media" folder.
for item in *; do
	if [ "$item" != "media" ]
		then rm -R "$item"
	fi
done

# Moving to internal storage.
cd $INTERNAL_STORAGE_PATH

FILES=(*)

# Deleting everything in the internal storage except for the "Download" and your ROM Installer folders.
for item in *; do
	if [ "$item" != "$ANDROID_SYSTEM_DOWNLOAD_FOLDER_NAME" ] && [ "$item" != "$ROM_FOLDER_NAME" ]
		then rm -R "$item"
	fi
done

```

# How to change App colors?

The App has two themes: **_"AppTheme.Light"_** and **_"AppTheme.Dark"._** To change the default theme, open the 
**_"AndroidManifest.xml"_** and edit the value of `android:theme=""`.

To change the colors of the App, you have to open **_"colors.xml"_** file located in _"res/values"_ menu.

The basic parameters are **8** for both Light and Dark themes:

1. _colorPrimary_
2. _colorPrimaryDark_
3. _colorAccent_
4. _colorBackground_
5. _textPrimaryColor_
6. _textSecondaryColor_
7. _downloadCenterButtonBorderColor_
8. _settingsPreferenceIconColor_

![Screenshots](https://raw.githubusercontent.com/peppe130/ROMInstaller/master/art/Colors_Guide.png)

## How to change Dialogs colors?

To change the colors of the Dialogs, you have to open **_"styles.xml"_** file located in _"res/values"_ menu. For each theme 
there are `<!-- Material Dialog Theme -->` and `<!-- Bouncing Dialog Theme -->` sections. Below each section name 
there are the parameters to edit the Dialog relating to the section.

# How to add your own changelog or agreement?

To edit both changelog and agreement you will have to use _HTML Text Formatting._ In the _"res/raw"_ menu there are two files:
* **_"agreement.html"_** which belongs to Agreement.
* **_"changelog.html"_** which belongs to Changelog.

# How to add your own info in Settings?

Open **_"strings.xml"_** file located in _"res/values"_ menu. At the bottom there is a **_"ROM Info"_** section composed 
of three strings:

1. _rom\_build\_number_ = write here the build number of your ROM.
2. _rom\_developer\_summary_ = write here the developer's name.
3. _rom\_themer\_summary_ = write here the themer's name.

To show a **_"Follow me"_** Dialog, open _Control Center_, look for the `ROMDeveloperInfoAction()` method and add 
inside it the following code:

```java

String[] mSocial = new String[] {"Google+", "Twitter"};

String[] mLinks = new String[] {"YourGoogle+Link", "YourTwitterLink"};

Utils.FollowMeDialog(mSocial, mLinks);

```

You can add as many social networks as you want. Just separate each other with a comma.

**Example:**

```java

public static void ROMDeveloperInfoAction() {

  String[] mSocial = new String[] {"Google+", "Twitter"};
    
  String[] mLinks = new String[] {"YourGoogle+Link", "YourTwitterLink"};
	
  Utils.FollowMeDialog(mSocial, mLinks);
	
}

```

# How to publish the App?

You **MUST** distribute the App for **FREE**, you can not earn money by selling it. By the way you **can** sell your ROM via 
your website and then, after the payment was successful, you **can** provide a link to download the App. You **can not** even use 
the App to redirect users to your website in order to buy your ROM because the App is not a **seller**. Before publishing 
remember to edit the App **_"versionCode"_** and **_"versionName"_** from Gradle in order to avoid conflicts with the 
other already published versions of your App. If you are going to release your first version of ROM Installer, you can set the 
following values in the Gradle: `versionCode 1` and `versionName "1.0"`.

### Can I publish the App on Google Play Store?
  * Sure, you can. You will have to register for a [Google Play publisher account](https://developer.android.com/distribute/googleplay/start.html) 
  if you don't have already one. 

# License

```
Copyright © 2016, Giuseppe Montuoro.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
```
