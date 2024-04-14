# UnsplashImageLoading

This Android app demonstrates image loading in a list without any third-party library, utilizing the Unsplash API along with network configuration.

## Prerequisites

Before running the app, ensure you have the following:

- Android Studio installed.
- A stable internet connection.

## Setup Instructions

1. **Clone the Repository**: 
   ```bash
   git clone https://github.com/monarc-h/UnsplashImageLoading.git

2. **Open the Project in Android Studio**:  
    • Launch Android Studio.  
    • Select "Open an existing Android Studio project".  
    • Navigate to the directory where you cloned the repository and select it.  

3. **Gradle and DaggerHilt Version**:  
    • Open **File > Project Structure**.  
    • Select "Project".  
    • Make sure Gradle Plugin Version is **8.2.0** and Gradle Version is **8.2**.
    • Open build.gradle.kts (project level) and make sure Dagger Hilt version is **2.48**.  
    • If project is asking for sync then sync it using **Ctrl+Shift+O**.  

5. **Build and Run**:  
    • Attach external device with [USB Debugging](https://www.embarcadero.com/starthere/xe5/mobdevsetup/android/en/enabling_usb_debugging_on_an_android_device.html) turned on or Create a [Virtual Emulator](https://developer.android.com/codelabs/basic-android-kotlin-compose-emulator#2).  
    • Click on the green play button (Run) in Android Studio.  
    • Wait until the build finish then it will launch in your selected device.  

6. **Explore the App**:  
    • The app will fetch images from the Unsplash API and display them in a list.  
