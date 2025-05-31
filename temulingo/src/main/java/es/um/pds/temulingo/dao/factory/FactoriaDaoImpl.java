package es.um.pds.temulingo.dao.factory;

import es.um.pds.temulingo.dao.base.Dao;
import es.um.pds.temulingo.dao.impl.BloqueDaoImpl;
import es.um.pds.temulingo.dao.impl.CursoDaoImpl;
import es.um.pds.temulingo.dao.impl.UsuarioDaoImpl;
import es.um.pds.temulingo.logic.Bloque;
import es.um.pds.temulingo.logic.Curso;
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
}
