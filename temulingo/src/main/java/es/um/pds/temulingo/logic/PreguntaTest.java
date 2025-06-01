package es.um.pds.temulingo.logic;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSetter;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TEST")
public class PreguntaTest extends Pregunta {

	@Column(name = "OPCIONES")
	private List<String> opciones;
	
	@Column(name = "SOLUCION")
	private String solucion;
	
	public List<String> getOpciones() {
		return opciones;
	}

	public void setOpciones(List<String> opciones) {
		this.opciones = opciones;
	}
	
	@JsonSetter
    public void setSolucion(String solucion) {
        if (solucion != null && opciones != null && !opciones.contains(solucion)) {
            throw new IllegalArgumentException(
                "La solución '" + solucion + "' debe estar entre las opciones disponibles: " + opciones
            );
        }
        this.solucion = solucion;
    }

	@Override
	public boolean esSolucion(String respuesta) {
		// TODO: Convertir a minusculas todo y remover tildes.
		return respuesta.equals(solucion);
	}
}
