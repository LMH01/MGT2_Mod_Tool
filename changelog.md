# Changelog
## Coming in [v2.3.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v2.3.0)

### Important
- **Previously saved settings will not work together with this release**
- **Previously exported mods will not work together with this release**
- **Restore points that have been created prior to this release will not work together with this release**

### New feature
- The tool now works under Linux (fr #72)
- Mod export has been rewritten to use .toml files. This increases the speed at wich mods are exported. (fr #73)
- Mod import has been rewritten from scratch. The result is a speed improvement, better reliability and more user-friendliness (fr #73)
- Mods can now be exported in a bundle, this means that all mod data is stored in a single .toml file. This increases export speed.

### Other
- Settings file now uses the .toml file format
- It is no longer possible to add randomized genres
- It is no longer possible to enable the debug logging in the settings window, instead the storage of exports can now be enabled. Debug logging can still be enabled by editing the settings.toml file
- Improved performance when handling files
- Improved text area outputs when importing, exporting, adding or removing mods
- Improved the error handling massively
  - Whenever something goes wrong the stacktrace is now written to the text area and an explanation of what went wrong is displayed
- Internal code has been massively improved
- Internal handling of paths and files has been improved
  - Renamed some folders to be more consistent
- When an initial backup is created the user is now prompted to verify the game files
- It is now checked if the initial backup is up-to-date when the tool is started. (currently vip to enable set "enableInitialBackupCheck" in the "settings.toml" file to true)
- Improved disclaimer message text
- Added new entry to the export menu
  - Export selected -> with this function only selected mods can be exported. Either single or together in a bundle.
- Mods are now sorted by alphabet when they are displayed in panels
  - e.g. When the user can choose which mods should be imported
- Added some missing translations

### Bug fixes
- When the hardware file was modified a specific data entry was not written to the file. This caused the controllers and screens to lose the requirements needed to implement them into a console.

## [v2.2.1](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v2.2.1) (Latest Version)

### Bug fixes
- Fixed bug #68
- Fixed bug #69

## [v2.2.0a](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v2.2.0a)

### HOTFIX
- Fixed #67 : The tool will now load properly and the default_content.toml file is generated properly

## [v2.2.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v2.2.0)

### New feature
- The default content files will now update themselfs whenever they are outdated. 
  - This means that the tool will no longer have to be updated whenever the dev changes some small things.

### Other
- Improved english translations
- Progress bar now shows "initializing tool" when the tool is starting
- Updated default content files that ship with this tool to work with the latest game version (BUILD 2021.08.13A)

## [v2.1.2](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v2.1.2)

### Other
- Updated the default_platforms file to include the two new platformes added in patch 2021.07.19A

## [v2.1.1](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v2.1.1)

### Other
- Added Katari VCC to the default platforms file
- Fixed typo in german language file
- Fixed typo in reademe file
- Internal coding improvements

## [v2.1.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v2.1.0)

### New feature
- Added support for hardware: You can now add custom hardware that can be used to create your own consoles
- Added support for hardware features
- You can now edit the existing genre/theme fits: This feature is located under Mods -> Themes
- It is now possible to create new initial backups

### Other
- Internal coding improvements
- Updated the default content files to include the latest changes

### Bug fixes
- Fixed: Removing themes will fail sometimes
- Fixed bug #60 - The genre summary does not fit on the whole screen

## [v2.0.7](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v2.0.7)

### Other
- Fixed some issues in the default content files that would make default content displayed as mod.

## [v2.0.6](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v2.0.6)

### Bug fixes
- Fixed bug #61
- Fixed bug #62
- Fixed bug #63

## [v2.0.5](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v2.0.5)

### Other
- When creating a theme it is now possible to write numbers in the name

### Bug fixes
- Fixed bug #59

## [v2.0.4](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v2.0.4)

### Bug fixes
- Fixed bug #58
  - When a mod is imported that uses translations for its name the import names are not displayed correctly. It could also happen that the tool would say that a mod is duplicated

## [v2.0.3](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v2.0.3)

### Other
- Added two translations that have been missing
- Renamed violence level to age rating in add theme menu. The age ratings are now displayed and no longer the values 0-3.
- Adding publisher: The genres that can be set as special genre are now determined by the release date of the publisher.

### Bug fixes
- Fixed: Program will not uninstall mods when only licences have been added
- Fixed: When showing modded themes all themes where displayed

## [v2.0.2](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v2.0.2)

### Other
- Improved stability when importing things
- Added missing translation in the add translation windows
- When specific mods are removed the game should no longer not load (fr #50)
- When npc games, npc engines and platforms are exported ids are no longer written to the file, instead the names of the features are written
- This makes the import more reliable

## Bug fixes
- Fixed bug #49 - Selecting good/bad gameplay features will select the wrong features
- Fixed bug #51 - Importing gameplay features will not import the good/bad genres properly
- Fixed bug #52 - Genre export/import will not copy the languages correctly
- Fixed bug #53 - Importing genres will not copy the good/bad gameplay features correctly
- Fixed bug #54 - Adding a genre without translations will cause the genre not to be displayed in the game
- Fixed bug #55 - When adding npc engines the platform is not selected correctly
- Fixed bug #56 - Clicking cancel in uninstall window will not close the window

## [v2.0.1](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v2.0.1)

### Other
- Added text to disclaimer message: It is now explained that the game might no longer load if certain mods are removed

### Bug fixes
- Fixed bug #47
- Fixed bug #48

## [v2.0.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v2.0.0)

### Important
- **Previously exported licences are not compatible with this version!**

### New feature
- Added support for anti cheat. Anti cheat can now be added/removed/exported/imported (fr #41)
- Added support for copy protection. Copy protection can now be added/removed/exported/imported (fr #42)
- Added support for npc games. Npc games can now be added/removed/exported/imported (fr #44)
- Added support for npc engines. Npc engines can now be added/removed/exported/imported. The technology level and the number of engine features is determined by the year the engine is released in (fr #43)
- Added support for platforms. Platforms can now be added/removed/exported/imported (fr #40)
  - Important: Currently only two platform pictures are supported by the game, when the limit is increased i will update it accordingly in the tool
- Added a progress bar and log output to the main window: This way it does no longer seam like the program is frozen when tasks are executed (fr #35)
- A log file is now created where some log output is written to. This does not include the whole console log.

### Other
- Code
  - Massive internal coding improvements
  - Some small performance improvements (Action availability is now less often checked and the default content is now only read once)
- Log
  - Some more log outputs are now only shown when the debug logging is active
  - When log output is written the timer now also displays seconds
  - The changes.log file will no longer be written
- Backups
  - Restoring the initial backup will now cause all mods to be uninstalled beforehand
  - It is no longer possible to restore the latest backup when the safety features are enabled as this could lead to problems
  - It is no longer possible to create individual backups for genre and themes file
- Removed the following options from the utilities menu: Show active themes, show active genres, open genre.txt file, open log file and open genres by id txt file.
- Settings
  - Improved how the combo box mgt2 folder works
  - When no changes have been made and "save" is clicked a special message is now displayed
  - Added new option, you can now choose if the log files should be stored
- Export
  - The summary message is no longer displayed. Instead a more generalised message is shown
- Import
  - When a zip archive has been found you can now check a box to remember the selected option
  - When duplicate mods have been found while searching folders or wile importing, the user can now check a box to remember the selected option
  - When to many folders are detected a message is now shown where the user can confirm if the import process should continue to search the folders for mods
- Uninstalling
  - The program will now only exit if configuration files are deleted
  - Initial backups are no longer deleted when the safety features are enabled. However if everything should be uninstalled the initial backups are deleted anyway.
- Added error messages for functions that require internet, when no internet is available
- German translations have been completed. Every text that is displayed can now be translated. (fr #30)
- The window size has been increased to fit the progress bar and log output better
- Reworked how the npc games list window looks and works
- When clicking quit while a task is performed a confirm message is now shown
- When a publisher is imported for which the special genre does not exist, the special genre is now allocated randomly
- When this tool is not compatible with the current mgt2 version, a warning is now displayed
- When the mgt2 folder has not been found all menus are now locked until the correct folder is selected
- When a operation is performed the menu bar is now locked to prevent multiple operations from being performed simultaneously
- When the publishers have been replaced with the real life publishers, the replaced publishes are now shown in the active mods page and the remove publishers window
- Changed how the active mods are displayed in the show active mods window

### Bug fixes
- Fixed bug #38
- Fixed bug #39
- Fixed bug #45
- Fixed bug #46
- Uninstalling mods would fail when no genre or publishers where installed
- It was possible to add duplicate licences. This was possible for import and manual adding.
- It was possible to add duplicate publishers, genres, engine and gameplay features when adding them manually
- The "[NAME AR]" tag is no longer shown when selecting what entries should be imported
- Duplicate licences where not detected when scanning the file system for compatible mods
- When removing all publisher icons some icons where removed that belonged to the game.
- When the add company icon process is canceled a error message was shown

## [v1.12.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.12.0)

### New feature
- Mods can now be imported by using a direct download url
- It is now possible see a list where every installed mod is listed

### Other
- Import menu
  - You can now open a webpage where mods are listed. These mods can be imported by using Import from URL

## [v1.11.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.11.0)

### New feature
- Adding gameplay feature:
  - You can now select if the gameplay feature should be compatible with arcade cabinets/mobile games. (Reference to BUILD 2021.04.09A)
- It is now possible to create a mod restore point
  - All currently installed mods will be safed and they can later be reinstalled with the click of one button
  - This is useful when the game is updated to reinstall all previously active mods. This feature is found in the backup menu.

### Other
- Added recognition for the new gameplay features that have been added in BUILD 2021.04.09A
- Added a couple more german translations. These include: About, Import process, Update checker, backup and uninstall window
- It is no longer possible to delete backups when the safety features are enabled.
- The capitalisation in the main menu has been changed.

### Bug fixes
- Fixed bug #32
- Exporting genre: When no bad/good gameplay features have been detected the export will no longer fail. Instead, the fields are just left blank.
- Fixed an issue where no names where added when the translation process was canceled when adding a gameplay/engine feature.

## [v1.10.3](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.10.3)

### Other
- Added support for the 12 new features (Reference to BUILD 2021.03.25A)

### Bug fixes
- When closing the "Add new genre" windows the programm will no longer exit

## [v1.10.2](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.10.2)

### New feature
- It is now possible to select how much a topic should influence the games age rating

### Other
- Renamed "Extreme Violence" to "Explicit Content" in the add genre page 8

## [v1.10.1](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.10.1)

### Bug fixes
- A message was not written in the change log file when a licence has been added/removed
- Genre export would fail in some cases when a genre has been deleted prior

## [v1.10.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.10.0)

### New feature
- It is now possible to add/remove/import/export licences

### Bug fixes
- When clicking open backup folder the export folder was opened

## [v1.9.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.9.0)

### New feature
- It is now possible to generate randomized genres and add them to the game

### Other
- Add publisher menu: Spinners have been locked, tooltips have been added and some enhancements where made to understand some things better in this menu
- Added about window
- Added a message that is being displayed when you click the mod menu for the first time. It contains some useful information on the tool
- Added a option to change the ui language of the tool. The german translations are currently wip. The main window and settings window are translated completely
- Overhauled the import function
  - Incompatible mods are no longer added to the import list
  - The detected genres are now verified beforehand if they work with this tool
  - ".zip"-Files that have been detected can now be extracted and searched for mods
  - It is possible to disable the safety features to allow incompatible mods to be listed in the import list anyway
  - If duplicate mods have been found the user is now asked if the duplicate should be added to the import list anyway
- The maximum input value for some spinners has been adjusted
- The spinner values for research, development cost and research cost have been adjusted 
- Uninstall menu: When no check box is selected an error message is now shown
- Update checker
  - The key features for the new version are now shown in the update message
  - You can now select which channel should be searched for updates: Release or Alpha

### Bug fixes
- Fixed bug #29
- Genre image files where not removed when only a single genre was removed

## [v1.8.3b](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.8.3b)

### Hotfix
- It is now possible again to add and import genres.
- The tool now works with this update:

### BUILD 2021.03.20A
- The game design settings (slider) have been completely revised.

### Important
- **All genres that have been exported prior to this release will not work together with this update.**

## [v1.8.3a](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.8.3a)

### Hotfix
- Fixed a bug where it would fail to import mods that where exported in version 1.8.2 and 1.8.3 of the tool.

## [v1.8.3](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.8.3)

### Other
- Added support for the 14 new topics [MGT2 BUILD 2021.03.18B]

### Bug fixes
- Fixed bug #23

## [v1.8.2](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.8.2)

### New feature
- New menu: Experimental Features
- Added entry to experimental features: It is now possible to exchange all publisher names and icons with the real life equivalents

### Other
- Internal coding improvements

### Bug fixes
- Fixed bug where the wrong icon was displayed when adding of gameplay/engine feature fails
- Fixed bug where the import failed message would be displayed even when the import was successful

## [v1.8.1](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.8.1)

### New feature
- Importing: When all selected folders have been scanned, it is now possible to select what items should be/should not be imported

### Other
- Removed the import sub-menus in the genre/publisher/themes/engine/gameplay feature -> Main import function is now used for all imports

## [v1.8.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.8.0)

### New feature
- It is now possible to add/remove/export/import engine features (#16 #21)
- It is now possible to add/remove/export/import gameplay features (#18 #20)
- It is now possible to export all supported mods at once
- It is now possible to export/import themes
- It is now possible to restore save game backups
- It is now possible to select a folder that is searched for compatible mods. Mods that are found can be imported at once

### Other
- Added support for japanese language
- Massive internal code changes
- The backup menu has been reorganized
- The share menu has been reorganized: Imports are now accessed fia the mod menu

### Bug fixes
- Fixed bug #11
- Fixed bug #12
- Fixed bug #14
- Fixed bug #15
- Fixed bug: It was possible to remove original themes when the safety features where enabled

## [v1.7.1](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.7.1)

### Bug fixes
- Fixed bug #9
- Fixed bug #10

## [v1.7.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.7.0)

### New feature
- It is now possible to select gameplay features that work good/bad together with the genre that is added.
  - This is a response to this update : "Gameplay features now have a suitability in connection with genres. (new game required!)"

### Other
- Some menu items are now sorted in sub-menus.
- Internal coding improvements

## [v1.6.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.6.0)

### New feature
- It is now possible to add, remove, import and export publishers/developers
- It is now possible to add new company logos

## [v1.5.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.5.0)

### New feature
- It is now possible to import and export multiple genres
- It is now possible to import multiple genre screenshots simultaneously
- It is now possible to delete all mod manager files and revert the game files to its original condition

### Other
- The ui has been changed completely. Is is now arranged in menus.
- The remove genre window has been reworked from scratch. The genres are now displayed by name and no longer by id

## [v1.4.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.4.0)

### New feature
- You can now export and import genres. This way genres can now be shared.

### Other
- Added support for italian language

## [v1.3.1](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.3.1)

### Other
- It is now supported to add arabic translations to your genres and themes.

### Bug fixes
- Fixed bug #8

## [v1.3.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.3.0)

### New feature
- You can now add new themes to the game.
- You can now remove themes from the game.

### Other
- Renamed "Other" menu to "Utilities"

## [v1.2.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.2.0)

### New feature
- You can now add translations for your genres.

### Other
- Changed the order in which the genre name and description are being printed into the Genres.txt file.

## [v1.1.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.1.0)

### New feature
- You can now add screenshots for your genre that are shown when you view your development progress.
- If you don't select a game screenshot yourself a default one will be used.

## [v1.0.1](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/1.0.1)

### Bug fixes
- Fixed bug #5
- Fixed bug #6

## [v1.0.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v1.0.0)

#### Initial release