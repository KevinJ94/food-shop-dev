package com.shop;


import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ESConfig {
    private final ElasticsearchProperties properties;
    public ESConfig(ElasticsearchProperties properties) {
        this.properties = properties;
    }

    /**
     * 解决netty引起的issue
     */
    @PostConstruct
    void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }




    @Bean
    public TransportClient transportClient(){
        return new PreBuiltXPackTransportClient(settings())
                .addTransportAddresses(addresses());
    }
    private Settings settings() {
        Settings.Builder builder = Settings.builder();
        builder.put("cluster.name", properties.getClusterName());
        properties.getProperties().forEach(builder::put);
        return builder.build();
    }

    private TransportAddress[] addresses() {
        String clusterNodesStr = properties.getClusterNodes();
        Assert.hasText(clusterNodesStr, "Cluster nodes source must not be null or empty!");
        String[] nodes = StringUtils.delimitedListToStringArray(clusterNodesStr, ",");

        return Arrays.stream(nodes).map(node -> {
            String[] segments = StringUtils.delimitedListToStringArray(node, ":");
            Assert.isTrue(segments.length == 2,
                    () -> String.format("Invalid cluster node %s in %s! Must be in the format host:port!", node, clusterNodesStr));
            String host = segments[0].trim();
            String port = segments[1].trim();
            Assert.hasText(host, () -> String.format("No host name given cluster node %s!", node));
            Assert.hasText(port, () -> String.format("No port given in cluster node %s!", node));
            return new TransportAddress(toInetAddress(host), Integer.valueOf(port));
        }).toArray(TransportAddress[]::new);
    }

    private static InetAddress toInetAddress(String host) {
        try {
            return InetAddress.getByName(host);
        } catch (UnknownHostException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

}