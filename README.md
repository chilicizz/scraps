# scraps

This project is intended to contain sample code, misc scaps and experiments and practice code. 
The code here is not expected to be complete.

## Upgrade gradle wrapper 
`gradle wrapper --gradle-version 9.2.1 --distribution-type all`

## Run JMH example with the plugin

The plugin reads the benchmarks from the jmh directory. 

`./gradlew :jmh:jmh`