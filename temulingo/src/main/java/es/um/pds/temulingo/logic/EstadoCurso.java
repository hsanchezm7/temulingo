package es.um.pds.temulingo.logic;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//Nueva clase en el paquete logic:
public class EstadoCurso {
	private Pregunta preguntaActual;
	private Map<Pregunta, String> respuestasDadas;
	private double progresoActual;
	private long tiempoTranscurrido;
	private Date fechaUltimaSesion;

	// Constructor y getters/setters
	public EstadoCurso(Pregunta preguntaActual, Map<Pregunta, String> respuestasDadas, double progresoActual,
			long tiempoTranscurrido, Date fechaUltimaSesion) {
		this.preguntaActual = preguntaActual;
		this.respuestasDadas = new HashMap<>(respuestasDadas);
		this.progresoActual = progresoActual;
		this.tiempoTranscurrido = tiempoTranscurrido;
		this.fechaUltimaSesion = fechaUltimaSesion;
	}

	public Pregunta getPreguntaActual() {
		return preguntaActual;
	}

	public void setPreguntaActual(Pregunta preguntaActual) {
		this.preguntaActual = preguntaActual;
	}

	public Map<Pregunta, String> getRespuestasDadas() {
		return respuestasDadas;
	}

	public void setRespuestasDadas(Map<Pregunta, String> respuestasDadas) {
		this.respuestasDadas = respuestasDadas;
	}

	public double getProgresoActual() {
		return progresoActual;
	}

	public void setProgresoActual(double progresoActual) {
		this.progresoActual = progresoActual;
	}

	public long getTiempoTranscurrido() {
		return tiempoTranscurrido;
	}

	public void setTiempoTranscurrido(long tiempoTranscurrido) {
		this.tiempoTranscurrido = tiempoTranscurrido;
	}

	public Date getFechaUltimaSesion() {
		return fechaUltimaSesion;
	}

	public void setFechaUltimaSesion(Date fechaUltimaSesion) {
		this.fechaUltimaSesion = fechaUltimaSesion;
	}

}