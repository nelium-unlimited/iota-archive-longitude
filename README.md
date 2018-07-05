# iota-archive-longitude
This is an IOTA loader project. It reads data from IOTA Node's ZMQ port and writes it to data repository. The data repository is backed by SQL or NewSQL database.
Three goals underpin this project: simplicity, high availability, and high throughput. The loader shall be ready to scale when the tangle reaches thousands of tps.

===
## Table of Contents
* [Table of Contents](#table-of-contents)
* [Usage](#usage)
* [Scope](#scope)
* [Input & Output](#input--output)
* [Environment & Dependencies](#environment--dependencies)
* [Technical & Design Decisions](#technical--design-Decisions)
* [References](#references)

## Usage
    git clone https://github.com/nelium-unlimited/iota-archive-longitude.git
    cd iota-archive-longitude
    mvn clean install
    java -jar target/iota-archive-longitude.jar

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


