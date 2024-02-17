package com.darek;

import com.darek.customer.Customer;
import com.darek.customer.CustomerRepository;
import com.github.javafaker.Faker;
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
            Faker faker = new Faker();
            String name = faker.name().firstName();
            String email = faker.internet().emailAddress() + "@gmail.com";
            int age = faker.number().numberBetween(1,120);
            Customer customer1 = new Customer(name,email,age);
            Customer customer2 = new Customer(name,email,age);
            List<Customer> customers = List.of(customer1, customer2);
//            customerRepository.saveAll(customers);
        };
    }
}

