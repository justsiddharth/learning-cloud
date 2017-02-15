package com.spring.seed.io.repository;

import com.spring.seed.io.entity.BookFeed;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IBookFeedRepository extends ElasticsearchRepository<BookFeed, String> {
}
