package com.darek.customer;

import com.darek.exception.DuplicateResourceException;
import com.darek.exception.RequestValidationException;
import com.darek.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }


    @Test
    void itShouldGetAllCustomers() {
        // When
        underTest.getAllCustomers();
        // Then
        then(customerDao).should().selectAllCustomers();
    }

    @Test
    void itShouldGetCustomer() {
        // Given
        long id = 2;
        Customer customer = new Customer(id, "john", "john@gmail.com", 35);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // When
        Customer actual = underTest.getCustomer(id);
        // Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenCustomerNotFound() {
        // Given
        long id = 2;
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());
        // Then
        // When
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("customer with id [%s] not found".formatted(id));
    }

    @Test
    void itShouldAddCustomer() {
        // Given
        long id = 2;
        String email = "john@gmail.com";
        Customer customer = new Customer(id, "john", email, 35);
        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);
        // When
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                customer.getName(),
                customer.getEmail(),
                customer.getAge()
        );
        underTest.addCustomer(request);
        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
    }

    @Test
    void itShouldThrowWhenEmailExistsWhileAddingCustomer() {
        // Given
        long id = 2;
        String email = "john@gmail.com";
        Customer customer = new Customer(id, "john", email, 35);
        // When
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                customer.getName(),
                customer.getEmail(),
                customer.getAge()
        );
        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);
        // Then
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Customer with this email already exists");
        then(customerDao).should(never()).insertCustomer(customer);
    }

    @Test
    void itShouldDeleteCustomerById() {
        // Given
        Long id = 3L;
        // When
        when(customerDao.existsPersonWithId(id)).thenReturn(true);
        // Then
        underTest.deleteCustomer(id);
        then(customerDao).should().deleteCustomer(id);
    }

    @Test
    void itShouldThrownWhileDeleteCustomerById() {
        // Given
        Long id = 3L;
        // When
        when(customerDao.existsPersonWithId(id)).thenReturn(false);
        // Then
        assertThatThrownBy(() -> underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Customer with id [%s]".formatted(id));
        then(customerDao).should(never()).deleteCustomer(id);
    }

    @Test
    void itShouldUpdateCustomer() {
        // Given
        Long id = 1L;
        Customer customer = new Customer(id, "shady", "shady@gmail.com", 26);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("danbo", "danbo@gmail.com", 27);
        // When
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        underTest.updateCustomer(id, updateRequest);
        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        then(customerDao).should().updateCustomer(customerArgumentCaptor.capture());
//        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
    }

    @Test
    void canOnlyUpdateCustomerName() {
        // Given
        Long id = 1L;
        Customer customer = new Customer(id, "shady", "shady@gmail.com", 26);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("newShady", null, null);
        // When
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        underTest.updateCustomer(id, updateRequest);
        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        then(customerDao).should().updateCustomer(customerArgumentCaptor.capture());
//        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
    }

    @Test
    void canOnlyUpdateCustomerEmail() {
        // Given
        Long id = 1L;
        Customer customer = new Customer(id, "shady", "shady@gmail.com", 26);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, null, 28);
        // When
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        underTest.updateCustomer(id, updateRequest);
        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        then(customerDao).should().updateCustomer(customerArgumentCaptor.capture());
//        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
    }

    @Test
    void canOnlyUpdateCustomerAge() {
        // Given
        Long id = 1L;
        Customer customer = new Customer(id, "shady", "shady@gmail.com", 26);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("shady", "shadynew@gmail.com", null);
        // When
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        underTest.updateCustomer(id, updateRequest);
        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        then(customerDao).should().updateCustomer(customerArgumentCaptor.capture());
//        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
    }

    @Test
    void willThrowWhenTryingToUpdateCustomerEmailWhenAlreadyTaken() {
        // Given
        Long id = 1L;
        Customer customer = new Customer(id, "shady", "shady@gmail.com", 26);
        String newEmail = "newShady@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("danbo", newEmail, 27);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(true);
        //when
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Email already taken");
        //then
        then(customerDao).should(never()).updateCustomer(any());
    }


    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        // Given
        Long id = 1L;
        Customer customer = new Customer(id, "shady", "shady@gmail.com", 26);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(customer.getName(), customer.getEmail(), customer.getAge());
        // When
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
//               throw new RequestValidationException("No Data changes found");
        assertThatThrownBy(()->underTest.updateCustomer(id,updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("No Data changes found");
        then(customerDao).should(never()).updateCustomer(any());
    }
}