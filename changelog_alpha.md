# Changelog (Alpha versions)
## Coming in [v3.0.0-alpha-2](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v3.0.0-alpha-1)

### Other
- No longer is each mod type listed in the import summary instead only mods for which a new mod is found are displayed
- It is now checked if the initial backup is up-to-date when the tool is started. (currently vip to enable set "enableInitialBackupCheck" in the "settings.toml" file to true)
- Improved disclaimer message text
- Added new entry to the export menu
  - Export selected -> with this function only selected mods can be exported. Either single or together in a bundle.

## [v3.0.0-alpha-1](https://github.com/LMH01/MGT2_Mod_Tool/releases/tag/v3.0.0-alpha-1)

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

### Bug fixes
- When the hardware file was modified a specific data entry was not written to the file. This caused the controllers and screens to lose the requirements needed to implement them into a console.
