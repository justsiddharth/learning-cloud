package com.spring.seed.io.service.impl;

import com.spring.seed.io.configuration.ElasticsearchConfiguration;
import com.spring.seed.io.entity.User;
import com.spring.seed.io.repository.IUserRepository;
import com.spring.seed.io.service.IUserService;
import java.time.LocalDateTime;
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
public class UserServiceImpl extends OperationsHelper implements IUserService {

    @Autowired
    IUserRepository repository;

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public User create(User user) {
        String id = UUID.randomUUID().toString();
        user.setId(id);
        String dateTime = LocalDateTime.now().format(formatter).toString();
        user.setCreatedDate(dateTime);
        user.setModifiedDate(dateTime);
        user.setScore(0L);
        User savedUser = repository.save(user);
        return savedUser;
    }

    @Override
    public Page<User> findAll() {
        return repository.findAll(constructPageRequest(0, 100, "username", "ASC"));
    }

    @Override
    public void delete(String id) {
        repository.delete(id);
    }

    @Override
    public Page<User> findAllPaginatedAndSorted(int page, int size, String sortBy, String sortOrder) {
        return repository.findAll(constructPageRequest(page, size, sortBy, sortOrder));
    }

    @Override
    public Page<User> search(int page, int size, String sortBy, String sortOrder, Map<String, String[]> filters) {
        QueryBuilder query = addFilters(filters);
        return repository.search(query, constructPageRequest(page, size, sortBy, sortOrder));
    }

    @Override
    public void update(String id, User resource) {
        User user = findOne(id);
        if (user != null) {
            String dateTime = LocalDateTime.now().format(formatter).toString();
            resource.setModifiedDate(dateTime);
            final String doc = toJSON(resource);
            final UpdateRequest updateRequest = new UpdateRequest("prod_user", "user", id);
            updateRequest.doc(doc);
            BulkRequestBuilder bulkRequest = ElasticsearchConfiguration.getUnicastEsClient().prepareBulk();
            bulkRequest.add(updateRequest).execute().actionGet();
            bulkRequest.setRefresh(true);
            //repository.update(id, resource);
        }
    }

    @Override
    public User findOne(String id) {
        return repository.findOne(id);
    }

    @Override
    public User login(Map<String, String[]> filters) {
        QueryBuilder query = addFilters(filters);
        List<User> users = repository.search(query, constructPageRequest(0, 10, "name", SortOrder.ASC.toString())).getContent();
        if (CollectionUtils.isNotEmpty(users) && users.size() == 1) {
            return users.get(0);
        } else {
            return null;
        }
    }
}
