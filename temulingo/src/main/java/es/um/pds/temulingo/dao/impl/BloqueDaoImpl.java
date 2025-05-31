package es.um.pds.temulingo.dao.impl;

import es.um.pds.temulingo.dao.base.BloqueDao;
import es.um.pds.temulingo.logic.Bloque;

public class BloqueDaoImpl extends DaoImpl<Bloque> implements BloqueDao {
	private static BloqueDaoImpl instance = null;

	private BloqueDaoImpl() {
		super();
	}

	public static BloqueDaoImpl getInstance() {
		if (instance == null) {
			instance = new BloqueDaoImpl();
		}

		return instance;
	}

}
