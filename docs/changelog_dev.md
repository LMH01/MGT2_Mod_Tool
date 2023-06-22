# Changelog

## v4.10.0

### Other
- Updated tech level spinner max value to 9 (fr #137)
- Updated file reader to recognize // as commented line, this fixes the issue that the game files could no longer be analyzed (fr #139)
- New fields (T, ST, TG, ARA, ROM) in `NpcGame.txt` are now recognized, even though they can't be set by gui yet (fr #140)

## [v4.9.0-beta7]

### Bug fixes
- Fixed #132 - Genre import: Good/bad gameplay features, and good themes where not imported correctly
- Fixed #133 - Licence import/export: Release year was not read properly
- Fixed #135 - It could happen that too many data entries where written to the german theme file when themes where imported
- Theme import: When multiple themes where imported simultaneously they where all written in the same line in the german theme file

## [v4.9.0-beta6]

### New feature
- Platform adding: The publisher that has developed a platform can now be set (#130)
- Platform adding: Backwards compatible platforms can now be selected (limited to 4)(#131)

### Other
- Small documentation improvements

### Bug fixes
- Fixed #129 - NOTFORSALE and ONLYMOBILE parameters are now read correctly when publishers are imported
- Hardware import/export: ONLY_STATIONARY and ONLY_HANDHELD parameters where not properly read
- Hardware Feature: need internet was not applied when hardware feature was added
- Hardware Feature import/export: NEEDINTERNET parameter was not properly read
- Platform import/export: STARTPLATFORM parameter was not properly read

## [v4.9.0-beta5]

### Bug fixes
- Fixed #126 - Import failed when content was modified and the import was costomized
- Fixed #128 - False image files where deleted when platforms where deleted

## [v4.9.0-beta4]

### Other
- (Import) Added missing text area message that notifies user when mod has been found that modifies or replaces content
- Target group name SENIOR can now be resolved, even though OLD would be the correct type name
- Improved error message when content that should be modified is missing from the game

### Bug fixes
- Fixed NullPointerException when content was imported that modifes content and the import was customized
- Fixed NullPointerException that caused content that can be imported to not show up

## [v4.9.0-beta3]

### Other
- Dependency replacement now contains content that is currently being imported (fr #124)
- Added info message to explain that NoSuchFileExceptions that occurred when restoring the initial backup can be ignored

## [v4.9.0-beta2]

### Other
- The dependencies section is no longer required when contents are imported, this cleans up .toml files immensely and makes it easier to modify and replace content

## [v4.9.0-beta1]

### Other
- Assets folders are now only copied once to the temp folder
- Documentation improvements
- Added new update channel: Beta

### Bug fixes
- Fixed #120 - Image files should now be found under Linux
- Npc_ips can now be modified, throws no longer NullPointerException
- Fixed problem that caused customized import to not work

## [v4.9.0-alpha1]

### New feature
- Exported content can now be marked to modify existing content. See the [documentation](documentation_for_mod_creators.md) for further details. (#86)

### Other
- `PIC` entry is no longer added to export toml file as this was redundant and could caused confusion

### Bug fixes
- Fixed NullPointException when no `requires_pictures` key was set in any mod map inside the `.toml` file when mods where imported
- Fixed #118 - The customized publisher icon was not used when publishers where imported, instead the default icon name was used
