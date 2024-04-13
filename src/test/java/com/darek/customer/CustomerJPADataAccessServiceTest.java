package com.darek.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private CustomerRepository customerRepository;


    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        // When
        underTest.selectAllCustomers();
        // Then
        verify(customerRepository).findAll();
        then(customerRepository).should().findAll();
    }

    @Test
    void selectCustomerById() {
        //Given
        long id = 1;
        // When
        underTest.selectCustomerById(id);
        // Then
        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = new Customer(1L, "Marcin", "marcina@gmail.com", 25);
        // When
        underTest.insertCustomer(customer);
        // Then
        verify(customerRepository).save(customer);
    }

    @Test
    void existsPersonWithEmail() {
        // Given
        String email = "foo@gmail.com";
        // When
        underTest.existsPersonWithEmail(email);
        // Then
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void deleteCustomer() {
        // Given
        Long id =1L;
        // When
        underTest.deleteCustomer(id);
        // Then
        then(customerRepository).should().deleteById(id);
    }

    @Test
    void existsPersonWithId() {
        // Given
        Long id = 1L;
        // When
        underTest.existsPersonWithId(id);
        // Then
        then(customerRepository).should().existsCustomerById(id);
    }

    @Test
    void updateCustomer() {
        // Given
        Customer customer = new Customer(1L, "Marcin", "marcina@gmail.com", 25);
        // When
        underTest.updateCustomer(customer);
        // Then
        then(customerRepository).should().save(customer);
    }
}