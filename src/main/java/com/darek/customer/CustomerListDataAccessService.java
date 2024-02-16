package com.darek.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {
    private static final List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer alex = new Customer(1, "Alex", "alex@gmail.com", 46);
        Customer kamila = new Customer(2, "Kamila", "kamila@gmail.com", 20);
        customers.add(alex);
        customers.add(kamila);
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
         return customers
                .stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst();
    }
}
