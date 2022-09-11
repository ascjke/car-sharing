package ru.borisov.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDaoImpl implements CarDao {

    private Connection connection;

    public CarDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public List<Car> getAllCarsOfCompany(int companyId) {

        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM car WHERE company_id=? ORDER BY id";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, companyId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");

                    cars.add(new Car(id, name, companyId));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to getAllCarsOfCompany()", ex);
        }

        return cars;
    }

    @Override
    public Car getCar(int carId) {

        Car car;
        String sql = "SELECT * FROM car WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, carId);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                String name = resultSet.getString("name");
                int companyId = resultSet.getInt("company_id");
                car = new Car(carId, name, companyId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

        return car;
    }

    @Override
    public Car createCar(Car car) {

        String sql = "INSERT INTO car(name, company_id) VALUES(?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, car.getName());
            pstmt.setInt(2, car.getCompanyId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return car;
    }

    @Override
    public List<Car> getAvailableCarsOfCompany(int companyId) {

        List<Car> availableCars = new ArrayList<>();
        String sql = "SELECT car.id, car.name, car.company_id " +
                "FROM car " +
                "LEFT JOIN customer ON car.id = customer.rented_car_id " +
                "WHERE customer.name IS NULL AND car.company_id = ?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, companyId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");

                    availableCars.add(new Car(id, name, companyId));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to getAvailableCarsOfCompany()", ex);
        }

        return availableCars;
    }

    public Connection getConnection() {
        return connection;
    }
}
