package ru.borisov.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImpl implements CompanyDao {

    private Connection connection;

    public CompanyDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Company> getAllCompanies() {

        List<Company> companies = new ArrayList<>();
        String sql = "SELECT * FROM company ORDER BY id";

        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");

                    companies.add(new Company(id, name));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to getAllCompanies()", ex);
        }

        return companies;
    }

    @Override
    public Company getCompany(int companyId) {

        Company company;
        String sql = "SELECT * FROM company where id=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, companyId);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                String name = resultSet.getString("name");
                company = new Company(companyId, name);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

        return company;
    }

    @Override
    public Company createCompany(Company company) {

        String sql = "INSERT INTO company(name) VALUES(?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, company.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return company;
    }

    public Connection getConnection() {
        return connection;
    }
}
