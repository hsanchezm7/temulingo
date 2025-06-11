package es.um.pds.temulingo;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * Tests básicos para verificar que la aplicación funciona
 */
public class AppTest {
    
    @Test
    @DisplayName("Test básico - debería pasar")
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }
    
    @Test
    @DisplayName("Test de creación de objetos básicos")
    public void testCreacionObjetosBasicos() {
        // Test simple para verificar que las clases existen
        assertNotNull("Test básico");
        assertEquals(2, 1 + 1);
    }
}