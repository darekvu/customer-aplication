package com.darek.customer;

import com.darek.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainers {

    @Autowired
    private CustomerRepository underTest;
    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerByEmail() {
        // Given
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.save(customer);
        // When
        boolean actual = underTest.existsCustomerByEmail(email);
        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByEmailReturnsFalseWhenEmailNotPresent() {
        // Given
        String email = FAKER.internet().emailAddress();
        // When
        boolean actual = underTest.existsCustomerByEmail(email);
        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.save(customer);

        Long id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        boolean actual = underTest.existsCustomerById(id);
        // Then
        assertThat(actual).isTrue();
    }


    @Test
    void existsCustomerByIdReturnsFalseWhenIdNotPresent() {
        // Given
        long id = -1;
        // When
        boolean actual = underTest.existsCustomerById(id);
        // Then
        assertThat(actual).isFalse();
    }
}