# SensorDB Java API

This is Java API to the sensordb sensor stream server.

It is intended to act as a jar that can be imported by other projects.

## Installation

The project uses maven. To install it in your maven repository, use

    mvn install

It can then be accessed by other projects.

## Use

### Access

To include the API in your maven project, use

    <dependency>
      <groupId>au.csiro.cmar</groupId>
      <artifactId>sensordb-api</artifactId>
      <version>0.1.1-SNAPSHOT</version>
    </dependency>

### API

The main accessor is SensorDB, which can be created via a factory instance

    SensorDB sdb = SensorDBFactory.instance().create("http://sensordb.host.com:9903", "username", "password");

When constructed, the SensorDB object will login to the server and download the user's
experiments, user definitions and measurement definitions. These can
be accessed by name, via

    SensorDB.getExperiment
        SDBExperiment.getNode
          SDBNode.getStream
            SDBStream
    
