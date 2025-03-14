package com.alebarre.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.alebarre.models.Customer;

public interface CustomerService {

    Optional<Customer> getCustomerById(UUID uuid);

    List<Customer> getAllCustomers();

    Customer saveNewCustomer(Customer customer);

    void updateCustomerById(UUID customerId, Customer customer);

    void deleteCustomerById(UUID customerId);

    void patchCustomerById(UUID customerId, Customer customer);
}
