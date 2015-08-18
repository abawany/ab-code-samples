Simple Rule Runner
==================
This project comprises of a single jar file that contains a utility 
to run some pre-built rules against a list of integers passed as 
command-line arguments.

Pre-requisites
--------------
### To build: 
* gradle
* JDK 1.5+

NOTE: an untested ant build.xml script is also included. 

### To run:
* Built jar file, e.g. ProgEx-BasicRulesEngine-1.0.jar
* java -jar <jar file above> <list of integers>

Recipe For Adding New Rules
---------------------------
* Define a new class and ensure that it extends class com.abawany.RulesCommon
* Implement the <code>eval</code> function as defined in the IRules interface
* Edit <code>src/main/resources/META-INF/services/com.abawany.IRules</code> to add the 
  fully-qualified path to this new class
* The framework will take care of the rest, including class discovery, running 
  the rule in parallel with the other rules in the project

TODO
----
* Enable thread pooling
* Use annotations to automatically discover Rule classes
* (done)Run multi-threaded
* (done)Allow for asynchronous completion check (e.g. java.util.concurrent.CyclicBarrier)

