package com.example.elasticsearch.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.annotation.Resource;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.TimeZone;

@Configuration
public class ElasticsearchConfig {

    @Resource
    private ElasticsearchProperties elasticsearchProperties;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(
                        new SimpleModule().addSerializer(
                                BigDecimal.class, new JsonSerializer<>() {
                                    @Override
                                    public void serialize(BigDecimal value, JsonGenerator gen,
                                                          SerializerProvider serializers) throws IOException {
                                        gen.writeString(value.toPlainString());
                                    }
                                }))
                .setTimeZone(TimeZone.getDefault());
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(ObjectMapper objectMapper) {
        final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(
                elasticsearchProperties.getUsername(),
                elasticsearchProperties.getPassword())
        );

        RestClientBuilder builder = RestClient
                .builder(HttpHost.create(elasticsearchProperties.getUris().getFirst()))
                .setHttpClientConfigCallback(
                        httpClientBuilder ->
                                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                )
                .setRequestConfigCallback(
                        requestConfigBuilder ->
                                requestConfigBuilder
                                        .setConnectTimeout(5000)
                                        .setSocketTimeout(60000)
                );

        RestClient restClient = builder.build();
        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper(objectMapper));
        return new ElasticsearchClient(transport);
    }
}
