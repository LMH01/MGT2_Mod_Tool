# Current features

## Table of contents
1. [Modify the game](#modify-the-game)
   1. [Types of content](#the-following-types-of-content-is-supported)
   2. [NpcGames.txt](#edit-the-npcgamestxt-file)
2. [Mod restore points](#mod-restore-points)
3. [Share your modifications](#share-your-modifications)
4. [Backups](#create-backups)
5. [Utilities](#utilities)

## Modify the game
### The following types of content is supported:
- Anti Cheat
- Copy Protection
- Dev Legend
- Engine Feature
- Gameplay Feature
- Genre
- Hardware
- Hardware Feature
- Licence
- NpcIp
- Platform
- Publisher/Developer
- Theme

### Edit the `NpcGames.txt` file
- You can add or remove a genre id to/from this file. This has the effect that games released by NPC-Companies can have the specified genre.
- Additionally, you can change the chance with which the genre id should be added to the file. (100% = All games will have the corresponding genre as genre)
- Example: When you add the genre "Sandbox" as genre to MGT2, and you use this feature to add the corresponding genre id to the `NpcGames.txt` file, NPC-Games will have a chance to use "Sandbox" as genre.

## Mod restore points
Normally changes can be lost when the game receives an update. To counteract this you can create an ``restore point``
that can be restored later to restore the mods that are currently active.

To create a restore point navigate to: ``Backup -> Mod restore point -> Create mod restore point``

To restore a restore point navigate to ``Backup -> Mod restore point -> Restore mod restore point``

## Share your modifications
- It is supported to export all mods, to be loaded later. Here is how it works:
    - `Menu: Share -> Export -> Export all` or select the item manually that you would like to export.
    - To share the mod you simply have to click: `Menu: Share -> Open export folder`
        - This folder contains all mods that have been exported. Copy the desired folder and send it to your friend.
    - To import mods you have to click: `Menu: Mods -> Import -> Import from file system`
        - Select the folder where your mods are saved. The program then scans all folders for mods and displays a summary of what can be imported. Just click **ok**, and the program will import the files.
- You can also import mods by using a direct download url. For a list of mods you can take a look [here](https://github.com/LMH01/MGT2_Mod_Tool/discussions/34).
## Create backups
- Every time you change the game files a backup is created. This backup will be stored as latest backup until another backup is made. It is not recommended however to restore these backups, if you would like to remove mods please do so by using the respective menu. If you would like to restore the backup anyway, you have to enable the option ``Disable backup security mechanisms`` in the settings under ``Configure safety features``.
- Backups can be created manually
- When starting this program for the first time an initial backup is created that can be restored later with the click of one button.
- Backups of your saved games will be created but not automatically restored. To restore a save game backup go into the **Backup** menu, click **Restore Backup** and then **Restore Save Game Backup**.
- **Even though backups are created I will not take any responsibility if you loose or damage you saved games!**
## Utilities
- **Update Checker**
    - When started the tool will look for updates. If an update is available the user will be notified.
- **Tool tips**
    - Should you need help to figure out what an input or button means simply hover over it with your mouse.
- **Settings**
    - If you wish to not use the automatically detected MGT2 folder you can select a folder manually.
    - Safety features
        - You can disable specific security mechanisms to circumvent specific behaviours.
        - The settings are:
            - ``Inlude original contents in lists``
                - If this option is enabled, the original contents of the game files will be included in the lists. This can be used to remove or export content that belongs to the game. In addition, the original content is also exported when all content is exported.
            - ``Skip assets folder check``
                - If this option is enabled, the check for the existence of the assets' folder will be skipped when importing contents.
            - ``Disable platform picture limit``
                - Currently, only two images per platform are supported, so currently no more than two images can be added. If this option is enabled, more images can be added, although this is not supported by the game.
            - ``Unlock spinners``
                - Enable to unlock all spinners and disable the maximum values
            - ``Disable game file integrity check``
                - If this option is enabled, the game file integrity check at startup will be skipped.
            - ``Disable backup security mechanisms``
                - If this option is enabled, the initial backups will also be deleted when backups are deleted. When enabled, there will be a new menu entry with which all backups can be deleted. When enabled, it is also possible to restore the latest backup.
    - It is possible to change the **language** of the tool. The supported languages are currently german and english.
- **The Mad Games Tycoon 2 folder is set automatically**
    - When launching the application all Steam libraries will be searched if they contain Mad Games Tycoon 2. If they don't a prompt is displayed where the path can be entered.
- **Generate help sheet**
    - It is possible to generate a sheet with the perfect game sliders and genre combinations. This sheet is generated live from the game files so it is even supported to get help information for custom genres.
    - The generated file is automatically opened in the default `.md` app and the files content is copied to the clipboard.
    - Ways to open the markdown file 
        - The **recommended way** is to visit [markdownlivepreview.com](https://markdownlivepreview.com/) and paste the files content. (Automatically copied to clipboard by generating the help sheet)
        - Typing something like this into the browsers url bar: `file:///PATH/TO/FILE.md` (Markdown view plugin is required)
        - Using `VS Code` or another text editor that supports markdown
    - The file is located in `%APPDATA%/LMH01/MGT2_Mod_Manager/help_sheet.md` (Windows) or `~/.local/share/mgt2_mod_tool/help_sheet.md` (Linux)
    - [Example](media/help_sheet.md)
    - Big thanks goes out to ``Ali`` from the MGT2 discord who helped me figure out the formula on how the game calculates the design sliders between genre and sub-genre.
- **Other**
    - You can open the MGT2 folder with the click of one button