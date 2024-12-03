package Controller;
import java.io.File;
import javax.sound.sampled.*;

public class MusicLoader 
{
    // TODA CANCIÓNN QUE VAYA A ESTAR GUARDAD EN EL SISTEMA DEBE ESTAR EN LA CARPETA audios/
    public static Clip loadASong(String pathname)
    {
        Clip audioClip = null;
        try 
        {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(pathname));
            audioClip = AudioSystem.getClip();
            
            audioClip.open(audioStream);
            audioClip.start();
        }
        catch (Exception e) {}
        return audioClip;
    }
}
