package es.um.pds.temulingo;

import es.um.pds.temulingo.controlador.ControladorTemulingo;
import es.um.pds.temulingo.dao.base.Dao;
import es.um.pds.temulingo.dao.factory.FactoriaDao;
import es.um.pds.temulingo.logic.Bloque;
import es.um.pds.temulingo.logic.Curso;
import es.um.pds.temulingo.logic.Usuario;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class App {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello World!");

        H2EmbeddedServer.start();
        FactoriaDao factoriaDao = FactoriaDao.getDaoFactory();
        Dao<Usuario> jpaUsuarioDao = factoriaDao.getJpaUsuarioDao();
        Dao<Curso> jpaCursoDao = factoriaDao.getJpaCursoDao();
        Dao<Bloque> jpaBloqueDao = factoriaDao.getJpaBloqueDao();

        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario");
        usuario.setEmail("olha@gmail.com");
        usuario.setFechaNacim(LocalDate.now());

        Usuario usuario2 = new Usuario();
        usuario2.setNombre("Usuario2");
        usuario2.setEmail("olha2@gmail.com");
        usuario2.setFechaNacim(LocalDate.now());

        List<Bloque> bloques = new LinkedList<>();
        Curso curso = new Curso();
        curso.setTitulo("Curso");
        curso.setDescripcion("Curso");
        curso.setFechaCreacion(LocalDate.now());
        curso.setBloques(bloques);

        jpaCursoDao.save(curso);

        Bloque bloque = new Bloque();
        bloque.setNombre("nombre");
        bloque.setDescripcion("esto es un bloque");
        bloque.setTipo(Bloque.TipoEjercicio.TRADUCCION);
        bloque.setCurso(curso);

        jpaBloqueDao.save(bloque);

        bloques.add(bloque);

        jpaUsuarioDao.save(usuario);
        jpaUsuarioDao.save(usuario2);

        curso.setBloques(bloques);
        jpaCursoDao.edit(curso);

        ControladorTemulingo controlador = ControladorTemulingo.getInstance();

        Thread.sleep(10000000);
    }
}
