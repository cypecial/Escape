import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class EditorView extends JPanel implements Observer, ActionListener {

    private EditorModel model;
    Timer myTimer;

    BufferedImage inGamePic = null;
    BufferedImage cutButtonPic = null;
    BufferedImage copyButtonPic = null;
    BufferedImage pasteButtonPic = null;
    BufferedImage moveButtonPic = null;
    BufferedImage undoButtonPic = null;
    BufferedImage redoButtonPic = null;
    BufferedImage blockButtonPic = null;
    BufferedImage resizeButtonPic = null;
    BufferedImage selectButtonPic = null;

    JButton cutButton = new JButton();
    JButton copyButton = new JButton();
    JButton pasteButton = new JButton();
    JButton moveButton = new JButton();
    JButton undoButton = new JButton();
    JButton redoButton = new JButton();
    JButton blockButton = new JButton();
    JButton resizeButton = new JButton();
    JButton selectButton = new JButton();


    public EditorView(EditorModel model) {
        this.model = model;
        this.model.addObserver(this);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addKeyListener(new EditorKeyController(this.model));

        try {
            this.inGamePic = ImageIO.read(new File("./Design/inGameBackground.png"));
            this.cutButtonPic = ImageIO.read(new File("./Design/cut.png"));
            this.copyButtonPic = ImageIO.read(new File("./Design/copy.png"));
            this.pasteButtonPic = ImageIO.read(new File("./Design/paste.png"));
            this.moveButtonPic = ImageIO.read(new File("./Design/move.png"));
            this.undoButtonPic = ImageIO.read(new File("./Design/undo.png"));
            this.redoButtonPic = ImageIO.read(new File("./Design/redo.png"));
            this.blockButtonPic = ImageIO.read(new File("./Design/block.png"));
            this.resizeButtonPic = ImageIO.read(new File("./Design/resize.png"));
            this.selectButtonPic = ImageIO.read(new File("./Design/select.png"));

            cutButton.setIcon(new ImageIcon(cutButtonPic));
            copyButton.setIcon(new ImageIcon(copyButtonPic));
            pasteButton.setIcon(new ImageIcon(pasteButtonPic));
            moveButton.setIcon(new ImageIcon(moveButtonPic));
            undoButton.setIcon(new ImageIcon(undoButtonPic));
            redoButton.setIcon(new ImageIcon(redoButtonPic));
            blockButton.setIcon(new ImageIcon(blockButtonPic));
            resizeButton.setIcon(new ImageIcon(resizeButtonPic));
            selectButton.setIcon(new ImageIcon(selectButtonPic));
        }
        catch (IOException e){
            System.out.println("error loading pic");
        }

        EditorMouseController controller = new EditorMouseController(model);
        this.addMouseListener(controller);
        this.addMouseMotionListener(controller);

        this.add(Box.createVerticalGlue());

        this.add(selectButton);
        this.add(blockButton);
        this.add(moveButton);
        this.add(resizeButton);
        this.add(cutButton);
        this.add(copyButton);
        this.add(pasteButton);
        this.add(undoButton);
        this.add(redoButton);

        selectButton.addActionListener(this);
        blockButton.addActionListener(this);
        moveButton.addActionListener(this);
        resizeButton.addActionListener(this);
        cutButton.addActionListener(this);
        copyButton.addActionListener(this);
        pasteButton.addActionListener(this);
        undoButton.addActionListener(this);
        redoButton.addActionListener(this);

        addButton(blockButton, this);
        addButton(selectButton, this);
        addButton(moveButton,this);
        addButton(cutButton,this);
        addButton(copyButton,this);
        addButton(pasteButton,this);
        addButton(resizeButton, this);
        addButton(undoButton,this);
        addButton(redoButton,this);


        setTransparent(selectButton);
        setTransparent(cutButton);
        setTransparent(copyButton);
        setTransparent(pasteButton);
        setTransparent(moveButton);
        setTransparent(undoButton);
        setTransparent(redoButton);
        setTransparent(blockButton);
        setTransparent(resizeButton);

        this.myTimer = new Timer(10, this);
        this.myTimer.start();

        this.model.editorFrame.add(this);
        setVisible(true);
    }

    private static void addButton(JButton button, Container container) {
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(button);
    }

    private static void setTransparent(JButton button){
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object evt = e.getSource();
        if(evt == moveButton){
            model.setTool("move");
            //model.move();
        }
        if(evt == cutButton){
            model.setTool("cut");
        }
        if(evt == copyButton){
            model.setTool("copy");
        }
        if(evt == pasteButton){
            model.setTool("paste");
        }
        if(evt == undoButton){
            model.undo();
        }
        if(evt == redoButton){
            model.redo();
        }
        if(evt == blockButton){
            model.setTool("block");
        }
        if(evt == selectButton){
            model.setTool("select");
        }
        if(evt == resizeButton){
            model.setTool("resize");
        }

    }

    public void paintComponent(Graphics g){
        //if(model.gameField == null) return;
        double factor = Math.ceil(-1.0 * model.getScreenX() / inGamePic.getWidth());
        g.drawImage(this.inGamePic, model.getScreenX() + (int) (factor - 1) * inGamePic.getWidth(), 0, inGamePic.getWidth(), this.getHeight(), null);
        g.drawImage(this.inGamePic, model.getScreenX() + (int) (factor * inGamePic.getWidth()), 0, inGamePic.getWidth(), this.getHeight(), null);
        GameField field = model.gameField;
        int dimX = field.getDimX();
        int dimY = field.getDimY();
        model.setBlockSize(this.getHeight() / dimY);

        //stop scrolling if end of map
        if (model.getScreenX() < -1 * (dimX * model.getBlockSize() - this.getWidth())) {
            model.setScreenX(-1 * (dimX * model.getBlockSize() - this.getWidth()));
        }
        if(model.getScreenX() >= model.toolBoxWidth){
            model.setScreenX(model.toolBoxWidth);
        }
        //draw grid
        for (int x = 0; x < dimX; x++) {
            for (int y = 0; y < dimY; y++) {
                g.setColor(Color.BLUE);
                g.drawRect(x * model.getBlockSize() + model.getScreenX(), y * model.getBlockSize(), model.getBlockSize(), model.getBlockSize());
            }
        }
        //draw obstacles
        ArrayList<Rectangle> obstacleArray = model.gameField.getObstacleArray();
        for(int i = 0; i < obstacleArray.size(); i++){
            g.setColor(Color.black);
            Rectangle rect = OperationLogic.getDrawRect(obstacleArray.get(i), model);
            g.fillRect(rect.x,rect.y,rect.width,rect.height);
        }

        //draw selectedObstacle
        if(!model.getSelectedObstacle().isEmpty() && model.getTool().equals("select")){
            g.setColor(Color.red);
            Rectangle rect = OperationLogic.getDrawRect(model.getSelectedObstacle(), model);
            g.drawRect(rect.x,rect.y,rect.width,rect.height);
        }

        //draw boxes when dragging
        if(model.getTool().equals("block") && model.pressedInField) {
            Rectangle mouseRect = OperationLogic.getSelectionRect(model.getInitialPos(),model.getLargestPt());
            for (int x = 0; x < dimX; x++) {
                for (int y = 0; y < dimY; y++) {
                    g.setColor(Color.black);
                    Rectangle rect = new Rectangle(x * model.getBlockSize() + model.getScreenX(), y * model.getBlockSize(), model.getBlockSize(), model.getBlockSize());
                    if (rect.intersects(mouseRect)) {
                        g.fillRect(rect.x, rect.y, rect.width, rect.height);
                    }
                }
            }
        }
        if(model.getTool().equals("move") && model.pressedInField && model.validMove){
            Rectangle rect = model.getSelectedMoveObstacle();
            int mx = model.getBlockMoveMx();
            int my = model.getBlockMoveMy();
            g.setColor(Color.black);
            g.fillRect( (mx-model.moveMxOffset) * model.getBlockSize() + model.getScreenX(),
                    (my - model.moveMyOffset) * model.getBlockSize(),
                    rect.width * model.getBlockSize(), rect.height * model.getBlockSize());
        }
        if(model.getTool().equals("resize")){
            Point bl = OperationLogic.getBL(model.getSelectedResizeObstacle());
            Point br = OperationLogic.getBR(model.getSelectedResizeObstacle());
            Point tr = OperationLogic.getTR(model.getSelectedResizeObstacle());
            Point tl = OperationLogic.getTL(model.getSelectedResizeObstacle());
            int mx = model.getBlockSizeMx();
            int my = model.getBlockSizeMy();
            //draw resizing block
            g.setColor(Color.black);
            if (model.getResizeCorner().equals("TL")) {
                //drag to NW
                if(mx <= br.x && my <= br.y){
                    g.fillRect(mx* model.getBlockSize() + model.getScreenX(), my* model.getBlockSize(), (br.x-mx+1)* model.getBlockSize(),(br.y-my+1)* model.getBlockSize());
                }
                //drag to SW
                else if(mx <= br.x && my > br.y){
                    g.fillRect(mx* model.getBlockSize() + model.getScreenX(), (br.y+1)* model.getBlockSize(), (br.x-mx+1)* model.getBlockSize(), (my-br.y)* model.getBlockSize());
                }
                //drag to NE
                else if(mx > br.x && my <= br.y){
                    g.fillRect((br.x+1)* model.getBlockSize() + model.getScreenX(), my* model.getBlockSize(), (mx-br.x)* model.getBlockSize(), (br.y-my+1)* model.getBlockSize());
                }
                //drag to SE
                else{
                    g.fillRect((br.x+1)* model.getBlockSize() + model.getScreenX(), (br.y+1)* model.getBlockSize(), (mx-br.x)* model.getBlockSize(), (my-br.y)* model.getBlockSize());
                }
            } else if (model.getResizeCorner().equals("TR")) {
                //drag to NE
                if(mx >= bl.x && my <= bl.y){
                    g.fillRect(bl.x* model.getBlockSize() + model.getScreenX(), my* model.getBlockSize(), (mx-bl.x+1)* model.getBlockSize(), (bl.y-my+1)* model.getBlockSize());
                }
                //SE
                else if(mx >= bl.x && my > bl.y){
                    g.fillRect(bl.x* model.getBlockSize() + model.getScreenX(), (bl.y+1)* model.getBlockSize(), (mx-bl.x+1)* model.getBlockSize(), (my-bl.y)* model.getBlockSize());
                }
                //NW
                else if(mx < bl.x && my <= bl.y){
                    g.fillRect(mx* model.getBlockSize() + model.getScreenX(), my* model.getBlockSize(), (bl.x-mx)* model.getBlockSize(), (bl.y-my+1)* model.getBlockSize());
                }
                //SW
                else{
                    g.fillRect(mx* model.getBlockSize() + model.getScreenX(), (bl.y+1)* model.getBlockSize(), (bl.x-mx)* model.getBlockSize(), (my-bl.y)* model.getBlockSize());
                }
            } else if (model.getResizeCorner().equals("BL")) {
                //SW
                if(mx <= tr.x && my >= tr.y){
                    g.fillRect(mx* model.getBlockSize() + model.getScreenX(), tr.y* model.getBlockSize(), (tr.x-mx+1)* model.getBlockSize(), (my-tr.y+1)* model.getBlockSize());
                }
                //NW
                else if(mx <= tr.x && my < tr.y){
                    g.fillRect(mx* model.getBlockSize() + model.getScreenX(), my* model.getBlockSize(), (tr.x-mx+1)* model.getBlockSize(), (tr.y-my)* model.getBlockSize());
                }
                //SE
                else if(mx > tr.x && my >= tr.y){
                    g.fillRect((tr.x+1)* model.getBlockSize() + model.getScreenX(), tr.y* model.getBlockSize(), (mx-tr.x)* model.getBlockSize(), (my-tr.y+1)* model.getBlockSize());
                }
                //NE
                else{
                    g.fillRect((tr.x+1)* model.getBlockSize() + model.getScreenX(), my* model.getBlockSize(), (mx-tr.x)* model.getBlockSize(), (tr.y-my)* model.getBlockSize());
                }
            } else if (model.getResizeCorner().equals("BR")) {
                //SE
                if(mx >= tl.x && my >= tl.y){
                    g.fillRect(tl.x* model.getBlockSize() + model.getScreenX(), tl.y* model.getBlockSize(), (mx-tl.x+1)* model.getBlockSize(), (my-tl.y+1)* model.getBlockSize());
                }
                //NE
                else if(mx >= tl.x && my < tl.y){
                    g.fillRect(tl.x* model.getBlockSize() + model.getScreenX(), my* model.getBlockSize(), (mx-tl.x+1)* model.getBlockSize(), (tl.y-my)* model.getBlockSize());
                }
                //SW
                else if(mx < tl.x && my >= tl.y){
                    g.fillRect(mx* model.getBlockSize() + model.getScreenX(), tl.y* model.getBlockSize(), (tl.x-mx)* model.getBlockSize(), (my-tl.y+1)* model.getBlockSize());
                }
                //NW
                else{
                    g.fillRect(mx* model.getBlockSize() + model.getScreenX(), my* model.getBlockSize(), (tl.x-mx)* model.getBlockSize(), (tl.y-my)* model.getBlockSize());
                }
            }
        }

        //draw mouse
        for (int x = 0; x < dimX; x++) {
            for (int y = 0; y < dimY; y++) {
                g.setColor(Color.WHITE);
                Rectangle rect = new Rectangle(x * model.getBlockSize() + model.getScreenX(), y * model.getBlockSize(), model.getBlockSize(), model.getBlockSize());
                if (rect.contains(model.getMousePt())) {
                    if(model.getTool().equals("paste")){
                        int mx = (model.getMousePt().x + (-1 * model.getScreenX()))/model.getBlockSize();
                        int my = model.getMousePt().y/model.getBlockSize();
                        if(model.gameField.checkValidPaste(model.getSelectedObstacle(),mx,my)) {
                            g.setColor(Color.green);
                        }
                        else{
                            g.setColor(Color.red);
                        }
                    }
                    g.drawRect(rect.x, rect.y, rect.width, rect.height);
                }

            }
        }

        //draw toolbox
        g.setColor(Color.WHITE);

        g.fillRect(0,0, model.toolBoxWidth, this.getHeight());
    }

    @Override
    public void update(Object observable) {
        this.requestFocusInWindow();
        this.repaint();
    }
}
