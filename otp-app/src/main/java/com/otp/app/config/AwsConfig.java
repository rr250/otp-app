package com.otp.app.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    @Value("${aws.credentials.accessKey}")
    private  String awsAccessKey;

    @Value("${aws.credentials.secretKey}")
    private String awsSecretKey;

    @Value("${aws.credentials.region}")
    private  String awsRegion;

    @Bean
    public AmazonSNS amazonSNSClient(){
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        AWSStaticCredentialsProvider awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        return AmazonSNSClientBuilder.standard().withCredentials(awsStaticCredentialsProvider).withRegion(awsRegion).build();
    }
}
