package com.img.CarItemsElastic.search;

import lombok.Data;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;

import static org.elasticsearch.search.sort.SortOrder.ASC;

@Data
public class SearchRequestDTO {
    private List<String> fields;
    private String searchTerm;
    private String sortBy;
    private SortOrder order = ASC;
}
