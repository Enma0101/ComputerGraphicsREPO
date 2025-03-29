package main;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;




public class Main {
	
	   public static Font GLOBAL_FONT;
	    public static Font GLOBAL_FONT2;

	    static {
	        try {
	            // Cargar la fuente desde el classpath de los recursos
	            InputStream fontStream = Main.class.getResourceAsStream("/resources/Fonts/PressStart2P-Regular.ttf");
	            if (fontStream == null) {
	                throw new IOException("Fuente no encontrada en el classpath.");
	            }
	            
	            // Crear la fuente desde el stream
	            GLOBAL_FONT = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(50f);
	            
	            // Registrar la fuente
	            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	            ge.registerFont(GLOBAL_FONT);
	            
	            // Crear una versión más pequeña de la fuente
	            GLOBAL_FONT2 = GLOBAL_FONT.deriveFont(16f);

	        } catch (IOException | FontFormatException e) {
	            e.printStackTrace();
	            GLOBAL_FONT = new Font("SansSerif", Font.BOLD, 18); 
	            GLOBAL_FONT2 = GLOBAL_FONT.deriveFont(16f);
	        }
	    }

	
	

}