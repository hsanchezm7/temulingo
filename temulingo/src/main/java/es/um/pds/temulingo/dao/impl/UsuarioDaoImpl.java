package es.um.pds.temulingo.dao.impl;

import es.um.pds.temulingo.dao.base.UsuarioDao;
import es.um.pds.temulingo.logic.Usuario;

public class UsuarioDaoImpl extends DaoImpl<Usuario> implements UsuarioDao {

	private static UsuarioDaoImpl instance = null;

	private UsuarioDaoImpl() {
		super();
	}

	public static UsuarioDaoImpl getInstance() {
		if (instance == null) {
			instance = new UsuarioDaoImpl();
		}

		return instance;
	}

}
