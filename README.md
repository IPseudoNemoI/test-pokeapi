# Pokédex Android Client

This is a simple Android application that serves as a client for the [PokéAPI](https://pokeapi.co/). The app allows users to browse and explore Pokémon with detailed information, pagination, sorting, and more.

## Features

- 🔍 **Pokémon List:** Displays a list of 30 Pokémon on the main screen, each with a name and image.
- 📄 **Detail Screen:** Tap a Pokémon to view detailed info including:
  - Height
  - Weight
  - Types (bird, bug, poison, etc.)
  - Stats: Attack, Defense, HP
- 📥 **Pagination:** Automatically loads 30 more Pokémon when the user scrolls to the end of the list.
- 🎲 **Random Start:** A button that re-initializes the list starting from a random Pokémon, always displaying at least 30.
- ⚔️ **Stat-Based Sorting:** Three checkboxes (Attack, Defense, HP) allow sorting the current list by one or more selected stats in descending order.
  - If multiple stats are selected, sorting is done in order of selection priority.
  - The strongest Pokémon(s) based on selected criteria are moved to the top of the list.

## Technologies Used

- **Kotlin**
- **MVVM Architecture**
- **Jetpack Components:**
  - ViewModel
  - LiveData
  - Navigation Component
- **Retrofit** for networking
- **Hilt** for dependency injection
- **Glide** for image loading
- **RecyclerView** for lists

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/IPseudoNemoI/test-pokeapi.git
2. Open the project in **Android Studio**.
3. Build and run the app on an emulator or physical device.

## API

This app uses [PokéAPI](https://pokeapi.co/) as the data source.

## Additional Notes
- Implemented error outputs when there is no internet connection
- Implemented convenient return from the Pokemon information screen
- An intuitive interface has been implemented
- Exit confirmation implemented

## Authors
Andrey Bashkov </br>
[@GitHub](https://github.com/IPseudoNemoI) </br>
[@Telegram](https://t.me/ipseudonemoi) 
