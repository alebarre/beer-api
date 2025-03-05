package com.alebarre.service;

import java.util.List;
import java.util.UUID;

import com.alebarre.models.Customer;

public interface CustomerService {

    Customer getCustomerById(UUID uuid);

    List<Customer> getAllCustomers();

    Customer saveNewCustomer(Customer customer);
}
