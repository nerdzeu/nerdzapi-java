nerdzapi-java
=============

A Nerdz interface system for Nerdz, plus an implementation.

Contribute
==========

Everyone can fork and help.

But remember:

1) This is GPL'd code, so you MUST publish any change if you wish to redistribute this project, and any project in which you use this MUST be released under GPLv3 or any newer version.

2) You must respect some style directives:
  a. Opening braces MUST be on the same line as the opening of a block, i.e. "int method() {" or "public class ClassName {".
  b. Closing braces should be on on the same vertical line of the beginning statement of the block (look at the code and see).      
  c. Every internal field in a method MUST BE PREFIXED with this, super or NameOfTheClass.this when accessed, for sake of readability.
  d. Every public or protected method, if not inherited, must be properly commented for JavaDoc.
  
  
Dependencies
============

You need at compile and run time:

-Apache Commons Lang 3.x
-Apache Commons Logging 1.x
-Apache HttpCore 4.x
-Apache HttpClient 4.x
-org.json JSON Library

For building, you will also need Apache Ant.

Ant expects these libraries (.jar)  to reside in a directory called "libs", located in the same directory of this README.

Building
========

After having placed all the JARs there, while in the repository root dir run:

```shell
ant jars
```
to build the NerdzApi.jar or just

```shell
ant
```
to build the contents of /tests too.

Documentation
=============

If you wish to build the package documentation, run

```shell
ant docs
```

You will find docs in /docs.
