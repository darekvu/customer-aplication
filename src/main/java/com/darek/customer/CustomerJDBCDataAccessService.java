package com.darek.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {
    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT * FROM customer;
                """;
//       return jdbcTemplate.query(sql,(rs, rowNum) ->{
//            Customer customer = new Customer(
//                    rs.getLong("id"),
//                    rs.getString("name"),
//                    rs.getString("email"),
//                    rs.getInt("age")
//            );
//            return customer;
//        });
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        var sql = """
                SELECT id,name,email,age FROM customer WHERE id = ?
                    """;
        return jdbcTemplate.query(sql, customerRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name,email,age)
                VALUES (?, ? ,? )
                """;
        int result = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge());

        System.out.println("jdbcTemplate.update = " + result);

    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        var sql = """
                SELECT count(*)
                FROM customer
                where email = ?
                """;
        return false;
    }

    @Override
    public void deleteCustomer(Long id) {
        var sql = """
                DELETE from customer
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsPersonWithId(Long id) {
        return false;
    }

    @Override
    public void updateCustomer(Customer update) {
        var sql = """
                UPDATE customer
                SET name = ?,
                email = ?,
                 age = ?
                WHERE id= ?;
                """;
        jdbcTemplate.update(sql, update.getName(), update.getEmail(), update.getAge(),update.getId());
    }
}
