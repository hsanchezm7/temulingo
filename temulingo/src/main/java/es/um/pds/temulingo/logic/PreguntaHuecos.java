package es.um.pds.temulingo.logic;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("HUECOS")
public class PreguntaHuecos extends Pregunta {

	@Column(name = "SOLUCION")
	private String solucion;

	public void setSolucion(String solucion) {
		this.solucion = solucion;
	}

	public String getSolucion() {
		return solucion;
	}

	@Override
	public boolean esSolucion(String respuesta) {
		// TODO: Convertir a minusculas todo y remover tildes.
		return respuesta.equals(solucion);
	}

}
