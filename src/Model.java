import java.util.*;
import java.lang.String;


public class Model{
    /** The observers that are watching this model for changes. */
    private List<Observer> observers;
    private String curScreen, levelFilePath, defaultFileName;
    private Boolean endOfMap;
    private int FPS, SS, screenX, charX, charY, score;
    private static final int OBSTACLE = 1;
    public Boolean win, lose, inGame, paused;
    public GameField gameField;
    public MainFrame mainFrame;
    public EditorFrame editorFrame;

    public Model() {
        this.observers = new ArrayList();
        this.curScreen = "mainMenu";
        this.defaultFileName = "default_level.txt";
        this.FPS = 30;
        this.SS = 2;
        this.score = 0;
        restart();
    }

    private GameField importLevel(String path){
        if(path != null){
            return OperationLogic.importLevelFile(path);
        }
        return OperationLogic.importLevelFile("./savedLevels/" + defaultFileName);
    }

    public void setGameMenuVisible(){
        mainFrame.play.setVisible(true);
        mainFrame.pause.setVisible(true);
        mainFrame.scoreLabel.setVisible(true);
        mainFrame.score.setVisible(true);
    }
    public void setGameMenuInvisible(){
        mainFrame.play.setVisible(false);
        mainFrame.pause.setVisible(false);
        mainFrame.scoreLabel.setVisible(false);
        mainFrame.score.setVisible(false);
    }

    public void restart(){
        this.gameField = importLevel(levelFilePath);
        this.paused = false;
        this.endOfMap = false;
        this.inGame = true;
        this.win = false;
        this.lose = false;
        this.score = 0;
        setCharX(1);
        setCharY(gameField.getDimY()/2);
        screenX = 0;
        notifyObservers();
    }

    public void run(){
        if(curScreen.equals("inGame")){
            this.mainFrame.play.setVisible(true);
            this.mainFrame.pause.setVisible(true);
            if(!paused && !endOfMap && !win && !lose) {
                screenX -= SS;
                score += 1;
                mainFrame.score.setText(Integer.toString(score));
            }
        }
        notifyObservers();
    }

    public void addEditorFrame(EditorFrame frame){
        this.editorFrame = frame;
    }

    public void load(String path){
        this.levelFilePath = path;
        //this.gameField = OperationLogic.importLevelFile(path);
        System.out.println("loaded");
        notifyObservers();
    }

    public int getScore(){
        return score;
    }

    public int getSS(){
        return SS;
    }

    public void setSS(int ss){
        this.SS = ss;
    }

    public int getFPS(){
        return FPS;
    }

    public void setFPS(int fps){
        this.FPS = fps;
    }

    public void setPaused(Boolean b){
        if(b){inGame = false;}
        else{inGame = true;}
        this.paused = b;
    }

    public void setEndOfMap(Boolean b){
        this.endOfMap = b;
    }

    public void setBlockSize(int s){
        this.gameField.setBlockSize(s);
    }

    public int getBlockSize(){
        return gameField.getBlockSize();
    }

    public int getCharX(){
        return charX;
    }

    public int getCharY(){
        return charY;
    }

    public void setCharX(int x) {
        if (gameField.hasObstacle(x, charY)) {
            lose = true;
            inGame = false;
        }
        else if(x == gameField.getDimX()-1) {
            win = true;
            inGame = false;

        }
        else{
            this.charX = x;
        }
    }

    public void setCharY(int y){
        if(gameField.hasObstacle(charX, y)){
            lose = true;
            inGame = false;
        }
        else {
            this.charY = y;
        }
    }

    public void moveChar(String dir){
        if(dir.equals("UP")){
            if(charY <= 0){
                setCharY(0);
            }
            else {
                setCharY(charY - 1);
            }
        }
        else if(dir.equals("DOWN")){
            if(this.charY >= gameField.getDimY()-1){
                setCharY(gameField.getDimY()-1);
            }
            else {
                setCharY(charY + 1);
            }
        }
        else if(dir.equals("LEFT")){
            setCharX(charX - 1);
        }
        else{
            setCharX(charX + 1);
        }
        notifyObservers();
    }

    public String getScreen(){
        return curScreen;
    }

    public int getScreenX(){
        return screenX;
    }

    public void setScreen(String screen) {
        this.curScreen = screen;
        if(curScreen.equals("inGame")){
            setGameMenuVisible();
            restart();
        }
        else{
            setGameMenuInvisible();
        }
        this.notifyObservers();
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
}
