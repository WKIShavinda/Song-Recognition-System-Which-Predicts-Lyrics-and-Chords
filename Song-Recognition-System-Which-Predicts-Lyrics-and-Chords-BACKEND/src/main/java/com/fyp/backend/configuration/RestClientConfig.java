package com.fyp.backend.configuration;

import com.fyp.backend.util.SSLUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

import javax.net.ssl.SSLContext;

@Configuration
public class RestClientConfig extends AbstractElasticsearchConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestClientConfig.class);

    @Autowired
    private ElasticConfig elasticConfig;

    /**
     * Create Rest client which configured with elasticsearchHost, elasticsearchPort
     *
     * @return RestHighLevelClient
     */

    @Override
    @Bean(destroyMethod = "close")
    public RestHighLevelClient elasticsearchClient() {

        LOGGER.info("Creating Elasticsearch client");

        final RestHighLevelClient client;
        final SSLContext sslContext;
        try {
            sslContext = SSLUtils.getSSLContext();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(
                elasticConfig.getUser(), elasticConfig.getPass()));
        client = new RestHighLevelClient(RestClient.builder(new HttpHost(elasticConfig.getHost(), 9200, "https"))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setSSLContext(sslContext)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                        .setDefaultCredentialsProvider(credentialsProvider)));
        return client;
    }
}




