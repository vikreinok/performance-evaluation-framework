# TALLINN UNIVERSITY OF TEHNOLOGY #
### Faculty of Information technology ###
### Department of Computer Science ###

--------------------

# A framework for empirical evaluation of Java application performance #
### Master’s thesis ###

######**Author** Viktor Reinok######
######**Supervisor** Juhan Peep Ernits######
--------------------

 
## What is this repository for? ##

* FFEOJAP is a tool integrable to CI for analyzing performance issues and empiric approach
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
git clone https://bitbucket.org/viktor_reinok/petclinic.git
mvn clean tomcat7:run 
```

### Launch the load generator ###
```
#!command line (windows)
cd..
mkdir thesis
cd thesis
git clone https://bitbucket.org/viktor_reinok_thesis_team/thesis.git

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
stop existing deployed instance of Petclinic
cd../..
cd petclininc
git checkout abc4b24337c8fce97aa557620b8ad8d7e047a49a -f
mvn clean tomcat7:run
```

### Launch the load generator again  ###
```
#!command line (windows)
cd..
cd thesis
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