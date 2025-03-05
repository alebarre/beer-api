package com.alebarre.service;

import java.util.List;
import java.util.UUID;

import com.alebarre.models.Beer;


public interface BeerService {
	
    List<Beer> listBeers();
	
	Beer getBeerById(UUID id);

	Beer saveNewBeer(Beer beer);

}
