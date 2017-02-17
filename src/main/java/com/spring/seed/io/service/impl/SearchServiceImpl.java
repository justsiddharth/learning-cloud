package com.spring.seed.io.service.impl;

import com.spring.seed.io.entity.Book;
import com.spring.seed.io.entity.BookName;
import com.spring.seed.io.entity.xml.GoodreadsAuthor;
import com.spring.seed.io.entity.xml.GoodreadsBestBook;
import com.spring.seed.io.entity.xml.GoodreadsBook;
import com.spring.seed.io.entity.xml.GoodreadsBookResponse;
import com.spring.seed.io.entity.xml.GoodreadsResponse;
import com.spring.seed.io.entity.xml.GoodreadsSingleBook;
import com.spring.seed.io.service.ISearchService;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SearchServiceImpl implements ISearchService {

    private static final List<String> DEFAULT_PARAMS_TO_REMOVE = Arrays.asList("page", "size", "sortBy", "sortOrder", "fields");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static JAXBContext jc = null;

    final String base_uri = "https://www.goodreads.com";
    final String search_uri = "/search/index.xml";
    final String find_one_uri = "/book/show.xml";
    final String key = "?key=jhZOsAHmozaMo9GZKbDQg";
    final String id_query = "&id=";
    final String q = "&q=";

    Function<GoodreadsBook, Book> xmlToBook = new Function<GoodreadsBook, Book>() {
        @Override
        public Book apply(GoodreadsBook goodreadsBook) {
            Book internalBook = new Book();
            GoodreadsBestBook bestBook = goodreadsBook.getBestBook();
            internalBook.setTitle(bestBook.getTitle());
            internalBook.setAuthorName(bestBook.getAuthor().getName());
            internalBook.setBookPoints(bookPointCalculator(goodreadsBook.getAverage_rating()));
            internalBook.setAverageRating(Double.parseDouble(goodreadsBook.getAverage_rating()));
            internalBook.setImageUrl(bestBook.getImage_url());
            internalBook.setSmallImageUrl(bestBook.getSmall_image_url());
            internalBook.setReleaseDate(getBookReleaseDate(goodreadsBook));
            return internalBook;
        }
    };

    Function<GoodreadsSingleBook, Book> xmlToSingleBook = new Function<GoodreadsSingleBook, Book>() {
        @Override
        public Book apply(GoodreadsSingleBook goodreadsBook) {
            Book internalBook = new Book();
            internalBook.setBookId(goodreadsBook.getId());
            internalBook.setTitle(goodreadsBook.getTitle());
            internalBook.setBookId(goodreadsBook.getId());
            internalBook.setDescription(goodreadsBook.getDescription());
            internalBook.setFormat(goodreadsBook.getFormat());
            internalBook.setIsbn(goodreadsBook.getIsbn());
            internalBook.setIsbn13(goodreadsBook.getIsbn13());
            internalBook.setPublisher(goodreadsBook.getPublisher());
            internalBook.setNum_pages(goodreadsBook.getNum_pages());
            internalBook.setAuthorName(getAuthorsFromXML(goodreadsBook.getAuthors().getAuthor()));
            internalBook.setBookPoints(bookPointCalculator(goodreadsBook.getAverage_rating()));
            internalBook.setAverageRating(Double.parseDouble(goodreadsBook.getAverage_rating()));
            internalBook.setImageUrl(goodreadsBook.getImage_url());
            internalBook.setSmallImageUrl(goodreadsBook.getSmall_image_url());
            internalBook.setReleaseDate(getSingleBookReleaseDate(goodreadsBook));
            return internalBook;
        }
    };
    Function<GoodreadsBook, BookName> xmlToBookName = new Function<GoodreadsBook, BookName>() {

        @Override
        public BookName apply(GoodreadsBook goodreadsBook) {
            BookName internalBook = new BookName();
            GoodreadsBestBook bestBook = goodreadsBook.getBestBook();
            internalBook.setTitle(bestBook.getTitle());
            internalBook.setGoodReadsId(bestBook.getId());
            return internalBook;
        }
    };

    private String getAuthorsFromXML(List<GoodreadsAuthor> authors) {
        return authors.stream().map(author -> author.getName()).collect(Collectors.joining(", "));
    }

    private String getBookReleaseDate(GoodreadsBook goodreadsBook) {
        int year = goodreadsBook.getPublication_year() <= 0 ? 1 : goodreadsBook.getPublication_year();
        int month = goodreadsBook.getPublication_month() <= 0 ? 1 : goodreadsBook.getPublication_month();
        int day = goodreadsBook.getPublication_day() <= 0 ? 1 : goodreadsBook.getPublication_day();
        String releasedOn = LocalDateTime.of(year, month, day, 0, 0).format(formatter).toString();
        return releasedOn;
    }

    private String getSingleBookReleaseDate(GoodreadsSingleBook goodreadsBook) {
        int year = goodreadsBook.getPublication_year() <= 0 ? 1 : goodreadsBook.getPublication_year();
        int month = goodreadsBook.getPublication_month() <= 0 ? 1 : goodreadsBook.getPublication_month();
        int day = goodreadsBook.getPublication_day() <= 0 ? 1 : goodreadsBook.getPublication_day();
        String releasedOn = LocalDateTime.of(year, month, day, 0, 0).format(formatter).toString();
        return releasedOn;
    }

    @Override
    public List<BookName> searchNames(String query) {
        String fullUri = base_uri + search_uri + key + q + query;
        ResponseEntity<String> result = getBookResponse(fullUri);
        GoodreadsResponse response = XMLToGoodReadResponse(result);
        return nameExtractor(response);
    }

    private ResponseEntity<String> getBookResponse(String fullUri) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> result = restTemplate.exchange(fullUri, HttpMethod.GET, entity, String.class);
        return result;
    }

    @Override
    public List<Book> searchBooks(String query) {
        String fullUri = base_uri + search_uri + key + q + query;
        ResponseEntity<String> result = getBookResponse(fullUri);
        GoodreadsResponse response = XMLToGoodReadResponse(result);
        return booksExtractor(response);
    }

    @Override
    public Book findBook(String query) {
        String fullUri = base_uri + find_one_uri + key + id_query + query;
        ResponseEntity<String> result = getBookResponse(fullUri);
        GoodreadsBookResponse response = XMLToGoodReadBookResponse(result);
        return oneBookExtractor(response);
    }

    private Book oneBookExtractor(GoodreadsBookResponse response) {
        Book book = new Book();
        if (response != null) {
            GoodreadsSingleBook singleBook = response.getSingleBook();
            book = singleBook != null ? xmlToSingleBook.apply(singleBook) : new Book();
        }
        return book;
    }

    private List<Book> booksExtractor(GoodreadsResponse response) {
        List<Book> bookList = new ArrayList<>();
        if (response != null) {
            List<GoodreadsBook> books = response.getSearch().getResults().getBooks();
            bookList = CollectionUtils.isNotEmpty(books) ? books.stream().map(xmlToBook).collect(Collectors.<Book>toList()) : Collections.emptyList();
            return bookList;
        }
        return Collections.emptyList();
    }

    private double bookPointCalculator(String average_rating) {
        Double rating = Double.parseDouble(average_rating);
        Double score = ((rating * 100 + 99) / 100);
        return score.intValue() * 100;
    }

    private List<BookName> nameExtractor(GoodreadsResponse response) {
        List<BookName> bookNames = null;
        if (response != null) {
            List<GoodreadsBook> books = response.getSearch().getResults().getBooks();
            bookNames = CollectionUtils.isNotEmpty(books) ? books.stream().map(xmlToBookName).collect(Collectors.<BookName>toList()) : Collections.emptyList();
            return bookNames;
        }
        return Collections.emptyList();
    }

    private GoodreadsResponse XMLToGoodReadResponse(ResponseEntity<String> result) {
        JAXBContext jc = null;
        GoodreadsResponse resp = null;
        try {
            jc = JAXBContext.newInstance(GoodreadsResponse.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            resp = (GoodreadsResponse) unmarshaller.unmarshal(new StringReader(result.getBody()));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return resp;
    }

    private GoodreadsBookResponse XMLToGoodReadBookResponse(ResponseEntity<String> result) {
        GoodreadsBookResponse resp = null;
        try {
            jc = JAXBContext.newInstance(GoodreadsBookResponse.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            resp = (GoodreadsBookResponse) unmarshaller.unmarshal(new StringReader(result.getBody()));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return resp;
    }
}
