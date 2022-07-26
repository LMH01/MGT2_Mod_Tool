# Mod Creator Documentation

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

When the user accepts the replacement ``Minisoft`` will be replaced by ``Williams``.
### Limitations
- Only content of the same type can be replaced
- The original content can only be restored by restoring the initial backup