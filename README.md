# Welcome to the Mad Games Tycoon 2 Mod Tool

This is a little program that can modify some files of Mad Games Tycoon 2. 

**If you encounter an error please submit an issue, so it can be fixed**

**If you have any suggestions for this project feel free to submit a feature or pull request. You can also message me on Discord. My tag is: LMH01#1614**

**Because Mad Games Tycoon 2 is in early access, I can't promise that this version will be compatible with every update to come, but when an update makes this tool incompatible I will do my best to make it compatible again.**

#### WARNING:

#### WHEN THE GAME IS UPDATED IT MIGHT BE POSSIBLE THAT YOUR MODIFICATIONS TO THE GAME FILES ARE LOST

#### TO COUNTERACT THIS YOU CAN CREATE A MOD RESTORE POINT IN THE BACKUP MENU
## Download: [v2.1.0](https://github.com/LMH01/MGT2_Mod_Tool/releases/download/v2.1.0/MGT2_Mod_Tool_2.1.0.zip)
#### Getting started:
- Install Java 8 (if not already installed)
- Download and extract the `.zip` file
- Launch the tool by double-clicking one of the `.bat` files, located in the unzipped folder:
    - Use `start.bat` to launch the tool
    - Use `start_as_admin.bat` to launch the tool with admin rights. This is required when your MGT2 folder is located in `C:\Program Files (x86)\Steam\steamapps\common`.
    - If java is correctly installed you can also use the `.jar` file to launch the tool. If the tool is launched this way, the log window is not opened.
## Current features:
### Modify the game
#### Genres
  - You can easily add a new genre to Mad Games Tycoon 2 with the help of a step by step guide
  - When all parameters have been collected a summary is being displayed. You can then decide if you do want to add that genre or make change.
  - You can remove a genre simply by pressing a button
  - It is supported to add a **custom image as genre icon**. This has to be a `.png` file.
  - It is also possible to add custom genre screenshots. These are displayed during the development progress of your game.
   
  **Note: You should be able to load an existing save game with a new genre, but once you have loaded this save, the genre is bound to that file. That means that it can't be removed later from that file.**
#### Platforms
  - You can create custom platforms for which you can create games
  - You can currently add two pictures that change when a specific year has passed.
#### Themes
  - You can add your own themes
  - It is possible to select how strong a topic should influence the age rating
#### Publishers and developers
  - You can add custom publishers, this is useful when you add your own genre because otherwise there would be no publisher for your genre.
  - You can select if the publisher should just be a publisher or if it should be a developer and vice versa.
  - In the **Utilities** menu under **Experimental Features** you can switch all preexisting publisher and developer names to the real life equivalents.
#### Engine and gameplay features
  - You can add custom engine/gameplay features, however you should be careful what values you enter because it could happen that you break the balance of the game.
#### Hardware and hardware features
  - You can add custom hardware that can be used to create your own consoles
  - Custom hardware features can also be added
#### Licences
  - You can add custom licences for books, movies and sport events
#### Npc games
  - You can add new npc games, this means that a game with this title might be created by npc game studios.
#### NPC engines
  - You can add new npc engines
  - The release year decides how high the technology level is and what engine features will be included in the engine
#### Anti cheat
  - You can create custom anti cheat software that you can implement in your game
#### Copy protection
  - You can create custom copy protection software that you can implement in your game
#### Edit the `NpcGames.txt` file
  - You can add or remove a genre id to/from this file. This has the effect that games released by NPC-Companies can have the specified genre.
  - Additionally, you can change the chance with which the genre id should be added to the file. (100% = All games will have the corresponding genre as genre)
  - Example: When you add the genre "Sandbox" as genre to MGT2, and you use this feature to add the corresponding genre id to the `NpcGames.txt` file, NPC-Games will have a chance to use "Sandbox" as genre.
### Share your modifications
- It is supported to export all mods, to be loaded later. Here is how it works:
    - `Menu: Share -> Export -> Export all` or select the item manually that you would like to export. 
    - To share the mod you simply have click: `Menu: Share -> Open export folder` 
        - This folder contains all mods that have been exported. Copy the desired folder and send it to your friend. 
    - To import mods you have to click: `Menu: Mods -> Import -> Import from file system` 
        - Select then the folder where your mods are saved. The program then scans all folders for mods and displays a summary of what can be imported. Just click **ok**, and the program will import the files.
- You can also import mods by using a direct download url. For a list of mods you can take a look [here](https://github.com/LMH01/MGT2_Mod_Tool/discussions/34).
### Create backups
- Every time you change the game files a backup is created. This backup will be stored as latest backup until another backup is made. It is not recommended however to restore these backups, if you would like to remove mods please do so by using the respective menu. If you would like to restore the backup anyway, you have to disable the safety features.
- Backups can be created manually
- When starting this program for the first time an initial backup is created that can be restored later with the click of one button.
- Backups of your saved games will be created but not automatically restored. To restore a save game backup go into the **Backup** menu, click **Restore Backup** and then **Restore Save Game Backup**. 
- **Create a mod restore point**: When a restore point is set you will be able to restore all your currently installed mods later.<br>Useful when the game has been updated and the mods are lost.
- **Even though backups are created I will not take any responsibility if you loose or damage you saved games!**    
### Uitilities
- **Update Checker**
    - When started the tool will look for updates. If an update is available the user will be notified.
- **Tool tips**
    - Should you need help to figure out what an input or button means simply hover over it with your mouse.   
- **Settings**
    - If you wish to not use the automatically detected MGT2 folder you can select a folder manually.
    - Most spinners are locked and some features do only work on specific conditions to prevent problems. Disable the safety feaatures to circumvent this behaviour. **Do only do so if you know what you are doing! I WILL NOT TAKE ANY RESPONSIBILITY IF YOU BREAK SOMETHING!**
    - It is possible to change the **language** of the tool. The german translations are currently wip.
- **The Mad Games Tycoon 2 folder is set automatically**
    - When launching the application all Steam libraries will be searched if they contain Mad Games Tycoon 2. If they don't a prompt is displayed where the path can be entered.
- **Other**
    - You can open the MGT2 folder with the click of one button
## Adding a genre
#### Adding a new genre is pretty straight forward you just have to follow the guide provided by this tool. Here is an explanation for each page:
- **Page [1]**
    - **Genre name**: Enter your genre name here. This name will be displayed in each translation.
    - **Genre description**: The description that should be displayed when hovering with your mouse over the genre.
    - **TRANSL**: With these buttons you can add translations to your genre. The text that stands in the "main" field will be used as the english name/translation.
    - **Clear Translations**: With this button you can clear all translations that you have entered.
- **Page [2] Unlock date**
    - **Unlock month**: The month when your genre will be unlocked.
    - **Unlock year**: The year when your genre will be unlocked.
    
    **Note: If your genre is not displayed in game check if the unlock date has passed!**
- **Page [3] Research and Price**
    - **Research points**: This is the required amount of research points that has to be generated to unlock your genre.
    - **Development cost**: This is the cost that will be added to the total development cost when you choose to use this genre for your game.
    - **Price**: This is the price you have to pay when you decide to research this genre.
- **Page [4] Target group**
    - Select the target audience for your genre by clicking the desired entry.
- **Page [5] Genre combination**
    - Select here what genres work good together with your genre. This is used when you create a game with two genres. The game will check if the chosen genres work good together. This will influence the ratings of your game.
- **Page [6] Topic combination**
    - Select here what topics work good with your genre. If you create a game with a topic, that you did select in this list, your game will get a better rating.
- **Page [7] Gameplay features**
    - Select here what gameplay features work good and bad together with your genre. If you create a game with a gameplay feature that you have added as good feature you game will get a better raiting. If you create a game with a gameplay feature that you have added as bad your game will get a worse raiting.
    
    **Note: Use STRG and click with your mouse to select multiple entries from the lists**
- **Page [8] Design priority**
    - The values that are set in this window are the optimal **design** priorities for your genre. If you select the values that you have set with this tool in game, you will have the correct genre **design** priorities, and you will not get a penalty for a **design** priority miss match.  
    - **The values that are entered in this step are compared against the values that are entered here:**
    ![img](https://i.imgur.com/qzDhXsu.png "Desing priority ingame")
    - If they match your game will get a better rating
- **Page [9] Work priority**
    - The values that are set in this window are the optimal **work** priorities for your genre. If you select the values that you have set with this tool in game, you will have the correct genre **work** priorities, and you will not get a penalty for a **work** priority miss match.  
    - **The values that are entered in this step are compared against the values that are entered here:**
    ![img](https://i.imgur.com/DplSwSS.png "Desing priority ingame")
    - If they match your game will get a better rating
- **Page [10] Genre screenshots**
    - Here you can add pictures that should be used as game screenshots. These images are displayed when your development progress is shown.
    - If you don't add pictures manually default pictures will be used.
    - The pictures will be added in the order you add them with the tool. So it is recommended to use the least good-looking picture first.
    
    **Note: The selected files has to be `.png` format. It is highly suggested that you only add pictures with a 4:3 aspect ratio**
- **Page [11] Genre icon**
    - In this step you can enter a custom genre icon. This icon will be copied into the game directory to be displayed as the genre image in game.
    
    **Note: The selected image file has to be a `.png` file**
## Pictures
####This is how the summary looks like, when adding a genre
![img](https://i.imgur.com/ahPNay5.png "Add genre summary")

####This is what the added genre looks like in the research menu
![img](https://i.imgur.com/eghBt4S.png "Add genre summary")

####This is what the main window looks like
![img](https://i.imgur.com/ZnQQ8G1.png "The main window")

####This is what the import window could look like
![img](https://i.imgur.com/RvDoupc.png "The import window")

####This is what the mods menu looks like
![img](https://i.imgur.com/pdIwEHw.png "The mods window")