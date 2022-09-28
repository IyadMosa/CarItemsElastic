package com.img.CarItemsElastic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.img.CarItemsElastic.document.CarItem;
import com.img.CarItemsElastic.repository.CarItemsRepository;
import com.img.CarItemsElastic.search.SearchByMakerAndModel;
import com.img.CarItemsElastic.search.SearchRequestDTO;
import com.img.CarItemsElastic.service.util.SearchUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.img.CarItemsElastic.helper.Constants.INDEX_NAME;

@Service
public class CarItemsService {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final CarItemsRepository repository;

    private final RestHighLevelClient client;

    @Autowired
    public CarItemsService(CarItemsRepository repository, RestHighLevelClient client) {
        this.repository = repository;
        this.client = client;
    }

    public void add(CarItem item) throws Exception {
        repository.save(item);
    }

    public List<CarItem> findAll() {
        return Lists.newArrayList(repository.findAll());
    }

    public List<CarItem> searchByMAkerAndModel(SearchRequestDTO dto) {
        SearchRequest request = SearchUtil.buildSearchRequest(INDEX_NAME, dto);
        return searchInternal(request);
    }

    public List<CarItem> searchByMAkerAndModel(SearchByMakerAndModel dto) {
        SearchRequest request = SearchUtil.buildSearchRequest(INDEX_NAME, dto.getMaker(), dto.getModel());
        return searchInternal(request);
    }

    private List<CarItem> searchInternal(SearchRequest request) {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        if (request == null) {
            return Collections.emptyList();
        }

        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            SearchHit[] searchHits = response.getHits().getHits();
            List<CarItem> items = new ArrayList<>();
            for (SearchHit hit : searchHits) {
                try {
                    items.add(MAPPER.readValue(hit.getSourceAsString(), CarItem.class));
                } catch (JsonProcessingException e) {
                    continue;
                }
            }
            return items;

        } catch (IOException e) {
            return Collections.emptyList();
        }
    }


}
