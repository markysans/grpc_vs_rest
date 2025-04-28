package com.example.benchmarkclient;

import com.example.grpc.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.example.grpc.HelloServiceProto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@SpringBootApplication
public class BenchmarkClientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BenchmarkClientApplication.class, args);
    }

    @Override
    public void run(String... args)  {
        final int numberOfRequests = 100;  // Number of requests to send
        RestTemplate restTemplate = new RestTemplate();
        // REST Benchmark
        long restStart = System.nanoTime();

        for (int i = 0; i < numberOfRequests; i++) {
            try {
                // Generating dynamic list of numbers (for example, 1000 numbers from 1 to 1000)
                List<Integer> numbers = generateNumbers(1000);
                restTemplate.postForObject("http://localhost:8080/api/process", numbers, List.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        long restEnd = System.nanoTime();
        System.out.println("Total REST Time: " + (restEnd - restStart) / 1_000_000 + " ms");

        // gRPC Benchmark
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        HelloServiceGrpc.HelloServiceBlockingStub stub = HelloServiceGrpc.newBlockingStub(channel);

        long grpcStart = System.nanoTime();
        for (int i = 0; i < numberOfRequests; i++) {
            try {
                // Generating dynamic list of numbers (for example, 1000 numbers from 1 to 1000)
                HelloServiceProto.NumbersRequest request = HelloServiceProto.NumbersRequest.newBuilder()
                        .addAllNumbers(generateNumbers(1000)) // Example: list of numbers generated dynamically
                        .build();
                stub.processNumbers(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long grpcEnd = System.nanoTime();


        System.out.println("Total gRPC Time: " + (grpcEnd - grpcStart) / 1_000_000 + " ms");
    }

    // Generate dynamic list of numbers (e.g., 1 to n)
    private List<Integer> generateNumbers(int n) {
        List x = new ArrayList();
        for(int i=0; i<100000000; i++)
            x.add(i);
        return x;
    }

}
