package com.alebarre.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alebarre.models.Customer;
import com.alebarre.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
@RestController
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	
	 @PostMapping
	    public ResponseEntity handlePost(@RequestBody Customer customer){
	        Customer savedCustomer = customerService.saveNewCustomer(customer);

	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Location", "/api/v1/customer/" + savedCustomer.getId().toString());

	        return new ResponseEntity(headers, HttpStatus.CREATED);
	    }

	    @RequestMapping(method = RequestMethod.GET)
	    public List<Customer> listAllCustomers(){
	        return customerService.getAllCustomers();
	    }

	    @RequestMapping(value = "{customerId}", method = RequestMethod.GET)
	    public Customer getCustomerById(@PathVariable("customerId") UUID id){
	        return customerService.getCustomerById(id);
	    }

}
