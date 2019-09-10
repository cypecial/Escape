
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by yiping on 2017-07-07.
 */
public class EditorMouseController implements MouseListener, MouseMotionListener {
    private Point mousePt;
    private EditorModel model;

    public EditorMouseController(EditorModel model){
        this.model = model;
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mousePt = e.getPoint();
        model.setPressedInField(true);
        if(SwingUtilities.isLeftMouseButton(e) && e.getX() > model.toolBoxWidth) {
            if (model.getTool().equals("block") || model.getTool().equals("cut") || model.getTool().equals("copy")
                    || model.getTool().equals("select")) {
                model.setInitialPos(mousePt);
                model.setLargestPt(mousePt);
                if(model.getTool().equals("block")){
                    if(model.gameField.getCollisionObstacle(model.getMousePt(),model) == null){
                        model.validBlock = true;
                    }
                }
                if (model.getTool().equals("cut")){
                    model.cut();
                }
                if(model.getTool().equals("copy")){
                    model.copy();
                }
                if(model.getTool().equals("select")){
                    model.select();
                }
            }

            if (model.getTool().equals("paste")) {
                model.paste(model.getSelectedObstacle(), model.getBlockMx(), model.getBlockMy());
            }

            if (model.getTool().equals("move")) {
                model.setLargestPt(mousePt);
                Rectangle obstacle = model.gameField.getCollisionObstacle(model.getMousePt(), model);
                if (obstacle != null) {
                    model.validMove = true;
                    model.moveMxOffset = model.getBlockMx() - obstacle.x;
                    model.moveMyOffset = model.getBlockMy() - obstacle.y;
                    model.setSelectedMoveObstacle(obstacle);
                    model.gameField.removeObstacle(obstacle);
                }
            }

            if(model.getTool().equals("resize")){
                model.setLargestPt(mousePt);
                Rectangle obstacle = model.gameField.getCollisionObstacle(model.getMousePt(), model);
                if (obstacle != null) {
                    model.validResize = true;
                    int mx = model.getBlockMx();
                    int my = model.getBlockMy();
                    Boolean validPos = true;
                    if(mx == obstacle.x){
                        //top left corner
                        if(my == obstacle.y){
                            model.setResizeCorner("TL");
                        }
                        //bottom left
                        else if(my == obstacle.y + obstacle.height - 1){
                            model.setResizeCorner("BL");
                        }
                        else{
                            validPos = false;
                        }
                    }
                    else if(mx == obstacle.x + obstacle.width - 1){
                        //top right
                        if(my == obstacle.y){
                            model.setResizeCorner("TR");
                        }
                        //bottom right
                        else if(my == obstacle.y + obstacle.height - 1){
                            model.setResizeCorner("BR");
                        }
                        else{
                            validPos = false;
                        }

                    }
                    else{
                        validPos = false;
                    }
                    if(validPos) {
                        model.setSelectedResizeObstacle(obstacle);
                        model.gameField.removeObstacle(obstacle);
                    }
                }
            }
            model.setMousePt(e.getPoint());
        }
        else{
            model.pressedInField = false;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e) && e.getX() > model.toolBoxWidth) {
            if (model.getTool().equals("block")) {
                if(model.validBlock) {
                    Rectangle rect = OperationLogic.getSelectionRect(model.getInitialPos(), model.getLargestPt());
                    int x1 = (rect.x - model.getScreenX()) / model.getBlockSize();
                    int x2 = (rect.x + rect.width - model.getScreenX()) / model.getBlockSize();
                    int y1 = rect.y / model.getBlockSize();
                    int y2 = (rect.y + rect.height) / model.getBlockSize();
                    model.gameField.setObstacle(x1, y1, x2, y2);
                    model.validBlock = false;
                    model.addToHistory();
                    model.notifyObservers();
                }
            }
            if(model.getTool().equals("move")){

                if(model.validMove) {
                    model.paste(model.getSelectedMoveObstacle(),model.getBlockMoveMx(), model.getBlockMoveMy());
                    model.addToHistory();
                    model.validMove = false;
                }
            }
            if(model.getTool().equals("resize") && model.validResize){
                model.resize();
                //addToHistory() in model.resize();
                model.validResize = false;
            }
            model.setPressedInField(false);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int dx = e.getX() - mousePt.x;
        if(SwingUtilities.isRightMouseButton(e)) {
            model.pressedInField = false;
            model.setScreenX(model.getScreenX() + dx);
            mousePt = e.getPoint();
        }
        model.setMousePt(e.getPoint());
        Rectangle mouseRect = OperationLogic.getSelectionRect(model.getInitialPos(),model.getMousePt());
        Boolean intersect = false;
        for(int i = 0; i < model.gameField.getObstacleArray().size(); i++){
            Rectangle rect = OperationLogic.getDrawRect(model.gameField.getObstacleArray().get(i), model);
            Rectangle aRect = new Rectangle(rect.x,rect.y,rect.width,rect.height);

            if(aRect.intersects(mouseRect)){
                intersect = true;
            }
        }
        if(!intersect){
            model.setLargestPt(e.getPoint());
        }
        if(model.validMove) {
            int mx = (e.getPoint().x - model.getScreenX())/model.getBlockSize();
            int my = e.getPoint().y/model.getBlockSize();

            if(model.gameField.checkValidPaste(model.getSelectedMoveObstacle(),mx-model.moveMxOffset,my-model.moveMyOffset))
            {
                model.lastMovePt = e.getPoint();
            }
        }
        if(model.validResize) {
            int mx = (e.getPoint().x - model.getScreenX())/model.getBlockSize();
            int my = e.getPoint().y/model.getBlockSize();

            if(model.gameField.checkValidResize(model.getSelectedResizeObstacle(),mx,my, model.getResizeCorner()))
            {
                model.lastMovePt = e.getPoint();
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        model.setMousePt(e.getPoint());
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
