import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GameView extends JPanel implements Observer, ActionListener{

    private Model model;
    Timer myTimer;
    BufferedImage holderPic = null;
    BufferedImage inGamePic = null;
    BufferedImage charPic = null;
    BufferedImage gameOverPic = null;
    BufferedImage winPic = null;
    BufferedImage pausedPic = null;
    BufferedImage exitButtonPic = null;
    BufferedImage restartButtonPic = null;

    JButton exitButton = new JButton();
    JButton restartButton = new JButton();

    public GameView(Model model) {
        this.model = model;
        this.model.addObserver(this);
        addKeyListener(new GameKeyController(this.model));

        this.myTimer = new Timer(1000/model.getFPS(), this);
        this.myTimer.start();

        this.setBorder(new EmptyBorder(400, 0, 0, 0));

        setVisible(false);

        try {
            this.holderPic = ImageIO.read(new File("./Design/background.png"));
            this.inGamePic = ImageIO.read(new File("./Design/inGameBackground.png"));
            this.charPic = ImageIO.read(new File("./Design/char.png"));
            this.gameOverPic = ImageIO.read(new File("./Design/gameOver.png"));
            this.winPic = ImageIO.read(new File("./Design/win.png"));
            this.pausedPic = ImageIO.read(new File("./Design/paused.png"));
            this.exitButtonPic = ImageIO.read(new File("./Design/exitButton.png"));
            this.restartButtonPic = ImageIO.read(new File("./Design/restartButton.png"));
            exitButton.setIcon(new ImageIcon(exitButtonPic));
            restartButton.setIcon(new ImageIcon(restartButtonPic));
        }
        catch (IOException e){
            System.out.println("error loading pic");
        }
        this.add(exitButton);
        this.add(restartButton);

        //this.add(Box.createVerticalGlue());

        exitButton.addActionListener(this);
        restartButton.addActionListener(this);

        setTransparent(exitButton);
        setTransparent(restartButton);

        exitButton.setVisible(false);
        restartButton.setVisible(false);
    }

    private static void setTransparent(JButton button){
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object evt = e.getSource();
        if(evt == myTimer){
            model.run();
        }
        if(evt == exitButton){
            model.setScreen("mainMenu");
        }
        if(evt == restartButton){
            model.restart();
        }
    }

    public void paintComponent(Graphics g){
        if(this.model.inGame) {
            exitButton.setVisible(false);
            restartButton.setVisible(false);
            //Draw background for scrolling
            double factor = Math.ceil(-1.0 * this.model.getScreenX() / inGamePic.getWidth());
            g.drawImage(this.inGamePic, this.model.getScreenX() + (int) (factor - 1) * inGamePic.getWidth(), 0, inGamePic.getWidth(), this.getHeight(), null);
            g.drawImage(this.inGamePic, this.model.getScreenX() + (int) (factor * inGamePic.getWidth()), 0, inGamePic.getWidth(), this.getHeight(), null);

            GameField field = this.model.gameField;
            int dimX = field.getDimX();
            int dimY = field.getDimY();
            model.setBlockSize(this.getHeight() / dimY);

            //stop scrolling if end of map
            if (this.model.getScreenX() <= -1 * (dimX * model.getBlockSize() - this.getWidth())) {
                this.model.setEndOfMap(true);
            }

            //draw obstacles
            ArrayList<Rectangle> obstacleArray = model.gameField.getObstacleArray();
            for(int i = 0; i < obstacleArray.size(); i++){
                g.setColor(Color.black);
                Rectangle rect = OperationLogic.getDrawRect(obstacleArray.get(i), model);
                g.fillRect(rect.x,rect.y,rect.width,rect.height);
            }
            //draw char
            int charX = this.model.getScreenX() + this.model.getCharX() * model.getBlockSize();
            int charY = this.model.getCharY() * model.getBlockSize();

            //moves out of left screen
            if (charX <= 0) {
                this.model.lose = true;
                this.model.inGame = false;
            } else {
                g.drawImage(this.charPic, charX, charY, model.getBlockSize()-10, model.getBlockSize(), null);
            }
        }
        if(this.model.lose){
            g.drawImage(this.holderPic, 0, 0, this.getWidth(), this.getHeight(), null);
            g.drawImage(this.gameOverPic, 0, 0, this.getWidth(), this.getHeight(), null);
            exitButton.setVisible(true);
            restartButton.setVisible(true);
        }
        if(this.model.win){
            g.drawImage(this.holderPic, 0, 0, this.getWidth(), this.getHeight(), null);
            g.drawImage(this.winPic, 0, 0, this.getWidth(), this.getHeight(), null);
            exitButton.setVisible(true);
            restartButton.setVisible(true);
        }
        if(this.model.paused){
            g.drawImage(this.holderPic, 0, 0, this.getWidth(), this.getHeight(), null);
            g.drawImage(this.pausedPic, 0, 0, this.getWidth(), this.getHeight(), null);
        }

    }

    @Override
    public void update(Object observable) {
        // XXX Fill this in with the logic for updating the view when the model
        // changes.
        if(this.model.getScreen().equals("inGame")) {
            this.model.mainFrame.changeView(this);
        }

        this.requestFocus();
        this.repaint();
    }
}
