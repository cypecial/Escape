import java.awt.*;
import java.util.ArrayList;

/**
 * Created by yiping on 2017-06-27.
 */
public class GameField {
    private int dimX, dimY, blockSize;
    private ArrayList<Rectangle> obstacleArray;


    public GameField(int x, int y){
        this.dimX = x;
        this.dimY = y;
        this.blockSize = 54;
        this.obstacleArray = new ArrayList<>();
    }

    public void setBlockSize(int s){
        this.blockSize = s;
    }
    public int getBlockSize(){
        return blockSize;
    }

    public Boolean hasObstacle(int x, int y){
        for(Rectangle rect: obstacleArray){
            if(rect.contains(x,y)){
                return true;
            }
        }
        return false;
    }

    //create exact copy of field
    public GameField copy() {
        GameField ret = new GameField(dimX,dimY);
        for(int i = 0; i < obstacleArray.size(); i++){
            ret.obstacleArray.add(obstacleArray.get(i));
        }
        return ret;
    }

    public void removeObstacle(Rectangle obs){
        obstacleArray.remove(obs);
    }

    public Rectangle getCollisionObstacle(Point mp, EditorModel model){
        int mx = (mp.x - model.getScreenX())/getBlockSize();
        int my = mp.y/getBlockSize();
        Point relPoint = new Point(mx,my);
        for(int i = 0; i < obstacleArray.size(); i++){
            Rectangle rect = obstacleArray.get(i);
            if(rect.contains(relPoint)){
                return rect;
            }
        }
        return null;
    }
    public ArrayList<Rectangle> getObstacleArray(){
        return obstacleArray;
    }

    public Boolean checkValidResize(Rectangle obstacle, int mx, int my, String resizeCorner){
        Rectangle rect = OperationLogic.getResizeRect(obstacle, resizeCorner, mx, my);
        Boolean valid = true;
        if(!rect.isEmpty()) {
            for (int i = 0; i < obstacleArray.size(); i++) {
                if (obstacleArray.get(i).intersects(rect)) {
                    valid = false;
                }
            }
        }
        else{
            valid = false;
        }
        return valid;
    }
    public Boolean checkValidPaste(Rectangle rect, int mx, int my){
        Boolean valid = true;
        if(!rect.isEmpty()) {
            Rectangle obstacle = new Rectangle(mx, my, rect.width, rect.height);
            for (int i = 0; i < obstacleArray.size(); i++) {
                if (obstacleArray.get(i).intersects(obstacle)) {
                    valid = false;
                }
            }
            if (mx + obstacle.width > dimX || my + obstacle.height > dimY) {
                valid = false;
            }
        }
        else{
            valid = false;
        }
        return valid;
    }

    public void paste(Rectangle rect, int mx, int my){
        setObstacle(new Rectangle(mx, my, rect.width, rect.height));
    }

    public GameField resizeField(int dx, int dy){
        GameField ret = new GameField(dx,dy);
        for(int i = 0; i < obstacleArray.size(); i++){
            Rectangle rect = obstacleArray.get(i);
            if(rect.x + rect.width <= dx && rect.y + rect.height <= dy){
                ret.setObstacle(rect);
            }
        }

        return ret;
    }

    public int getDimX(){
        return dimX;
    }
    public int getDimY(){
        return dimY;
    }

    public void setObstacle(Rectangle rect){
        obstacleArray.add(rect);
    }

    public void setObstacle(int x1, int y1, int x2, int y2){
        Rectangle rect = new Rectangle(x1,y1,x2-x1+1,y2-y1+1);
        obstacleArray.add(rect);
    }

    public void clearField(){
        this.obstacleArray = new ArrayList<Rectangle>();
    }

}
