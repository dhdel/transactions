# transactions - codechalenge.backend

The microservice is built with the Spring boot framework, using Webflux and embedded reactive MongoDB.

The code can be tested with:
``mvn install``  

The scope of the task is not clear so I left out of it everything related with remote configuration, resilience, profiles, logging, healthchecks, caching, security, swagger, etc.

The format of the field `reference` is not specified so if not provided it's set to a random UUID. As the reference may not be provided it's not clear how to discard duplicated records.
