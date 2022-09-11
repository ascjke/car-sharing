package ru.borisov.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {

    private Connection connection;

    public CustomerDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Customer> getAllCustomers() {

        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customer ORDER BY id";

        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int rentedCarId = resultSet.getInt("rented_car_id");

                    customers.add(new Customer(id, name, rentedCarId));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to getAllCustomers()", ex);
        }

        return customers;
    }

    @Override
    public Customer getCustomer(int customerId) {

        Customer customer;
        String sql = "SELECT * FROM customer where id=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                String name = resultSet.getString("name");
                int rentedCarId = resultSet.getInt("rented_car_id");
                customer = new Customer(customerId, name, rentedCarId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

        return customer;
    }

    @Override
    public Customer createCustomer(Customer customer) {

        String sql = "INSERT INTO customer(name) VALUES(?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, customer.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return customer;
    }

    @Override
    public Customer rentACar(Customer customer, int carId) {

        String sql = "UPDATE customer SET rented_car_id = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, carId);
            pstmt.setInt(2, customer.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        customer.setRentedCarId(carId);
        return customer;
    }

    @Override
    public Customer returnARentedCar(Customer customer) {

        String sql = "UPDATE customer SET rented_car_id = null WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, customer.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        customer.setRentedCarId(0);
        return customer;
    }
}
