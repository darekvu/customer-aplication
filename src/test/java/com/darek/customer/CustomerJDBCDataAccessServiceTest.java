package com.darek.customer;

import com.darek.AbstractTestContainers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestContainers {
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();
    private CustomerJDBCDataAccessService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }


    @Test
    void selectAllCustomers() {
        // Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + UUID.randomUUID(),
                FAKER.number().numberBetween(1, 100)
        );
        underTest.insertCustomer(customer);
        // When
        List<Customer> actual = underTest.selectAllCustomers();
        // Then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(1, 100)
        );
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        Optional<Customer> actual = underTest.selectCustomerById(id);
        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        //given
        long id = 300;
        //When
        Optional<Customer> actual = underTest.selectCustomerById(id);
        //then
        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(1, 100)
        );
        // When
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();
        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent();
    }

    @Test
    void existsPersonWithEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(1, 100)
        );
        // When

        underTest.insertCustomer(customer);
        boolean actual = underTest.existsPersonWithEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithEmailReturnsFalseWhenDoesNotExists() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + UUID.randomUUID();
        // When
        boolean actual = underTest.existsPersonWithEmail(email);
        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomer() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(1, 100)
        );
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();
        // When
        underTest.deleteCustomer(id);
        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void existsPersonWithId() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        // When
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers()
                .stream().filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        boolean actual = underTest.existsPersonWithId(id);
        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithIdReturnFalseWhenIdNotPresent() {
        // Given
        long id = -1;
        // When
        boolean actual = underTest.existsPersonWithId(id);
        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void updateCustomer() {
//         Given
        String email = FAKER.internet().safeEmailAddress() + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(1, 100)
        );
        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        String newName = "Harold";
        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);
//        update.setEmail(newName);
//        update.setAge(11);
        underTest.updateCustomer(update);
        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }

    @Test
    void updateAllPropertiesCustomer() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(1, 100)
        );
        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        String newName = "foo";
        String newEmail = "brainanderson@gmail.com";
        int newAge = 24;
        // When
        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);
        update.setEmail(newEmail);
        update.setAge(newAge);
        underTest.updateCustomer(update);
        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValue(update);

    }
}