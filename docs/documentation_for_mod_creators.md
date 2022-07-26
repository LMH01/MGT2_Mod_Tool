# Documentation for Mod Creators

This documentation aims to explain some advanced stuff about the mod tool that is
especially helpful for mod creators.

## Table of contents
1. [Toml file format](#toml-file-format)
   1. [Toml file types](#toml-file-types)
   2. [Mod types](#mod-types)
   3. [Data fields](#data-fields)
   4. [Dependencies](#dependencies)
2. [Assets' folder](#assets-folder)
3. [Exporting mods](#exporting-mods)
4. [Importing mods](#importing-mods)
   1. [Customize import](#customize-import)
   2. [How it works](#how-it-works)
      1. [Recognition requirements](#recognition-requirements)
5. [Mark a mod as replacement](#mark-a-mod-as-replacement)
   1. [Example](#example)
   2. [Limitations](#limitations)

## Toml file format
This tool uses the ``.toml`` file format to store exported mods.
This makes it possible to create a mod by simply writing a custom-made ``.toml`` file.

An example ``.toml`` file could look like [this](media/example_export/export.toml).

Each ``.toml`` file always contains these fields:
```toml
mod_tool_version = "4.3.1" # Indicates the version in which this mod list was exported
                           # This is used to determine if the mods in this list are still compatible with the mod tool
type = "export_bundled" # the type of which this toml file is

# Here comes the mod data
# A mod entry is formatted this way:

# [mods.MOD_TYPE.MOD_NAME]
# SPECIFIC DATA FIELDS (Content specific)
#
# [mods.MOD_TYPE.MOD_NAME.dependencies]
# DEPENDENCY_MOD_TYPE = ["DEPENDENCY_NAME"]
```
### Toml file types
- ``export_bundled`` 
  - Indicates that this ``.toml`` file contains a bundled mod list
  - This should be the default
- ``single_export`` - Indicates that this ``.toml`` file contains only a single mod
- ``restore_point`` - Indicates that this ``.toml`` file is a restore point

### Mod types
- anti_cheat
- copy_protect
- dev_legend
- engine_feature
- gameplay_feature
- genre
- hardware
- hardware_feature
- licence
- npc_engine
- npc_game
- npc_ip
- platform
- publisher
- theme

### Data fields
The data fields are specific for each content. To know what data fields each mod requires take a look [here](media/example_export/export.toml).

In addition to these fields most content (all content that can be translated) supports the following way of adding translations:
```toml
"NAME LANGUAGE_KEY" = "TRANSLATION"
"DESC LANGUAGE_KEY" = "DESCRIPTION TRANSLATION"
```
The language keys are the same the game uses for the localisation directories.

### Dependencies
This section contains all the content that this mod depends on. This can be content that is already added to the game
or content that will be added to the game when this content is added (e.g. a mod that is standing in the same file).

Even if this content is missing the mod import will not fail because the tool checks if dependencies are missing.
If they are missing the user is notified and can replace these missing dependencies with a content that is available.

The ``DEPENDENCY_MOD_TYPE`` is the type the dependency is of, one of [these](#mod-types).

The ``DEPENDENCY_NAME`` is the name of the dependency, multiple dependencies of the same type can be chained like this:
```toml
[mods.MOD_TYPE.MOD_NAME.dependencies]
DEPENDENCY_MOD_TYPE = ["DEPENDENCY_NAME_1", "DEPENDENCY_NAME_2", "DEPENDENCY_NAME_3", "...", "DEPENDENCY_NAME_N"]
```
If the mod depends on two or more different types of dependencies they can be set in the following way:
```toml
DEPENDENCY_MOD_TYPE_1 = ["DEPENDENCY_NAME_1", "DEPENDENCY_NAME_2", "...", "DEPENDENCY_NAME_N"]
DEPENDENCY_MOD_TYPE_2 = ["DEPENDENCY_NAME_1", "...", "DEPENDENCY_NAME_N"]
DEPENDENCY_MOD_TYPE_3 = ["DEPENDENCY_NAME_1", "...", "DEPENDENCY_NAME_N"]
```
## Assets' folder
When a mod is exported that requires pictures, an assets' folder is created in the directory where the ``export.toml``
file is located. This folder contains all images that are required by the mods in the ``.toml`` file. Import will not
work if the images are missing.

## Exporting mods

Mods can be exported by navigating to ``Share -> Export``.

There are two different ways of exporting mods:
1. Single mods
    - Using this option will create a separate ``.toml`` file for each mod.
2. Bundled mods
    - Using this option will create a ``.toml`` file that contains each mod that has been selected.
    All images are put into the same assets' folder. Using this makes sure that all mods that require other mods are
    available when importing this bundle.

## Importing mods

As mods are stored in ``.toml`` files an unlimited amount of mods can be imported simultaneously.

The only prerequisite is that the ``.toml`` file is formatted as described in [Toml file format](#toml-file-format) and
contains all the required mod data.

Mods can be imported by navigating to ``Mods -> Import -> Import from URL`` or `` -> Import from file system``.

### Customize import
The user can customize the import to select a range of found mods that should be added to the game.

Note: When the import is customized it can happen that some mods complain that dependencies are missing.

### How it works
This section describes what the tool does when mods are being imported.
 
The first step is for the user to select files and folders that should be searched for mods.
This can be done by navigating to ``Mods -> Import -> Import from URL`` or `` -> Import from file system``.
No matter what is chosen the next steps are similar.

The (downloaded) files and folders are first searched if they are ``.toml files``.

Once all ``.toml`` files are found the file content is analyzed to determine if they contain mods.

#### Recognition requirements
In order for the mod tool to recognize a file as a file that contains mods the following requirements must be met:
- File extension is ``.toml``
- The file is valid ``toml``
- The following two tags must be included in the file:
    - ``mod_tool_version``
    - ``type``

All files that where identified to contain mods are then turned into maps, where the ``key`` and ``value`` are the
corresponding data entries that are required to add the mod.

Next all mods are checked for the following:
- Already added? (Is a mod with this name already active)
- Duplicated? (Is a mod with this name duplicated in the ``.toml`` files)
- Compatible with the current mod tool version?

If the check passes (= not already added, not duplicated and compatible with the current mod tool version)
the mod is added to a list of mods that can be added to the game. A summary is then displayed to the user where they
can select if all mods should be added or if only selected mods should be added to the game.

Once the user made a selection it is checked if all assets folders are available and if all **dependencies** are satisfied.

If every check is passed the **mods are constructed** to be added to the game. This step can **fail** when
specific data entries are missing from the mod file. Failing means that the whole import process is canceled.

If everything went properly the mods have been added to the game.

## Mark a mod as replacement
Mods can be marked as replacement by adding the following line to the toml-section of the content:
```toml
replaces = "CONTENT_TO_REPLACE"
```
This allows a mod to replace original game content for example when publishers/developers should be replaced by their
real life counterpart.
### Example
This is the data entry in a toml file for the mod ``Williams`` of type publisher before adding the ``replaces`` tag:

```toml
[mods.publisher.williams]
PUBLISHER = "true"
DEVELOPER = "true"
MARKET = "37"
GENRE = "Simulation"
PIC = "190"
"NAME FR" = "Williams"
SHARE = "7"
SPEED = "4"
DATE = "JAN 1982"
COMVAL = "15400000"
"NAME EN" = "Williams"
publisher_icon = "publisher_williams_publisher_icon.png"
COUNTRY = "0"
"NAME TU" = "Williams"
requires_pictures = true
"NAME GE" = "Williams"

[mods.publisher.williams.dependencies]
genre = ["Simulation"]
```
After adding the ``replaces`` tag the entry looks like this:
```toml
[mods.publisher.williams]
PUBLISHER = "true"
DEVELOPER = "true"
MARKET = "37"
GENRE = "Simulation"
PIC = "190"
"NAME FR" = "Williams"
SHARE = "7"
SPEED = "4"
DATE = "JAN 1982"
COMVAL = "15400000"
"NAME EN" = "Williams"
publisher_icon = "publisher_williams_publisher_icon.png"
COUNTRY = "0"
"NAME TU" = "Williams"
requires_pictures = true
"NAME GE" = "Williams"
replaces = "Minisoft"

[mods.publisher.williams.dependencies]
genre = ["Simulation"]
```
When the mod is imported now the mod tool notices the ``replaces`` tag and notifies the user that content will be replaced.

When the user accepts the publisher ``Minisoft`` will be replaced by ``Williams``.
### Limitations
- Only content of the same type can be replaced
- The original content can only be restored by restoring the initial backup