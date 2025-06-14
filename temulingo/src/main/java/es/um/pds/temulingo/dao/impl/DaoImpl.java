package es.um.pds.temulingo.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

import es.um.pds.temulingo.config.ConfiguracionTemulingo;
import es.um.pds.temulingo.dao.base.Dao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public abstract class DaoImpl<T> implements Dao<T> {

	private final EntityManagerFactory emf;

	private final Class<T> entityClass;

	private static final String persistenceUnitName = ConfiguracionTemulingo.PERSISTENCE_UNIT_NAME;

	protected DaoImpl() {
		emf = Persistence.createEntityManagerFactory(persistenceUnitName);

		entityClass = getEntityClass();
	}

	@Override
	public T save(T obj) {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			em.persist(obj);
			em.getTransaction().commit();
			return obj;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@Override
	public T edit(T obj) {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			T updatedObj = em.merge(obj);
			em.getTransaction().commit();
			return updatedObj;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@Override
	public Optional<T> get(long id) {
		EntityManager em = null;
		try {
			em = getEntityManager();
			T entity = em.find(entityClass, id);
			return Optional.ofNullable(entity);
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@Override
	public List<T> getAll() {
		EntityManager em = null;
		try {
			em = getEntityManager();
			CriteriaQuery<T> q = em.getCriteriaBuilder().createQuery(entityClass);
			Root<T> all = q.from(entityClass);
			q.select(all);
			return em.createQuery(q).getResultList();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@Override
	public void delete(T obj) {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			if (obj != null) {
				em.remove(obj);
			}
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public EntityManager getEntityManager() {
		return emf.createEntityManager();
	}

	@SuppressWarnings("unchecked")
	protected Class<T> getEntityClass() {
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		Class<T> entityClass = (Class<T>) type.getActualTypeArguments()[0];
		return entityClass;
	}
}
