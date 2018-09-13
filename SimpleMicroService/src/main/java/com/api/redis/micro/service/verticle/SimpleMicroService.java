package com.api.redis.micro.service.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

public class SimpleMicroService extends AbstractVerticle{
	
	protected RedisClient redisClient;

	@Override
	public void start() {
		String redis_host = "127.0.0.1";
		int redis_port = 6379;
		
		redisClient = RedisClient.create(vertx, new RedisOptions().setHost(redis_host).setPort(redis_port));
		
		redisClient.subscribe("channelForServerToPublish", handler -> {
			if(handler.succeeded())
				System.out.println("SimpleMicroService subscibed to the channel successfully");
		});
		
		// considering all positive scenario
		vertx.eventBus().consumer("io.vertx.redis.channelForServerToPublish", handler -> {
			JsonObject message = (JsonObject) handler.body();
			
			if(message.getString("status").equals("ok") && message.getJsonObject("value").getString("message").equals("ServiceOne"))
			{
				redisClient.get("request", res ->{
					if(res.succeeded()) {
						JsonObject requestAsJson = new JsonObject(res.result());
						JsonObject responseAsJson = new JsonObject().put("status", "ok").
								put("body", new JsonObject().put("key1","value1")).put("uuid", requestAsJson.getString("uuid"));
						redisClient.set("response", responseAsJson.toString(), resSetHandler -> {
							if(resSetHandler.succeeded()) {
								redisClient.publish("channelForClientToPublish", "responseNotification", pubHandler -> {
									if(pubHandler.succeeded()) {
										System.out.println("SimpleMicroService published to the channel");
									}
								});
							}
						});
					}
				});
			}
					
		});
		
		
	}
}
