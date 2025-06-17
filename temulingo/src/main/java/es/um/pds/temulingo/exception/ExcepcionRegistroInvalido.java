package es.um.pds.temulingo.exception;

public class ExcepcionRegistroInvalido extends Exception {

	private static final long serialVersionUID = 1L;

	public ExcepcionRegistroInvalido() {
		super();
	}

	public ExcepcionRegistroInvalido(String mensaje) {
		super(mensaje);
	}

	public ExcepcionRegistroInvalido(String mensaje, Throwable causa) {
		super(mensaje, causa);
	}

	public ExcepcionRegistroInvalido(Throwable causa) {
		super(causa);
	}
}
