package com.img.CarItemsElastic.repository;

import com.img.CarItemsElastic.document.CarItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CarItemsRepository extends ElasticsearchRepository<CarItem, String> {
}
