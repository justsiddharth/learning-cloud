package com.spring.seed.io.service.impl;

import com.spring.seed.io.entity.Book;
import com.spring.seed.io.entity.xml.GoodreadsBook;
import com.spring.seed.io.entity.xml.GoodreadsResponse;
import com.spring.seed.io.repository.IEntityRepository;
import com.spring.seed.io.service.ISearchService;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

    final String uri = "https://www.goodreads.com/search/index.xml?key=jhZOsAHmozaMo9GZKbDQg";

    @Autowired
    IEntityRepository repository;

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
    public Book searchBook(String query) {
        return null;
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
