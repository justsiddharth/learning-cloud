package com.spring.seed.io.repository.impl;

import com.spring.seed.io.entity.Group;
import com.spring.seed.io.service.impl.OperationsHelper;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.update.UpdateRequest;

public abstract class GroupRepositoryImpl extends OperationsHelper {

    public void update(String id, Group resource) {
        final String doc = toJSON(resource);
        final UpdateRequest updateRequest = new UpdateRequest("prod_group", "group", id);
        updateRequest.doc(doc);
        BulkRequestBuilder bulkRequest = null;
        bulkRequest.add(updateRequest).execute().actionGet();
        bulkRequest.setRefresh(true);
    }
}
