package es.um.pds.temulingo.logic;

import es.um.pds.temulingo.logic.Curso.EstrategiaAprendizaje;

public class EstrategiaFactory {
    public static EstrategiaAprendizajeStrategy crear(EstrategiaAprendizaje tipo) {
        return switch (tipo) {
            case SECUENCIAL -> new EstrategiaSecuencial();
            case ALEATORIA -> new EstrategiaAleatoria();
            case REPETICION_ESPACIADA -> new EstrategiaRepeticionEspaciada();
        };
    }
}
