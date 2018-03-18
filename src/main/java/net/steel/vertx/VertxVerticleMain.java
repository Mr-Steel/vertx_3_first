package net.steel.vertx;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class VertxVerticleMain {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setInstances(1);
        vertx.deployVerticle("net.steel.vertx.MyVerticle", deploymentOptions, res -> {
            if (res.succeeded()) {
                String deploymentId = res.result();
                System.out.println("Deployment id is: " + deploymentId);
                vertx.undeploy(deploymentId);
            } else {
                System.out.println("Deployment failed!");
            }
        });

    }
}