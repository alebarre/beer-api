package com.alebarre.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.alebarre.models.Beer;
import com.alebarre.models.Customer;
import com.alebarre.service.BeerServiceImpl;
import com.alebarre.service.CustomerService;
import com.alebarre.service.CustomerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;
    
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CustomerService customerService;
    
    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;
    
    @Captor
    ArgumentCaptor<Customer> customerArgumentCaptor;
    
    @BeforeEach
    void setup() {
    	customerServiceImpl = new CustomerServiceImpl();
    }

    CustomerServiceImpl customerServiceImpl = new CustomerServiceImpl();
    
    @Test
    void testUpdateCustomer() throws Exception {
        Customer customer = customerServiceImpl.getAllCustomers().get(0);

        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                .content(objectMapper.writeValueAsString(customer))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).updateCustomerById(uuidArgumentCaptor.capture(), any(Customer.class));

        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }
    
    @Test
    void TestCreateNewCustomer() throws Exception {
    	
    	Customer customer = customerServiceImpl.getAllCustomers().get(0);
    	customer.setId(null);
    	
    	given(customerService.saveNewCustomer(any(Customer.class))).willReturn(customerServiceImpl.getAllCustomers().get(1));
    	
    	mockMvc.perform(post(CustomerController.CUSTOMER_PATH)
    			.accept(MediaType.APPLICATION_JSON)
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(customer)))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"));
    	
    }
    
    @Test
    void testListCustomers() throws Exception {
    	
    	given(customerService.getAllCustomers()).willReturn(customerServiceImpl.getAllCustomers());
    	
    	mockMvc.perform(get(CustomerController.CUSTOMER_PATH)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    			.andExpect(jsonPath("$.length()", is(3)));
    	
    }

    @Test
    void testGetCustomerById() throws Exception {
        Customer testCustomer = customerServiceImpl.getAllCustomers().get(0);

        given(customerService.getCustomerById(testCustomer.getId())).willReturn(Optional.of(testCustomer));

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, testCustomer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testCustomer.getId().toString())))
                .andExpect(jsonPath("$.name", is(testCustomer.getName())));
    }
    
    @Test
    void testDeleteCustomer() throws Exception {
    	
    	Customer customer = customerServiceImpl.getAllCustomers().get(0);
    	
    	mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, customer.getId())
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isNoContent());
    	
    	ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
    	verify(customerService).deleteCustomerById(uuidArgumentCaptor.capture());
    	
    }
    
    @Test
    void testPatchCustomer() throws Exception{
    	
    	Customer customer= customerServiceImpl.getAllCustomers().get(0);
    	
    	Map<String, Object> customerMap = new HashMap<>();
    	customerMap.put("Customer name", "new name");
    	
    	mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID, customer.getId())
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(customerMap)))
    	.andExpect(status().isNoContent());
    	
    	verify(customerService).patchCustomerById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());
    	
    	assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    	assertThat(customerMap.get("beerName")).isEqualTo(customerArgumentCaptor.getValue().getName());
    	
    }
    
}