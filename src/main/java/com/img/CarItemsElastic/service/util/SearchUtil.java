package com.img.CarItemsElastic.service.util;

import com.img.CarItemsElastic.search.SearchRequestDTO;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

public class SearchUtil {

    private SearchUtil() {
    }

    public static SearchRequest buildSearchRequest(final String indexName,
                                                   final SearchRequestDTO dto) {
        try {

            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .postFilter(getQueryBuilder(dto));

            if (StringUtils.hasLength(dto.getSortBy())) {
                builder = builder.sort(
                        dto.getSortBy(),
                        dto.getOrder() != null ? dto.getOrder() : SortOrder.ASC
                );
            }
            final SearchRequest request = new SearchRequest(indexName);
            request.source(builder);

            return request;
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static QueryBuilder getQueryBuilder(final SearchRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        final List<String> fields = dto.getFields();
        if (CollectionUtils.isEmpty(fields)) {
            return null;
        }

        if (fields.size() > 1) {
            final MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(dto.getSearchTerm())
                    .type(MultiMatchQueryBuilder.Type.CROSS_FIELDS)
                    .operator(Operator.AND);

            fields.forEach(queryBuilder::field);

            return queryBuilder;
        }

        return fields.stream()
                .findFirst()
                .map(field ->
                        QueryBuilders.matchQuery(field, dto.getSearchTerm())
                                .operator(Operator.AND))
                .orElse(null);
    }


    private static BoolQueryBuilder getQueryBuilder(final String maker, final String model) {
        if (!StringUtils.hasLength(maker) || !StringUtils.hasLength(model)) {
            return null;
        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder
                .must(QueryBuilders.matchQuery("maker", maker));
        boolQueryBuilder
                .must(QueryBuilders.matchQuery("model", model));

        return boolQueryBuilder;
    }

    public static SearchRequest buildSearchRequest(final String indexName,
                                                   final String maker, final String model) {
        try {

            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .postFilter(getQueryBuilder(maker, model));

            final SearchRequest request = new SearchRequest(indexName);
            request.source(builder);

            return request;
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
