package es.um.pds.temulingo.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EstrategiaSecuencial implements EstrategiaAprendizajeStrategy {
	@Override
	public Map<Bloque, List<Pregunta>> prepararPreguntas(List<Bloque> bloques) {

		return bloques.stream().collect(Collectors.toMap(b -> b, b -> new ArrayList<>(b.getPreguntas())));

	}
}