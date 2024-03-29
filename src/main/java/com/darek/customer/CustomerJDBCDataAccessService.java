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
                SELECT COUNT(id)
                FROM customer
                WHERE email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public void deleteCustomer(Long customerId) {
        var sql = """
                DELETE from customer
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, customerId);
    }

    @Override
    public boolean existsPersonWithId(Long id) {
        var sql = """
                SELECT count(name)
                FROM customer
                WHERE id = ?
                """;
        Long count = jdbcTemplate.queryForObject(sql, Long.class, id);
        return count != null && count > 0;
    }

    @Override
    public void updateCustomer(Customer update) {

        if (update.getName() != null) {
            String sql = "UPDATE customer SET name = ? WHERE id =?";
            int result = jdbcTemplate.update(sql, update.getName(), update.getId());
            System.out.println("Updated name result: " + result);
        }

        if (update.getEmail() != null) {
            String sql = "UPDATE customer SET email = ? WHERE id =?";
            int result = jdbcTemplate.update(sql, update.getEmail(), update.getId());
            System.out.println("Updated email result: " + result);
        }
        if (update.getAge() != null) {
            String sql = "UPDATE customer SET age = ? WHERE id =?";
            int result = jdbcTemplate.update(sql, update.getAge(), update.getId());
            System.out.println("Updated age result: " + result);
        }
    }
}
