Simple RESTful Service
======================
This is a basic RESTful Java service to better understand Gradle and as a platform for API experiments.

Dependencies
------------
* Uses https://github.com/jasonray/jersey-starterkit/wiki
* Uses Jersey and Gradle

Steps
-----
* Type `gradle eclipse` to generate eclipse project files
...
* Type `gradle clean war jettyRunWar` to run the project. This will launch Jetty at port 8080. You should be able to navigate with your browser to `http://localhost:8080/ProgEx-JavaRestService/hello` to see the result.
