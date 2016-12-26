package com.ntr1x.storage.core.resources;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ntr1x.storage.core.model.Resource;
import com.ntr1x.storage.core.repository.ResourceRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Path("resources")
@Api("Resources")
@Component
public class ResourceResource {
	
	@Inject
	private ResourceRepository repository;
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@RolesAllowed({ "res:///:admin" })
	public List<Resource> list(
    		@QueryParam("pattern") @ApiParam(example = "%") String pattern,
			@QueryParam("page") @ApiParam(example = "0") int page,
    		@QueryParam("size") @ApiParam(example = "10") int size
    ) {
		return (
			pattern == null
				? repository.findOrderByAlias(new PageRequest(page, size))
				: repository.findByAliasLikeOrderByAlias(pattern, new PageRequest(page, size))
		).getContent();
    }
	
	@GET
	@Path("/i/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@RolesAllowed({ "res:///:admin" })
	public Resource select(@PathParam("id") long id) {
		return repository.findOne(id);
    }
	
	@GET
	@Path("/a/{alias}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@RolesAllowed({ "res:///:admin" })
	public Resource select(@PathParam("alias") String alias) {
		return repository.findByAlias(alias);
    }
}