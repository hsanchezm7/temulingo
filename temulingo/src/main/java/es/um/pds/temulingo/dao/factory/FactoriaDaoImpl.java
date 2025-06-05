package es.um.pds.temulingo.dao.factory;

import es.um.pds.temulingo.dao.base.Dao;
import es.um.pds.temulingo.dao.impl.BloqueDaoImpl;
import es.um.pds.temulingo.dao.impl.CursoDaoImpl;
import es.um.pds.temulingo.dao.impl.PreguntaDaoImpl;
import es.um.pds.temulingo.dao.impl.ProgresoDaoImpl;
import es.um.pds.temulingo.dao.impl.UsuarioDaoImpl;
import es.um.pds.temulingo.logic.Bloque;
import es.um.pds.temulingo.logic.Curso;
import es.um.pds.temulingo.logic.Pregunta;
import es.um.pds.temulingo.logic.Progreso;
import es.um.pds.temulingo.logic.Usuario;

public class FactoriaDaoImpl extends FactoriaDao {
	@Override
	public Dao<Usuario> getUsuarioDao() {
		return UsuarioDaoImpl.getInstance();
	}

	@Override
	public Dao<Curso> getCursoDao() {
		return CursoDaoImpl.getInstance();
	}

	@Override
	public Dao<Bloque> getBloqueDao() {
		return BloqueDaoImpl.getInstance();
	}

	@Override
	public Dao<Pregunta> getPreguntaDao() {
		return PreguntaDaoImpl.getInstance();
	}

	@Override
	public Dao<Progreso> getProgresoDao() {
		return ProgresoDaoImpl.getInstance();
	}
}
