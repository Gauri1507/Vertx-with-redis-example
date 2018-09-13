package com.api.redis.gateway.verticle;

import java.util.UUID;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class SimpleRestChild extends SimpleRestServer{
	
	public void handleSubscription(RoutingContext routingContext) {
		JsonObject requestAsJson = routingContext.getBodyAsJson();
		
		requestAsJson.put("uuid", getUUID());
		
		// this client object is null.
		// How to get client object ?
		// client should be intialize only once. And Server should subscribed to the channel only once
		// so kept that part in start method of parent verticle
		client.set("request", requestAsJson.toString(), handler ->{
			System.out.println("Simple server is setting value to redis client");
			if(handler.succeeded()) {
				System.out.println("Key and value is stored in Redis Server");
			}else if(handler.failed()) {
				System.out.println("Key and value is failed to be stored on Redis Server with cause : "+ handler.cause().getMessage());
			}
		});
		
		
		client.publish("channelForServerToPublish", "ServiceOne", handler -> {
			if(handler.succeeded()) {
				System.out.println("Simple Server published message successfully");
			}else if(handler.failed()) {
				System.out.println("Simple Server failed to published message");
			}
		});
		
		routingContext.vertx().eventBus().consumer("io.vertx.redis.channelForServiceToPublish", handler -> {
			client.get("response", res ->{
				if(res.succeeded()) {
					JsonObject responseAsJson = new JsonObject(res.result());
					if(responseAsJson.getString("uuid").equalsIgnoreCase(requestAsJson.getString("uuid"))) {
						routingContext.response().setStatusCode(200).end(res.result());
					}
				}else if(res.failed()) {
					System.out.println("Failed to get message from Redis Server");
					routingContext.response().setStatusCode(500).end("Server Error ");
				}
			});
			
			
		});
	}

	private String getUUID() {
		UUID uid = UUID.randomUUID();
		return uid.toString();
	}
	

}
