import javax.swing.JFrame;

/*
 * Provides the screens for the GUI
 */

public class ScreenProvider {
    public static Screen getIdleScreen(){
        JFrame idleScreen = new JFrame();
        
        return new Screen();
    }
}
