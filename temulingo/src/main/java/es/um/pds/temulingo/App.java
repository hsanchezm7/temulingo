package es.um.pds.temulingo;

import java.time.LocalDate;
import java.util.List;

import es.um.pds.temulingo.dao.base.Dao;
import es.um.pds.temulingo.dao.factory.FactoriaDao;
import es.um.pds.temulingo.logic.Curso;
import es.um.pds.temulingo.logic.Usuario;

public class App {
	
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello World!");
        
        H2EmbeddedServer.start();
        FactoriaDao factoriaDao = FactoriaDao.getDaoFactory();
        Dao<Usuario> jpaUsuarioDao = factoriaDao.getJpaUsuarioDao();
        Dao<Curso> jpaCursoDao = factoriaDao.getJpaCursoDao();

        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario");
        usuario.setEmail("olha@gmail.com");
        usuario.setFechaNacim(LocalDate.now());

        Usuario usuario2 = new Usuario();
        usuario2.setNombre("Usuario2");
        usuario2.setEmail("olha2@gmail.com");
        usuario2.setFechaNacim(LocalDate.now());

        Curso curso = new Curso();
        curso.setTitulo("Curso");
        curso.setDescripcion("Curso");
        curso.setFechaCreacion(LocalDate.now());

        Curso curso2 = new Curso();
        curso2.setTitulo("Curso2");
        curso2.setDescripcion("Curso2");
        curso2.setFechaCreacion(LocalDate.now());

        jpaUsuarioDao.save(usuario);
        jpaUsuarioDao.save(usuario2);
        jpaCursoDao.save(curso);
        jpaCursoDao.save(curso2);

        List<Usuario> todos = jpaUsuarioDao.getAll();

        Thread.sleep(10000000);
    }
}
