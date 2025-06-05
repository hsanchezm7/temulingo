package es.um.pds.temulingo.dao.impl;

import es.um.pds.temulingo.dao.base.ProgresoDao;
import es.um.pds.temulingo.logic.Progreso;

public class ProgresoDaoImpl extends DaoImpl<Progreso> implements ProgresoDao {
	private static ProgresoDaoImpl instance = null;

	private ProgresoDaoImpl() {
		super();
	}

	public static ProgresoDaoImpl getInstance() {
		if (instance == null) {
			instance = new ProgresoDaoImpl();
		}

		return instance;
	}

}
