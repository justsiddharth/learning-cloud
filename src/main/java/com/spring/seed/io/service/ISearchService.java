package com.spring.seed.io.service;

import com.spring.seed.io.entity.Book;
import java.util.List;

public interface ISearchService {

    long count();

    List<String> searchNames(String query);

    List<Book> searchBook(String query);
}

