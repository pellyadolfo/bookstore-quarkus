package com.example.bookstore.config;

import io.quarkus.vertx.http.runtime.filters.Filters;
import io.vertx.core.http.HttpMethod;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class CorsFilter {

    public void init(@Observes Filters filters) {
        filters.register(
            rc -> {
                rc.response()
                    .putHeader("Access-Control-Allow-Origin", "*")
                    .putHeader("Access-Control-Allow-Credentials", "true")
                    .putHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .putHeader("Access-Control-Allow-Headers", "DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range,Authorization")
                    .putHeader("Access-Control-Expose-Headers", "Content-Length,Content-Range");
                
                if (rc.request().method().equals(HttpMethod.OPTIONS)) {
                    rc.response()
                        .putHeader("Access-Control-Max-Age", "86400")
                        .end();
                } else {
                    rc.next();
                }
            },
            10 // Priority
        );
    }
}