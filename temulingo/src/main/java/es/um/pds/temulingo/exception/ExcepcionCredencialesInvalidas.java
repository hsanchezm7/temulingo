package es.um.pds.temulingo.exception;

public class ExcepcionCredencialesInvalidas extends Exception {

	private static final long serialVersionUID = 1L;

	public ExcepcionCredencialesInvalidas() {
		super();
	}

	public ExcepcionCredencialesInvalidas(String mensaje) {
		super(mensaje);
	}

	public ExcepcionCredencialesInvalidas(String mensaje, Throwable causa) {
		super(mensaje, causa);
	}

	public ExcepcionCredencialesInvalidas(Throwable causa) {
		super(causa);
	}
}