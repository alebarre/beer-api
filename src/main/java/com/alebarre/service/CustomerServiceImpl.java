package com.alebarre.service;

import java.time.LocalDateTime;
import java.util.*;

import com.alebarre.models.Beer;
import org.springframework.stereotype.Service;

import com.alebarre.models.Customer;
import org.springframework.util.StringUtils;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	private final Map<UUID, Customer> customerMap;

    public CustomerServiceImpl() {
        Customer customer1 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Customer 1")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Customer 2")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Customer 3")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        customerMap = new HashMap<>();
        customerMap.put(customer1.getId(), customer1);
        customerMap.put(customer2.getId(), customer2);
        customerMap.put(customer3.getId(), customer3);
    }

    @Override
    public Customer saveNewCustomer(Customer customer) {

        Customer savedCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .version(customer.getVersion())
                .updateDate(LocalDateTime.now())
                .createdDate(LocalDateTime.now())
                .name(customer.getName())
                .build();

        customerMap.put(savedCustomer.getId(), savedCustomer);

        return savedCustomer;
    }

    @Override
    public void patchCustomerById(UUID customerId, Customer customer) {
        Customer existingCustomer = customerMap.get(customerId);
        if (existingCustomer != null && existingCustomer.getId() != customerId) {
            if (StringUtils.hasText(customer.getName())) {
                existingCustomer.setName (customer.getName());
            }
            if (customer.getVersion() != existingCustomer.getVersion()) {
                existingCustomer.setVersion(customer.getVersion());
            }
            existingCustomer.setUpdateDate(LocalDateTime.now());
        } else {
            throw new NoSuchElementException("🚫 No such Customer with id: " + customerId);
        }
    }

    @Override
    public void updateCustomerById(UUID customerId, Customer customer) {
        Customer existing = customerMap.get(customerId);
        if (existing != null && existing.getId().equals(customerId)) {
            existing.setName(customer.getName());
            existing.setVersion(customer.getVersion());
            existing.setUpdateDate(LocalDateTime.now());
            existing.setCreatedDate(customer.getCreatedDate());
            customerMap.put(existing.getId(), existing);
        } else {
            throw new NoSuchElementException("🚫 " + "No such Customer with id: " + customerId);
        }
    }

    @Override
    public void deleteCustomerById(UUID customerId) {
        Customer existing = customerMap.get(customerId);
        if (existing != null &&  existing.getId().equals(customerId)) {
            customerMap.remove(customerId);
        } else {
            throw new NoSuchElementException("🚫 " + "No such Customer with id: " + customerId);
        }
    }

    @Override
    public Optional<Customer> getCustomerById(UUID uuid) {
        return Optional.of(customerMap.get(uuid));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customerMap.values());
    }

}
