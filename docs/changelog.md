# Changelog

## [v5.1.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v5.1.0) (Latest Version)

### Feature
- Added `set as subgenre` checkbox to add genre window 5
    - When selected the new genre is automatically set as compatible subgenre for all its compatible subgenres
- Added menu `Mods -> Genres -> Edit genre/subgenre fit`
    - This menu can be used to edit what genre/subgenre combinations should be allowed.
- Added field `MUTUAL COMPATIBLE GENRES` to genre exports with the following behaviour:
    - Field is missing: When genres are imported the imported genre is now automatically set as comptible subgenre for all its compatible subgenres
    - Field is present: The genre will only be set as compatible subgenre for genres defined in here
    - For future exports this field is automatically generated from the game files 

### Game compatibility
- Added recognition for these data fields (note that it is not possible to edit these fields with the gui):
    - `Genres.txt` - `P_PC`, `P_CONSOLE`, `P_HANDHELD`, `P_PHONE`, `P_ARCADE`, `SUC YEAR`
- Added support for translations `LA`, `TH` and `UA`

### Other
- About window now includes build date/time and git rev

## [v5.0.1](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v5.0.1)

### Bug fixes
- Add genre: fixed error when no themes or compatible genres where selected
- Probably fixed rare error when adding genres (#161)

## [v5.0.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v5.0.0)

### Important
- **(Breaking) This release requires Java 21 it will not run with older java versions!**

### Other
- Fixed spelling mistakes

### Game compatibility
- The descriptions in the game files what specific settings in them do are no longer removed when the game file is modified
- Fixed compatibitily with NpcIps, the integraty of the game file is no longer shown as violated
- Fixed compatibitily with NpcGames, the integraty of the game file is no longer shown as violated
- Added recognition for these data fields (note that it is not possible to edit these fields with the gui):
    - `NpcGames.txt` - `NOSPIN`
    - `NpcIPs.txt` - `PL(ID), PLSTATIC, EX, MMO, F2P, NOSPIN` (fr #156)

### Bug fixes
- Fixed NpcGames lists function not working properly
- Fixed adding of genres failing when tool was restarted
- When genres are added to the NpcGames list, ids are only added when the line does not already contain them
- Add genre: fixed translations already added popup showing up even though translations where not added yet
- Fixed NpcGame import failing if the NpcGame did not contain the fields `THEME`, `SUB_THEME`, `TARGET_GROUP` and `SEQUEL_NUMERATION`
- Fixed potential memory leaks
- Fixed empty tooltip texts shown in UI
- Fixed update available message not showing linebreaks

## [v4.10.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v4.10.0)

### Important
- **Previously exported Dev Legends will not work together with this release!**

### Other
- Updated tech level spinner max value to 9 (fr #137)
- All dev legend types are now supported (fr #146, #136)
- Added recognition for many new data fields in the games files (note that it is not possible to edit all of those fields with the gui yet), these include:
    - `Q` in `Licences.txt` (fr #142)
    - `T, ST, TG, ARA, ROM` in `NpcGames.txt` (fr #140)
    - `ARA, ROM` in `NpcIPs.txt` (fr #143)
    - `PRE, SUC` in `Platforms.txt` (fr #144)
    - `EXCLUSIVE` in `Publishers.txt` (fr #145)

### Game compatibility
- Fixed theme handling, the place where genres that match themes are saved was changed (fr #141)
- Updated file reader to recognize // as commented line, this fixes the issue that the game files could no longer be analyzed (fr #139)

## [v4.9.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v4.9.0)

### New feature
- Exported content can now be marked to modify existing content. See the [documentation](documentation_for_mod_creators.md) for further details. (#86)
- Platform adding: The publisher that has developed a platform can now be set (#130)
- Platform adding: Backwards compatible platforms can now be selected (limited to 4)(#131)

### Other
- `PIC` entry is no longer added to export toml file as this was redundant and could caused confusion
- Added new update channel: Beta
- The dependencies section is no longer required when contents are imported, this cleans up .toml files immensely and makes it easier to modify and replace content
- Dependency replacement now contains content that is currently being imported (fr #124)
- Added info message to explain that NoSuchFileExceptions that occurred when restoring the initial backup can be ignored
- Temp folder is now also deleted before import process is started (could cause issues if previous import was canceled)
- Target group name SENIOR can now be resolved, even though OLD would be the correct type name

### Bug fixes
- Fixed NullPointException when no `requires_pictures` key was set in any mod map inside the `.toml` file when mods where imported
- Fixed #118 - The customized publisher icon was not used when publishers where imported, instead the default icon name was used
- Fixed #120 - Image files should now be found under Linux
- Fixed #128 - Sometimes the false platform images where deleted
- Fixed #129 - NOTFORSALE and ONLYMOBILE parameters are now read correctly when publishers are imported
- Fixed #132 - Genre import: Good/bad gameplay features, and good themes where not imported correctly
- Fixed #133 - Licence import/export: Release year was not read properly
- Fixed #135 - It could happen that too many data entries where written to the german theme file when themes where imported
- Hardware import/export: ONLY_STATIONARY and ONLY_HANDHELD parameters where not properly read
- Hardware Feature: need internet was not applied when hardware feature was added
- Hardware Feature import/export: NEEDINTERNET parameter was not properly read
- Platform import/export: STARTPLATFORM parameter was not properly read
- Theme import: When multiple themes where imported simultaneously they where all written in the same line in the german theme file

## [v4.8.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v4.8.0)

### Other
- Number of default game icons is now automatically set (used when publishers are replaced) (#115)
- The value for the GAMEPASS key in the publisher file can now be set, even tough I don't quite know yet what it does

### Bug fixes
- Fixed #113 - Original image files where deleted by accident when original publishers where replaced
- Fixed #114 - Dummy content was written to game files when it should not have

## [v4.7.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v4.7.0)

### Other
- Default content is no longer shipped with the tool, instead the default content files are generated from the game files.
  - This way I will no longer have to upload fixed default content files when the game is updated.

## [v4.6.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v4.6.0)

### New feature
- Gameplay features can now be marked to require internet (#110)
- Gameplay features can now require another gameplay feature (#111)

### Other
- Updated default content files

### Bug fixes
- Fixed #109 - New file format of NpcGames.txt is now read correctly

### Related game patchnotes
- MODDING: Gameplay feature can now require other gameplay features (e.g. "Online Multiplayer" requires "Multiplayer Matchmaker") "[NEED_GPF]"

## [v4.5.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v4.5.0)

### New feature
- A help sheet can now be generated that displayes the perfect game slider and genre combinations. This sheet is generated live from the game files, so it is even supported to get help information for custom genres.
  - This function can be found in the `utilities` menu.
  - To properly view the file a text editor with markdown support or a browser plugin is required.
    - It is also possible to paste the files contents to [markdownlivepreview.com](https://markdownlivepreview.com/) (Content is automatically copied to clipboard)
  - Big thanks goes out to ``Ali`` from the MGT2 discord who helped me figure out the formula on how the game calculates the design sliders between genre and sub-genre.
- Added new button to `Utilities` menu: Open feature list - This button opens the feature list located at https://github.com/LMH01/MGT2_Mod_Tool/blob/master/docs/features.md

### Other
- The component rating the hardware will receive is now displayed in the add hardware window. Thanks
to `✗ Mehmet Ali` from the MGT2 discord for figuring out the formula.
- Missing locale: Changed log message from `INFO` to `WARN`
- Updated default content files

### Bug fixes
- Fixed utility function `getCompatibleThemeIdsForGenreNew` not returning the correct theme ids

## [v4.4.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v4.4.0)

### New feature
- Exported content can now be marked to replace existing content. See the [documentation](documentation_for_mod_creators.md) for further details.

### Other
- A backup of all game images is now created
  - This means that the steam function ``verify game files`` will no longer have to be used when images get corrupted.

## [v4.3.1](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v4.3.1)

### Other
- Simple content: An empty line in the games ``.txt`` files is no longer treated as content

### Bug fixes
- Fixed #102 - NPCIP's that where added did not show up in game
- Fixed #103 - Publisher/Developer setting is now set correctly when adding new Publisher/Developer

## [v4.3.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v4.3.0)

### New feature
- Added support for dev-legends

### Other
- When an image file already exists in the game files during import the user is now asked if it should be overwritten or if the import should be canceled.
- When a new update is available and the GitHub repository is opened, the specific release page is now opened
- Optimized localization files
- Error message is now written to the console when the import fails because image files could not be copied

### Bug fixes
- Fixed error message formatting when restore point content could not be constructed
- Fixed #99 - Adding of npc ips now works again
- Fixed two misspelled translation keys in the german localization file

## [v4.2.2](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v4.2.2)

### Other
- Updated default content files

### Bug fixes
- Deleting backups from the uninstallation menu would sometimes fail
- Deleting export folder would throw exception when it did not exist

## [v4.2.1](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v4.2.1)

### Other
- Safety feature settings are now printed to log file

### Bug fixes
- #98 - The initial backup could not be created because a boolean value was not set properly

## [v4.2.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v4.2.0)

### Important
- Previously exported licences will not work together with this release

### New feature
- Licences can now be assigned good / bad fitting genres and the release year.
  - This is a response to update `2022.06.16A` (fr #69)
- When the tool is incompatible with the current game version the reason is now displayed

### Other
- Improved the message that is shown when the content integrity check fails
- When a new initial backup should be created but the uninstallation fails the user can now decide to create the new initial backup anyway
- Linux:
  - Added information message that is shown when the tool runs on linux and the integrity of a game file is violated.

## [v4.1.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v4.1.0)

### New feature
- Custom hardware icons can now be added
  - Hardware icons that have been set by editing the game files are now recognized. This means that they will be included when importing and exporting the hardware.

### Removed features
- It is no longer possible to add a description to custom hardware
  - This is because the game no longer supports descriptions for custom hardware
  - When the game supports the description again, it will be added back

### Other
- Updated some spinner values:
  - Global:
    - Research point cost:
      - Max: 10.000 -> 6.000
  - Engine feature:
    - Gameplay, Graphics, Sound, Tech
      - Max: 4.500 -> 3.000
  - Gameplay feature:
    - Gameplay, Graphics, Sound, Tech
      - Default: 100 -> 30
      - Max: 4.500 -> 400
  - Hardware:
    - Price
      - Max: 10.000.000 -> 30.000.000
      - Step size: 10.000 -> 100.000
    - Dev costs:
      - Max: 100.000.000 -> 50.000.000
  - Npc engine:
    - Profit share:
      - Default: 10 -> 5
  - Publisher:
    - Speed:
      - Default: 5 -> 3
    - Com value:
      - Min: 10.000.000 -> 2.000.000
      - Max: 100.000.000 -> 40.000.000
      - Step size: 500.000 -> 200.000

### Bug fixes
- Fixed export fail when a npc engine was exported where genre/platform ids where ``-1``
  - This would only occur when the safety features where off, and it was tried to export the ``without engine`` npc engine
- Fixed game content not showing up in export selected lists when safety feature ``Include original contents in lists`` was off
- Fixed import failing when ``DESC EN`` tag was missing even though it was not needed

## [v4.0.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v4.0.0)

### Important
- **Previously exported mods will not work together with this release**
- **Restore points that have been created prior to this release will not work together with this release**

### New feature
- Added new possibilities to customize new publishers
  - The country of publishers and developers can now be set
- Gameplay features can now be of type artificial intelligence
- The integrity of the most game files (The `.txt` files) is now checked at the start
  - These checks include:
    - Check if images are available
    - Check if data entries are existing, valid and can be parsed as numbers (where applicable)
  - This check can be disabled by disabling the safety features
- Added button ``reanalyze game files`` to the ``utilities`` menu
  - This way the game files can be manually analyzed, useful if the integrity check fails, and you want to fix it

### Removed feature
- It is no longer possible to select a custom genre id when adding a genre
- Removed experimental menu: Publishers can no longer be replaced with real life publishers

### Other
- Added an informative message that explains how the hardware component rating is determined
- Significant performance improvements when adding and removing mods
  - The `.txt` files are now only written once per cycle and not once for each content.
  - Example: Before it could take about 3 and 1/2 minutes to remove 9870 NpcEngines. Now it is almost instant.
- General improvements to the text area outputs and the progress bar
- Split ``disable safety features`` up into multiple options
  - This way it is now possible to disable specific safety features only
    - For example, it is now possible to only unlock the spinners without turning of other safety features
- All combo boxes are now sorted alphabetically
- The presence of image files is now checked before the images are copied and the content is added when importing mods
  - If images are missing the import is canceled and a message is shown that displays which image files are missing
- Reworked the backend of the tool
- Massive internal code changes
- Updated default content files
- Improved and added some missing translations
- In the ``settings.toml`` file it can now be enabled to write the text area output to the console

### Bug fixes
- Turkish translation is now displayed in the translation summary again
- The description text label should now be correct when adding a new hardware and hardware feature
- Fixed #95 - Many things would fail if more than one empty lines are between two data entries in the game files

## [v3.2.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v3.2.0)

### New feature
- Platforms can now be marked as available from the start of the game (fr #80)

### Other
- Updated some texts in the "add npcip" window

### Bug fixes
- Fixed bug #81 - Npcip requirements are now correct
- Fixed bug #82 - Npcip topics are now set correctly
- Fixed bug #83 - Startplatforms are now available again after a new platform has been added

## [v3.1.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v3.1.0)

### New feature
- NPC IPs can now be added to the game

### Other
- Updated default content files

### Bug fixes
- It now possible again to select the genre of a publisher

## [v3.0.3](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v3.0.3)
### Other
- Added button to utilities menu with which the settings.toml file can be opened
- Updated default content files
- It is now checked if the initial backup is up-to-date when the tool is started
  -  This feature can be disabled in the settings.toml file

### Bug fixes
- Button `Restore mod restore point` is no longer disabled when the safety feature are off
- Import no longer fails when gameplay features are imported that are missing bad / good genres
- Theme export no longer replaces genre names falsely
- Program will no longer fail to load when the settings.toml file does not contain valid toml

## [v3.0.2](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v3.0.2)
### Other
- Import
  - When an image file already exists it is now replaced and the import of the mod is not canceled
  - The import will no longer be canceled when a folder can not be accessed

### Bug fixes
- "Import selected" button now works properly and the action will no longer fail.
- One translation key was not found

## [v3.0.1](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v3.0.1)
### Bug fixes
- Fixed: "Export selected" button now works properly and the action will no longer fail.

## [v3.0.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v3.0.0)

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
- It is no longer possible to enable the debug logging in the settings window, instead the storage of exports can now be enabled.
  - Debug logging can still be enabled by editing the settings.toml file
- Improved performance when handling files
- Improved text area outputs when importing, exporting, adding and removing mods
- Improved the error handling massively
  - Whenever something goes wrong the stacktrace is now written to the text area and an explanation of what went wrong is displayed
- Internal code has been massively improved
- Internal handling of paths and files has been improved
  - Renamed some folders to be more consistent
- When an initial backup is created the user is now prompted to verify the game files
- It is now checked if the initial backup is up-to-date when the tool is started
  - This feature is currently wip, to enable it set "enableInitialBackupCheck" to true in the "settings.toml" file
- Improved disclaimer message text
- Added new entry to the export menu
  - Export selected -> with this function only selected mods can be exported. Either single or together in a bundle.
- Mods are now sorted by alphabet when they are displayed in panels
  - e.g. When the user can choose which mods should be imported
- Translations
  - Fixed some translations
  - Added some missing translations

### Bug fixes
- When the hardware file was modified a specific data entry was not written to the file. This caused the controllers and screens to lose the requirements needed to implement them into a console.

## [v2.2.1](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v2.2.1)

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
