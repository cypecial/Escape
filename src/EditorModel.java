import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class EditorModel {
    /** The observers that are watching this model for changes. */
    private List<Observer> observers;
    private int screenX;
    private String curTool, resizeCorner;
    private int historyIndex;
    private ArrayList<GameField> historyStack;
    private Rectangle selectedObstacle, selectedMoveObstacle, selectedResizeObstacle;

    public boolean pressedInField, obstacleCopied, validMove, validBlock, validResize;
    public final int toolBoxWidth = 60;

    //used for block tool
    private Point initialPos, mousePt, largestPt;
    public int moveMxOffset, moveMyOffset;

    public GameField gameField;
    public EditorFrame editorFrame;

    public Point lastMovePt;

    public EditorModel() {
        this.observers = new ArrayList();
        this.screenX = toolBoxWidth;
        this.pressedInField = false;
        this.validMove = false;
        this.validBlock = false;
        this.validResize = false;
        this.curTool = "block";
        this.resizeCorner = "";
        this.historyIndex = 0;
        this.historyStack = new ArrayList();
        this.selectedMoveObstacle = new Rectangle();
        this.selectedObstacle = new Rectangle();
        this.selectedResizeObstacle = new Rectangle();
        this.moveMxOffset = 0;
        this.moveMyOffset = 0;
        this.obstacleCopied = false;
        //default empty field
        this.gameField = new GameField(50,10);
        this.setInitialPos(new Point(0,0));
        this.setMousePt(new Point(0,0));
        this.setLargestPt(new Point(0,0));
        this.lastMovePt = new Point(0,0);
        historyStack.add(gameField.copy());
    }

    public Rectangle getSelectedObstacle(){
        return selectedObstacle;
    }
    public void setSelectedObstacle(Rectangle obs){
        this.selectedObstacle = obs;
    }

    public void setPressedInField(boolean b){
        this.pressedInField = b;
    }

    public int getBlockSize(){
        return gameField.getBlockSize();
    }
    public void setBlockSize(int s){
        this.gameField.setBlockSize(s);
    }

    public int getBlockMx(){
        return (mousePt.x - getScreenX())/getBlockSize();
    }
    public int getBlockMy(){
        return mousePt.y/getBlockSize();
    }

    public int getBlockMoveMx(){
        return (lastMovePt.x - getScreenX())/getBlockSize();
    }
    public int getBlockMoveMy(){
        return lastMovePt.y/getBlockSize();
    }

    public int getBlockSizeMx(){
        return (lastMovePt.x - getScreenX())/getBlockSize();
    }
    public int getBlockSizeMy(){
        return lastMovePt.y/getBlockSize();
    }

    public int getScreenX(){
        return screenX;
    }

    public void setCopied(Boolean b) {
        this.obstacleCopied = b;
    }

    public void setTool(String tool){
        this.curTool = tool;
        editorFrame.curToolLabel.setText("Current Tool: " + tool);
    }

    public String getTool(){
        return curTool;
    }

    public void setScreenX(int x){
        this.screenX = x;
        notifyObservers();
    }

    public Point getInitialPos(){
        return initialPos;
    }
    public void setInitialPos(Point pt){
        this.initialPos = pt;
        notifyObservers();
    }

    public Point getMousePt(){
        return mousePt;
    }
    public void setMousePt(Point pt){
        this.mousePt = pt;
        notifyObservers();
    }

    public void select(){
        Rectangle obstacle = gameField.getCollisionObstacle(getMousePt(), this);
        if(obstacle != null) {
            setSelectedObstacle(obstacle);
        }
    }

    public void cut(){
        Rectangle obstacle = gameField.getCollisionObstacle(getMousePt(), this);
        if(obstacle != null) {
            gameField.removeObstacle(obstacle);
            setSelectedObstacle(obstacle);
            setCopied(true);
            addToHistory();
        }
    }
    public void copy(){
        Rectangle obstacle = gameField.getCollisionObstacle(getMousePt(), this);
        if(obstacle != null) {
            setSelectedObstacle(obstacle);
            setCopied(true);
        }
    }

    public void paste(Rectangle obstacle, int mx, int my){
        if(selectedObstacle != null || !obstacleCopied){
            if(getTool().equals("move")){
                if(gameField.checkValidPaste(obstacle,mx-moveMxOffset,my-moveMyOffset)) {
                    gameField.paste(obstacle, mx - moveMxOffset, my - moveMyOffset);
                }
            }
            else {
                if (gameField.checkValidPaste(obstacle, mx, my)) {
                    gameField.paste(obstacle, mx, my);
                    addToHistory();
                }
            }
        }
        notifyObservers();
    }

    public void addToHistory(){
        while(historyIndex < historyStack.size()-1){
            historyStack.remove(historyStack.size()-1);
        }
        historyStack.add(gameField.copy());
        historyIndex += 1;
    }

    public void load(String path){
        this.gameField = OperationLogic.importLevelFile(path);
        notifyObservers();
    }

    public void save(String path){
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter(path);
            bw = new BufferedWriter(fw);
            bw.write(gameField.getDimX() + ", " + gameField.getDimY() + "\n");
            for(Rectangle rect: gameField.getObstacleArray()){
                bw.write(rect.x + ", " + rect.y + ", " + (rect.x+rect.width-1) + ", " + (rect.y + rect.height-1) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void resize(){
        if(resizeCorner != "") {
            int mx = getBlockSizeMx();
            int my = getBlockSizeMy();
            Rectangle rect = OperationLogic.getResizeRect(selectedResizeObstacle, resizeCorner, mx, my);
            gameField.setObstacle(rect);
            setResizeCorner("");
            addToHistory();
        }
        notifyObservers();
    }

    public void undo(){
        //last undo step
        if(historyIndex != 0){
            historyIndex -= 1;
            gameField = historyStack.get(historyIndex).copy();
        }
        notifyObservers();
    }
    public void redo(){
        if(historyIndex != historyStack.size() - 1){
            historyIndex += 1;
            gameField = historyStack.get(historyIndex).copy();
        }
        notifyObservers();
    }

    public void changeDim(int dx, int dy){
        gameField = gameField.resizeField(dx,dy);
        addToHistory();
    }

    /**
     * Add an observer to be notified when this model changes.
     */
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    /**
     * Remove an observer from this model.
     */
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    /**
     * Notify all observers that the model has changed.
     */
    public void notifyObservers() {
        for (Observer observer: this.observers) {
            observer.update(this);
        }
    }

    public Point getLargestPt() {
        return largestPt;
    }

    public void setLargestPt(Point largestPt) {
        this.largestPt = largestPt;
    }

    public Rectangle getSelectedMoveObstacle() {
        return selectedMoveObstacle;
    }

    public void setSelectedMoveObstacle(Rectangle selectedMoveObstacle) {
        this.selectedMoveObstacle = selectedMoveObstacle;
    }

    public String getResizeCorner() {
        return resizeCorner;
    }

    public void setResizeCorner(String resizeCorner) {
        this.resizeCorner = resizeCorner;
    }

    public Rectangle getSelectedResizeObstacle() {
        return selectedResizeObstacle;
    }

    public void setSelectedResizeObstacle(Rectangle selectedResizeObstacle) {
        this.selectedResizeObstacle = selectedResizeObstacle;
    }
}
