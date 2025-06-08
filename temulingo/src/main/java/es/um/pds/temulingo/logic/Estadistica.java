package es.um.pds.temulingo.logic;

public class Estadistica {

    private int preguntasRespondidas;
    private int preguntasAcertadas;

    private int cursosCompletados;

    public Estadistica() { }

    public Estadistica(int preguntasRespondidas, int preguntasAcertadas, int cursosCompletados) {
        this.preguntasRespondidas = preguntasRespondidas;
        this.preguntasAcertadas = preguntasAcertadas;
        this.cursosCompletados = cursosCompletados;
    }

    public int getPreguntasRespondidas() {
        return preguntasRespondidas;
    }

    public void setPreguntasRespondidas(int preguntasRespondidas) {
        this.preguntasRespondidas = preguntasRespondidas;
    }

    public int getPreguntasAcertadas() {
        return preguntasAcertadas;
    }

    public void setPreguntasAcertadas(int preguntasAcertadas) {
        this.preguntasAcertadas = preguntasAcertadas;
    }

    public int getCursosCompletados() {
        return cursosCompletados;
    }

    public void setCursosCompletados(int cursosCompletados) {
        this.cursosCompletados = cursosCompletados;
    }

    public double getTasaAcierto() {
        if (preguntasRespondidas == 0) {
            return 0.0; // evita división por cero
        }
        return (double) preguntasAcertadas / preguntasRespondidas;
    }
}
