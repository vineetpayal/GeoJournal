# 🗺️ GeoJournal: A Location-Based Journal

GeoJournal is a modern Android application built with 100% Kotlin and Jetpack Compose. It allows users to create journal entries that are automatically tagged with their current geographical location. Users can view their entries in a list or see them all plotted on a map.


## 📸 Screenshots

| Journal List | Map View | Add Entry |
| :---: | :---: | :---: |
| ![Home Screen](https://github.com/user-attachments/assets/9f32c17c-1df8-4a79-9772-87ab3c627f95) | ![Map Screen](https://github.com/user-attachments/assets/634e6ad3-3b05-45e7-995a-1b4f14e6c711) | ![Create Entry Screen](https://github.com/user-attachments/assets/c4cc7e71-daba-413e-ac3a-933d44458555) |

## ✨ Features

* **Create Entries:** Save journal entries with custom text and a photo.
* **Automatic Geotagging:** Each entry is automatically tagged with the user's current latitude and longitude using the `FusedLocationProviderClient`.
* **Journal Feed:** A `LazyColumn` displays all saved entries from the local database.
* **Interactive Map:** All entries are plotted as markers on a 100% free **OpenStreetMap (OSMdroid)** view.
* **Current Location:** The map requests location permissions and automatically animates to the user's current position.
* **Persistent Storage:** All journal entries are saved locally using a **Room** database, making them available offline.

## 🛠️ Tech Stack & Architecture

This project is built using a modern, industry-standard tech stack and follows a clean MVVM architecture.

* **Language:** 100% **Kotlin**
* **UI:** **Jetpack Compose**
* **Architecture:** **MVVM** (Model-View-ViewModel)
* **Asynchronous:** **Kotlin Coroutines & Flow**
* **Dependency Injection:** **Hilt** (for managing dependencies and scoping ViewModels)
* **Database:** **Room** (for local, offline storage)
* **Navigation:** **Compose Navigation** (for managing the 3-screen app)
* **Maps:** **OSMdroid** (a 100% free and open-source alternative to Google Maps)
* **Location:** **Google Play Services** `FusedLocationProviderClient` (for getting accurate location)
* **Image Loading:** **Coil** (for loading images from the device)
* **Image Picking:** Modern Android **Photo Picker**

### Architecture Diagram

The app is structured with a clear separation of concerns, following a repository pattern.

`View (Compose) -> ViewModel -> Repository -> (Room DAO & LocationClient)`

## 🚀 How to Run

To build and run this project, you'll need:
1.  Android Studio (latest version recommended)
2.  A device or emulator with Google Play Services (for the location client to work)

Simply clone the repository and open it in Android Studio.

```bash
git clone [https://github.com/vineetpayal/GeoJournal.git](https://github.com/vineetpayal/GeoJournal.git)
