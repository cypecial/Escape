import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by yiping on 2017-07-07.
 */
public class OperationLogic {
    public OperationLogic(){

    }
    public static boolean isNum(String strNum) {
        boolean ret = true;
        try {

            Integer.parseInt(strNum);

        }catch (NumberFormatException e) {
            ret = false;
        }
        return ret;
    }
    public static Point getTL(Rectangle rect){
        return new Point(rect.x, rect.y);
    }
    public static Point getTR(Rectangle rect){
        return new Point(rect.width + rect.x - 1, rect.y);
    }
    public static Point getBL(Rectangle rect){
        return new Point(rect.x, rect.height + rect.y - 1);
    }
    public static Point getBR(Rectangle rect){
        return new Point(rect.width + rect.x - 1, rect.height + rect.y - 1);
    }

    public static Rectangle getDrawRect(Rectangle rect, Model model){
        int rx = rect.x * model.getBlockSize() + model.getScreenX();
        int ry = rect.y * model.getBlockSize();
        int rw = rect.width * model.getBlockSize();
        int rh = rect.height * model.getBlockSize();
        return new Rectangle(rx,ry,rw,rh);

    }
    public static Rectangle getDrawRect(Rectangle rect, EditorModel model){
        int rx = rect.x * model.getBlockSize() + model.getScreenX();
        int ry = rect.y * model.getBlockSize();
        int rw = rect.width * model.getBlockSize();
        int rh = rect.height * model.getBlockSize();
        return new Rectangle(rx,ry,rw,rh);
    }

    public static Rectangle getResizeRect(Rectangle obstacle, String resizeCorner, int mx, int my){
        Point bl = OperationLogic.getBL(obstacle);
        Point br = OperationLogic.getBR(obstacle);
        Point tr = OperationLogic.getTR(obstacle);
        Point tl = OperationLogic.getTL(obstacle);
        Rectangle rect = new Rectangle();
        if (resizeCorner.equals("TL")) {
            //drag to NW
            if(mx <= br.x && my <= br.y){
                rect.setBounds(mx, my, br.x-mx+1,br.y-my+1);
            }
            //drag to SW
            else if(mx <= br.x && my > br.y){
                rect.setBounds(mx, br.y+1, br.x-mx+1, my-br.y);
            }
            //drag to NE
            else if(mx > br.x && my <= br.y){
                rect.setBounds(br.x+1, my, mx-br.x, br.y-my+1);
            }
            //drag to SE
            else{
                rect.setBounds(br.x+1, br.y+1, mx-br.x, my-br.y);
            }
        } else if (resizeCorner.equals("TR")) {
            //drag to NE
            if(mx >= bl.x && my <= bl.y){
                rect.setBounds(bl.x, my, mx-bl.x+1, bl.y-my+1);
            }
            //SE
            else if(mx >= bl.x && my > bl.y){
                rect.setBounds(bl.x, bl.y+1, mx-bl.x+1, my-bl.y);
            }
            //NW
            else if(mx < bl.x && my <= bl.y){
                rect.setBounds(mx, my, bl.x-mx, bl.y-my+1);
            }
            //SW
            else{
                rect.setBounds(mx, bl.y+1, bl.x-mx, my-bl.y);
            }
        } else if (resizeCorner.equals("BL")) {
            //SW
            if(mx <= tr.x && my >= tr.y){
                rect.setBounds(mx, tr.y, tr.x-mx+1, my-tr.y+1);
            }
            //NW
            else if(mx <= tr.x && my < tr.y){
                rect.setBounds(mx, my, tr.x-mx+1, tr.y-my);
            }
            //SE
            else if(mx > tr.x && my >= tr.y){
                rect.setBounds(tr.x+1, tr.y, mx-tr.x, my-tr.y+1);
            }
            //NE
            else{
                rect.setBounds(tr.x+1, my, mx-tr.x, tr.y-my);
            }
        } else if (resizeCorner.equals("BR")) {
            //SE
            if(mx >= tl.x && my >= tl.y){
                rect.setBounds(tl.x, tl.y, mx-tl.x+1, my-tl.y+1);
            }
            //NE
            else if(mx >= tl.x && my < tl.y){
                rect.setBounds(tl.x, my, mx-tl.x+1, tl.y-my);
            }
            //SW
            else if(mx < tl.x && my >= tl.y){
                rect.setBounds(mx, tl.y, tl.x-mx, my-tl.y+1);
            }
            //NW
            else{
                rect.setBounds(mx, my, tl.x-mx, tl.y-my);
            }
        }
        return rect;
    }
    public static Rectangle getSelectionRect(Point p1, Point p2){
        int x1 = Math.min(p1.x, p2.x);
        int x2 = Math.max(p1.x, p2.x);
        int y1 = Math.min(p1.y, p2.y);
        int y2 = Math.max(p1.y, p2.y);
        return new Rectangle(x1, y1, x2-x1, y2-y1);
    }

    public static GameField importLevelFile(String path){
        String curLine;
        GameField gameField = null;
        ArrayList<String> textData = new ArrayList();
        try{
            BufferedReader levelFile = new BufferedReader(new FileReader(path));
            //Read line and skip comments
            while ((curLine = levelFile.readLine()) != null)   {
                if (!curLine.contains("#"))
                    textData.add(curLine);
            }
            //First line of text file indicates dimension of world
            int dimX = Integer.parseInt(textData.get(0).split(",")[0].trim());
            int dimY = Integer.parseInt(textData.get(0).split(",")[1].trim());
            //Create the game field
            gameField = new GameField(dimX,dimY);
            //Char starting position

            textData.remove(0);
            //import obastacles from text file
            importObstacles(textData, gameField);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return gameField;
    }
    /*
        Import obstacles from data array
    */
    private static void importObstacles(ArrayList<String> data, GameField gameField){
        for(int i = 0; i < data.size(); i++){
            String line = data.get(i);
            int x1 = Integer.parseInt(line.split(",")[0].trim());
            int y1 = Integer.parseInt(line.split(",")[1].trim());
            int x2 = Integer.parseInt(line.split(",")[2].trim());
            int y2 = Integer.parseInt(line.split(",")[3].trim());
//            System.out.println(x1 + ", " + y1 + ", " + x2 + ", " + y2);
            gameField.setObstacle(x1,y1,x2,y2);
        }
    }
}
