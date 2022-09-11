package ru.borisov.dao;

import java.util.List;

public interface CarDao {

    List<Car> getAllCarsOfCompany(int companyId);
    Car getCar(int carID);
    Car createCar(Car car);
    List<Car> getAvailableCarsOfCompany(int companyId);
}
