package com.darek.customer;

import jakarta.persistence.*;

import java.util.Objects;
@Entity
public class Customer {
    @Id
    @SequenceGenerator(
            name = "customer_id_sequence",
            sequenceName = "customer_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_id_sequence"
    )
    private Integer Id;
    private String name;
    private String email;
    private Integer age;

    public Customer() {
    }

    public Customer(Integer id, String name, String email, Integer age) {
        Id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (!Objects.equals(Id, customer.Id)) return false;
        if (!Objects.equals(name, customer.name)) return false;
        if (!Objects.equals(email, customer.email)) return false;
        return Objects.equals(age, customer.age);
    }

    @Override
    public int hashCode() {
        int result = Id != null ? Id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        return result;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
