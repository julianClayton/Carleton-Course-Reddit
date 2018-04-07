package com.carleton.ccr.resources;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;


@Path("/ccr")
public class CCR {

	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	private String name;

	public CCR() {
		name = "Carleton reddit courses";
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String ccr() {
		return "<html><head><title>COMP 4601</title></head><body><h1>"+ name +"</h1></body></html>";
	}
}
