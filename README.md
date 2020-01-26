# TALLINN UNIVERSITY OF TEHNOLOGY #
### Faculty of Information technology ###
### Department of Computer Science ###

--------------------

# A framework for empirical evaluation of Java application performance #
### Master’s thesis ###

**Author** Viktor Reinok
**Supervisor** Juhan Peep Ernits
--------------------

 
## What is this repository for? ##

FFEOJAP is a tool integrable into CI loop to detect and track performance issues. The framework will provide solution for following questions:

  1. How do I find performance issues?
  2. How do I narrow down the root cause of the performance issue?
  3. How do I validate the effectiveness of the fix
  4. How do I make sure the fix does not affect other parts of the system in negative way. 
  
Version 0.0.1


## How do I get set up? ##

Following dependencies are required:

* JDK 7
* Maven 3
* Elasticsearch (Running locally. Accessible on port 9200) [ES 2.3 download](https://www.elastic.co/downloads/past-releases/elasticsearch-2-3-0)

--------------------

### Run an example application [Petclinic ](https://bitbucket.org/viktor_reinok/petclinic) ###
```
#!command line (windows)
git clone https://bitbucket.org/viktor_reinok/petclinic.git
cd petclinic
mvn clean tomcat7:run 
```

### Launch the load generator ###
```
#!command line (windows)
# new shell session
git clone https://bitbucket.org/viktor_reinok_thesis_team/thesis.git
cd thesis

mvn -pl load-generator -am package assembly:single -DskipTests -P load-generator-build-profile
cd load-generator/target
java -jar load-generator-jar-with-dependencies.jar 0000

```

### Launch the data analyzer ###
```
#!command line (windows)
cd../..
mvn -pl analyzer -am package assembly:single -DskipTests -P data-analyzer-build-profile
cd analyzer/target
java -jar analyzer-jar-with-dependencies.jar
 
```

### Emulate the source code change and re launch the Petclinic applicatoin ###
```
#!command line (windows)
# stop existing deployed instance of Petclinic
git checkout abc4b24337c8fce97aa557620b8ad8d7e047a49a -f
mvn clean tomcat7:run
```

### Launch the load generator again  ###
```
#!command line (windows)
# back in thesis shell
cd../..
mvn -pl load-generator -am package assembly:single -DskipTests -P load-generator-build-profile
cd load-generator/target
java -jar load-generator-jar-with-dependencies.jar 0001
```

### Launch the data analyzer again ###
```
#!command line (windows)
cd../..
mvn -pl analyzer -am package assembly:single -DskipTests -P data-analyzer-build-profile
cd analyzer/target
java -jar analyzer-jar-with-dependencies.jar
```


[Application performance monitoring tool used - Stagemonitor](https://github.com/stagemonitor/stagemonitor)
