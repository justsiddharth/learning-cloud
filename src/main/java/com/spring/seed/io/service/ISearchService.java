package com.spring.seed.io.service;

import com.spring.seed.io.entity.Book;
import com.spring.seed.io.entity.BookName;
import java.util.List;

public interface ISearchService {

    List<BookName> searchNames(String query);

    List<Book> searchBooks(String query);

    Book findBook(String query);
}

