Metrics provider
==========

Main objective of this service is to serve specific data about cloud foundry instance. We may divide these information into two categories:
* Informations related to particular organization (for example: number of applications running)
* Metrics related to whole CF instance, generally.

Mentioned data are gathered by **console** project and displayed in main organization dashboard.

*NOTE: Some values are still fake, just to show possibilities in UI*

Required libraries
------------------

* **cf-client** - library containing methods to call Cloud Foundry platform.

Required services
-----------------

Metrics provider binds to two services when deployed on cloud foundry platform:
* **SSO** - a collection of URLs for services like UAA, Cloud Controller, Login server, etc ...
* **datacatalog** - holds URL of data-catalog microservice

Running locally, replacements from application.yml file are used. For example:
```
url: ${vcap.services.datacatalog.credentials.host:http://data-catalog.example.com}
``

How to build
------------
It's a Spring Boot application build by maven. All that's needed is a single command to compile, run tests and build a jar:

```
$ mvn verify
```

How to run locally
------------------
There are meaningful configuration values provided that allow for local testing. The server can be run by maven spring boot plugin:

```
$ mvn spring-boot:run
```

Or by an ordinary java command:

```
$ java -jar target/user-management-0.4.1-SNAPSHOT.jar
```

Though the second way requires building the jar first (mvn package).

After server has been started an ordinary curl command can be used to test the functionality, i.e.:

```
$ curl http://localhost:9999/rest/orgs/69e8563a-f182-4c1a-9b9d-9a475297cb41/users -v -H "Authorization: `cf oauth-token|grep bearer`"
```
