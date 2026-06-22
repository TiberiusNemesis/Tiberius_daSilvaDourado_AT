package com.musicstream.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Composition root of the music-streaming platform. Boots Spring, which scans
 * the bootstrap module for the REST interface, the JPA repository adapters, the
 * Anticorruption Layer adapters and the wiring that assembles the
 * framework-free Bounded Contexts.
 */
@SpringBootApplication
public class StreamingApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamingApplication.class, args);
    }
}
