package ru.borisov;


import ru.borisov.dao.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CLI {

    private final Scanner scanner = new Scanner(System.in);
    private CompanyDao companyRepository;
    private CarDao carsRepository;
    private CustomerDao customerRepository;

    public CLI(CompanyDao companyDao, CarDao carDao, CustomerDao customerDao) {
        this.companyRepository = companyDao;
        this.carsRepository = carDao;
        this.customerRepository = customerDao;
    }

    public void start() {

        int choice = mainMenuChoice();

        switch (choice) {
            case 1:
                managerMenu();
                break;

            case 2:
                chooseCustomer();
                break;

            case 3:
                System.out.println("\nEnter the customer name:");
                scanner.nextLine();
                String customerName = scanner.nextLine();
                customerRepository.createCustomer(new Customer(customerName));
                System.out.println("The customer was added!\n");
                start();

            case 0:
                try {
                    companyRepository.getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void customerMenu(Customer customer) {

        int choice = customerMenuChoice();
        int rentedCarId = customer.getRentedCarId();

        switch (choice) {

            case 1:
                if (rentedCarId != 0) {
                    System.out.println("You've already rented a car!\n");
                } else {
                    List<Company> companies = companyRepository.getAllCompanies();
                    if (companies.size() == 0) {
                        System.out.println("\nThe company list is empty!");
                    } else {
                        System.out.println("Choose a company:");
                        companies.forEach(com -> System.out.println(companies.indexOf(com) + 1 + ". " + com.getName()));
                        System.out.println("0. Back");
                        int numOfCompany = scanner.nextInt();
                        if (numOfCompany != 0) {
                            Company company = companies.get(numOfCompany - 1);
                            List<Car> availableCars = carsRepository.getAvailableCarsOfCompany(company.getId());
                            if (availableCars.size() == 0) {
                                System.out.println("No available cars in the '" + company.getName() + "' company");
                            } else {
                                System.out.println("\nChoose a car:");
                                availableCars.forEach(car ->
                                        System.out.println(availableCars.indexOf(car) + 1 + ". " + car.getName()));
                                System.out.println("0. Back");
                                int numOfCar = scanner.nextInt();
                                if (numOfCar != 0) {
                                    Car rentedCar = availableCars.get(numOfCar - 1);
                                    customerRepository.rentACar(customer, rentedCar.getId());
                                    System.out.println("\nYou rented '" + rentedCar.getName() + "'\n");
                                }
                            }
                        }
                    }
                }
                customerMenu(customer);
                break;


            case 2:
                if (rentedCarId == 0) {
                    System.out.println("You didn't rent a car!\n");
                } else {
                    customerRepository.returnARentedCar(customer);
                    System.out.println("You've returned a rented car!\n");
                }
                customerMenu(customer);
                break;

            case 3:
                if (rentedCarId == 0) {
                    System.out.println("You didn't rent a car!\n");
                    customerMenu(customer);
                    break;
                } else {
                    Car car = carsRepository.getCar(rentedCarId);
                    int companyId = car.getCompanyId();
                    Company company = companyRepository.getCompany(companyId);
                    System.out.println("Your rented car:\n" + car.getName());
                    System.out.println("Company:\n" + company.getName() + "\n");
                    customerMenu(customer);
                    break;
                }


            case 0:
                start();
                break;
        }

    }

    private void chooseCustomer() {

        List<Customer> customers = customerRepository.getAllCustomers();
        if (customers.size() == 0) {
            System.out.println("\nThe customer list is empty!\n");
            start();
        } else {
            System.out.println("\nCustomer list:");
            customers.forEach(customer -> System.out.println(customers.indexOf(customer) + 1 + ". " + customer.getName()));
            System.out.println("0. Back");
            int numOfCustomer = scanner.nextInt();
            if (numOfCustomer == 0) {
                start();
            } else {
                Customer customer = customers.get(numOfCustomer - 1);
                System.out.println();
                customerMenu(customer);
            }
        }
    }

    public int mainMenuChoice() {

        String choice;
        int digitChoice;

        while (true) {
            System.out.println("1. Log in as a manager");
            System.out.println("2. Log in as a customer");
            System.out.println("3. Create a customer");
            System.out.println("0. Exit");

            choice = scanner.next();

            if (isDigit(choice)) {
                digitChoice = Integer.parseInt(choice);

                if (digitChoice < 0 || digitChoice > 3) {
                    System.out.println("Your input should be in 0-3!");
                } else {
                    break;
                }
            } else {
                System.out.println("Your input should be digit!");
            }
        }

        return digitChoice;
    }

    private void managerMenu() {

        int choice = managerMenuChoice();

        switch (choice) {
            case 1:
                List<Company> companies = companyRepository.getAllCompanies();
                if (companies.size() == 0) {
                    System.out.println("\nThe company list is empty!");
                    managerMenu();
                } else {
                    System.out.println("\nChoose the company:");
                    companies.forEach(com -> System.out.println(companies.indexOf(com) + 1 + ". " + com.getName()));
                    System.out.println("0. Back");
                    int numOfCompany = scanner.nextInt();
                    if (numOfCompany == 0) {
                        managerMenu();
                    } else {
                        Company company = companies.get(numOfCompany - 1);
                        System.out.println("\n'" + company.getName() + "'" + " company:");
                        companyMenu(company);
                    }
                }
                break;

            case 2:
                System.out.println("\nEnter the company name:");
                scanner.nextLine();
                String name = scanner.nextLine();
                companyRepository.createCompany(new Company(name));
                System.out.println("The company was created!");
                managerMenu();
                break;

            case 0:
                start();
                break;
        }
    }

    private int managerMenuChoice() {

        String choice;
        int digitChoice;

        while (true) {
            System.out.println("\n1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");

            choice = scanner.next();

            if (isDigit(choice)) {
                digitChoice = Integer.parseInt(choice);
                if (digitChoice < 0 || digitChoice > 2) {
                    System.out.println("Your choice is incorrect!");
                } else {
                    break;
                }
            } else {
                System.out.println("Your input should be digit!");
            }
        }

        return digitChoice;
    }

    private void companyMenu(Company company) {

        int choice = companyMenuChoice();

        switch (choice) {
            case 1:
                List<Car> cars = carsRepository.getAllCarsOfCompany(company.getId());
                if (cars.size() == 0) {
                    System.out.println("The car list is empty!\n");
                } else {
                    System.out.println("Car list:");
                    cars.forEach(car -> System.out.println(cars.indexOf(car) + 1 + ". " + car.getName()));
                    System.out.println();
                }
                companyMenu(company);
                break;

            case 2:
                System.out.println("Enter the car name:");
                scanner.nextLine();
                String name = scanner.nextLine();
                carsRepository.createCar(new Car(name, company.getId()));
                System.out.println("The car was added!\n");
                companyMenu(company);
                break;

            case 0:
                managerMenu();
                break;
        }
    }

    private int companyMenuChoice() {

        String choice;
        int digitChoice;

        while (true) {
            System.out.println("1. Car list");
            System.out.println("2. Create a car");
            System.out.println("0. Back");

            choice = scanner.next();

            if (isDigit(choice)) {
                digitChoice = Integer.parseInt(choice);
                if (digitChoice < 0 || digitChoice > 2) {
                    System.out.println("Your choice is incorrect!");
                } else {
                    break;
                }
            } else {
                System.out.println("Your input should be digit!");
            }
        }
//        System.out.println();
        return digitChoice;
    }

    private int customerMenuChoice() {

        String choice;
        int digitChoice;

        while (true) {
            System.out.println("1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("0. Back");

            choice = scanner.next();

            if (isDigit(choice)) {
                digitChoice = Integer.parseInt(choice);
                if (digitChoice < 0 || digitChoice > 3) {
                    System.out.println("Your choice is incorrect!");
                } else {
                    break;
                }
            } else {
                System.out.println("Your input should be digit!");
            }
        }
        System.out.println();
        return digitChoice;
    }


    private boolean isDigit(String s) {

        boolean isDigit = true;
        for (char ch : s.toCharArray()) {
            if (!Character.isDigit(ch)) {
                isDigit = false;
                break;
            }
        }

        return isDigit;
    }

}
