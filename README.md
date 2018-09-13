# Vertx-with-redis-example

This is sample project to show communication between HttpServer and Micro service using Redis PUB/SUB.

SimpleAPIGateway is HttpServer which gets request, try to publish it so that micro service will get nootified and it set the request body on Redis server for microservice to consume it.

Once microservice get acknowledgement for request.It get request body from Redis server. Do business logic. Frame response as Json and set it on Redis server to consume it. and publish it so that httpserver will get notified.

Once Httpserver gets acknowledge. It gets respone data from Redis Server and finally frame response for Http Request and send it to client.

SimpleMicroService is sample Micro service here.
