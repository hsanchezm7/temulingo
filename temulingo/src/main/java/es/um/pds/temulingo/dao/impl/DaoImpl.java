package es.um.pds.temulingo.dao.impl;

import es.um.pds.temulingo.dao.base.Dao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public abstract class DaoImpl<T> implements Dao<T> {

    private EntityManagerFactory emf;

    private final Class<T> entityClass;

    private static final String PUname = "temulingo-persistence-unit";

    protected DaoImpl() {
        emf = Persistence.createEntityManagerFactory(PUname);

        entityClass = getEntityClass();
    }

    @Override
    public void save(T obj) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void edit(T obj) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(obj);
            em.getTransaction().commit();
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

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    protected Class<T> getEntityClass() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class<T> entityClass = (Class<T>) type.getActualTypeArguments()[0];
        return entityClass;
    }
}
