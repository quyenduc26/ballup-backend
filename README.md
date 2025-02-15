# Spring Boot Project Setup

## System Requirements

- **JDK 17** (Must be installed and JAVA_HOME configured)
- **Maven** (Optional, if not using an IDE to manage dependencies)
- **IDE**: VS Code or IntelliJ IDEA

## Environment Setup

### 1. Check JDK Installation

Ensure JDK 17 is installed by running the following command:
```sh
java -version
```
Expected output:
```sh
java version "17.x.x" 202x-xx-xx
```
If not installed, download and install JDK 17 from [Oracle](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or [OpenJDK](https://openjdk.org/).

### 2. Install Dependencies

Before running the project, install dependencies by running:
```sh
./mvnw clean install
```
This will download all required dependencies and set up the project.

### 3. Open the Project with VS Code

1. Open VS Code.
2. Install the **Spring Boot Extension Pack**:
   - Go to **Extensions** (`Ctrl + Shift + X`).
   - Search for `Spring Boot Extension Pack`.
   - Click **Install**.
3. Open the project folder (`File -> Open Folder`).
4. Open the terminal (`Ctrl + ~`) and run:
   ```sh
   ./mvnw spring-boot:run
   ```

### 4. Open the Project with IntelliJ IDEA

1. Open IntelliJ IDEA.
2. Click **Open** and select the project folder.
3. If IntelliJ does not recognize the Maven project:
   - Go to **File** > **Project Structure** > **Modules**.
   - Select `pom.xml` and click **OK**.
4. Ensure JDK 17 is set up in **File > Project Structure > SDKs**.
5. Install dependencies by opening the terminal in IntelliJ and running:
   ```sh
   ./mvnw clean install
   ```
6. Open **Run/Debug Configurations** (`Shift + Alt + F10`), create a new configuration:
   - Select `Application`
   - Main class: `com.example.MainApplication`
   - Apply & Run.
7. Alternatively, run the command in the terminal:
   ```sh
   ./mvnw spring-boot:run
   ```

## Running the Project

Once set up, you can run the project with:
```sh
./mvnw spring-boot:run
```
The application will be available at `http://localhost:8080/`.

## Building the Project

To build the project into a JAR file:
```sh
./mvnw clean package
```
The JAR file will be located in the `target/` directory.

Run the application from the JAR file:
```sh
java -jar target/*.jar
```

## Conclusion
You have successfully set up the Spring Boot project and are ready to start developing! 🚀

