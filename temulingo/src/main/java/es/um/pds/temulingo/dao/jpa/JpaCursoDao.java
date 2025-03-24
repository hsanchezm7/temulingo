package es.um.pds.temulingo.dao.jpa;

import es.um.pds.temulingo.dao.base.CursoDao;
import es.um.pds.temulingo.logic.Curso;

public class JpaCursoDao extends JpaDao<Curso> implements CursoDao {

    private static JpaCursoDao instance = null;

    private JpaCursoDao() {
        super(Curso.class);
    }

    public static JpaCursoDao getInstance() {
        if (instance == null) {
            instance = new JpaCursoDao();
        }

        return instance;
    }
}
