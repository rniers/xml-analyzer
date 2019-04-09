Smart XML Analyzer

Program search for element with specified id in first html document (or with id 
`make-everything-ok-button` if not specified) and tries to find most similar element
in second file.

Currently it compare:

1. element ids
2. tag names
3. titles
4. number of equal attributes

Build
-----
To build source files into binaries:

```sbtshell
sbt assembly
```
to run binaries:

```shell
java -jar target/scala-2.12/smart-xml-analyzer-assembly-0.1.jar <pathToFile1> <pathToFile2> [idOfTheElement]
```

Run
---

Running via sbt:

```sbtshell
sbt run "<pathToFile1> <pathToFile2> [idOfTheElement]"
```