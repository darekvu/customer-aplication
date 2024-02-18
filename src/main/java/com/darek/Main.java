package com.darek;

import com.darek.customer.Customer;
import com.darek.customer.CustomerRepository;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Random;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {

        return args -> {
            Faker faker = new Faker();
            Random random = new Random();
            Name name = faker.name();

            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();


            Customer customer1 = new Customer(
                    firstName + " " + lastName,
                    firstName.toLowerCase() + "." + lastName.toLowerCase() + "@gmail.com",
                    random.nextInt(16, 99)
            );

            String firstName2 = faker.name().firstName();
            String lastName2 = faker.name().lastName();
            Customer customer2 = new Customer(
                    firstName2 + " " + lastName2,
                    firstName2.toLowerCase() + "." + lastName2.toLowerCase() + "@gmail.com",
                    random.nextInt(16, 99)
            );
            List<Customer> customers = List.of(customer1, customer2);
            customerRepository.saveAll(customers);
        };
    }
}

