package es.um.pds.temulingo.logic;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TRADUCCION")
public class PreguntaTraduccion extends Pregunta {

    //@ElementCollection
    //@CollectionTable(name = "ITEM_PREGUNTA", joinColumns = @JoinColumn(name = "ID"))
    //@Column(name = "ITEMS")
    //private List<String> items;

    private String solucion;
}
