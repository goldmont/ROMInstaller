
# ROM Installer

*Screenshot*

# Table of contents
1. [Introduction](https://github.com/peppe130/ROMInstaller#introduction)
2. [How to import the project?](https://github.com/peppe130/ROMInstaller#how-to-import-the-project)
3. [Project structure](https://github.com/peppe130/ROMInstaller#project-structure)
4. [What is Utils?](https://github.com/peppe130/ROMInstaller#what-is-utils)
5. [What is test mode?](https://github.com/peppe130/ROMInstaller#what-is-test-mode)
6. [What is trial mode?](https://github.com/peppe130/ROMInstaller#what-is-trial-mode)
7. [How to add device to compatibility list?](https://github.com/peppe130/ROMInstaller#how-to-add-device-to-compatibility-list)
8. [How to add MD5 to its list?](https://github.com/peppe130/ROMInstaller#how-to-add-md5-to-its-list)
9. [How to set which UI to use?](https://github.com/peppe130/ROMInstaller#how-to-set-which-ui-to-use)
10. [How to add Preferences?](https://github.com/peppe130/ROMInstaller#how-to-add-preferences)
11. [How to add Fragments?](https://github.com/peppe130/ROMInstaller#how-to-add-fragments)
12. [How to download files?](https://github.com/peppe130/ROMInstaller#how-to-download-files)
13. [How to flash recoveries?](https://github.com/peppe130/ROMInstaller#how-to-flash-recoveries)
14. [How to read preferences from updater-script?](https://github.com/peppe130/ROMInstaller#how-to-read-preferences-from-updater-script)


# Introduction
ROM Installer è il nuovo metodo rivoluzionario per installare le Custom ROM su tutti i dispositivi Android (5.0+).
Ciò che rende ROM Installer unica, è la sua caratteristica di essere facilmente riprogrammabile dallo sviluppatore che l'adotta. L'App possiede due interfacce utente (ButtonUI e SwipeUI), offre la possibilità di aggiungere una SplashScreen ed una schermata per il disclaimer, di verificare l'integrità della ROM prima dell'installazione, di scaricare qualsiasi tipo di file in cinque diverse modalità, di scaricare ed installare una recovery.


# How to import the project?
1. Scaricare il progetto da [GitHub](https://github.com/peppe130/ROMInstaller/archive/master.zip).
2. Estrarlo e spostare la sua cartella nella directory dei progetti di Android Studio.
3. Rinominare la cartella del progetto con un nuovo nome. Ad esempio: _NewROMInstaller_.
4. Avviare Android Studio.
5. Cliccare su **_"Open an existing Android Studio project"_**.
6. Dalla finestra che si aprirà, selezionare il progetto ed attendere che venga importato.
7. Dal pannello laterale a sinistra, selezionare la vista **_"Android"_**.
8. Estendere il menù **_"app"_** e poi il menù **_"java"_**. Il nome del pacchetto sarà ora visibile (**_com.peppe130.rominstaller_**).
![Screenshots](https://raw.githubusercontent.com/peppe130/ROMInstaller/master/Screenshot/Step8.png)
9. Tasto destro del mouse sul nome del pacchetto > **_"Refactor"_** > **_"Rename"_**.
![Screenshots](https://raw.githubusercontent.com/peppe130/ROMInstaller/master/Screenshot/Step9.png)
10. Nella nuova finestra che si aprirà, cliccare su **_"Rename Package"_**, inserire un nuovo nome in minuscolo
<dl /> (Ad esempio: **_newrominstaller_**) e cliccare su **_"Refactor"_**.
![Screenshots](https://raw.githubusercontent.com/peppe130/ROMInstaller/master/Screenshot/Step10.png)
11. Nella nuova finestra che apparirà in basso, cliccare su **_"Do Refactor"_** e attendere che il processo venga completato.
![Screenshots](https://raw.githubusercontent.com/peppe130/ROMInstaller/master/Screenshot/Step11.png)
12. Estendere il menù **_"Gradle Scripts"_** e aprire **_"build.gradle(Module: app)"_**.
13. Cambiare il nome del vecchio pacchetto definito in **_"applicationId"_** con il nuovo.
<dl />Ad esempio: **_applicationId "com.peppe130.newrominstaller"_**
![Screenshots](https://raw.githubusercontent.com/peppe130/ROMInstaller/master/Screenshot/Step13.png)

**NB:** Nel caso in cui si verificasse l'errore **_"Activity class {…} does not exist"_** quando viene compilata l'App da Android Studio, sincronizzare nuovamente il progetto. Vedi immagine sottostante:
![Screenshots](https://raw.githubusercontent.com/peppe130/ROMInstaller/master/Screenshot/Step14.png)

# Project structure
Il progetto è composto da due pacchetti e una classe:
  1. **_activities:_** contiene tutte le Activity dell'App.
  2. **_core:_** contiene il cuore dell'App.
  3. **_Utils:_** è il centro di controllo dell'App (Guarda [QUI](https://github.com/peppe130/ROMInstaller#what-is-utils)).

**NB:** Non verrà fornito alcun supporto per eventuali modifiche apportate al pacchetto **_core_**.

# What is Utils?
Come scritto nell'[Introduzione](https://github.com/peppe130/ROMInstaller#introduction), la principale caratteristica di ROM Installer è quella di essere facilmente riprogrammabile dallo sviluppatore che l'adotta. _Utils_ non è altro che un centro di controllo per regolare il comportamento dell'App modificando poche stringhe di codice. _Utils_ è divisa in due sezioni: **_Editable_** e **_Uneditable_**. Noi ci occuperemo della sezione marcata come _Editable_. <dl />
**NB:** Non verrà fornito alcun supporto per eventuali modifiche apportate al codice marcato come **_Uneditable_**.

Alla sezione **_Editable_** appartengono:
* _DEVICE\_COMPATIBILITY\_LIST_ = Lista dei dispositivi compatibili con la ROM.
* _ROM\_MD5\_LIST_ = Lista degli MD5 della ROM e di eventuali suoi componenti aggiuntivi.
* _RECOVERY\_MD5\_LIST_ = Lista degli MD5 delle recovery.
* _TEST\_MODE_ = Se impostato su **_true_**, attiva la modalità test.
* _TRIAL\_MODE_ = Se impostato su **_true_**, attiva la modalità trial.
* _BUTTON\_UI_ = Se impostato su **_true_**, attiva la modalità l'interfaccia con i bottoni.
* _SHOULD\_SHOW\_SPLASH\_SCREEN_ = Se impostato su **_true_**, attiva la SplashScreen.
* _SPLASH\_SCREEN\_DELAY_ = Tempo di durata della SplashScreen (Valore espresso in millisecondi).
* _SPLASH\_SCREEN\_IMAGE_ = Immagine da mostrare nella SplashScreen.
* _APP\_ICON\_MULTITASKING_ = Icona dell'header dell'App nel multitasking.
* _APP\_HEADER\_COLOR\_MULTITASKING_ = Colore dell'header dell'App nel multitasking.
* _PROGRESS\_BAR\_COLOR_ = Colore della ProgressBar dei Dialog di caricamento.
* _FILE\_CHOOSER\_TITLE\_COLOR_ = Colore del titolo del FileChooser.
* _FILE\_CHOOSER\_CONTENT\_COLOR_ = Colore del contenuto del FileChooser.
* _FILE\_CHOOSER\_BACKGROUND\_COLOR_ = Colore dello sfondo del FileChooser.
* _SETTINGS\_ICON_ = Icona delle Impostazioni nella Toolbar.
* _CHANGELOG\_ICON_ = Icona del Changelog nella Toolbar.
* _DEFAULT\_OPTIONS\_ICON_ = Icona "Opzioni default" nella Toolbar.
* _CLEAR\_DOWNLOADS\_ICON_ = Icona "Clear downloads" nella Toolbar.

# What is test mode?
La test mode è una particolare modalità dell'App che disattiva ogni controllo che normalmente sarebbe obbligatorio. È stata creata per permettere allo sviluppatore di testare rapidamente il nuovo codice compilato.

# What is trial mode?
La trial mode è un'altra particolare modalità che permette di testare in sicurezza l'App su ogni dispositivo, a prescindere se compatibile o meno. È stata creata per permettere allo sviluppatore di testare l'App su più dispositivi senza il bisogno di aggiornare la lista di compatibilità.

# How to add device to compatibility list?
Per aggiungere un dispositivo alla lista di compatibilità, aprire la classe **_Utils_**, inserire nella lista il modello del dispositivo tra virgolette e separare gli uni dagli altri con una virgola.

**Esempio:** <dl />
```java
public static String[] DEVICE_COMPATIBILITY_LIST = new String[] {"Device", "Device2", "Device3"};
```

# How to add MD5 to its list?
Come per la lista di compatibilità, anche per l'MD5 basta aprire la classe **_Utils_**, inserire nella lista il codice alfanumerico tra virgolette e separare gli uni dagli altri con una virgola.

**Esempio:** <dl />
```java
public static String[] ROM_MD5_LIST = new String[] {"4ba678ca02e60557757d29e91e1e792f", "7b8fd857b3448827c93864d691324ddf", "edbaef755357a3e82e45f1837fd2e10b"};
```

**oppure**
```java
public static String[] RECOVERY_MD5_LIST = new String[] {"5fb732eea3d3e2b407fa7685c27a5354", "b79abd4696176ccd48c883a43027b2b4", "44c22fbf70977c3b44ac1e2a0c5626c3"};
```

# How to set which UI to use?
Per cambiare **UI** bisogna aprire la classe **_Utils_** e cambiare il valore al Boolean **_BUTTON_UI:_** <dl />
1. **_TRUE:_** Attiva la UI con i bottoni _"Next"_ e _"Back"_. <dl />
2. **_FALSE:_** Attiva la UI sensibile allo scorrimento del dito sullo schermo.

# How to add Preferences?

1. Espandere il menù _"res"_, tasto destro del mouse su _"xml"_ e creare un nuovo file **_XML_**.
2. Espandere il menù _"xml"_ ed aprire il nuovo file creato. Dentro di esso si dovrà aggiungere la preferenza:
  * Per un CheckBox basta aggiungere semplicemente la preferenza:
  
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
  * Per una Lista, bisogna innanzitutto definire il numero di elementi che la comporranno ed i loro rispettivi valori. Per fare ciò, utilizzeremo il file **_"arrays.xml"_** che si trova nel menù _"values":_
  
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
            
          Each item has its own value.
          "Item 1" has value 1.
          "Item 2" has value 2.
          "Item 3" has value 3.
          "Item 4" has value 4.
            
        -->
    ```
    * Una volta definiti gli elementi, bisogna creare la preferenza:
    
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

Il numero ottimale di preferenze per file **XML** è **_quattro_**.

Configurata la preferenza, affinchè sia possibile esportare il suo valore, è necessario dichiararla in Java. Per fare ciò apriamo la **_MainActivity_** situata in _"java"_ > _"com.peppe130.yourpackagename"_ > _"activities"._

Nel metodo `ExportPreferences()` aggiungere:
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

Nel metodo `DefaultValues()` aggiungere:
```java
  // To CheckBoxes:
  mEditor.putBoolean("YourCheckBoxID", DefaultValue).commit();
  
  // To Lists:
  mEditor.putString("YourListID", "DefaultValue").commit();

```

# How to add Fragments?
Dopo aver aggiunto le preferenze, dobbiamo configurare il file **XML** in Java. Per fare ciò è necessario creare un Fragment. Il Fragment ha lo scopo di mostrare le preferenze sullo schermo in modo tale da poter interagire con esse. Per aggiungere un Fragment, apriamo la **MainActivity** e aggiungiamo in fondo:

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

Ora bisogna dichiarare il Fragment appena creato aggiungendolo alla lista dei Fragment. Per fare ciò, aprire la classe **MainActivity** e, nel metodo onCreate(), sotto

```java

  if (mListFragment != null) {
      mListFragment.clear();
  }

```

scrivere:

```java

  mListFragment.add(new YourFragment());

```

**Esempio:** <dl />
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
L'App è provvista di un codice interno che permette di scaricare qualsiasi tipo di file in diverse modalità al fine di soddisfare ogni esigenza.

## **Legend**

```

Uri mDownloadLink = It's the download link of your file
File mDownloadDirectory = It's the destination folder
String mDownloadedFileFinalName = It's the name that the downloaded file should assume
String mDownloadedFileMD5 = It's the correct MD5 of the file.
DownloadManager.Request mRequest = It's the download request.

// Multiple downloads
Utils.DOWNLOAD_LINK_LIST = List of download links for multiple downloads.
Utils.DOWNLOAD_DIRECTORY_LIST = List of download destination folders for multiple downloads.
Utils.DOWNLOADED_FILE_NAME_LIST = List of names that the downloaded files should assume for multiple downloads.
Utils.DOWNLOADED_FILE_MD5_LIST = List of files MD5s for multiple downloads.
Utils.DOWNLOAD_REQUEST_LIST = List of download requests for multiple downloads.

```

**NB:** The environment is already placed in the internal storage. So, in **_mDownloadDirectory_**, you have to store only the path from the internal storage to your destination folder and not the full path from root directory.

### Le modalità disponibili sono le seguenti:

1. Simple download without MD5 check

  ```java
  
    Uri mDownloadLink = Uri.parse("http://www.YourDownloadLink.com");
    File mDownloadDirectory = new File(DestinationFolderInTheInternalStorage);
    String mDownloadedFileFinalName = "File.zip";
    
    DownloadManager.Request mRequest = new DownloadManager.Request(mDownloadLink);
    mRequest.setDestinationInExternalPublicDir(mDownloadDirectory.getPath(), mDownloadedFileFinalName);
  
    new Download(
        mRequest,
        mDownloadDirectory,
        mDownloadedFileFinalName).execute();
  
  ```
  
2. Simple download with MD5 check

  ```java
  
    Uri mDownloadLink = Uri.parse("http://www.YourDownloadLink.com");
    File mDownloadDirectory = new File(DestinationFolderInTheInternalStorage);
    String mDownloadedFileFinalName = "File.zip";
    mDownloadedFileMD5 = "3a416cafb312cb15ce6b3b09249fe6e6";
    
    DownloadManager.Request mRequest = new DownloadManager.Request(mDownloadLink);
    mRequest.setDestinationInExternalPublicDir(mDownloadDirectory.getPath(), mDownloadedFileFinalName);
  
    new Download(
        mRequest,
        mDownloadDirectory,
        mDownloadedFileFinalName,
        mDownloadedFileMD5).execute();
  
  ```
  
3. Download ROM

  ```java
  
    Uri mDownloadLink = Uri.parse("http://www.YourDownloadLink.com");
    File mDownloadDirectory = new File(DestinationFolderInTheInternalStorage);
    String mDownloadedFileFinalName = "ROM.zip";
    
    DownloadManager.Request mRequest = new DownloadManager.Request(mDownloadLink);
    mRequest.setDestinationInExternalPublicDir(mDownloadDirectory.getPath(), mDownloadedFileFinalName);
  
    new Download(
        mRequest,
        mDownloadDirectory,
        mDownloadedFileFinalName, true).execute();
  
  ```
  
4. Multiple downloads without MD5 check

  ```java
  
    // Download N°1
    Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.YourDownloadLink.com"));
    Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(DestinationFolderInTheInternalStorage));
    Utils.DOWNLOADED_FILE_NAME_LIST.add("File.zip");
    DownloadManager.Request mRequest0 = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(0));
    mRequest0.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(0).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(0));
    Utils.DOWNLOAD_REQUEST_LIST.add(mRequest0);

    // Download N°2
    Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.YourDownloadLink.com"));
    Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(DestinationFolderInTheInternalStorage));
    Utils.DOWNLOADED_FILE_NAME_LIST.add("File2.zip");
    DownloadManager.Request mRequest1 = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(1));
    mRequest1.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(1).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(1));
    Utils.DOWNLOAD_REQUEST_LIST.add(mRequest1);

    // Download N°3
    Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.YourDownloadLink.com"));
    Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(DestinationFolderInTheInternalStorage));
    Utils.DOWNLOADED_FILE_NAME_LIST.add("File3.zip");
    DownloadManager.Request mRequest2 = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(2));
    mRequest2.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(2).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(2));
    Utils.DOWNLOAD_REQUEST_LIST.add(mRequest2);
  
    new Download(
        Utils.DOWNLOAD_REQUEST_LIST.get(0),
        Utils.DOWNLOAD_DIRECTORY_LIST.get(0),
        Utils.DOWNLOADED_FILE_NAME_LIST.get(0), 1).execute();
  
  ```
  
5. Multiple downloads with MD5 check

  ```java
  
    // Download N°1
    Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.YourDownloadLink.com"));
    Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(DestinationFolderInTheInternalStorage));
    Utils.DOWNLOADED_FILE_NAME_LIST.add("File.zip");
    Utils.DOWNLOADED_FILE_MD5_LIST.add("5fb732eea3d3e2b407fa7685c27a5354");
    DownloadManager.Request mRequest0 = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(0));
    mRequest0.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(0).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(0));
    Utils.DOWNLOAD_REQUEST_LIST.add(mRequest0);

    // Download N°2
    Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.YourDownloadLink.com"));
    Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(DestinationFolderInTheInternalStorage));
    Utils.DOWNLOADED_FILE_NAME_LIST.add("File2.zip");
    Utils.DOWNLOADED_FILE_MD5_LIST.add("3a416cafb312cb15ce6b3b09249fe6e6");
    DownloadManager.Request mRequest1 = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(1));
    mRequest1.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(1).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(1));
    Utils.DOWNLOAD_REQUEST_LIST.add(mRequest1);

    // Download N°3
    Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.YourDownloadLink.com"));
    Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(DestinationFolderInTheInternalStorage));
    Utils.DOWNLOADED_FILE_NAME_LIST.add("File3.zip");
    Utils.DOWNLOADED_FILE_MD5_LIST.add("f946055c11a6a25d202f81171944fa1e");
    DownloadManager.Request mRequest2 = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(2));
    mRequest2.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(2).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(2));
    Utils.DOWNLOAD_REQUEST_LIST.add(mRequest2);
  
    new Download(
        Utils.DOWNLOAD_REQUEST_LIST.get(0),
        Utils.DOWNLOAD_DIRECTORY_LIST.get(0),
        Utils.DOWNLOADED_FILE_NAME_LIST.get(0),
        Utils.DOWNLOADED_FILE_MD5_LIST.get(0), 1).execute();
  
  ```
  
6. Multiple downloads mixed

  Nel caso in cui si voglia disattivare il controllo dell'MD5 per un determinato file, bisogna semplicemente impostare il valore **_null_** come MD5 di quel file.

  ```java
  
    // Download N°1 - Controllo dell'MD5 attivato
    Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.YourDownloadLink.com"));
    Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(DestinationFolderInTheInternalStorage));
    Utils.DOWNLOADED_FILE_NAME_LIST.add("File.zip");
    Utils.DOWNLOADED_FILE_MD5_LIST.add("5fb732eea3d3e2b407fa7685c27a5354");
    DownloadManager.Request mRequest0 = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(0));
    mRequest0.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(0).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(0));
    Utils.DOWNLOAD_REQUEST_LIST.add(mRequest0);

    // Download N°2  - Controllo dell'MD5 disattivato
    Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.YourDownloadLink.com"));
    Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(DestinationFolderInTheInternalStorage));
    Utils.DOWNLOADED_FILE_NAME_LIST.add("File2.zip");
    Utils.DOWNLOADED_FILE_MD5_LIST.add(null);
    DownloadManager.Request mRequest1 = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(1));
    mRequest1.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(1).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(1));
    Utils.DOWNLOAD_REQUEST_LIST.add(mRequest1);

    // Download N°3 - Controllo dell'MD5 attivato
    Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.YourDownloadLink.com"));
    Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(DestinationFolderInTheInternalStorage));
    Utils.DOWNLOADED_FILE_NAME_LIST.add("File3.zip");
    Utils.DOWNLOADED_FILE_MD5_LIST.add("f946055c11a6a25d202f81171944fa1e");
    DownloadManager.Request mRequest2 = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(2));
    mRequest2.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(2).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(2));
    Utils.DOWNLOAD_REQUEST_LIST.add(mRequest2);
  
    new Download(
        Utils.DOWNLOAD_REQUEST_LIST.get(0),
        Utils.DOWNLOAD_DIRECTORY_LIST.get(0),
        Utils.DOWNLOADED_FILE_NAME_LIST.get(0),
        Utils.DOWNLOADED_FILE_MD5_LIST.get(0), 1).execute();
  
  ```

# How to flash recoveries?

## **Legend**

```

Uri mDownloadLink = It's the download link of your file
File mDownloadDirectory = It's the destination folder
String mDownloadedFileFinalName = It's the name that the downloaded file should assume
String mDownloadedFileMD5 = It's the correct MD5 of the file.
String mRecoveryPartition = Device's recovery partition.
DownloadManager.Request mRequest = It's the download request.

// Multiple downloads
Utils.DOWNLOAD_LINK_LIST = List of download links for multiple downloads.
Utils.DOWNLOAD_DIRECTORY_LIST = List of download destination folders for multiple downloads.
Utils.DOWNLOADED_FILE_NAME_LIST = List of names that the downloaded files should assume for multiple downloads.
Utils.DOWNLOADED_FILE_MD5_LIST = List of files MD5s for multiple downloads.
Utils.DOWNLOAD_REQUEST_LIST = List of download requests for multiple downloads.

```

**NB:** The environment is already placed in the internal storage. So, in **_mDownloadDirectory_**, you have to store only the path from the internal storage to your destination folder and not the full path from root directory.

### Le modalità disponibili sono le seguenti:

1. Download recovery
  ```java
  
    Uri mDownloadLink = Uri.parse("http://www.YourDownloadLink.com");
    File mDownloadDirectory = new File(DestinationFolderInTheInternalStorage);
    String mDownloadedFileFinalName = "Recovery.zip";
    mRecoveryPartition = "DeviceRecoveryPartition";
    
    DownloadManager.Request mRequest = new DownloadManager.Request(mDownloadLink);
    mRequest.setDestinationInExternalPublicDir(mDownloadDirectory.getPath(), mDownloadedFileFinalName);
  
    new FlashRecovery(
        mRequest,
        mDownloadDirectory,
        mDownloadedFileFinalName,
        mRecoveryPartition, false).execute();
  
  ```
  
2. Download Recovery with Add-Ons

  ```java
  
    Uri mDownloadLink = Uri.parse("http://www.YourDownloadLink.com");
    File mDownloadDirectory = new File(DestinationFolderInTheInternalStorage);
    String mDownloadedFileFinalName = "Recovery.zip";
    mRecoveryPartition = "DeviceRecoveryPartition";
    
    DownloadManager.Request mRequest = new DownloadManager.Request(mDownloadLink);
    mRequest.setDestinationInExternalPublicDir(mDownloadDirectory.getPath(), mDownloadedFileFinalName);
    
    // Download Add-On N°1
    Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.YourDownloadLink.com"));
    Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(DestinationFolderInTheInternalStorage));
    Utils.DOWNLOADED_FILE_NAME_LIST.add("Add-On.zip");
    Utils.DOWNLOADED_FILE_MD5_LIST.add("5fb732eea3d3e2b407fa7685c27a5354");
    DownloadManager.Request mRequest0 = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(0));
    mRequest0.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(0).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(0));
    Utils.DOWNLOAD_REQUEST_LIST.add(mRequest0);

    // Download Add-On N°2
    Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.YourDownloadLink.com"));
    Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(DestinationFolderInTheInternalStorage));
    Utils.DOWNLOADED_FILE_NAME_LIST.add("Add-On2.zip");
    Utils.DOWNLOADED_FILE_MD5_LIST.add("3a416cafb312cb15ce6b3b09249fe6e6");
    DownloadManager.Request mRequest1 = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(1));
    mRequest1.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(1).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(1));
    Utils.DOWNLOAD_REQUEST_LIST.add(mRequest1);

    // Download Add-On N°3
    Utils.DOWNLOAD_LINK_LIST.add(Uri.parse("http://www.YourDownloadLink.com"));
    Utils.DOWNLOAD_DIRECTORY_LIST.add(new File(DestinationFolderInTheInternalStorage));
    Utils.DOWNLOADED_FILE_NAME_LIST.add("Add-On3.zip");
    Utils.DOWNLOADED_FILE_MD5_LIST.add("f946055c11a6a25d202f81171944fa1e");
    DownloadManager.Request mRequest2 = new DownloadManager.Request(Utils.DOWNLOAD_LINK_LIST.get(2));
    mRequest2.setDestinationInExternalPublicDir(Utils.DOWNLOAD_DIRECTORY_LIST.get(2).getPath(), Utils.DOWNLOADED_FILE_NAME_LIST.get(2));
    Utils.DOWNLOAD_REQUEST_LIST.add(mRequest2);
  
    new FlashRecovery(
        mRequest,
        mDownloadDirectory,
        mDownloadedFileFinalName,
        mRecoveryPartition, true).execute();
  
  ```
  
# How to read preferences from updater-script?

Per leggere le preferenze dall'updater-script, bisogna innanzitutto montare la partizione `/data` con il comando:

```C

run_program("/sbin/mount", "-t", "auto", "/data");

```

Una volta montata la partizione `/data`, avremo accesso alla memoria interna e di conseguenza alle preferenze. Per leggere le preferenze basta utilizzare la sintassi standard per recuperare i file con estensione `.prop`:

```C

if file_getprop("/sdcard/YourROMFolder/preferences.prop", "YourPreferenceID") == "PreferenceValue" then
	# Do something
endif;

```


**Esempio:**

```C
# Mount /data partition
run_program("/sbin/mount", "-t", "auto", "/data");

# Read from preferences
if file_getprop("/sdcard/SomeROM/preferences.prop", "wipe_data") == "true" then
	ui_print("- Wiping data");
else 
	ui_print("- Skipping wipe data");
endif;

# Unmount /data partition
unmount("/data");

```
