
# ROM Installer

*Screenshot*

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
10. [How to add Preferences?](https://github.com/peppe130/ROMInstaller#how-to-add-preferences)
11. [How to add Fragments?](https://github.com/peppe130/ROMInstaller#how-to-add-fragments)
12. [How to download files?](https://github.com/peppe130/ROMInstaller#how-to-download-files)
13. [How to flash recoveries?](https://github.com/peppe130/ROMInstaller#how-to-flash-recoveries)
14. [How to read preferences from updater-script?](https://github.com/peppe130/ROMInstaller#how-to-read-preferences-from-updater-script)
15. [How to change App colors?](https://github.com/peppe130/ROMInstaller#how-to-change-app-colors)
16. [How to add your own info in Settings?](https://github.com/peppe130/ROMInstaller#how-to-add-your-own-info-in-settings)


# Introduction
ROM Installer is a source code project. This means that you have to download the project and to [import](https://github.com/peppe130/ROMInstaller#how-to-import-the-project) it into your Android Studio. This project requires at least a basic knowledge of Android Studio and Java programming. ROM Installer is the new revolutionary way to flash Custom ROM on every Android device (5.0+). What makes ROM Installer unique is its characteristic of being easily reprogrammable by the developer who adopts it. The App has two user interfaces (ButtonUI and SwipeUI), offers the possibility to add a splash and a disclaimer screen, to verify the integrity of the ROM before installing, to download any type of file in five different ways, to download and install a recovery.


# How to import the project?

Make sure that your SDK is updated to the latest version available.

1. Download the project from [GitHub](https://github.com/peppe130/ROMInstaller/archive/master.zip).
2. Extract it and move its folder to the Android Studio's project directory.
3. Give a new name to the project's folder. For example: _NewROMInstaller_.
4. Launch Android Studio.
5. Click on **_"Open an existing Android Studio project"_**.
6. From the window that opens, select the project and wait while it is being imported.
7. From the side panel on the left, select the **_"Android"_** view.
8. Expand the **_"app"_** menu and then the **_"java"_** one. The package name should now be visible. (**_com.peppe130.rominstaller_**).

![Screenshots](https://raw.githubusercontent.com/peppe130/ROMInstaller/master/Screenshot/Step8.png) <dl />
9. Right click on the package name > **_"Refactor"_** > **_"Rename"_**.

![Screenshots](https://raw.githubusercontent.com/peppe130/ROMInstaller/master/Screenshot/Step9.png) <dl />
10. From the new window that opens, click on **_"Rename Package"_**, enter a new name in lowercase
<dl /> (for example: **_newrominstaller_**) and click on **_"Refactor"_**.

![Screenshots](https://raw.githubusercontent.com/peppe130/ROMInstaller/master/Screenshot/Step10.png) <dl />
11. From the new window that appears at the bottom, click on **_"Do Refactor"_** and wait until the process is completed.

![Screenshots](https://raw.githubusercontent.com/peppe130/ROMInstaller/master/Screenshot/Step11.png) <dl />
12. Expand the **_"Gradle Scripts"_** menu and open **_"build.gradle(Module: app)"_**. Replace the old package name defined in **_"applicationId"_** with the new one. For example: **_applicationId "com.peppe130.newrominstaller"_**

![Screenshots](https://raw.githubusercontent.com/peppe130/ROMInstaller/master/Screenshot/Step13.png)

**NB:** If the error **_"Activity class {…} does not exist"_** does occur while the App is compiled by Android Studio, just sync again the project. Look at the picture below:

![Screenshots](https://raw.githubusercontent.com/peppe130/ROMInstaller/master/Screenshot/Step14.png)

# Project structure
Project is composed of two packages and one class:
  1. **_activities:_** it contains all the App's activities.
  2. **_core:_** it contains the "heart" of the App.
  3. **_Control Center:_** it is the control center of the App (look [HERE](https://github.com/peppe130/ROMInstaller#what-is-control-center)).
  4. **_FragmentsCollector:_** it is the preferences collector.

**NB:** I will not provide any support for any changes made to the **_core_** package.

# What is Control Center?

As written in the [Introduction](https://github.com/peppe130/ROMInstaller#introduction), the main feature of ROM Installer is its characteristic of being easily reprogrammable by the developer who adopts it. _Control Center_ allows the developer to adjust the App behavior by changing a few lines of code.

The following items belong to **_Control Center_**:
* _DEVICE\_COMPATIBILITY\_LIST_ = List of devices compatible with the ROM.
* _ROM\_MD5\_LIST_ = List of ROM's MD5s and any of its add-ons.
* _RECOVERY\_MD5\_LIST_ = List of recoveries MD5s.
* _TEST\_MODE_ = If set to **_true_**, it enables the [test mode](https://github.com/peppe130/ROMInstaller#what-is-test-mode).
* _TRIAL\_MODE_ = If set to **_true_**, it enables the [trial mode](https://github.com/peppe130/ROMInstaller#what-is-trial-mode).
* _BUTTON\_UI_ = If set to **_true_**, it enables the Button user interface.
* _SHOULD\_SHOW\_SPLASH\_SCREEN_ = If set to **_true_**, it enables the splash screen.
* _SHOULD\_SHOW\_DISCLAIMER\_SCREEN_ = If set to **_true_**, it enables the disclaimer screen.
* _SPLASH\_SCREEN\_DELAY_ = Duration time of splash screen (value in milliseconds).
* _SPLASH\_SCREEN\_IMAGE_ = Image to display in the splash screen.
* _SETTINGS\_ICON_ = Toolbar Settings icon.
* _CHANGELOG\_ICON_ = Toolbar Changelog icon.
* _DEFAULT\_OPTIONS\_ICON_ = Toolbar "Default options" icon.
* _CLEAR\_DOWNLOADS\_ICON_ = Toolbar "Clear downloads" icon.

# What is test mode?
Test mode is a special mode of the App that disables each control that would normally be required. It was created to allow the developer to quickly test the new compiled code.

# What is trial mode?
Trial mode is another special mode that allows the developer to safely test the App on every device, no matter if it is compatible or not. It was created to allow the developer to test the App on multiple devices without the need to update the compatibility list.

# How to add a device to compatibility list?
To add a device to the compatibility list, open the **_Control Center_** class, enter the device model on the list in quotation marks and separate from each other with a comma.

**Example:** <dl />
```java
public static String[] DEVICE_COMPATIBILITY_LIST = new String[] {"Device", "Device2", "Device3"};
```

# How to add MD5 to its list?
As for the compatibility list, even for the MD5 just open the **_Control Center_** class, enter the alphanumeric code on the list in quotation marks and separate from each other with a comma.

**Example:** <dl />
```java
public static String[] ROM_MD5_LIST = new String[] {"4ba678ca02e60557757d29e91e1e792f", "7b8fd857b3448827c93864d691324ddf", "edbaef755357a3e82e45f1837fd2e10b"};
```

**or**

```java
public static String[] RECOVERY_MD5_LIST = new String[] {"5fb732eea3d3e2b407fa7685c27a5354", "b79abd4696176ccd48c883a43027b2b4", "44c22fbf70977c3b44ac1e2a0c5626c3"};
```

# How to set which UI to use?
To change **UI**, you have to open the **_Control Center_** class and change the value to the boolean **_BUTTON_UI:_** <dl />
1. **_TRUE:_** Enables the UI with _"Next"_ and _"Back"_ buttons. <dl />
2. **_FALSE:_** It enables the sensitive UI to the sliding of the finger on the screen.

# How to add Preferences?

1. Expand _"res"_ menu, right click on _"xml"_ menu and create a new **_XML_** file.
2. Expand _"xml"_ menu and open the newly created file. Inside it you have to add the preference:
  * To CheckBoxes just add the preference:
  
    ```xml
        <PreferenceCategory
          android:title="CheckBox">
          <com.peppe130.rominstaller.core.CustomCheckBoxPreference
              android:key="testCheckBox"
              android:defaultValue="false"
              android:title="CheckBox Title"
              android:summary="CheckBox Summary" />
        </PreferenceCategory>
        
        <!--
        
        android:key = Preference ID
        android:defaultValue = If set to true, the CheckBox is automatically checked.
        
        -->
    ```
  * To Lists, you need first to define the number of items that will make up the list and their respective values. To do this, we will use the **_"arrays.xml"_** file which is located in _"values"_ menu:
  
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
    * After defining the elements, you have to create the preference:
    
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
            
            android:key = Preference ID
            android:defaultValue = Set which item should be selected by default. First item has value 1.
            android:entries = Declare the group of items that should be set in the List.
            android:entryValues = Declare the items values.
            
          -->
          
      ```

The optimal number of preferences for **XML** file is **_4_**.

Once the preference is configured, in order to export its value, you must declare it in Java. To do this, just open the **_Control Center_** and, in the `ExportPreferences()` method, add:
```java
mBufferedWriter.write(
                      "CheckBox=" + (String.valueOf(SP.getBoolean("YourCheckBoxID", DefaultValue))) +
                      "\nList=" + (SP.getString("YourListID", "DefaultValue")) +
                      "\nList2=" + (SP.getString("YourList2ID", "DefaultValue")) +
                      "\nCheckBox2=" + String.valueOf(SP.getBoolean("YourCheckBox2ID", DefaultValue))
                    );
                    
  /*
  
  Please notice the "\n" at the beginning of the second, third or fourth string. It is necessary to go in a new line.
  Each line, except the first, needs "\n". Moreover, for each new line, you have to add a "+" at the end of the previous one.
  
  To CheckBoxes the syntax to get the preference value is: (String.valueOf(SP.getBoolean("YourCheckBoxID", DefaultValue)))
  To Lists the syntax to get the preference value is: (SP.getString("YourListID", "DefaultValue"))
  
  The "YourCheckBoxID" or "YourListID" is the "android:key" that you have defined in the XML file.
  
  The DefaultValue is the value that the esported preference will assume if something goes wrong during the export process and is equal to the default value you have set in the XML file.
  To CheckBoxes the DefaultValue could be "true" or "false" (without quotes).
  To Lists the DefaultValue is the value is a number (put the DefaultValue inside quotes).
  
  */
```

In the `DefaultValues()` method add:
```java
  // To CheckBoxes:
  mEditor.putBoolean("YourCheckBoxID", DefaultValue).commit();
  
  // To Lists:
  mEditor.putString("YourListID", "DefaultValue").commit();

```

# How to add Fragments?
After adding the preferences, you must set up the **XML** file in Java. To do this you must create a Fragment. Fragment has the purpose to show the preferences on the screen in such a way as to interact with them. To add a Fragment, just open the **FragmentsCollector** and add at the bottom:

```java

  public static class YourFragment extends XpPreferenceFragment {

      public static YourFragment newInstance() {
          return new YourFragment();
      }

      @Override
      public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          addPreferencesFromResource(R.xml.your_xml_file);
      }

      @Override
      public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
          // Do nothing
      }

  }

```

Then declare the Fragment newly created by adding it to the Fragment list. Under

```java

  if (mListFragment != null) {
      mListFragment.clear();
  }

```

write:

```java

  mListFragment.add(new YourFragment());

```

**Example:** <dl />
```java

  if (mListFragment != null) {
      mListFragment.clear();
  }

  mListFragment.add(new YourFragment());
  mListFragment.add(new YourFragment2());
  mListFragment.add(new YourFragment3());
  ...

```

# How to download files?
The App is provided with an internal code that lets you download any type of file in different modes in order to satisfy every need.

## **Legend**

```

String mDownloadLink = It's the download link of your file.
String mDownloadDirectory = It's the destination folder.
String mDownloadedFileFinalName = It's the name that the downloaded file should assume.
String mDownloadedFileMD5 = It's the correct MD5 of the file.

```

**NB:**

1. You should use hosting services which provide you direct download links such as Mediafire or Dropbox (on Dropbox, to download a file rather than display it, you can use **_dl=1_** as a query parameter in your URL).

2. The environment is already placed in the internal storage. So, in **_mDownloadDirectory_**, you have to store only the path from the internal storage to your destination folder and not the full path from root directory.

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
String mDownloadedFileMD5 = "3a416cafb312cb15ce6b3b09249fe6e6";

Utils.StartSingleDownload(mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mDownloadedFileMD5);
  
  ```
  
3. Download ROM

   **NB:** In _Contro Center_ there is a method called `DownloadROM()`. You can configure it to download the ROM and you can call it by typing `DownloadROM()` wherever you want.

  ```java
  
ControlCenter.DownloadROM();
  
  ```
  
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
		"5fb732eea3d3e2b407fa7685c27a5354");

// Download N°2
	Utils.EnqueueDownload(
		"YourDownloadLink",
		getString(R.string.rom_folder),
		"File2.zip",
		"3a416cafb312cb15ce6b3b09249fe6e6");

// Download N°3
	Utils.EnqueueDownload(
		"YourDownloadLink",
		getString(R.string.rom_folder),
		"File3.zip",
		"f946055c11a6a25d202f81171944fa1e");

Utils.StartMultipleDownloads();
  
  ```
  
6. Multiple downloads mixed

  In case you want to disable the MD5 check for a particular file, simply set **_null_** value as MD5 of that file.

  ```java
  
// Download N°1 - MD5 check enabled
	Utils.EnqueueDownload(
		"YourDownloadLink",
		getString(R.string.rom_folder),
		"File.zip",
		"f946055c11a6a25d202f81171944fa1e");

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
		"f946055c11a6a25d202f81171944fa1e");
  
Utils.StartMultipleDownloads();
  
  ```

# How to flash recoveries?

## **Legend**

```

String mDownloadLink = It's the download link of your file.
String mDownloadDirectory = It's the destination folder.
String mDownloadedFileFinalName = It's the name that the downloaded file should assume.
String mDownloadedFileMD5 = It's the correct MD5 of the file.

```

**NB:**

1. You should use hosting services which provide you direct download links such as Mediafire or Dropbox (on Dropbox, to download a file rather than display it, you can use **_dl=1_** as a query parameter in your URL).

2. The environment is already placed in the internal storage. So, in **_mDownloadDirectory_**, you have to store only the path from the internal storage to your destination folder and not the full path from root directory.

### The available modes are as follows:

1. Download recovery
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
		"5fb732eea3d3e2b407fa7685c27a5354");

// Download Add-On N°2
	Utils.EnqueueDownload(
		"YourDownloadLink",
		getString(R.string.rom_folder),
		"Add-On2.zip",
		"3a416cafb312cb15ce6b3b09249fe6e6");

// Download Add-On N°3
	Utils.EnqueueDownload(
		"YourDownloadLink",
		getString(R.string.rom_folder),
		"Add-On3.zip",
		"f946055c11a6a25d202f81171944fa1e");

Utils.StartFlashRecoveryWithAddons(mDownloadLink, mDownloadDirectory, mDownloadedFileFinalName, mRecoveryPartition);
  
  ```
  
# How to read preferences from updater-script?
First you need to mount `/data` partition using the following command:

```C

run_program("/sbin/mount", "-t", "auto", "/data");

```

Once the `/data` partition is mounted, you will have access to the internal memory and therefore to the preferences. To read the preferences, just use the standard syntax to retrieve files with `.prop` extension:

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

# How to change App colors?

The App has two themes: **_AppTheme.Light_** and **_AppTheme.Dark._** To change the theme, open the **_AndroidManifest.xml_** file and edit the value of `android:theme=""`.

To change the colors of the App, you need to open **_"colors.xml"_** file located in _"res"_ > _"values"_ menu.

The basic parameters are **6**:

1. _colorPrimary_
2. _colorPrimaryDark_
3. _colorAccent_
4. _colorBackground_
5. _textPrimaryColor_
6. _textSecondaryColor_

![Screenshots](https://raw.githubusercontent.com/peppe130/ROMInstaller/master/Screenshot/Colors_Guide.png)

# How to add your own info in Settings?

Open **_"strings.xml"_** file located in _"res"_ > _"values"_ menu. At the bottom there is a **_"ROM Info"_** section composed of three strings:

1. _rom\_build\_number_ = write here the build number of your ROM.
2. _rom\_developer\_summary_ = write here the developer's name.
3. _rom\_themer\_summary_ = write here the themer's name.

To show a **_"Follow me"_** Dialog, open **_SettingsActivity_** located in the _"activities"_ package, look for the `setOnPreferenceClickListener()` method relative to your preference and add inside the following code:

```java

String[] mSocial = new String[] {"Google+", "Twitter"};
String[] mLinks = new String[] {"YourGoogle+Link", "YourTwitterLink"};
Utils.FollowMeDialog(mSocial, mLinks);

```

You can add as many social networks as you want. Just separate from each other with a comma.

**Example:**

```java

ROM_DEVELOPER.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
                String[] mSocial = new String[] {"Google+", "Twitter"};
                String[] mLinks = new String[] {"YourGoogle+Link", "YourTwitterLink"};
                Utils.FollowMeDialog(mSocial, mLinks);
                return false;
        }
});

```
