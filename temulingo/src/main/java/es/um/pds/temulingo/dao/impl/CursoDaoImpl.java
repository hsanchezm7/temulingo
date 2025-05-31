package es.um.pds.temulingo.dao.impl;

import es.um.pds.temulingo.dao.base.CursoDao;
import es.um.pds.temulingo.logic.Curso;

public class CursoDaoImpl extends DaoImpl<Curso> implements CursoDao {

	private static CursoDaoImpl instance = null;

	private CursoDaoImpl() {
		super();
	}

	public static CursoDaoImpl getInstance() {
		if (instance == null) {
			instance = new CursoDaoImpl();
		}

		return instance;
	}
}
