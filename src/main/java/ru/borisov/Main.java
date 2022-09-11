package ru.borisov;

import ru.borisov.dao.*;

import java.sql.*;
import java.util.Objects;

public class Main {

    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:file:D:\\Hyperskill\\car-sharing\\src\\main\\resources\\db\\";

    public static void main(String[] args) throws SQLException {

        Connection connection = connectToDataBase(getDatabaseName(args));
        connection.setAutoCommit(true);
        createTableCompany(connection);
        createTableCars(connection);
        createTableCustomers(connection);

        CompanyDao companyRepository = new CompanyDaoImpl(connection);
        CarDao carRepository = new CarDaoImpl(connection);
        CustomerDao customerRepository = new CustomerDaoImpl(connection);
        CLI cli = new CLI(companyRepository, carRepository, customerRepository);
        cli.start();
    }

    private static void createTableCompany(Connection connection) {

        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS company (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(255) UNIQUE NOT NULL" +
                ");";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlCreateTable);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void createTableCars(Connection connection) {

        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS car (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(255) UNIQUE NOT NULL, " +
                "company_id INT NOT NULL, " +
                "CONSTRAINT fk_company FOREIGN KEY (company_id) " +
                "REFERENCES company (id) " +
                "ON DELETE SET NULL" +
                ");";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlCreateTable);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void createTableCustomers(Connection connection) {

        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS customer (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(255) UNIQUE NOT NULL, " +
                "rented_car_id INT DEFAULT NULL, " +
                "CONSTRAINT fk_car_id FOREIGN KEY (rented_car_id) " +
                "REFERENCES car (id) " +
                "ON DELETE SET NULL" +
                ");";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlCreateTable);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static Connection connectToDataBase(String database) {

        String url = DB_URL + database;
        Connection connection = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return connection;
    }


    private static String getDatabaseName(String[] args) {

        String database = "carsharingDefault";
        for (int i = 0; i < args.length; i++) {
            if (Objects.equals("-databaseFileName", args[i])) {
                database = args[i + 1];
            }
        }
        return database;
    }
}