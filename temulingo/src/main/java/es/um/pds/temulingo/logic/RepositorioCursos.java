package es.um.pds.temulingo.logic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import es.um.pds.temulingo.dao.base.Dao;
import es.um.pds.temulingo.dao.factory.FactoriaDao;

public class RepositorioCursos {

	private final Map<Long, Curso> cursos = new HashMap<>();

	private static RepositorioCursos instancia;

	private final Dao<Curso> cursoDao;

	private RepositorioCursos() {
		this.cursoDao = FactoriaDao.getDaoFactory().getCursoDao();

		inicializarRepositorio();
	}

	public static RepositorioCursos getInstance() {
		if (instancia == null) {
			instancia = new RepositorioCursos();
		}

		return instancia;
	}

	private void inicializarRepositorio() {
		cursoDao.getAll().stream().forEach(curso -> cursos.put(curso.getId(), curso));
	}

	public List<Curso> obtenerTodosLosCursos() {
		return new LinkedList<>(cursos.values());
	}

	public void guardarCurso(Curso curso) {
		cursoDao.save(curso);
		cursos.put(curso.getId(), curso);
	}

}