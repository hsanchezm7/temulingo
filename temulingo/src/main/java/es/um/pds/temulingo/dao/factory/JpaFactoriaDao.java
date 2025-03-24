package es.um.pds.temulingo.dao.factory;

import es.um.pds.temulingo.dao.base.Dao;
import es.um.pds.temulingo.dao.jpa.JpaCursoDao;
import es.um.pds.temulingo.dao.jpa.JpaUsuarioDao;
import es.um.pds.temulingo.logic.Curso;
import es.um.pds.temulingo.logic.Usuario;

public class JpaFactoriaDao extends FactoriaDao {
    @Override
    public Dao<Usuario> getJpaUsuarioDao() {
        return JpaUsuarioDao.getInstance();
    }

    @Override
    public Dao<Curso> getJpaCursoDao() {
        return JpaCursoDao.getInstance();
    }
}
