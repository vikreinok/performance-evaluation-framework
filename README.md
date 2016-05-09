# Java application performance evaluation framework for empirical approach #


This README would normally document whatever steps are necessary to get your application up and running.

## What is this repository for? ##

* JAPEFFEA is a tool integatable to CI for analyzing performance issues
* Version 0.0.1


## How do I get set up? ##

Following dependencies are required:

* JDK 7
* Maven 3
* Elasticsearch (Running locally. Accessible on port 9200) [ES 2.3 download](https://www.elastic.co/downloads/past-releases/elasticsearch-2-3-0)

--------------------

### Run an example application [Perclinic ](https://bitbucket.org/viktor_reinok/petclinic) ###
```
#!command line (windows)

mkdir petclinic
cd petclinic
git clone https://viktor_reinok@bitbucket.org/viktor_reinok/petclinic.git
mvn clean tomcat7:run 
```

### Run the load generator ###
```
#!command line (windows)
cd..
mkdir thesis
cd thesis
git clone https://viktor_reinok@bitbucket.org/viktor_reinok_thesis_team/thesis.git

mvn -pl load-generator -am package assembly:single -DskipTests
cd load-generator/target
java -jar load-generator-jar-with-dependencies.jar

```

### Run the data analyzer ###
```
#!command line (windows)
cd..
cd..
mvn -pl analyzer -am package assembly:single -DskipTests
cd analyzer/target
java -jar analyzer-jar-with-dependencies.jar 0000
 
```

### Emulate the source code change and rerun Petclinic ###
```
#!command line (windows)
stop existing Petclinic
cd..
cd..
cd petclininc
git checkout abc4b24337c8fce97aa557620b8ad8d7e047a49a -f
mvn clean tomcat7:run
```

### Run again the load generator ###
```
#!command line (windows)
cd..
cd thesis
mvn -pl load-generator -am package assembly:single -DskipTests
cd load-generator/target
java -jar load-generator-jar-with-dependencies.jar

```

### Run again the data analyzer ###
```
#!command line (windows)
cd..
cd..
mvn -pl analyzer -am package assembly:single -DskipTests
cd analyzer/target
java -jar analyzer-jar-with-dependencies.jar
```





### TODO ###

* Summary of set up
* Configuration
* Dependencies
* Database configuration
* How to run tests
* Deployment instructions

### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact