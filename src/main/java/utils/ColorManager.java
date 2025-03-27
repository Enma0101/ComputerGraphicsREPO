package utils;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

//PARA MANEJAR LOS COLORES QUE SE UTILIZARAN
 public class ColorManager {
	    private static final Map<String, Color[]> coloresEquipos = new HashMap<>();

	    static {

	        coloresEquipos.put("Ferrari", new Color[]{new Color(188, 24, 35), new Color(255, 242, 0)});  		// Rojo & Negro
	        coloresEquipos.put("Red Bull", new Color[]{new Color(35, 50, 106), new Color(253, 217, 0)});  		// Azul & Amarillo
	        coloresEquipos.put("Mercedes", new Color[]{new Color(200, 204, 206), new Color(0, 161, 155)});  	// Plateado & Verde Aqua
	        coloresEquipos.put("McLaren", new Color[]{new Color(255, 128, 0), new Color(0, 0, 0)});  			// Naranja & Negro
	        coloresEquipos.put("Aston Martin", new Color[]{new Color(0, 100, 90), new Color(255, 255, 255)});  	// Verde & Blanco
	    }

	    
	  
	    public static Color getPrimaryColor(String equipo) {
	        Color[] colores = coloresEquipos.get(equipo); 
	        return (colores != null) ? colores[0] : Color.WHITE; 
	    }


	    public static Color getSecondaryColor(String equipo) {
	        Color[] colores = coloresEquipos.get(equipo); 
	        return (colores != null) ? colores[1] : Color.WHITE; 
	    }
	}

