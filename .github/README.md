<div align="center">
<h1>🏔️ ImageUtils </h1>

A lightweight Java CLI tool for image manipulation, such as iterative
compression and affine transform based rotation, built with Maven

Current version : 1.0

For a brief overview on the process of creating the API, check out
`📁notes/notes.md`

</div>

---

## ImageUtils

This is a CLI tool intended to streamline the mundane tasks of performing various
image manipulations such as compression, rotation and file conversion.

As of version 1.0, compression and rotation functionalities have been implemented

## Usage

To build the project, clone the repository and run the following:

```bash
mvn clean package -DskipTests
```

then run the test suite

```bash
mvn test
```

Once the tests have all passed, you may use the jar in `./target` freely.

Run the following for help:

```bash
java -jar target/ImageUtils-1.0-SNAPSHOT.jar --help
```

---

### Example usage

(1) Compressing an image `IMG1.jpg` of format `.jpg` to `IMG2.jpg` with a
threshold size of 2MB;

```bash
java -jar target/ImageUtils-1.0-SNAPSHOT.jar \
    --source IMG1.jpg \
    --dest IMG2 \
    --ft jpg \
    --maxSizeMB 2 \
    --action compress
```

(2) Rotating an image `IMG1.png` of format `.png` to `IMG2.png` by an angle of
90 degrees.

```bash
java -jar target/ImageUtils-1.0-SNAPSHOT.jar \
    --source IMG1.jpg \
    --dest IMG2 \
    --ft jpg \
    --angle 90 \
    --action compress
```

---

### Note

(1) The compression utility also checks for `EXIF` tags to maintain orientation.
Use `--ignore true` to skip this step. Read point (2) on why you may need this.

(2) Rotation uses affine transformations, so if the image is rectangular,
possible losses of pixels may occur (refer to the core logic in `ImageRotator.java`).
This is unavoidable as of now.

(3) It is possible that files are irresponsibly renamed to something they are not.
An example is renaming and changing the extension (from `.jpg` to `.png`). While
this may not appear to cause any problems when viewing the image, it is
a critical piece of information for the ImageUtils tool. As such, the `ft` tag
should be used correctly to ensure accurate results. In the future, it is expected
that the `ft` is automatically read using the mimetype of the file.

---

Project started on: 12/10/2024

(v1.0) First functional version completed on: 13/10/2024
