# DataStore-Preference
Key Features:
Preference Storage: The Preferences class includes set and get functions to store and retrieve data of various primitive types (Boolean, String, Int, Long, Float, and Double). Each data type has its own dedicated storage and retrieval function.

Reactive Data Flow (Flow): In addition to traditional retrieval functions, the class provides functions that return a Flow to observe changes in stored data. This allows the application to react to preference updates in a reactive manner.

Error Handling: The Flow functions implement basic error handling, returning a default value in case of exceptions such as I/O errors.

Preference Keys: Private getPreference* functions generate the necessary keys for accessing stored values in DataStore.

# Example of Use Case
```kotlin
// Creating an instance of Preferences
val preferences = Preferences(context)

// Store a boolean value
preferences.set("dark_mode", true)

// Retrieve a boolean value
val darkMode = runblocking { preferences.getBoolean("dark_mode", false) }
```
