package com.alebarre.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.alebarre.models.Beer;


public interface BeerService {
	
    List<Beer> listBeers();

    Optional<Beer> getBeerById(UUID id);

    Beer saveNewBeer(Beer beer);

    void updateBeerById(UUID beerId, Beer beer);

    void deleteById(UUID beerId);

    void patchBeerById(UUID beerId, Beer beer);
}
