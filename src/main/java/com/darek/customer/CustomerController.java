package com.darek.customer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("api/v1/customers")
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("api/v1/customers/{customer_id}")
    public Customer getCustomer(@PathVariable(name = "customer_id") Integer id) {
        return customerService.getCustomer(id);

    }

    @PostMapping
    public ResponseEntity<String> postUser(){
        return ResponseEntity.ok("Posting User");
        }
}
