package es.um.pds.temulingo.logic;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("HUECOS")
public class PreguntaHuecos extends Pregunta {

	@Column(name = "SOLUCION")
	private String solucion;
	
	public PreguntaHuecos() {
        super();
    }
    
    public PreguntaHuecos(Long id, String enunciado, Bloque bloque, String solucion) {
        super(id, enunciado, bloque);
        this.solucion = solucion;
    }

	public void setSolucion(String solucion) {
		this.solucion = solucion;
	}

	public String getSolucion() {
		return solucion;
	}
	

	@Override
	public boolean esSolucion(String respuesta) {
		if (respuesta == null || solucion == null) {
            return false;
        }
        
        // Normalizar respuestas
        String respuestaNormalizada = respuesta.trim().toLowerCase();
        String solucionNormalizada = solucion.trim().toLowerCase();
        
        return respuestaNormalizada.equals(solucionNormalizada);
	}

}
