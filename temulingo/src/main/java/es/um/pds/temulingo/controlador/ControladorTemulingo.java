package es.um.pds.temulingo.controlador;


import es.um.pds.temulingo.dao.base.Dao;
import es.um.pds.temulingo.dao.factory.FactoriaDao;
import es.um.pds.temulingo.logic.Bloque;
import es.um.pds.temulingo.logic.Curso;
import es.um.pds.temulingo.logic.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorTemulingo {

    private static ControladorTemulingo instance = null;

    private Usuario usuarioActual;

    private FactoriaDao factoriaDao;
    private Dao<Usuario> usuarioDao;
    private Dao<Curso> cursoDao;
    private Dao<Bloque> bloqueDao;

    private HashMap<Long, Usuario> usuarios;

    private List<Curso> cursos;

    private ControladorTemulingo() {
        inicializarAdaptadores();
        cargarUsuarios();
        cargarCursos();

        this.usuarioActual = null;
    }

    public static ControladorTemulingo getInstance() {
        if (instance == null) {
            instance = new ControladorTemulingo();
        }

        return instance;
    }

    private void cargarCursos() {
        this.cursos = new ArrayList<>(cursoDao.getAll());
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public Map<Long, Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Map<Long, Usuario> usuarios) {
        this.usuarios = (HashMap<Long, Usuario>) usuarios;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }

    private void inicializarAdaptadores() {
        factoriaDao = FactoriaDao.getDaoFactory();

        usuarioDao = factoriaDao.getUsuarioDao();
        cursoDao = factoriaDao.getCursoDao();
        bloqueDao = factoriaDao.getBloqueDao();

    }

    private void cargarUsuarios() {
        List<Usuario> usuariosBD = usuarioDao.getAll();

        this.usuarios = new HashMap<>();

        for (Usuario usuario : usuariosBD) {
            this.usuarios.put(usuario.getId(), usuario);
        }
    }

    public void iniciarSesion(String nombre, String email) {
        
    }

}
