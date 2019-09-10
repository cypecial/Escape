import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.BorderUIResource;


public class HelpView extends JPanel implements Observer, ActionListener{
    private Model model;
    private BufferedImage gameControlsPic = null;
    private BufferedImage levelEditorControlsPic = null;
    private BufferedImage nextPic = null;
    private BufferedImage prevPic = null;
    private BufferedImage backPic = null;
    private BufferedImage prevUAPic = null;
    private BufferedImage nextUAPic = null;

    private Boolean gameControls, levelEditorControls;

    private JButton nextButton, prevButton, backButton;

    public HelpView(Model model) {
        this.model = model;
        this.model.addObserver(this);
        this.gameControls = true;
        this.levelEditorControls = false;

        this.nextButton = new JButton();
        this.prevButton = new JButton();
        this.backButton = new JButton();

        JPanel buttonPane = new JPanel(new FlowLayout());
        this.setLayout(new BorderLayout());

        try {
            this.gameControlsPic = ImageIO.read(new File("./Design/gameControls.png"));
            this.levelEditorControlsPic = ImageIO.read(new File("./Design/levelEditorControls.png"));
            this.nextPic = ImageIO.read(new File("./Design/next.png"));
            this.backPic = ImageIO.read(new File("./Design/back.png"));
            this.prevPic = ImageIO.read(new File("./Design/prev.png"));
            this.prevUAPic = ImageIO.read(new File("./Design/prevUA.png"));
            this.nextUAPic = ImageIO.read(new File("./Design/nextUA.png"));

            this.nextButton.setIcon(new ImageIcon(nextPic));
            this.prevButton.setIcon(new ImageIcon(prevUAPic));
            this.backButton.setIcon(new ImageIcon(backPic));

        }catch (Exception e) {
            System.out.println(e);
        }

        nextButton.addActionListener(this);
        prevButton.addActionListener(this);
        backButton.addActionListener(this);

        buttonPane.add(prevButton);
        buttonPane.add(backButton);
        buttonPane.add(nextButton);

        setTransparent(prevButton);
        setTransparent(nextButton);
        setTransparent(backButton);

        buttonPane.setOpaque(false);
        this.add(buttonPane,BorderLayout.SOUTH);

        this.setVisible(false);
    }

    private static void setTransparent(JButton button){
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
    }


    @Override
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if(source == nextButton){
            if(gameControls) {
                gameControls = false;
                levelEditorControls = true;
                prevButton.setIcon(new ImageIcon(prevPic));
                nextButton.setIcon(new ImageIcon(nextUAPic));
            }
            model.notifyObservers();
        }
        if(source == backButton){
            model.setScreen("mainMenu");
        }
        if(source == prevButton){
            if(levelEditorControls) {
                gameControls = true;
                levelEditorControls = false;
                nextButton.setIcon(new ImageIcon(nextPic));
                prevButton.setIcon(new ImageIcon(prevUAPic));
            }
            model.notifyObservers();
        }
    }

    public void paintComponent(Graphics g){
        if(gameControls) {
            g.drawImage(this.gameControlsPic, 0, 0, this.getWidth(), this.getHeight(), null);
        }
        if(levelEditorControls){
            g.drawImage(this.levelEditorControlsPic, 0, 0, this.getWidth(), this.getHeight(), null);
        }
    }

    @Override
    public void update(Object observable) {
        if(this.model.getScreen().equals("help")) {
            this.model.mainFrame.changeView(this);
        }
    }
}
