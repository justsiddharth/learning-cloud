package com.spring.seed.io.controller;

import com.spring.seed.io.entity.Book;
import com.spring.seed.io.service.ISearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1/search")
@Api(value = "User", description = "The Search API")
public class SearchController {

    @Autowired
    ISearchService service;

    @Autowired
    private HttpServletRequest request;

    /**
     * Counts all Resources in the system
     */
    @RequestMapping(method = RequestMethod.GET, value = "/count")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public long count() {
        return service.count();
    }

    @RequestMapping(value = "/names", method = RequestMethod.GET)
    public List<String> searchNames(@RequestParam("query") @ApiParam(value = "The query string") final String query) throws JAXBException, IOException {
        List<String> names = service.searchNames(query);
        if (CollectionUtils.isNotEmpty(names)) {
            return names;
        }
        return Collections.emptyList();
    }

    @RequestMapping(value = "/book", method = RequestMethod.GET)
    public List<Book> searchBook(@RequestParam("query") @ApiParam(value = "The query string") final String query) throws JAXBException, IOException {
        List<Book> result = service.searchBook(query);
        return result;
    }

}
