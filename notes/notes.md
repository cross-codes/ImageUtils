# ImageUtils

## Initializing project

Run the following command to initialize a project using the quickstart template

```bash
mvn archetype:generate \
    -DgroupId=com.github.cross.imageUtils \
    -DartifactId=ImageUtils \
    -DarchetypeArtifactId=maven-archetype-quickstart \ 
    -DinteractiveMode=false
```

Note that this will create a folder called `ImageUtils/` within which you should
have the `src/` folder. You should not make an empty folder and then run this command
within it.

Maven can automatically create the files so the project can be indexed by JDTLS.
To do so, first update the `pom.xml` file with your dependencies:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.github.cross.imageUtil</groupId>
  <artifactId>ImageUtils</artifactId>
  <packaging>jar</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>ImageUtils</name>
  <url>http://maven.apache.org</url>
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
  </properties>
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.imgscalr</groupId>
      <artifactId>imgscalr-lib</artifactId>
      <version>4.2</version>
    </dependency>
  </dependencies>
</project>
```

Then run the following command:

```bash
mvn eclipse:eclipse
```

Whenever you need to update the contents of the file,
run `mvn eclipse:clean`, and restart.

You can have a local installation of the dependencies using `mvn dependency:sources`.
This will install the dependencies in a .m2 folder from which you should be able
to see the `jars` for any installations. JDTLS will also be able to pick up
documentation from here.

However, you can view the javadocs from [here](https://www.javadoc.io/doc/org.imgscalr/imgscalr-lib/latest/index.html),
for the ImageScalr library

## Updating during the coding stage

At this point, you can start writing code for the application. To streamline the
process, you can first ignore OOP principles and try to get a working
implementation down. Once that is fixed, the code can be refactored to be more structured
and organized.

If you need to add a new dependency, simply add the entry into `pom.xml` from
maven central, and restart JDTLS. You will be able to continue from where you
left off.

During development, in order to build the app, you would need to add the classes
from the third-party libraries into your final JAR. To do this, you need the `maven-shade`
plugin and this must be added to your build plugins:

```xml
<build>
<plugins>
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.2.0</version>
    <executions>
      <execution>
        <phase>package</phase>
        <goals>
          <goal>shade</goal>
        </goals>
      </execution>
    </executions>
  </plugin>
</plugins>
</build>
```

Then, you should be able to run `mvn package`
(`mvn clean package` for after this), and execute the app like so:

```bash
java -cp target/ImageUtils-1.0-SNAPSHOT.jar com.github.cross.ImageUtils.Orchestrator
```

## Final release

Notice that the jar cannot be run using `java -jar` because no main class is specified.
Typically you'd use a `MANIFEST.MF` file, but you can modify the shade plugin to
do this while building the uber jar.

```xml
<build>
<plugins>
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.2.0</version>
    <executions>
      <execution>
        <phase>package</phase>
        <goals>
          <goal>shade</goal>
        </goals>
        <configuration>
          <transformers>
            <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
              <mainClass>com.github.cross.Orchestrator</mainClass>
            </transformer>
          </transformers>
        </configuration>
      </execution>
    </executions>
  </plugin>
</plugins>
</build>
```

Now, a simple `java -jar` on the compiler jar will work, after
executing `mvn clean package`.

## Unit tests

The unique case of this app relying on executing the jar via a command means the
jar needs to first be available in `target`. You can then write unit tests, and
test the project using `mvn test`. Because the project is a small scale one,
only minor tests have been included.

What you should do to test this is:

```bash
mvn clean install -DskipTests
```

```bash
mvn test
```
