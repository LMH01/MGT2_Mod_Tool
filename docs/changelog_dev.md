# Changelog

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
