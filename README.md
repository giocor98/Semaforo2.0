# Semaphore for mini 4wd races.
# Semaforo2.0

Program to run a Semaphore to manage mini 4wd races.

| Functionality          | State                                |
|:-----------------------|:----------------------------------------------------:|
| Initialisation         | [![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#)|
| - configuration files  | [![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#)|
| - parameter reading    | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| - select port          | [![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#)|
| -- select port GUI     | [![RED](https://placehold.it/15/f03c15/f03c15)](#)   |
| -- select port CLI     | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| - language selection   | [![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#)|
| -- bundle out resources| [![RED](https://placehold.it/15/f03c15/f03c15)](#)   |
| -- language find & use | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| - logging              | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Program Flow           | [![RED](https://placehold.it/15/f03c15/f03c15)](#)   |
| Waiting                | [![RED](https://placehold.it/15/f03c15/f03c15)](#)   |
| Home                   | [![RED](https://placehold.it/15/f03c15/f03c15)](#)   |
| Race                   | [![RED](https://placehold.it/15/f03c15/f03c15)](#)   | 
| Endurance              | [![RED](https://placehold.it/15/f03c15/f03c15)](#)   |
| Settings               | [![RED](https://placehold.it/15/f03c15/f03c15)](#)   |

<!--
[![RED](https://placehold.it/15/f03c15/f03c15)](#)
[![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#)
[![GREEN](https://placehold.it/15/44bb44/44bb44)](#)
-->

## Project lifecycle
Program developed with maven, so to run its lifecycle just execute:

- Clean
```
mvn clean
```
- Test
```
mvn test
```
- Package
```
mvn package
```
- clean, test and create JAR
```
mvn clean package
```

## accepted parameters:

To launch the program and manipulate its functionalities it's useful to launch the program with some parameters, so that its behaviour can match the wanted one.

`--cli` this parameter makes the program use the CLI viewer; <br/>
`--gui` this parameter makes the program use the GUI viewer; <br/>
`--debug` this parameter makes the program use a low log level; <br/>

`--view:` followed by `cli` or `gui` sets the viewer; <br/>
`--log:` followed by a log level, will set the log level to that value; <br/>
`--port:` followed by a port name tries to open it as defaultPort; <br/>
`--lang:` followed by a language identifier sets the language for this execution;


