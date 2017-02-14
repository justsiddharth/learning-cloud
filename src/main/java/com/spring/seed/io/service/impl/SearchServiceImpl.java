package com.spring.seed.io.service.impl;

import com.spring.seed.io.entity.Book;
import com.spring.seed.io.entity.xml.GoodreadsBestBook;
import com.spring.seed.io.entity.xml.GoodreadsBook;
import com.spring.seed.io.entity.xml.GoodreadsResponse;
import com.spring.seed.io.repository.IEntityRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
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

    final String uri = "https://www.goodreads.com/search/index.xml?key=jhZOsAHmozaMo9GZKbDQg";

    @Autowired
    IEntityRepository repository;

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

    private String getBookReleaseDate(GoodreadsBook goodreadsBook) {
        int year = goodreadsBook.getPublication_year() <= 0 ? 1 : goodreadsBook.getPublication_year();
        int month = goodreadsBook.getPublication_month() <= 0 ? 1 : goodreadsBook.getPublication_month();
        int day = goodreadsBook.getPublication_day() <= 0 ? 1 : goodreadsBook.getPublication_day();
        String releasedOn = LocalDateTime.of(year, month, day, 0, 0).format(formatter).toString();
        return releasedOn;
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public List<String> searchNames(String query) {
        String fullUri = uri + "&q=" + query;
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
    public List<Book> searchBook(String query) {
        String fullUri = uri + "&q=" + query;
        ResponseEntity<String> result = getBookResponse(fullUri);
        GoodreadsResponse response = XMLToGoodReadResponse(result);
        return bookExtractor(response);
    }

    private List<Book> bookExtractor(GoodreadsResponse response) {
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
        Double score = ((rating * 100 + 99) / 100) * 100;
        return Math.round(score);
    }

    private List<String> nameExtractor(GoodreadsResponse response) {
        List<String> names = null;
        if (response != null) {
            List<GoodreadsBook> books = response.getSearch().getResults().getBooks();
            names = CollectionUtils.isNotEmpty(books) ? books.stream().map(book -> book.getBestBook().getTitle()).collect(Collectors.toList()) : Collections.EMPTY_LIST;
        }
        return names;
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


}
