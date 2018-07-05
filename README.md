# iota-archive-longitude
This is an IOTA import project for archiving IORA transactions. It reads data from IOTA Node's ZMQ port and writes it to data repository. The data repository is backed by SQL or NewSQL database.
Three goals underpin this project: simplicity, high availability, and high throughput. The loader shall be ready to scale when the tangle reaches thousands of tps.

## Table of Contents
* [Table of Contents](#table-of-contents)
* [Usage](#usage)
* [Scope](#scope)
* [Schema](#schema)
* [Dependencies](#dependencies)
* [Notes](#notes)

## Usage
    git clone https://github.com/nelium-unlimited/iota-archive-longitude.git
    cd iota-archive-longitude
    mvn clean install
    java -jar target/iota.archive.longitude-0.0.1-SNAPSHOT.jar

This a development deployment that will launch the importer with embedded H2 database. It will also point to a random IRI node. For a more serious launch, edit src/main/resources/application.properties and supply your database and IRI details. You then start the importer with following command:

   java -jar target/iota-archive-longitude.jar --spring.config.location=file://path/to/my/file/application.properties

For connecting to an external H2 database, your application.properties could look as following:

    io.nelium.zmq.urls=tcp://192.168.1.102:5556
    io.nelium.zmq.enabled=true
    io.nelium.shard.range=9-Z

    spring.main.web-environment=false
    spring.jpa.show-sql=false
    spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
    spring.jpa.hibernate.ddl-auto=update
    spring.datasource.password=

    spring.datasource.username=sa
    spring.datasource.url=jdbc:h2:tcp://192.168.1.205:9092//var/h2/iota.db
    spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
    spring.datasource.driver-class-name=org.h2.Driver


## Scope
1. Longitude Importer connects to IRI nodes at the provided ZMQ ports. It reads transactions and trytes from these nodes in realtime as they are made available.
2. Longitude importer writes the transactions to the underlying repository which can be backed by a relational (SQL or NewSQL) database. Tests have been performed with H2, Postgres, and Cockroachdb.
3. Longitude importer is idempotent; ensuring that multiple threads can read from different nodes at the same time.
4. Cassandra is not supported; although the repository classes can be implemented to use NoSQL as backing storage.


## Schema
Data is stored in two tables:
1. Transaction table: includes all transactions data that arrive on the tx and sn topics. This includes such data as hash, bundle, timestamp, trunk,branch, milestone, and persistence state. By default, there are secondary indexes on address and bundle.
2. Signature fragment table: this includes all information that arrive on the tx_trytes topic. The key is the hash column.


## Dependencies
This is a Java application. This implies following dependencies:
1. Java 8
2. A relational database; this has been tested with H2, Postgres, and Cockroachdb

## Notes
1. For performance optimization and to avoid overloading the database, the importer cashes transactions. Separate threads read from the internal queue and write the transactions in batch to the database. Use this feature wisely as it increases the write load on your database.
2. The property io.nelium.shard.range allows sharding the import process. It takes two strings of same length as input. This input determines the range of transaction that should be accepted by a given node.
