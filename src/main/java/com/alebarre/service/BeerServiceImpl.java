package com.alebarre.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alebarre.models.Beer;
import com.alebarre.models.BeerStyle;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private final Map<UUID, Beer> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        Beer beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }

    @Override
    public void deleteById(UUID beerId) {
        if (beerMap.containsKey(beerId)) {
            beerMap.remove(beerId);
        } else {
            throw new NoSuchElementException("🚫 Beer with id " + beerId + " not found");
        }
    }

    @Override
    public void patchBeerById(UUID beerId, Beer beer) {
        Beer existingBeer = beerMap.get(beerId);
        if (existingBeer != null && existingBeer.getId() != beerId) {
            if (StringUtils.hasText(beer.getBeerName())) {
                existingBeer.setBeerName(beer.getBeerName());
            }
            if (beer.getBeerStyle() != null) {
                existingBeer.setBeerStyle(beer.getBeerStyle());
            }
            if (beer.getPrice() != null) {
                existingBeer.setPrice(beer.getPrice());
            }
            if (beer.getQuantityOnHand() != null) {
                existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
            }
            if (beer.getUpc() != null) {
                existingBeer.setUpc(beer.getUpc());
            }
            existingBeer.setUpdateDate(LocalDateTime.now());
        } else {
            throw new NoSuchElementException("🚫 No such Beer with id: " + beerId);
        }
    }

    @Override
    public void updateBeerById(UUID beerId, Beer beer) {
        Beer existingBeer = beerMap.get(beerId);
        if (existingBeer != null && existingBeer.getId() == beerId) {
            existingBeer.setId(beerId);
            existingBeer.setBeerName(beer.getBeerName());
            existingBeer.setPrice(beer.getPrice());
            existingBeer.setUpc(beer.getUpc());
            existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
            beerMap.put(beerId, beer);
        } else {
            throw new NoSuchElementException("🚫 No such Beer with id: " + beerId);
        }
    }

    @Override
    public List<Beer> listBeers(){
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Optional<Beer> getBeerById(UUID id) {
        if (beerMap.containsKey(id)) {
            log.debug("Get Beer by Id - in service. Id: " + id.toString());
            return Optional.of(beerMap.get(id));
        } else {
            throw new NoSuchElementException("🚫 Beer with id: " + id + " not found");
        }
    }
    
    @Override
    public Beer saveNewBeer(Beer beer) {
    	Beer savedBeer = Beer.builder()
    			.id(UUID.randomUUID())
    			.createdDate(LocalDateTime.now())
    			.updateDate(LocalDateTime.now())
    			.version(beer.getVersion())
    			.beerName(beer.getBeerName())
    			.beerStyle(beer.getBeerStyle())
    			.quantityOnHand(beer.getQuantityOnHand())
    			.upc(beer.getUpc())
    			.price(beer.getPrice())
    			.build();
    	beerMap.put(savedBeer.getId(), savedBeer);
    	return savedBeer;
    }

}