package com.alebarre.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alebarre.models.Beer;
import com.alebarre.service.BeerService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/beer")
public class BeerController {
	
	private final BeerService beerService;
	
	@PostMapping
	public ResponseEntity handlePost(@RequestBody Beer beer){
		Beer savedBeer = beerService.saveNewBeer(beer);
		HttpHeaders headers = new HttpHeaders();
		
		headers.add("Location", "/api/v1/beer/" + savedBeer.getId().toString());		
		return new ResponseEntity(headers, HttpStatus.CREATED);
	}
	
	@RequestMapping()
	public List<Beer> listBeers(){
		return beerService.listBeers();
	}
	
	@RequestMapping("{beerId}")
	public Beer getBeerById(@PathVariable("beerId") UUID beerId) {
		log.debug("Get beer by Id - In controller with id: " + beerId.toString());
		return beerService.getBeerById(beerId);
	}

}
