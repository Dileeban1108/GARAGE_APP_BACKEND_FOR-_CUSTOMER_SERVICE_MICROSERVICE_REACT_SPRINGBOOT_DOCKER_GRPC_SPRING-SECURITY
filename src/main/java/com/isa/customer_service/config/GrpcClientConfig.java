package com.isa.customer_service.config;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import isa.example.garage.grpc.GarageProvideServiceGrpc;
import isa.example.garage.grpc.TimeslotServiceGrpc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

@Configuration
public class GrpcClientConfig {

    @Value("${grpc.customer-service.host}")
    private String grpcHost;

    @Value("${grpc.customer-service.port}")
    private int grpcPort;

    private ManagedChannel managedChannel;

    @Bean
    public ManagedChannel managedChannel() {
        this.managedChannel = ManagedChannelBuilder
                .forAddress(grpcHost, grpcPort)
                .usePlaintext()  // Use plaintext (without TLS); you should enable TLS for production
                .build();
        return managedChannel;
    }

    @Bean
    public TimeslotServiceGrpc.TimeslotServiceBlockingStub timeslotServiceStub(ManagedChannel channel) {
        return TimeslotServiceGrpc.newBlockingStub(channel);
    }
    @Bean
    public GarageProvideServiceGrpc.GarageProvideServiceBlockingStub garageProvideServiceBlockingStub(ManagedChannel channel) {
        return GarageProvideServiceGrpc.newBlockingStub(channel);
    }

    @PreDestroy
    public void shutdownChannel() {
        if (managedChannel != null && !managedChannel.isShutdown()) {
            managedChannel.shutdown();
        }
    }
}
