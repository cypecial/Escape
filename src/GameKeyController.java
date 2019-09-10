import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by yiping on 2017-07-06.
 */
public class GameKeyController implements KeyListener{
    private Model model;

    public GameKeyController(Model model){
        this.model = model;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(this.model.inGame) {
            int key = e.getKeyChar();
            //System.out.println(e.getKeyChar() + " typed");
            if (key == 'w') {
                this.model.moveChar("UP");
            }
            if (key == 's') {
                this.model.moveChar("DOWN");
            }
            if (key == 'a') {
                this.model.moveChar("LEFT");
            }
            if (key == 'd') {
                this.model.moveChar("RIGHT");
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_UP) {
            this.model.moveChar("UP");
        }
        if(keyCode == KeyEvent.VK_DOWN) {
            this.model.moveChar("DOWN");
        }
        if(keyCode == KeyEvent.VK_LEFT) {
            this.model.moveChar("LEFT");
        }
        if(keyCode == KeyEvent.VK_RIGHT){
            this.model.moveChar("RIGHT");
        }
        if(keyCode == KeyEvent.VK_ESCAPE){
            this.model.setPaused(true);
        }
        if(keyCode == KeyEvent.VK_SPACE){
            this.model.setPaused(false);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
