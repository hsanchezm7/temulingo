package es.um.pds.temulingo.dao.jpa;

import es.um.pds.temulingo.dao.base.UsuarioDao;
import es.um.pds.temulingo.logic.Usuario;

public class JpaUsuarioDao extends JpaDao<Usuario> implements UsuarioDao {

    private static JpaUsuarioDao instance = null;

    private JpaUsuarioDao() {
        super(Usuario.class);
    }

    public static JpaUsuarioDao getInstance() {
        if (instance == null) {
            instance = new JpaUsuarioDao();
        }

        return instance;
    }

}
