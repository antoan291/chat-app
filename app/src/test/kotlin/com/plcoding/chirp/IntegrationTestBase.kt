package com.plcoding.chirp

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
abstract class IntegrationTestBase {

    companion object {
        @Container
        val postgres = PostgreSQLContainer("postgres:16-alpine").apply {
            withDatabaseName("chirp_test")
            withUsername("test")
            withPassword("test")
        }

        @Container
        val redis = GenericContainer("redis:7-alpine").apply {
            withExposedPorts(6379)
        }

        @Container
        val rabbitmq = RabbitMQContainer("rabbitmq:3-management-alpine")

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            // PostgreSQL
            registry.add("spring.datasource.url") { postgres.jdbcUrl }
            registry.add("spring.datasource.username") { postgres.username }
            registry.add("spring.datasource.password") { postgres.password }

            // Redis
            registry.add("spring.data.redis.host") { redis.host }
            registry.add("spring.data.redis.port") { redis.getMappedPort(6379) }

            // RabbitMQ
            registry.add("spring.rabbitmq.host") { rabbitmq.host }
            registry.add("spring.rabbitmq.port") { rabbitmq.amqpPort }
            registry.add("spring.rabbitmq.username") { rabbitmq.adminUsername }
            registry.add("spring.rabbitmq.password") { rabbitmq.adminPassword }
            registry.add("spring.rabbitmq.ssl.enabled") { false }
            registry.add("spring.rabbitmq.virtual_host") { "/" }
        }
    }
}