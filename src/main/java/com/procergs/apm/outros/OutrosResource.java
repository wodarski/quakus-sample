package com.procergs.apm.outros;

import java.util.Objects;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("me")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
public class OutrosResource {
	
    @Inject
    JsonWebToken jwt; 
	
	@GET
	@PermitAll
	@Operation(summary = "Mostra alguns dados do usuário autenticado")
	public JsonObject me() {

		if (Objects.isNull(jwt.getSubject())) {
			return Json.createObjectBuilder()
				.add("sub", "Nenhum usuário autenticado")
				.build();
		}
		
		return Json.createObjectBuilder()
			.add("sub", jwt.getSubject())
			.add("nome", jwt.getClaim("name").toString())
			.add("organização", jwt.getClaim("soe:organizacao").toString())
			.build();
	}

}
