package ru.borisov.dao;

import java.sql.Connection;
import java.util.List;

public interface CompanyDao {

    List<Company> getAllCompanies();
    Company getCompany(int companyId);
    Company createCompany(Company company);
    Connection getConnection();
}
