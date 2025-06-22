package es.um.pds.temulingo.controlador;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import es.um.pds.temulingo.dao.base.Dao;
import es.um.pds.temulingo.dao.factory.FactoriaDao;
import es.um.pds.temulingo.exception.ExcepcionCredencialesInvalidas;
import es.um.pds.temulingo.exception.ExcepcionRegistroInvalido;
import es.um.pds.temulingo.logic.Curso;
import es.um.pds.temulingo.logic.Progreso;
import es.um.pds.temulingo.logic.RepositorioCursos;
import es.um.pds.temulingo.logic.RepositorioUsuarios;
import es.um.pds.temulingo.logic.Usuario;
import es.um.pds.temulingo.utils.PasswordUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para el controlador de Temulingo")
class ControladorTemulingoTest {

	@Mock
	private RepositorioUsuarios mockRepoUsuarios;

	@Mock
	private RepositorioCursos mockRepoCursos;

	@Mock
	private FactoriaDao mockFactoriaDao;

	@Mock
	private Dao<Progreso> mockProgresoDao;

	@Mock
	private Dao<Curso> mockCursoDao;

	@Mock
	private Usuario mockUsuario;

	private ControladorTemulingo controlador;

	@BeforeEach
	void setUp() throws Exception {
		reiniciarSingleton();

		// Inicializar mocks
		try (MockedStatic<RepositorioUsuarios> repoUsuariosMock = mockStatic(RepositorioUsuarios.class);
				MockedStatic<RepositorioCursos> repoCursosMock = mockStatic(RepositorioCursos.class);
				MockedStatic<FactoriaDao> factoriaDaoMock = mockStatic(FactoriaDao.class)) {

			repoUsuariosMock.when(RepositorioUsuarios::getInstance).thenReturn(mockRepoUsuarios);
			repoCursosMock.when(RepositorioCursos::getInstance).thenReturn(mockRepoCursos);
			factoriaDaoMock.when(FactoriaDao::getDaoFactory).thenReturn(mockFactoriaDao);

			when(mockFactoriaDao.getProgresoDao()).thenReturn(mockProgresoDao);
			when(mockFactoriaDao.getCursoDao()).thenReturn(mockCursoDao);

			controlador = ControladorTemulingo.getInstance();
		}
	}

	private void reiniciarSingleton() throws Exception {
		Field campoInstancia = ControladorTemulingo.class.getDeclaredField("instance");
		campoInstancia.setAccessible(true);
		campoInstancia.set(null, null);
	}

	// Singleton
	@DisplayName("Debería retornar la misma instancia del singleton")
	@Test
	void getInstanceMismoObjeto() {
		ControladorTemulingo instancia1 = ControladorTemulingo.getInstance();
		ControladorTemulingo instancia2 = ControladorTemulingo.getInstance();

		assertSame(instancia1, instancia2, "getInstance() debe devolver la misma instancia");
	}

	/**
	 * Test que verifica el estado inicial del controlador. El usuario actual debe
	 * ser null cuando el controlador se inicializa.
	 */
	@DisplayName("Usuario actual debería ser null inicialmente")
	@Test
	void getUsuarioInicialmenteNull() {
		assertNull(controlador.getUsuarioActual(), "El usuario actual debe ser null inicialmente");
	}

	/**
	 * Test que verifica el estado inicial del curso actual. El curso actual debe
	 * ser null cuando el controlador se inicializa.
	 */
	@Test
	@DisplayName("Curso actual debería ser null inicialmente")
	void getCursoInicialmenteNull() {
		assertNull(controlador.getCursoActual(), "El curso actual debe ser null inicialmente");
	}

	/**
	 * Test que verifica que el setter del usuario actual funciona correctamente.
	 * Debe asignar el usuario correctamente y poder recuperarlo con el getter.
	 */
	@Test
	@DisplayName("Debe asignar correctamente el usuario actual")
	void setUsuarioActualAsignaCorrectamente() {
		controlador.setUsuarioActual(mockUsuario);

		assertEquals(mockUsuario, controlador.getUsuarioActual(), "El usuario actual debe ser el que se asignó");
	}

	// Autenticación
	/**
	 * Test de caso exitoso de registro de usuario. Verifica que cuando se
	 * proporcionan datos válidos y únicos, el usuario se registra correctamente en
	 * el repositorio.
	 */
	@Test
	@DisplayName("Debe registrar exitosamente un usuario con datos válidos")
	void registroExitoso() throws NullPointerException, ExcepcionRegistroInvalido {
		String nombre = "Juan Pérez";
		String email = "juanp@temulingotest.es";
		String username = "juanp";
		String password = "password123";
		LocalDate fecha = LocalDate.of(1990, 5, 15);

		when(mockRepoUsuarios.obtenerPorEmail(email)).thenReturn(Optional.empty());
		when(mockRepoUsuarios.obtenerPorUsername(username)).thenReturn(Optional.empty());

		boolean resultado = controlador.registrarUsuario(nombre, email, username, password, fecha);

		assertTrue(resultado, "El registro debe ser exitoso");
		verify(mockRepoUsuarios).guardarUsuario(nombre, email, username, password, fecha);
	}

	/**
	 * Test que verifica el manejo de emails duplicados en el registro. Debe lanzar
	 * ExcepcionRegistroInvalido cuando se intenta registrar un usuario con un email
	 * que ya existe en el sistema.
	 */
	@Test
	@DisplayName("Debe lanzar excepción cuando el email ya existe")
	void registroEmailExiste() {
		String email = "juanp@test.com";
		when(mockRepoUsuarios.obtenerPorEmail(email)).thenReturn(Optional.of(mockUsuario));

		ExcepcionRegistroInvalido excepcion = assertThrows(ExcepcionRegistroInvalido.class,
				() -> controlador.registrarUsuario("Juan", email, "juanp", "pass", LocalDate.now()));

		assertEquals("Ya existe un usuario con el email: " + email, excepcion.getMessage());
		verify(mockRepoUsuarios, never()).guardarUsuario(any(), any(), any(), any(), any());
	}

	/**
	 * Test que verifica el manejo de usernames duplicados en el registro. Debe
	 * lanzar ExcepcionRegistroInvalido cuando se intenta registrar un usuario con
	 * un username que ya existe en el sistema.
	 */
	@Test
	@DisplayName("Debe lanzar excepción cuando el username ya existe")
	void registroUsernameExiste() {
		String username = "juanp";
		when(mockRepoUsuarios.obtenerPorEmail(any())).thenReturn(Optional.empty());
		when(mockRepoUsuarios.obtenerPorUsername(username)).thenReturn(Optional.of(mockUsuario));

		ExcepcionRegistroInvalido excepcion = assertThrows(ExcepcionRegistroInvalido.class,
				() -> controlador.registrarUsuario("Juan", "juan@test.com", username, "pass", LocalDate.now()));

		assertEquals("Ya existe un usuario con el nombre de usuario: " + username, excepcion.getMessage());
		verify(mockRepoUsuarios, never()).guardarUsuario(any(), any(), any(), any(), any());
	}

	/**
	 * Test de validación de parámetros nulos en el registro. Debe lanzar
	 * NullPointerException cuando el nombre es null.
	 */
	@Test
	@DisplayName("Debe lanzar NullPointerException cuando el nombre es null")
	void registroNombreEsNull() {
		NullPointerException excepcion = assertThrows(NullPointerException.class,
				() -> controlador.registrarUsuario(null, "email@test.com", "user", "pass", LocalDate.now()));

		assertEquals("El nombre no puede ser nulo", excepcion.getMessage());
	}

	/**
	 * Test de validación de parámetros nulos en el registro. Debe lanzar
	 * NullPointerException cuando el email es null.
	 */
	@Test
	@DisplayName("Debe lanzar NullPointerException cuando el email es null")
	void registroUsuarioEmailEsNull() {
		NullPointerException excepcion = assertThrows(NullPointerException.class,
				() -> controlador.registrarUsuario("Juan", null, "user", "pass", LocalDate.now()));

		assertEquals("El email no puede ser nulo", excepcion.getMessage());
	}

	/**
	 * Test de inicio de sesión exitoso usando email. Verifica que un usuario puede
	 * iniciar sesión correctamente con email y contraseña válidos, y que se
	 * actualiza el flag de primer login si corresponde.
	 */
	@Test
	@DisplayName("Debe iniciar sesión exitosamente con email válido")
	void iniciarSesionEmail() throws Exception {
		String email = "juan@test.com";
		String passwordPlano = "password123";
		String passwordHasheada = PasswordUtils.hashPassword(passwordPlano);

		when(mockUsuario.getPassword()).thenReturn(passwordHasheada);
		when(mockUsuario.isFirstLogin()).thenReturn(true);
		when(mockRepoUsuarios.obtenerPorEmail(email)).thenReturn(Optional.of(mockUsuario));

		boolean resultado = controlador.iniciarSesion(email, passwordPlano);

		assertTrue(resultado, "El inicio de sesión debe ser exitoso");
		assertEquals(mockUsuario, controlador.getUsuarioActual());
		verify(mockUsuario).setFirstLogin(false);
		verify(mockRepoUsuarios).actualizarUsuario(mockUsuario);
	}

	/**
	 * Test de inicio de sesión exitoso usando username. Verifica que un usuario
	 * puede iniciar sesión correctamente con username y contraseña válidos, y que
	 * se maneja correctamente el caso donde no es el primer login.
	 */
	@Test
	@DisplayName("Debe iniciar sesión exitosamente con username válido")
	void iniciarSesionUsername() throws Exception {
		String username = "juanp";
		String passwordPlano = "password123";

		// Simula el hash de la contraseña (podrías usar PasswordUtils para generar el
		// hash real)
		String passwordHasheada = PasswordUtils.hashPassword(passwordPlano);

		when(mockUsuario.getPassword()).thenReturn(passwordHasheada);
		when(mockUsuario.isFirstLogin()).thenReturn(true);
		when(mockRepoUsuarios.obtenerPorUsername(username)).thenReturn(Optional.of(mockUsuario));

		boolean resultado = controlador.iniciarSesion(username, passwordPlano);

		assertTrue(resultado, "El inicio de sesión debe ser exitoso");
		assertEquals(mockUsuario, controlador.getUsuarioActual());
		verify(mockUsuario).setFirstLogin(false);
		verify(mockRepoUsuarios).actualizarUsuario(mockUsuario);
	}

	/**
	 * Test de manejo de email inexistente en inicio de sesión. Debe lanzar
	 * ExcepcionCredencialesInvalidas cuando se intenta iniciar sesión con un email
	 * que no existe en el sistema.
	 */
	@Test
	@DisplayName("Debe lanzar excepción cuando el email no existe")
	void iniciarSesionEmailUsuarioNoExiste() {
		String email = "noexiste@temulingotest.es";
		when(mockRepoUsuarios.obtenerPorEmail(email)).thenReturn(Optional.empty());

		ExcepcionCredencialesInvalidas excepcion = assertThrows(ExcepcionCredencialesInvalidas.class,
				() -> controlador.iniciarSesion(email, "password"));

		assertEquals("Usuario con email '" + email + "' no encontrado", excepcion.getMessage());
	}

	/**
	 * Test de manejo de username inexistente en inicio de sesión. Debe lanzar
	 * ExcepcionCredencialesInvalidas cuando se intenta iniciar sesión con un
	 * username que no existe en el sistema.
	 */
	@Test
	@DisplayName("Debe lanzar excepción cuando el username no existe")
	void iniciarSesionUsernameUsuarioNoExiste() {
		String username = "noexiste";
		when(mockRepoUsuarios.obtenerPorUsername(username)).thenReturn(Optional.empty());

		ExcepcionCredencialesInvalidas excepcion = assertThrows(ExcepcionCredencialesInvalidas.class,
				() -> controlador.iniciarSesion(username, "password"));

		assertEquals("Usuario con nombre de usuario '" + username + "' no encontrado", excepcion.getMessage());
	}

	/**
	 * Test de validación de contraseña incorrecta. Debe lanzar
	 * ExcepcionCredencialesInvalidas cuando la contraseña no coincide y no debe
	 * establecer ningún usuario como actual.
	 */
	@Test
	@DisplayName("Debe lanzar excepción cuando la contraseña es incorrecta")
	void iniciarSesionPasswordIncorrecta() {
		String email = "juan@test.com";
		String passwordCorrectaPlano = "password123";
		String passwordIncorrecta = "wrongpassword";

		// Hasheamos la contraseña correcta para simular lo almacenado
		String passwordHasheada = PasswordUtils.hashPassword(passwordCorrectaPlano);

		when(mockUsuario.getPassword()).thenReturn(passwordHasheada);
		when(mockRepoUsuarios.obtenerPorEmail(email)).thenReturn(Optional.of(mockUsuario));

		ExcepcionCredencialesInvalidas excepcion = assertThrows(ExcepcionCredencialesInvalidas.class,
				() -> controlador.iniciarSesion(email, passwordIncorrecta));

		assertEquals("La contraseña introducida es incorrecta", excepcion.getMessage());
		assertNull(controlador.getUsuarioActual(), "El usuario actual debe seguir siendo null");
	}

	/**
	 * Test de validación de parámetros nulos en inicio de sesión. Debe lanzar
	 * NullPointerException cuando el username/email es null.
	 */
	@Test
	@DisplayName("Debe lanzar NullPointerException cuando username/email es null")
	void iniciarSesionInputNull() {
		NullPointerException excepcion = assertThrows(NullPointerException.class,
				() -> controlador.iniciarSesion(null, "password"));

		assertEquals("El username/email no puede ser nulo", excepcion.getMessage());
	}

	/**
	 * Test de validación de parámetros nulos en inicio de sesión. Debe lanzar
	 * NullPointerException cuando la contraseña es null.
	 */
	@Test
	@DisplayName("Debe lanzar NullPointerException cuando la contraseña es null")
	void iniciarSesionPasswordNull() {
		NullPointerException excepcion = assertThrows(NullPointerException.class,
				() -> controlador.iniciarSesion("user@test.com", null));

		assertEquals("La contraseña no puede ser nula", excepcion.getMessage());
	}

	/**
	 * Test de detección automática de formato de email. Verifica que el sistema
	 * puede distinguir correctamente entre emails y usernames, probando con
	 * diferentes formatos válidos de email.
	 */
	@Test
	@DisplayName("Debe detectar correctamente el formato de email válido")
	void iniciarSesionDetectaFormatoEmail() throws Exception {
		String[] emailsValidos = { "test@temulingotest.es", "user.name@temulingotest.co.uk",
				"test123@temulingo-test.es", "a+b@temulingo-test.org.es" };
		String passwordPlano = "pass";
		String passwordHasheada = PasswordUtils.hashPassword(passwordPlano);

		for (String email : emailsValidos) {
			when(mockUsuario.getPassword()).thenReturn(passwordHasheada);
			when(mockUsuario.isFirstLogin()).thenReturn(false); // Para simplificar, no es primer login
			when(mockRepoUsuarios.obtenerPorEmail(email)).thenReturn(Optional.of(mockUsuario));

			// No debe lanzar excepción para emails válidos
			assertDoesNotThrow(() -> controlador.iniciarSesion(email, passwordPlano),
					"Email válido no debe lanzar excepción: " + email);

			verify(mockRepoUsuarios, atLeastOnce()).obtenerPorEmail(email);
			reset(mockRepoUsuarios, mockUsuario);
		}
	}

}