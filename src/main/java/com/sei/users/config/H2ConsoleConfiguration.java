package com.sei.users.config;

import org.h2.tools.Server;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

@Configuration
@Profile("!prod")
public class H2ConsoleConfiguration {
    private Server server;

    @EventListener(ApplicationStartedEvent.class)
    public void start() throws java.sql.SQLException {
        this.server = Server.createWebServer("-webPort", "8082").start();
    }

    @EventListener(ContextClosedEvent.class)
    public void stop() {
        this.server.stop();
    }
}
