package com.spring.seed.io.service.impl;

import com.spring.seed.io.configuration.ElasticsearchConfiguration;
import com.spring.seed.io.entity.Group;
import com.spring.seed.io.repository.IGroupRepository;
import com.spring.seed.io.service.IGroupService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl extends OperationsHelper implements IGroupService {


    private static final List<String> DEFAULT_PARAMS_TO_REMOVE = Arrays.asList("page", "size", "sortBy", "sortOrder", "fields");

    @Autowired
    IGroupRepository repository;

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public Group create(Group group) {
        String id = UUID.randomUUID().toString();
        group.setId(id);
        String dateTime = LocalDateTime.now().format(formatter).toString();
        group.setCreatedDate(dateTime);
        group.setModifiedDate(dateTime);
        Group savedGroup = repository.save(group);
        return savedGroup;
    }

    @Override
    public Page<Group> findAll() {
        return repository.findAll(constructPageRequest(0, 100, "name", "ASC"));
    }

    @Override
    public void delete(String id) {
        repository.delete(id);
    }

    @Override
    public Page<Group> findAllPaginatedAndSorted(int page, int size, String sortBy, String sortOrder) {
        return repository.findAll(constructPageRequest(page, size, sortBy, sortOrder));
    }

    @Override
    public Page<Group> search(int page, int size, String sortBy, String sortOrder, Map<String, String[]> filters) {
        QueryBuilder query = addFilters(filters);
        return repository.search(query, constructPageRequest(page, size, sortBy, sortOrder));
    }

    @Override
    public void update(String id, Group resource) {
        Group group = findOne(id);
        if (group != null) {
            String dateTime = LocalDateTime.now().format(formatter).toString();
            resource.setModifiedDate(dateTime);
            final String doc = toJSON(resource);
            final UpdateRequest updateRequest = new UpdateRequest("prod_group", "group", id);
            updateRequest.doc(doc);
            BulkRequestBuilder bulkRequest = ElasticsearchConfiguration.getUnicastEsClient().prepareBulk();
            bulkRequest.add(updateRequest).execute().actionGet();
            bulkRequest.setRefresh(true);
            //repository.save(group);
        }
    }

    @Override
    public Group findOne(String id) {
        return repository.findOne(id);
    }

    @Override
    public boolean login(Map<String, String[]> filters) {
        QueryBuilder query = addFilters(filters);
        List<Group> groups = repository.search(query, constructPageRequest(0, 10, "firstName", SortOrder.ASC.toString())).getContent();
        if (CollectionUtils.isNotEmpty(groups) && groups.size() == 1) {
            return true;
        }
        return false;
    }
}
