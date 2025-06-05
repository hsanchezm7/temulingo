package es.um.pds.temulingo.dao.impl;

import es.um.pds.temulingo.dao.base.PreguntaDao;
import es.um.pds.temulingo.logic.Pregunta;

public class PreguntaDaoImpl extends DaoImpl<Pregunta> implements PreguntaDao {
	private static PreguntaDaoImpl instance = null;

	private PreguntaDaoImpl() {
		super();
	}

	public static PreguntaDaoImpl getInstance() {
		if (instance == null) {
			instance = new PreguntaDaoImpl();
		}

		return instance;
	}

}
