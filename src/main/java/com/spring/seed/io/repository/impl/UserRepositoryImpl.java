package com.spring.seed.io.repository.impl;

import com.spring.seed.io.entity.User;
import com.spring.seed.io.service.impl.OperationsHelper;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.update.UpdateRequest;

public abstract class UserRepositoryImpl extends OperationsHelper {
    
    public void update(String id, User resource) {
        final String doc = toJSON(resource);
        final UpdateRequest updateRequest = new UpdateRequest("prod_user", "user", id);
        updateRequest.doc(doc);
        BulkRequestBuilder bulkRequest = null;
        bulkRequest.add(updateRequest).execute().actionGet();
        bulkRequest.setRefresh(true);
    }
}
