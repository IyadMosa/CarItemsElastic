package com.img.CarItemsElastic.controller;

import com.img.CarItemsElastic.document.CarItem;
import com.img.CarItemsElastic.search.SearchRequestDTO;
import com.img.CarItemsElastic.service.CarItemsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import search.SearchByMakerAndModel;

import java.util.List;

@RestController
@RequestMapping("/api/car-items")
public class CarItemsController {

    private final CarItemsService service;

    public CarItemsController(CarItemsService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<?> add(@RequestBody CarItem item) throws Exception {
        service.add(item);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PostMapping("/search")
    public ResponseEntity<List<CarItem>> search(@RequestBody SearchRequestDTO dto) {
        return new ResponseEntity<>(service.searchByMAkerAndModel(dto), HttpStatus.FOUND);
    }

    @PostMapping("/search-by-maker-model")
    public ResponseEntity<List<CarItem>> searchByMakerAndModel(@RequestBody SearchByMakerAndModel dto) {
        return new ResponseEntity<>(service.searchByMAkerAndModel(dto), HttpStatus.FOUND);
    }


    @GetMapping
    public ResponseEntity<List<CarItem>> getAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.FOUND);
    }

    @PostMapping("/get-by-ids")
    public ResponseEntity<List<CarItem>> getAllByIds(@RequestBody List<String> ids) {
        return new ResponseEntity<>(service.getAllByIds(ids), HttpStatus.FOUND);
    }
}
