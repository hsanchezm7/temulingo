package es.um.pds.temulingo.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EstrategiaAleatoria implements EstrategiaAprendizajeStrategy {
	public Map<Bloque, List<Pregunta>> prepararPreguntas(List<Bloque> bloques) {
		Map<Bloque, List<Pregunta>> resultado = new HashMap<>();
		for (Bloque bloque : bloques) {
			List<Pregunta> copia = new ArrayList<>(bloque.getPreguntas());
			Collections.shuffle(copia, new Random(System.nanoTime()));
			resultado.put(bloque, copia);
		}
		return resultado;
	}
}