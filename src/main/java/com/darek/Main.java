package com.darek;

import com.darek.customer.Customer;
import com.darek.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {

        return args -> {
            Customer alex = new Customer("Alex", "alex@gmail.com", 46);
            Customer kamila = new Customer("Kamila", "kamila@gmail.com", 20);
            List<Customer> customers = List.of(alex, kamila);
//            customerRepository.saveAll(customers);
        };
    }
}

