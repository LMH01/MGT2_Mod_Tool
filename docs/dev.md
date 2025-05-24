# Developer documentation

## Automatic detection of generated java code in vscode

Add the following code to `.vscode/settings.json`

```
    "java.project.sourcePaths": [
        "src/main/java",
        "src/integration-test/java",
        "build/generated/sources/version"
    ]
```