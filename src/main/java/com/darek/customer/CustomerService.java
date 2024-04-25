package com.darek.customer;

import com.darek.exception.DuplicateResourceException;
import com.darek.exception.RequestValidationException;
import com.darek.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Long id) {
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("customer with id [%s] not found".formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        if (customerDao.existsPersonWithEmail(customerRegistrationRequest.email())) {
            throw new DuplicateResourceException("Customer with this email already exists");
        }
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age()
        );
        customerDao.insertCustomer(customer);
    }

    public void deleteCustomer(Long customerId) {
        if (!customerDao.existsPersonWithId(customerId)) {
            throw new ResourceNotFoundException("Customer with id [%s]".formatted(customerId));
        }
        customerDao.deleteCustomer(customerId);
    }

    public Customer updateCustomer(Long customerId, CustomerUpdateRequest updateRequest) {
        Customer customer = getCustomer(customerId);
        boolean changes = false;

        if (updateRequest.name() != null && !updateRequest.name().equals(customer.getName())) {
            customer.setName(updateRequest.name());
            changes = true;
        }
        if (updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())) {
            customer.setAge(updateRequest.age());
            changes = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())) {
            if (customerDao.existsPersonWithEmail(updateRequest.email())) {
                throw new DuplicateResourceException("Email already taken");
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }

        if(!changes){
            throw new RequestValidationException("No Data changes found");
        }
         customerDao.updateCustomer(customer);
        return customer;
    }
}
