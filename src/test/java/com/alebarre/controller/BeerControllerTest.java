package com.alebarre.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.alebarre.models.Beer;
import com.alebarre.service.BeerService;
import com.alebarre.service.BeerServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    
    @MockitoBean
    BeerService beerService;

    BeerServiceImpl beerServiceImpl = new BeerServiceImpl();
    
    @Test
    void testListBeers() throws Exception {
    	
    	given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());
    	
    	mockMvc.perform(get("/api/v1/beer")
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    			.andExpect(jsonPath("$.length()", is(3)));
    	
    }
    
    @Test
    void createNewBeer() throws JsonProcessingException {
    	
    	objectMapper.findAndRegisterModules();
    	
    	Beer beer = beerServiceImpl.listBeers().get(0);
    	
    	System.out.println(objectMapper.writeValueAsString(beer));
    	
    }

    @Test
    void getBeerById() throws Exception {
        Beer testBeer = beerServiceImpl.listBeers().get(0);

        given(beerService.getBeerById(testBeer.getId())).willReturn(testBeer);

        mockMvc.perform(get("/api/v1/beer/" + testBeer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }
}