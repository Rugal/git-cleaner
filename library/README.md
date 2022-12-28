# GIT clean library

A Java library that could check and clean large file.

## usage

### Find large file


#### Large file in flat blob
```java
import config.DaggerGitCleaner;

var cleaner = DaggerGitCleaner.create();

// to find flat file that is >= 10000 bytes
cleaner.gitService().findLargeFile(10000, false).stream()
  .forEach(System.out::println);
```

#### Large file in pack file
```java
import config.DaggerGitCleaner;

var cleaner = DaggerGitCleaner.create();

// to find compressed file in pack file that is >= 2000 bytes
cleaner.gitService().findLargeFile(2000, true).stream()
  .forEach(System.out::println);
```
