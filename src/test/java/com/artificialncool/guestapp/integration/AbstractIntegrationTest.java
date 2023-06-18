package com.artificialncool.guestapp.integration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public abstract class AbstractIntegrationTest {

    static MongoDBContainer mongo =
            new MongoDBContainer(DockerImageName.parse("mongo:latest"));


    @DynamicPropertySource
    static void configureDBConnection(DynamicPropertyRegistry dynamicPropertyRegistry){
        dynamicPropertyRegistry.add("spring.data.mongodb.host", mongo::getHost);
        dynamicPropertyRegistry.add("spring.data.mongodb.port", mongo::getFirstMappedPort);
    }

    @BeforeAll
    static void beforeAll(){
        mongo.start();
    }

    @AfterAll
    static void afterAll(){
        mongo.stop();
    }
}
