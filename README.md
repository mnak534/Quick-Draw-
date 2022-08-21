# SE206 - 2022 - Assignment 1

### Quick, Draw! - first prototype

**Requirements**

- Java JDK 17.0.2 (download
  it [https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) )
  and JAVA_HOME env variable properly set
- Scene Builder (download it
  here [https://gluonhq.com/products/scene-builder/#download](https://gluonhq.com/products/scene-builder/#download))


**What to do first?**

Make sure that the provided tests pass.

Unix/MacOsX:  
`./mvnw clean test`

Windows:  
`.\mvnw.cmd clean test`

This will also install the GIT pre-commit hooks to activate the auto-formatting at every GIT commit.

**How to run the game?**

Unix/MacOsX:  
`./mvnw clean javafx:run`

Windows:  
`.\mvnw.cmd clean javafx:run`

**How to format the Java code?**

You can format the code at any time by running the command:

Unix/MacOsX:  
`./mvnw git-code-format:format-code `

Windows:  
`.\mvnw.cmd git-code-format:format-code `