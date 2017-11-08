# Simple Rule Runner

This is a simple rule engine framework demo that currently performs the following tasks:

* Collects a series of integers from the command line
* Runs a set of defined/implemented rules against the input integers
* For each rule, outputs to stdout a passed or failed message
* If all rules passed, outputs to stdout a message saying all rules passed

This is intended to be a flexible implementation that allows the addition of new rules in the future with minimal code churn. At the moment, it implements the following initial rules:

  * Rule A : If any two sequential integers in the sequence add up to 1000 or greater, the rule is "passed". If not, the rule is "failed".
  * Rule B : If any integer in the sequence is at least 500 greater than the one previous to it in the sequence, the rule is "passed", else it is "failed".
  * Rule C : If the average of all the integers in the sequence is 500 or greater the rule is "passed", else it is “failed".

This project comprises of a single jar file that contains a utility to run some pre-built rules against a list of integers passed as 
command-line arguments.

## Expected Output
```
java RuleRunner 100 50 30 50 60 20 5000
Rule RuleC passed.
Rule RuleB passed.
Rule RuleA passed.
ALL rules passed.
```

## Pre-requisites

### To build: 
* gradle
  * NOTE: an untested ant build.xml script is also included. 
* JDK 1.5+


### To run:
* Built jar file, e.g. ProgEx-BasicRulesEngine-1.0.jar
* java -jar <jar file above> <list of integers>

## Recipe For Adding New Rules

* Define a new class and ensure that it extends class com.abawany.RulesCommon.
* Implement the <code>eval</code> function as defined in the IRules interface.
* Edit <code>src/main/resources/META-INF/services/com.abawany.IRules</code> to add the fully-qualified path to this new class
* The framework will take care of the rest, including class discovery, running the rule in parallel with the other rules in the project.

# TODO

* Enable thread pooling
* Use annotations to automatically discover Rule classes
* (done)Run multi-threaded
* (done)Allow for asynchronous completion check (e.g. java.util.concurrent.CyclicBarrier)

