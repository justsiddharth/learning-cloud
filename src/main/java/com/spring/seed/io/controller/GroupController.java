package com.spring.seed.io.controller;

import com.spring.seed.io.entity.Group;
import com.spring.seed.io.service.IGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.elasticsearch.common.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1/group")
@Api(value = "Group", description = "The Groups API")
public class GroupController {

    @Autowired
    IGroupService service;

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
    @ApiOperation(value = "Create Group", notes = "Create a new Group")
    public Group create(@Valid @RequestBody @ApiParam(value = "The Resource to be Created") final Group resource) {

        Preconditions.checkNotNull(resource, "Resource provided is null");
        Optional<String> id = Optional.ofNullable(resource.getId());
        Preconditions.checkArgument(id.isPresent() == true, "Resource should have no id.");
        Group group = service.create(resource);
        return group;
    }

    @RequestMapping(params = {}, method = RequestMethod.GET)
    @ApiOperation(value = "Find All", nickname = "findAll", notes = "Find All (max 50 results). " + "<br>This endpoint supports generic filtering. Examples: " + "<br><ul>"
            + "<li> match one field: `?field=value`" + "<li> match multiple fields: `?field1=value1&field2=value2`" + "<li> multiple values for field: `?field=value1&field=value2`" + "</ul>")
    public List<Group> findAll() {
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
    public Page<Group> findAllPaginatedAndSorted(@RequestParam("page") final int page,
            @RequestParam("size") final int size,
            @RequestParam("sortBy") final String sortBy,
            @RequestParam("sortOrder") final String sortOrder) {
        return service.findAllPaginatedAndSorted(page, size, sortBy, sortOrder);
    }

    @RequestMapping(value = "/_search", params = {"page", "size", "sortBy", "sortOrder"}, method = RequestMethod.GET)
    @ApiOperation(value = "search", nickname = "search", notes = "This endpoint supports generic filtering. Examples: " + "<br><ul>" + "<li> match one field: `?field=value`"
            + "<li> match multiple fields: `?field1=value1&field2=value2`" + "<li> multiple values for field: `?field=value1&field=value2`" + "</ul>")
    public Page<Group> search(@RequestParam("page") final int page,
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
            @Valid @RequestBody @ApiParam(value = "The Resource to be Updated") final Group resource) {
        Preconditions.checkNotNull(resource, "Resource provided is null");
        Optional<String> resourceId = Optional.ofNullable(resource.getId());
        Preconditions.checkArgument(resourceId.isPresent() == false, "Resource should have no id.");
        service.update(id, resource);
    }

    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    public Group findOne(@PathVariable("id") @ApiParam(value = "The Id of the Existing Resource to be Retrieved") final String id) {
        Group group = service.findOne(id);
        Preconditions.checkNotNull(group, "Group not found by id = " + id);
        return group;
    }
}
