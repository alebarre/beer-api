package com.alebarre.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.alebarre.models.Customer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	@DeleteMapping("{beeId}")
	public ResponseEntity deleteById(@PathVariable("beeId") UUID beeId) {
		try {
			beerService.deleteById(beeId);

			return ResponseEntity.ok().body("✅ Deleted: " + beeId);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}

	@PutMapping("{beerId}")
	public ResponseEntity<?> updateBeerById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer){
		try {
			beerService.updateBeerById(beerId, beer);

			return ResponseEntity.ok().body("✅ Updated: " + beerId);
		}  catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}
	
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

	@RequestMapping(value = "{beerId}", method = RequestMethod.GET)
	public Beer getBeerById(@PathVariable("beerId") UUID beerId) {
		return beerService.getBeerById(beerId);
	}

}
