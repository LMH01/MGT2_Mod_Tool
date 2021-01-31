#Welcome to the Mad Games Tycoon 2 Mod Tool

This is a little program that can modify some files from Mad Games Tycoon 2. 

**If you encounter an error please submit an issue, so it can be fixed**

**Because Mad Games Tycoon 2 is in early access I can't promise that this version will be compatible with every update to come, but when an update makes this tool incompatible I will do my best to make it compatible again.**
###Download: *comming soon*
####Getting started:
- Install Java 8 (if not already installed)
- Download this tool (.jar file)
- Run the .jar file
- Choose the main folder of Mad Games Tycoon 2 (the folder that contains the .exe file)
## These are the current features:
- **Add and remove genres**
    - You can easily add a new genre to Mad Games Tycoon 2 with the help of a step by step guide.
    - When all parameters have been collected a summary is being displayed. You can then decide if you do wan't to add that genre or make changes.
    - You can remove a genre simply with a button.
    - It is supported to add a **custom image as genre icon**. This has to be a .png file.
    - **Note: When a genre has been added and a save game has been loaded it can not be removed from that save game later!**
- **Edit the NpcGames.txt file**
    - You can add or remove a genre id to/from this file. This has the effect that games released by NPC-Companies can have the specified genre.
    - Additionally, you can change the chance with which the genre id should be added to the file. (100% = All games will have the corresponding genre as genre)
    - Example: When you add the genre "Sandbox" as genre to MGT2 and you use this feature to add the corresponding genre id to the NpcGames.txt file, NPC-Games will have a chance to use "Sandbox" as genre.
- **Create backups**
    - Every time you change the game files a backup is created. This backup will be stored as latest backup until another backup is made. This latest backup can be restored with the click of one button.
    - Backups can be created manually
    - When starting this program for the first time an initial backup is created that can be restored later with the click of one button.
- **Update Checker**
    - When started the tool will look for updates. If an update is available the user will be notified.
- **Log file**
    - Every change you do to the game files will be noted here. *(eg. added genre [genre_name])*
    - When a backup is being created it will be noted here.
- **Tool tips**
    - Should you need help to figure out what an input or button means simply hover over it with your mouse.   
- **Settings**
    - Settings are saved when you exit the program so that you don't need to choose the MGT2 folder on each start.    