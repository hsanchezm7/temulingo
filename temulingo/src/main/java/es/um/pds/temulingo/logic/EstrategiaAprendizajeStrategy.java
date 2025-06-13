package es.um.pds.temulingo.logic;

import java.util.Map;
import java.util.List;

public interface EstrategiaAprendizajeStrategy {
    Map<Bloque, List<Pregunta>> prepararPreguntas(List<Bloque> bloques);
}
