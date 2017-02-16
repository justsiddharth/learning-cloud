package com.spring.seed.io.service.impl;

import com.spring.seed.io.configuration.ElasticsearchConfiguration;
import com.spring.seed.io.dto.StatusEnum;
import com.spring.seed.io.entity.BookFeed;
import com.spring.seed.io.repository.IBookFeedRepository;
import com.spring.seed.io.service.IBookFeedService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class BookFeedServiceImpl extends OperationsHelper implements IBookFeedService {

    @Autowired
    IBookFeedRepository repository;

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public BookFeed create(BookFeed bookfeed) {
        String id = UUID.randomUUID().toString();
        bookfeed.setId(id);
        String dateTime = LocalDateTime.now().format(formatter).toString();
        bookfeed.setCreatedDate(dateTime);
        bookfeed.setModifiedDate(dateTime);
        bookfeed.setStatus(StatusEnum.valueOf(bookfeed.getStatus().toString()));
        BookFeed savedBookfeed = repository.save(bookfeed);
        return savedBookfeed;
    }

    @Override
    public Page<BookFeed> findAll() {
        return repository.findAll(constructPageRequest(0, 100, "name", "ASC"));
    }

    @Override
    public void delete(String id) {
        repository.delete(id);
    }

    @Override
    public Page<BookFeed> findAllPaginatedAndSorted(int page, int size, String sortBy, String sortOrder) {
        return repository.findAll(constructPageRequest(page, size, sortBy, sortOrder));
    }

    @Override
    public Page<BookFeed> search(int page, int size, String sortBy, String sortOrder, Map<String, String[]> filters) {
        QueryBuilder query = addFilters(filters);
        return repository.search(query, constructPageRequest(page, size, sortBy, sortOrder));
    }

    @Override
    public void update(String id, BookFeed resource) {
        BookFeed bookfeed = findOne(id);
        if (bookfeed != null) {
            String dateTime = LocalDateTime.now().format(formatter).toString();
            resource.setModifiedDate(dateTime);
            final String doc = toJSON(resource);
            final UpdateRequest updateRequest = new UpdateRequest("prod_bookfeed", "bookfeed", id);
            updateRequest.doc(doc);
            BulkRequestBuilder bulkRequest = ElasticsearchConfiguration.getUnicastEsClient().prepareBulk();
            bulkRequest.add(updateRequest).execute().actionGet();
            bulkRequest.setRefresh(true);
            //repository.update(id, resource);
        }
    }

    @Override
    public BookFeed findOne(String id) {
        return repository.findOne(id);
    }

    @Override
    public List<BookFeed> showFeed(String userId) {
        Map<String, String[]> filters = new HashMap<>();
        String[] idFilter = {userId};
        filters.put("userUUID.not", idFilter);
        QueryBuilder query = addNotFilters(filters);
        return repository.search(query, constructPageRequest(0, 100000, OperationsHelper.CREATED_DATE, SortOrder.ASC.toString())).getContent();
    }
}
