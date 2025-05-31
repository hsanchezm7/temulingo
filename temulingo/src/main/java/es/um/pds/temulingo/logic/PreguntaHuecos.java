package es.um.pds.temulingo.logic;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("HUECOS")
public class PreguntaHuecos extends Pregunta {

	// @ElementCollection
	// @CollectionTable(name = "ITEM_PREGUNTA", joinColumns = @JoinColumn(name =
	// "ID"))
	// @Column(name = "ITEMS")
	// private List<String> items;

	private String solucion;
}
