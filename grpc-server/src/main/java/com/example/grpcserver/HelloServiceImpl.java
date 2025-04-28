package com.example.grpcserver;

import com.example.grpc.HelloServiceGrpc;
import com.example.grpc.HelloServiceProto;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void processNumbers(HelloServiceProto.NumbersRequest request, StreamObserver<HelloServiceProto.NumbersResponse> responseObserver) {
        long start = System.nanoTime();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            e.printStackTrace();
        }
        List<Integer> sorted = request.getNumbersList().stream().sorted().collect(Collectors.toList());
        HelloServiceProto.NumbersResponse response = HelloServiceProto.NumbersResponse.newBuilder()
                .addAllSortedNumbers(sorted)
                .build();
        long end = System.nanoTime();
        System.out.println("GRPC Time : " + (end - start) / 1_000_000 + " ms");
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}