package es.um.pds.temulingo.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstrategiaRepeticionEspaciada implements EstrategiaAprendizajeStrategy {
    
    @Override
    public Map<Bloque, List<Pregunta>> prepararPreguntas(List<Bloque> bloques) {
        Map<Bloque, List<Pregunta>> resultado = new HashMap<>();
        
        for (Bloque bloque : bloques) {
            List<Pregunta> preguntasOriginales = new ArrayList<>(bloque.getPreguntas());
            List<Pregunta> preguntasEspaciadas = new ArrayList<>();
            
            // Mapa para controlar cuántas veces se ha visto cada pregunta
            // 0 = no vista, 1 = vista una vez, 2 = vista dos veces (completada)
            Map<Pregunta, Integer> contadorVistas = new HashMap<>();
            
            // Inicializar contador
            for (Pregunta pregunta : preguntasOriginales) {
                contadorVistas.put(pregunta, 0);
            }
            
            // Primera fase: Añadir preguntas originales con repeticiones espaciadas
            for (int i = 0; i < preguntasOriginales.size(); i++) {
                Pregunta preguntaActual = preguntasOriginales.get(i);
                
                // Añadir la pregunta original (primera vez que se ve)
                preguntasEspaciadas.add(preguntaActual);
                contadorVistas.put(preguntaActual, 1);
                
                // Verificar si hay una pregunta para repetir (3 posiciones atrás)
                if (i >= 3) {
                    Pregunta preguntaARepetir = preguntasOriginales.get(i - 3);
                    
                    // Solo repetir si se ha visto exactamente una vez
                    if (contadorVistas.get(preguntaARepetir) == 1) {
                        preguntasEspaciadas.add(preguntaARepetir);
                        contadorVistas.put(preguntaARepetir, 2); // Marcar como completada
                    }
                }
            }
            
            // Segunda fase: Añadir preguntas que solo se vieron una vez
            // (esto pasa con las últimas 3 preguntas del bloque)
            for (Pregunta pregunta : preguntasOriginales) {
                if (contadorVistas.get(pregunta) == 1) {
                    preguntasEspaciadas.add(pregunta);
                    contadorVistas.put(pregunta, 2); // Marcar como completada
                }
            }
            
            resultado.put(bloque, preguntasEspaciadas);
        }
        
        return resultado;
    }
}