package com.example.restserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class HelloController {

    @PostMapping("/process")
    public List<Integer> process(@RequestBody List<Integer> numbers) {
        long start = System.nanoTime();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            e.printStackTrace();
        }
        long end = System.nanoTime();
        System.out.println("REST Time : " + (end - start) / 1_000_000 + " ms");
        return numbers.stream().sorted().collect(Collectors.toList());
    }
}