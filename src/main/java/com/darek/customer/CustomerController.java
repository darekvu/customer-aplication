package com.darek.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("{customer_id}")
    public Customer getCustomer(@PathVariable(name = "customer_id") Long id) {
        return customerService.getCustomer(id);

    }

    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest request) {
        customerService.addCustomer(request);
    }
    @DeleteMapping("{customer_id}")
    public void deleteCustomer(@PathVariable("customer_id")Long id) {
        customerService.deleteCustomer(id);
    }

    @PutMapping("{customer_id}")
    public void editCustomer(
            @PathVariable("customer_id")Long id,
            @RequestBody CustomerUpdateRequest updateRequest
    ){
        customerService.updateCustomer(id,updateRequest);
    }
}
