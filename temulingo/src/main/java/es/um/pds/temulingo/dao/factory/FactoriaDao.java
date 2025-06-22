package es.um.pds.temulingo.dao.factory;

import es.um.pds.temulingo.dao.base.Dao;
import es.um.pds.temulingo.logic.Bloque;
import es.um.pds.temulingo.logic.Curso;
import es.um.pds.temulingo.logic.Pregunta;
import es.um.pds.temulingo.logic.Progreso;
import es.um.pds.temulingo.logic.Usuario;

/**
 * Fábrica abstracta para obtener instancias de DAOs específicos del dominio.
 * <p>
 * Esta clase define los métodos de acceso para cada entidad persistente de la
 * aplicación, permitiendo una implementación desacoplada y flexible del patrón
 * DAO.
 * </p>
 *
 * <p>
 * Utiliza el patrón Factory para proporcionar una única implementación
 * concreta, facilitando la sustitución o ampliación futura de la lógica de
 * persistencia.
 * </p>
 */
public abstract class FactoriaDao {

	/**
	 * Devuelve la implementación concreta de la fábrica DAO.
	 *
	 * @return instancia de {@link FactoriaDao}
	 */
	public static FactoriaDao getDaoFactory() {
		return new FactoriaDaoImpl();
	}

	/**
	 * Devuelve el DAO para la entidad {@link Usuario}.
	 *
	 * @return DAO de Usuario
	 */
	public abstract Dao<Usuario> getUsuarioDao();

	/**
	 * Devuelve el DAO para la entidad {@link Curso}.
	 *
	 * @return DAO de Curso
	 */
	public abstract Dao<Curso> getCursoDao();

	/**
	 * Devuelve el DAO para la entidad {@link Bloque}.
	 *
	 * @return DAO de Bloque
	 */
	public abstract Dao<Bloque> getBloqueDao();

	/**
	 * Devuelve el DAO para la entidad {@link Pregunta}.
	 *
	 * @return DAO de Pregunta
	 */
	public abstract Dao<Pregunta> getPreguntaDao();

	/**
	 * Devuelve el DAO para la entidad {@link Progreso}.
	 *
	 * @return DAO de Progreso
	 */
	public abstract Dao<Progreso> getProgresoDao();
}
