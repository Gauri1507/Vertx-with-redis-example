package com.api.redis.gateway.verticle;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class Launcher {
	
	public static void main(String[] args) {
		Vertx.vertx().deployVerticle("com.api.redis.gateway.verticle.SimpleRestServer",new DeploymentOptions(), event -> {
			if(event.succeeded()) {
				System.out.println("Deployed SimpleRestServer Successfully ");
			}else if(event.failed()) {
				System.out.println("Deployment SimpleRestServer Failed");
			}
		});
	}

}
