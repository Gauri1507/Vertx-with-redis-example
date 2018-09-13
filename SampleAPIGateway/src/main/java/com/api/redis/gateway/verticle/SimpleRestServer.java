package com.api.redis.gateway.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;


public class SimpleRestServer extends AbstractVerticle{
	
	protected RedisClient client;
	
	@Override
	public void start(){
		String redis_host = "127.0.0.1";
		int redis_port = 6379;
		int http_port = 9001;
		
		setRedisClient(redis_host, redis_port);
		
		subscribeForTheServiceChannel();
		
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		
		SimpleRestChild child = null;
		
		try {
			child = (SimpleRestChild) Class.forName("com.api.redis.gateway.verticle.SimpleRestChild").newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		router.route("/subscription").handler(child::handleSubscription);
		vertx.createHttpServer().requestHandler(router::accept).listen(http_port);
		System.out.println("Server started at port : " + http_port);
	}


	private void setRedisClient(String redis_host, int redis_port) {
		client = RedisClient.create(vertx, new RedisOptions().setHost(redis_host).setPort(redis_port));
		
	}
	
	private void subscribeForTheServiceChannel() {
		client.subscribe("channelForServiceToPublish", handler -> {
			if(handler.succeeded())
				System.out.println("SimpleRestServer subscibed to the channel successfully");
		});
	}
}
