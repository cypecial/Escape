import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by yiping on 2017-07-10.
 */
public class EditorKeyController implements KeyListener{
    private EditorModel model;

    public EditorKeyController(EditorModel model){
        this.model = model;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        Boolean ctrl = e.isControlDown();
        if(ctrl && keyCode == KeyEvent.VK_C){
            model.copy();
        }
        if(ctrl && keyCode == KeyEvent.VK_X){
            model.cut();
        }
        if(ctrl && keyCode == KeyEvent.VK_V){
            model.paste(model.getSelectedObstacle(), model.getBlockMx(), model.getBlockMy());
        }
        if(ctrl && keyCode == KeyEvent.VK_Z){
            model.undo();
        }
        if(ctrl && keyCode == KeyEvent.VK_Y){
            model.redo();
        }
        if(keyCode == KeyEvent.VK_R){
            model.setTool("resize");
        }
        if(keyCode == KeyEvent.VK_V){
            model.setTool("select");
        }
        if(keyCode == KeyEvent.VK_M){
            model.setTool("move");
        }
        if(keyCode == KeyEvent.VK_B){
            model.setTool("block");
        }
        if(!ctrl && keyCode == KeyEvent.VK_C){
            model.gameField.clearField();
            model.addToHistory();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
