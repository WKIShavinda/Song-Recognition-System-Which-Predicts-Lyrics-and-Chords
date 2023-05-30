package com.fyp.backend.Service;

import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
@Component
@Service
public class FetchService {
    @Qualifier("elasticsearchClient")
    @Autowired
    RestHighLevelClient highLevelClient;

    @Value("${elasticsearch.index_pattern}")
    private String indexPattern;

    public List<Map<String, Object>> executeRequest(String searchKey) {
        QueryBuilder queryBuilder;
        queryBuilder = matchQuery("Song Title",searchKey);
        SearchRequest request = new SearchRequest(indexPattern);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.sort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC);
        request.source(searchSourceBuilder);
        List<Map<String, Object>> hitList = null;
        try {
            SearchResponse scrollResponse = highLevelClient.search(request, RequestOptions.DEFAULT);
            if (scrollResponse != null) {
                if (scrollResponse.getHits().getHits().length != 0) {
                    hitList = new ArrayList<>();
                        for (SearchHit hit : scrollResponse.getHits().getHits()) {
                            hitList.add(hit.getSourceAsMap());
                        }
                }
            }
        } catch (ElasticsearchStatusException | IOException e) {
            e.printStackTrace();
            if (e.getClass().getName().contains("ElasticsearchStatusException")) {
                return null;
            } else if (e.getClass().getName().contains("ConnectException")) {
                return null;
            }

        }
        return hitList;
    }
}


