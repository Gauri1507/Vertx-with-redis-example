package com.api.redis.micro.service.verticle;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class Launcher {
	
	public static void main(String[] args) {
		Vertx.vertx().deployVerticle("com.api.redis.micro.service.verticle.SimpleMicroService",new DeploymentOptions(), event -> {
			if(event.succeeded()) {
				System.out.println("Deployed SimpleMicroService Successfully ");
			}else if(event.failed()) {
				System.out.println("Deployment SimpleMicroService Failed");
			}
		});
	}

}
