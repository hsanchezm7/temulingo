package es.um.pds.temulingo.dao.jpa;

import es.um.pds.temulingo.dao.base.Dao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.Optional;

public abstract class JpaDao<T> implements Dao<T> {

    private EntityManagerFactory emf = null;

    private final Class<T> entityClass;

    private static final String PUname = "temulingo-persistence-unit";

    protected JpaDao(Class<T> entityClass) {
        emf = Persistence.createEntityManagerFactory(PUname);
        this.entityClass = entityClass;
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
    public Optional<T> get(long id) {
        EntityManager em = null;
        try {
            em = getEntityManager(); // Obtener el EntityManager
            T entity = em.find(entityClass, id); // Buscar la entidad por su ID
            return Optional.ofNullable(entity); // Si es null, retorna Optional.empty()
        } finally {
            if (em != null) {
                em.close(); // Cerrar el EntityManager
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
}
