package es.um.pds.temulingo.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilidades para el hash y verificación de contraseñas usando BCrypt.
 * 
 * <p>
 * Esta clase permite generar un hash seguro para una contraseña en texto plano,
 * así como verificar una contraseña comparándola con un hash previamente
 * generado.
 * </p>
 * 
 * <p>
 * Se utiliza un factor de trabajo (<i>work factor</i>) configurable para el
 * proceso de hash, que determina la complejidad y el tiempo de cómputo para
 * aumentar la seguridad.
 * </p>
 */
public class PasswordUtils {

	/**
	 * Factor de trabajo para BCrypt que determina la complejidad del hash. Valores
	 * más altos significan más seguridad pero mayor tiempo de procesamiento.
	 */
	private static final int WORK_FACTOR = 10;

	/**
	 * Genera un hash seguro a partir de una contraseña en texto plano.
	 *
	 * @param plainPassword la contraseña en texto plano que se quiere proteger
	 * @return una cadena con el hash BCrypt de la contraseña
	 */
	public static String hashPassword(String plainPassword) {
		return BCrypt.hashpw(plainPassword, BCrypt.gensalt(WORK_FACTOR));
	}

	/**
	 * Verifica si una contraseña en texto plano coincide con un hash BCrypt dado.
	 *
	 * @param plainPassword  la contraseña en texto plano a verificar
	 * @param hashedPassword el hash BCrypt contra el cual se compara la contraseña
	 * @return {@code true} si la contraseña coincide con el hash; {@code false} en
	 *         caso contrario
	 */
	public static boolean verifyPassword(String plainPassword, String hashedPassword) {
		return BCrypt.checkpw(plainPassword, hashedPassword);
	}
}
