# Build failure due to deprecated Maven 'classifier' parameter (#1296)

[Build failure due to deprecated Maven 'classifier' parameter](https://github.com/subhra74/xdm/issues/1296#top)

[Ateebhqhq](https://github.com/Ateebhqhq)
[on Aug 16, 2025](https://github.com/subhra74/xdm/issues/1296#issue-3327087012)

[#768](https://github.com/subhra74/xdm/discussions/768)
PLEASE DO NOT JUST SAY "It does not work, or something not working etc." Provide enough relevent details so that the issue can be analyzed and reproduced easily
Describe the bug The build is failing on the recent version of Maven. The error states: "parameter 'classifier' has been removed from the plugin, please verify documentation."
To Reproduce The issue occurs during the mvn package command.
The exact line that is causing the issue in pom.xml is
Expected behavior The build should have been completed successfully, and the application should have been packaged.
Screenshots If applicable, add screenshots to help explain your problem.
please complete the following information:
- Operating System: Arch Linux
- Browser: Zen browser
- Maven Version: You can get this by running mvn --version.
- XDM Version: 7.2.11 (from the PKGBUILD).
Generated log using below method
- [https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting](https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting)
[https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting](https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting)
Additional context I had to manually remove the line to get the build to work.
An instance of the working pom.xml file:
[ateeb@archlinux app]$ cat pom.xml 4.0.0

```
<groupId>xdman</groupId> <artifactId>xdman</artifactId> <version>0.0.1-SNAPSHOT</version> <packaging>jar</packaging> <name>xdman</name> <url>http://maven.apache.org</url> <properties> <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding> <maven.compiler.source>11</maven.compiler.source> <maven.compiler.target>11</maven.compiler.target> </properties> <dependencies> <!-- https://mvnrepository.com/artifact/commons-net/commons-net --> <dependency> <groupId>commons-net</groupId> <artifactId>commons-net</artifactId> <version>3.6</version> </dependency> <!-- https://mvnrepository.com/artifact/org.tukaani/xz --> <dependency> <groupId>org.tukaani</groupId> <artifactId>xz</artifactId> <version>1.8</version> </dependency> <!-- https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple --> <dependency> <groupId>com.googlecode.json-simple</groupId> <artifactId>json-simple</artifactId> <version>1.1.1</version> </dependency> <!-- https://mvnrepository.com/artifact/net.java.dev.jna/jna --> <dependency> <groupId>net.java.dev.jna</groupId> <artifactId>jna</artifactId> <version>5.5.0</version> </dependency> <!-- https://mvnrepository.com/artifact/net.java.dev.jna/jna-platform --> <dependency> <groupId>net.java.dev.jna</groupId> <artifactId>jna-platform</artifactId> <version>5.5.0</version> </dependency> <!-- <dependency> <groupId>junit</groupId> <artifactId>junit</artifactId> <version>3.8.1</version> <scope>test</scope> </dependency> --> </dependencies> <build> <finalName>xdman</finalName> <plugins> <plugin> <artifactId>maven-assembly-plugin</artifactId> <configuration> <archive> <manifest> <mainClass>xdman.Main</mainClass> </manifest> </archive> <descriptorRefs> <descriptorRef>jar-with-dependencies</descriptorRef> </descriptorRefs> <appendAssemblyId>false</appendAssemblyId> </configuration> <executions> <execution> <id>make-assembly</id> <!-- this is used for inheritance merges --> <phase>package</phase> <!-- bind to the packaging phase --> <goals> <goal>single</goal> </goals> </execution> </executions> </plugin> <plugin> <groupId>org.apache.maven.plugins</groupId> <artifactId>maven-surefire-plugin</artifactId> <version>3.0.0-M3</version> <configuration> <skipTests>true</skipTests> </configuration> </plugin> </plugins> </build>
```

```
<groupId>xdman</groupId> <artifactId>xdman</artifactId> <version>0.0.1-SNAPSHOT</version> <packaging>jar</packaging> <name>xdman</name> <url>http://maven.apache.org</url> <properties> <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding> <maven.compiler.source>11</maven.compiler.source> <maven.compiler.target>11</maven.compiler.target> </properties> <dependencies> <!-- https://mvnrepository.com/artifact/commons-net/commons-net --> <dependency> <groupId>commons-net</groupId> <artifactId>commons-net</artifactId> <version>3.6</version> </dependency> <!-- https://mvnrepository.com/artifact/org.tukaani/xz --> <dependency> <groupId>org.tukaani</groupId> <artifactId>xz</artifactId> <version>1.8</version> </dependency> <!-- https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple --> <dependency> <groupId>com.googlecode.json-simple</groupId> <artifactId>json-simple</artifactId> <version>1.1.1</version> </dependency> <!-- https://mvnrepository.com/artifact/net.java.dev.jna/jna --> <dependency> <groupId>net.java.dev.jna</groupId> <artifactId>jna</artifactId> <version>5.5.0</version> </dependency> <!-- https://mvnrepository.com/artifact/net.java.dev.jna/jna-platform --> <dependency> <groupId>net.java.dev.jna</groupId> <artifactId>jna-platform</artifactId> <version>5.5.0</version> </dependency> <!-- <dependency> <groupId>junit</groupId> <artifactId>junit</artifactId> <version>3.8.1</version> <scope>test</scope> </dependency> --> </dependencies> <build> <finalName>xdman</finalName> <plugins> <plugin> <artifactId>maven-assembly-plugin</artifactId> <configuration> <archive> <manifest> <mainClass>xdman.Main</mainClass> </manifest> </archive> <descriptorRefs> <descriptorRef>jar-with-dependencies</descriptorRef> </descriptorRefs> <appendAssemblyId>false</appendAssemblyId> </configuration> <executions> <execution> <id>make-assembly</id> <!-- this is used for inheritance merges --> <phase>package</phase> <!-- bind to the packaging phase --> <goals> <goal>single</goal> </goals> </execution> </executions> </plugin> <plugin> <groupId>org.apache.maven.plugins</groupId> <artifactId>maven-surefire-plugin</artifactId> <version>3.0.0-M3</version> <configuration> <skipTests>true</skipTests> </configuration> </plugin> </plugins> </build>
```

- 
            