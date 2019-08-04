# transactions - codechalenge.backend

The microservice is built with the Spring boot framework, using Webflux and embedded reactive MongoDB.

The code can be tested with:
``mvn install``  

The scope of the task is not clear so I left out of it everything related with remote configuration, resilience, profiles, logging, healthchecks, caching, security, swagger, etc.

The format of the field `reference` is not specified so if not provided it's set to a random UUID. As the reference may not be provided it's not clear how to discard duplicated records.

I didn't quite understand the relationship between transaction amount, fee and the balance of the account. `Fee that will be deducted from the amount` refers to the amount of the transaction or the balance of the account? The calculation may not be correct.