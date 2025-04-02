package utils;

import javax.sound.sampled.*;
import java.io.IOException;

import java.net.URL;

public class MusicaFondo {
	  private Clip clip;
	    private Clip clipAceleracion;
	    private Clip clipFrenado;
	    private Clip clipIdleMotor;
	    private Clip clipdesAceleracion;
	    
	    public Clip currentlyPlaying;

    public void reproducirMusica(String ruta) {
        try {
            URL audioUrl = getClass().getResource(ruta);
            if (audioUrl == null) {
                throw new IOException("Archivo de audio no encontrado: " + ruta);
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioUrl);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        
      
    }
    
    URL aceleracionURL = getClass().getClassLoader().getResource("resources/Audio/Car Accelerating.wav");
    URL frenadoURL = getClass().getClassLoader().getResource("resources/Audio/frenado.wav");
    URL idleURL = getClass().getClassLoader().getResource("resources/Audio/engine.wav");
    URL desaceURL = getClass().getClassLoader().getResource("resources/Audio/deceleration.wav");
  
    
    public MusicaFondo() {
    try {
        if (aceleracionURL != null) {
            AudioInputStream audioStreamAceleracion = AudioSystem.getAudioInputStream(aceleracionURL);
            clipAceleracion = AudioSystem.getClip();
            clipAceleracion.open(audioStreamAceleracion);
            
            AudioInputStream audioStreamFrenado = AudioSystem.getAudioInputStream(frenadoURL);
            clipFrenado = AudioSystem.getClip();
            clipFrenado.open(audioStreamFrenado);
            
            AudioInputStream audioStreamIdle = AudioSystem.getAudioInputStream(idleURL);
            clipIdleMotor = AudioSystem.getClip();
            clipIdleMotor.open(audioStreamIdle);
            
            
            AudioInputStream audioStreamdesa = AudioSystem.getAudioInputStream(desaceURL);
            clipdesAceleracion = AudioSystem.getClip();
            clipdesAceleracion.open(audioStreamdesa);
        } else {
            System.err.println("Sound files not found. Check your file paths.");
        }
        
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        System.err.println("Error loading sound files: " + e.getMessage());
        e.printStackTrace();
    }
}


public void reproducirAceleracion() {
    detenerMusica();
    if (clipAceleracion != null) {
        clipAceleracion.setFramePosition(0);
        clipAceleracion.loop(Clip.LOOP_CONTINUOUSLY);
        currentlyPlaying = clipAceleracion;
    }
}

public void reproducirdesAceleracion() {
    detenerMusica();
    if (clipdesAceleracion != null) {
    	clipdesAceleracion.setFramePosition(0);
    	clipdesAceleracion.loop(Clip.LOOP_CONTINUOUSLY);
        currentlyPlaying = clipdesAceleracion;
    }
}

public void reproducirFrenado() {
    detenerMusica();
    if (clipFrenado != null) {
        clipFrenado.setFramePosition(0);
        clipFrenado.start(); 
        currentlyPlaying = clipFrenado;
    }
}

public void reproducirIdleMotor() {
    detenerMusica();
    if (clipIdleMotor != null) {
        clipIdleMotor.setFramePosition(0);
        clipIdleMotor.loop(Clip.LOOP_CONTINUOUSLY);
        currentlyPlaying = clipIdleMotor;
    }
}


public void reproducirMusicaMenu() {
    detenerMusica(); 
    reproducirMusica("/resources/Audio/musicafondo.wav"); 
}



public void detenerMusica() {
    if (clipAceleracion != null && clipAceleracion.isRunning()) {
        clipAceleracion.stop();
    }
    if (clipFrenado != null && clipFrenado.isRunning()) {
        clipFrenado.stop();
    }
    if (clipIdleMotor != null && clipIdleMotor.isRunning()) {
        clipIdleMotor.stop();
    }
    if (clipdesAceleracion != null && clipIdleMotor.isRunning()) {
    	clipdesAceleracion.stop();
    }
    
    if (clip != null && clip.isRunning()) {
    	clip.stop();
    }
    currentlyPlaying = null;
}

public boolean isPlaying() {
    return (currentlyPlaying != null && currentlyPlaying.isRunning()) || 
           (clip != null && clip.isRunning());
}


public void detenerMusicaFondo() {

    
    if (clip != null && clip.isRunning()) {
    	clip.stop();
    	 clip.close();
    	    
        System.out.println("Música detenida.");
    }
    currentlyPlaying = null;
  
}

}
