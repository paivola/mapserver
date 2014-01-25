MapServer BADLY INCOMPLETE
==========================

    # Initialization
    Server reads settings and starts to listen for WebSocket connections.
    Client connects to server and requests for a new simulation instance to be created.
    Server creates a new simulation instance.
    Client asks for the possible models and settings.
    Server sends those.
    
    # Manual labour
    while the Client wants to add models:
        Client tells that there is a new model at x.y with the settings z
        Server tells the Client if it has violated the law!

    while the Client wants to tweak settings:
        Client sends updated values regarding model x
        Server updates them

    # Simulation time
    Client tells the Server to start doing something with it's threads
    Server steps trough the whole simulation, sending the simulation results to the client

Usage
-----

* Install [Maven](https://maven.apache.org/)
* Run `mvn javadoc:javadoc`
* Read the documentation in `target/site/apidocs/index.html`
* Run `mvn compile` to compile the program
* Run `mvn exec:java` to run the program
* Run `mvn exec:java -Dexec.args="src/main/resources/default_testcase.csv"` to run with a testcase CSV (change the path)

OR

* Install a recent version of Netbeans
* Open it in netbeans
* Run/Compile/Generate JavaDoc whatever

Models
------

Models define a point, connection, area or extending model as well as the map 
object that will represent it.

License
-------

The MapServer is licensed under the MIT License. See LICENSE for details.
