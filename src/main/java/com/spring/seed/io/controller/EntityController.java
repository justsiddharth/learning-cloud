package com.spring.seed.io.controller;

import com.spring.seed.io.entity.User;
import com.spring.seed.io.entity.xml.GoodreadsResponse;
import com.spring.seed.io.service.IEntityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.elasticsearch.common.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "api/v1/entity")
@Api(value = "User", description = "The User API")
public class EntityController {

    private static final int PRETTY_PRINT_INDENT_FACTOR = 4;
    final String uri = "https://www.goodreads.com/search/index.xml?key=jhZOsAHmozaMo9GZKbDQg";
    @Autowired
    IEntityService service;
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

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create User", notes = "Create a new User")
    public User create(@Valid @RequestBody @ApiParam(value = "The Resource to be Created") final User resource) {

        Preconditions.checkNotNull(resource, "Resource provided is null");
        Optional<String> id = Optional.ofNullable(resource.getId());
        Preconditions.checkArgument(id.isPresent() == false, "Resource should have no id.");
        User crusher = service.create(resource);
        return crusher;
    }

    @RequestMapping(params = {}, method = RequestMethod.GET)
    @ApiOperation(value = "Find All", nickname = "findAll", notes = "Find All (max 50 results). " + "<br>This endpoint supports generic filtering. Examples: " + "<br><ul>"
            + "<li> match one field: `?field=value`" + "<li> match multiple fields: `?field1=value1&field2=value2`" + "<li> multiple values for field: `?field=value1&field=value2`" + "</ul>")
    public List<User> findAll() {
        return service.findAll().getContent();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete Resource", notes = "Delete the Resource by Id")
    public void delete(@PathVariable("id") @ApiParam(value = "The Id of the Resource to be Deleted") final String id) {
        service.delete(id);
    }

    @RequestMapping(params = {"page", "size", "sortBy", "sortOrder"}, method = RequestMethod.GET)
    @ApiOperation(value = "Find All Paginated And Sorted", nickname = "findAllPaginatedAndSorted", notes = "This endpoint supports generic filtering. Examples: " + "<br><ul>"
            + "<li> match one field: `?field=value`" + "<li> match multiple fields: `?field1=value1&field2=value2`" + "<li> multiple values for field: `?field=value1&field=value2`"
            + "<li> date time range filters: `?dateTimeFilter=field,fromDate,toDate`" + "</ul>")
    public Page<User> findAllPaginatedAndSorted(@RequestParam("page") final int page,
            @RequestParam("size") final int size,
            @RequestParam("sortBy") final String sortBy,
            @RequestParam("sortOrder") final String sortOrder) {
        return service.findAllPaginatedAndSorted(page, size, sortBy, sortOrder);
    }

    @RequestMapping(value = "/_search", params = {"page", "size", "sortBy", "sortOrder"}, method = RequestMethod.GET)
    @ApiOperation(value = "search", nickname = "search", notes = "This endpoint supports generic filtering. Examples: " + "<br><ul>" + "<li> match one field: `?field=value`"
            + "<li> match multiple fields: `?field1=value1&field2=value2`" + "<li> multiple values for field: `?field=value1&field=value2`" + "</ul>")
    public Page<User> search(@RequestParam("page") final int page,
            @RequestParam("size") final int size,
            @RequestParam("sortBy") final String sortBy,
            @RequestParam("sortOrder") final String sortOrder) {
        Map<String, String[]> filters = new HashMap<>(request.getParameterMap());
        return service.search(page, size, sortBy, sortOrder, filters);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update Resource", notes = "Update the existing Resource")
    public void update(@PathVariable("id") @ApiParam(value = "The Id of the Existing Resource to be Updated") final String id,
            @Valid @RequestBody @ApiParam(value = "The Resource to be Updated") final User resource) {
        Preconditions.checkNotNull(resource, "Resource provided is null");
        Optional<String> resourceId = Optional.ofNullable(resource.getId());
        Preconditions.checkArgument(resourceId.isPresent() == false, "Resource should have no id.");
        service.update(id, resource);
    }

    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    public User findOne(@PathVariable("id") @ApiParam(value = "The Id of the Existing Resource to be Retrieved") final String id) {
        User crusher = service.findOne(id);
        Preconditions.checkNotNull(crusher, "User not found by id = " + id);
        return crusher;
    }

    @RequestMapping(value = "/convert", method = RequestMethod.GET)
    public String converter(@RequestParam("xmlString") @ApiParam(value = "The xml string to convert to json string") final String xmlString) throws JAXBException, IOException {
        //        JSONObject xmlJSONObj = XML.toJSONObject(xmlString);
        //        String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        String newUri = uri + "&q=harry";
        ResponseEntity<String> result = restTemplate.exchange(newUri, HttpMethod.GET, entity, String.class);

        //InputStream stream = new ByteArrayInputStream(result.getBody());
        JAXBContext jc = JAXBContext.newInstance(GoodreadsResponse.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        GoodreadsResponse response = (GoodreadsResponse) unmarshaller.unmarshal(new StringReader(result.getBody()));
System.out.println(response.getSearch().getResults().getBooks().get(0).getBestBook().getTitle());
        System.out.println(response.getSearch().getResults().getBooks().get(0).getBestBook().getAuthor());
        return "asdasd";
    }

}
