import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.border.EmptyBorder;


public class MenuView extends JPanel implements Observer, ActionListener{
    private Model model;
    BufferedImage mainMenuPic = null;
    BufferedImage playButtonPic = null;
    BufferedImage loadButtonPic = null;
    BufferedImage levelEditorButtonPic = null;
    BufferedImage helpButtonPic = null;
    BufferedImage settingButtonPic = null;

    JButton playButton = new JButton();
    JButton loadButton = new JButton();
    JButton levelEditorButton = new JButton();
    JButton helpButton = new JButton();
    JButton settingButton = new JButton();

    public MenuView(Model model) {
        this.model = model;
        this.model.addObserver(this);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(0, 0, 600/7, 0));

        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                //align border for menu buttons
                setBorder(new EmptyBorder(0, 0, getHeight()/7, 0));
            }
            @Override
            public void componentMoved(ComponentEvent e) {}
            @Override
            public void componentShown(ComponentEvent e) {}
            @Override
            public void componentHidden(ComponentEvent e) {}
        });

        try {
            this.mainMenuPic = ImageIO.read(new File("./Design/mainMenu.png"));
            this.playButtonPic = ImageIO.read(new File("./Design/playButton.png"));
            this.loadButtonPic = ImageIO.read(new File("./Design/loadButton.png"));
            this.levelEditorButtonPic = ImageIO.read(new File("./Design/levelEditorButton.png"));
            this.helpButtonPic = ImageIO.read(new File("./Design/helpButton.png"));
            this.settingButtonPic = ImageIO.read(new File("./Design/settingButton.png"));

            playButton.setIcon(new ImageIcon(playButtonPic));
            loadButton.setIcon(new ImageIcon(loadButtonPic));
            levelEditorButton.setIcon(new ImageIcon(levelEditorButtonPic));
            helpButton.setIcon(new ImageIcon(helpButtonPic));
            settingButton.setIcon(new ImageIcon(settingButtonPic));

        }catch (Exception e){
            System.out.println(e);
        }

        this.add(Box.createVerticalGlue());

        playButton.addActionListener(this);
        loadButton.addActionListener(this);
        levelEditorButton.addActionListener(this);
        helpButton.addActionListener(this);
        settingButton.addActionListener(this);

        addButton(playButton, this);
        addButton(loadButton, this);
        addButton(levelEditorButton, this);
        addButton(settingButton, this);
        addButton(helpButton, this);

        setTransparent(playButton);
        setTransparent(loadButton);
        setTransparent(levelEditorButton);
        setTransparent(helpButton);
        setTransparent(settingButton);

        this.setVisible(false);
    }

    private static void setTransparent(JButton button){
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
    }
    private static void addButton(JButton button, Container container) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(button);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if(source == this.playButton) {
            this.model.setScreen("inGame");
        }
        if(source == this.loadButton) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("./savedLevels"));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                model.load(selectedFile.getAbsolutePath());
            }
        }
        if(source == this.levelEditorButton) {
            this.model.editorFrame.setVisible(true);
        }
        if(source == this.helpButton) {
            this.model.setScreen("help");
        }
        if(source == this.settingButton) {
            SettingsDialog settingsDialog = new SettingsDialog(model.mainFrame, model);
        }
    }

    public void paintComponent(Graphics g){
        g.drawImage(this.mainMenuPic, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    @Override
    public void update(Object observable) {
        if(this.model.getScreen().equals("mainMenu")) {
            this.model.mainFrame.changeView(this);
        }
    }
}
