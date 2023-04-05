package com.procergs.apm.aluno;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AlunoService {
	
	List<Aluno> alunos = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		alunos.add(new Aluno("1", "Fulano de Tal", LocalDate.of(1982, 6, 17)));
		alunos.add(new Aluno("2", "Beltrano de Tal", LocalDate.of(1989, 4, 1)));
		alunos.add(new Aluno("3", "Aluno teste", LocalDate.of(1992, 1, 29)));
	}
	
	public List<Aluno> lista() {
		return alunos;
	}
	
	public Optional<Aluno> consultaPorId(String id) {
		return this.alunos.stream()
			.filter(a -> a.getId().equals(id))
			.findFirst();
	}
	
	public void exclui(String id) {
		alunos.removeIf(aluno -> aluno.getId().equals(id));
	}

	public Aluno inclui(Aluno aluno) {
		alunos.add(aluno);
		return aluno;
	}

	public void altera(Aluno aluno) {
		// TODOD
	}
	
}
