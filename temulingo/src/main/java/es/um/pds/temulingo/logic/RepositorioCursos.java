package es.um.pds.temulingo.logic;

import es.um.pds.temulingo.dao.base.Dao;
import es.um.pds.temulingo.dao.factory.FactoriaDao;

import java.util.*;

public class RepositorioCursos {

    private final Map<Long, Curso> cursos = new HashMap<>();

    private static RepositorioCursos instancia;

    private final Dao<Curso> cursoDao;

    // private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

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
        cursoDao.getAll().stream()
            .forEach(curso -> cursos.put(curso.getId(), curso));
    }
    
    public void guardarCurso(Curso curso) {
    	cursoDao.save(curso);
    	cursos.put(curso.getId(), curso);
    }
    
}