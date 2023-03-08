# Changelog

## [v4.9.0-beta4]

### Other
- (Import) Added missing text area message that notifies user when mod has been found that modifies or replaces content
- Target group name SENIOR can now be resolved, even though OLD would be the correct type name

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
- Fixed #118 - The customized publisher icon was not used when publishers where imported, instead the default icon name was usedgitui
