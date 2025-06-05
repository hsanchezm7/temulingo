package es.um.pds.temulingo.dao.factory;

import es.um.pds.temulingo.dao.base.Dao;
import es.um.pds.temulingo.logic.Bloque;
import es.um.pds.temulingo.logic.Curso;
import es.um.pds.temulingo.logic.Pregunta;
import es.um.pds.temulingo.logic.Progreso;
import es.um.pds.temulingo.logic.Usuario;

public abstract class FactoriaDao {
	public static FactoriaDao getDaoFactory() {
		return new FactoriaDaoImpl();
	}

	public abstract Dao<Usuario> getUsuarioDao();

	public abstract Dao<Curso> getCursoDao();

	public abstract Dao<Bloque> getBloqueDao();
	
	public abstract Dao<Pregunta> getPreguntaDao();

	public abstract Dao<Progreso> getProgresoDao();
}
