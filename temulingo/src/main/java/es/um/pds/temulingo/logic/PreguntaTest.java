package es.um.pds.temulingo.logic;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TEST")
public class PreguntaTest extends Pregunta {

	// @ElementCollection
	// @CollectionTable(name = "ITEM_PREGUNTA", joinColumns = @JoinColumn(name =
	// "ID"))
	// @Column(name = "ITEMS")
	// private List<String> items;

	private String solucion;
}
