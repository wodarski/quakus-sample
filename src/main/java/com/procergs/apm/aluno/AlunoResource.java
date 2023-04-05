package com.procergs.apm.aluno;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("/alunos")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AlunoResource {
	
	@Inject
	EntityManager em;

	@Inject
	AlunoService alunoService;

	@GET
	@PermitAll
	@Operation(summary = "Lista todos os alunos")
	public List<Aluno> lista() {
		return alunoService.lista();
	}
	
	@GET
	@Path("total")
	@PermitAll
	public BigDecimal total() {
		Query query = em.createNativeQuery("SELECT COUNT(1) FROM APM_ALUNO");
		return (BigDecimal) query.getSingleResult();
	}

	@GET
	@Path("{id}")
	@RolesAllowed("APM:ALUNO:CONSULTAR")
	@Operation(summary = "Consulta um aluno pelo seu ID")
	public Aluno consultaPorId(@PathParam("id") String id) {
		Optional<Aluno> aluno = alunoService.consultaPorId(id);
		return aluno.orElseThrow(() -> new WebApplicationException("Aluno não encontrado", 404));
	}

	@POST
	@RolesAllowed("APM:ALUNO:NOVO")
	@Operation(summary = "Inclui um novo aluno")
	public Response inclui(Aluno aluno, @Context UriInfo uriInfo) {
		aluno = alunoService.inclui(aluno);
		return Response.created(URI.create("/alunos/" + aluno.getId())).build();
	}

	@PUT
	@Path("{id}")
	@RolesAllowed("APM:ALUNO:EDITAR")
	@Operation(summary = "Altera um aluno. Deve-se informar todos os atributos do Aluno")
	public void altera(@PathParam("id") String id, Aluno aluno) {
		alunoService.altera(aluno);
	}
	
	@PATCH
	@Path("{id}")
	@RolesAllowed("APM:ALUNO:EDITAR")
	@Operation(summary = "Altera apenas os atributos informados no Aluno")
	public void alteraParcial(@PathParam("id") String id, Aluno aluno) {
		alunoService.altera(aluno);
	}

	@DELETE
	@Path("{id}")
	@RolesAllowed("APM:ALUNO:EXCLUIR")
	@Operation(summary = "Exclui um aluno pelo seu ID")
	public void exclui(@PathParam("id") String id) {
		alunoService.exclui(id);
	}

}