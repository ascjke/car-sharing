package ru.borisov.dao;

import java.util.List;

public interface CustomerDao {

    List<Customer> getAllCustomers();
    Customer getCustomer(int customerId);
    Customer createCustomer(Customer customer);
    Customer rentACar(Customer customer, int carId);
    Customer returnARentedCar(Customer customer);

}
