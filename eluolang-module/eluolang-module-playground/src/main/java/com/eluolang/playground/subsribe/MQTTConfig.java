package com.eluolang.playground.subsribe;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 工具类，用来加载配置参数
 */
@Component(value = "serverMqtt")
public class MQTTConfig {
    @Value("${spring.mqtt.url}")
    private String host;
    @Value("${spring.mqtt.client.id}")
    private String clientid;
    @Value("${spring.mqtt.username}")
    private String username;
    @Value("${spring.mqtt.password}")
    private String password;
    private boolean cleansession;
    @Value("#{${spring.mqtt.bluetooth.gateway}}")
    private Map<String,String> default_topic;
    @Value("${trigger.point}")
    private String[] aa;
    @Value("${trigger.ju}")
    private Integer ju;
    private int timeout;
    private int keepalive;
    private int connectionTimeout;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, String> getDefault_topic() {
        return default_topic;
    }

    public void setDefault_topic(Map<String, String> default_topic) {
        this.default_topic = default_topic;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(int keepalive) {
        this.keepalive = keepalive;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public boolean isCleansession() {
        return cleansession;
    }

    public void setCleansession(boolean cleansession) {
        this.cleansession = cleansession;
    }

    public String[] getAa() {
        return aa;
    }

    public void setAa(String[] aa) {
        this.aa = aa;
    }

    public Integer getJu() {
        return ju;
    }

    public void setJu(Integer ju) {
        this.ju = ju;
    }
}
