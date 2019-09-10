
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;

public class MainFrame extends JFrame implements Observer, ActionListener{

    private Model model;

    Timer myTimer;

    JMenuBar menuBar;
    JMenuItem newGame;
    JMenuItem mainMenu;
    JMenuItem help;
    JMenuItem endGame;
    JMenu menu;

    JButton play;
    JButton pause;

    JLabel scoreLabel;
    JLabel score;

    JComponent curView;
    /**
     * Create a new View.
     */
    public MainFrame(Model model) {
        // Set up the window.
        this.setTitle("Escape!");
        this.setMinimumSize(new Dimension(800, 610));
        this.setSize(800, 610);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());

        this.menu = new JMenu("Game");
        this.newGame = new JMenuItem("New Game");
        this.mainMenu = new JMenuItem("Main Menu");
        this.help = new JMenuItem("Help");
        this.endGame = new JMenuItem("End Game");
        this.newGame.addActionListener(this);
        this.mainMenu.addActionListener(this);
        this.help.addActionListener(this);
        this.endGame.addActionListener(this);
        this.menu.add(this.newGame);
        this.menu.add(this.mainMenu);
        this.menu.add(this.help);
        this.menu.add(this.endGame);
        this.menuBar = new JMenuBar();

        this.play = new JButton("Play");
        this.pause = new JButton("Pause");
        this.play.addActionListener(this);
        this.pause.addActionListener(this);

        this.scoreLabel = new JLabel("Score:");
        this.score = new JLabel("0");

        this.menuBar.add(this.menu);
        this.menuBar.add(this.play);
        this.menuBar.add(this.pause);
        this.menuBar.add(this.scoreLabel);
        this.menuBar.add(this.score);

        this.play.setVisible(false);
        this.pause.setVisible(false);
        this.scoreLabel.setVisible(false);
        this.score.setVisible(false);

        this.add(this.menuBar, BorderLayout.NORTH);

        // Hook up this observer so that it will be notified when the model
        // changes.
        this.model = model;
        model.addObserver(this);
        model.mainFrame = this;

        this.myTimer = new Timer(1000/this.model.getFPS(), this);
        this.myTimer.start();
    }

    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();

        if(source == this.newGame) {
            this.model.setScreen("inGame");
        }

        if(source == this.play){
            this.model.setPaused(false);
        }

        if(source == this.pause){
            this.model.setPaused(true);
        }

        if(source == this.mainMenu) {
            this.model.setScreen("mainMenu");
        }

        if(source == this.help) {
            this.model.setScreen("help");
        }

        if(source == this.endGame) {
            System.exit(0);
        }
    }

    public void changeView(JComponent view) {
        for(Component c : this.getContentPane().getComponents()) {
            if(c.equals(view)) {
                return;
            }
        }
        for(Component c : this.getContentPane().getComponents()) {
            if(c.equals(curView)) {
                this.remove(c);
                curView.setVisible(false);
            }
        }
        this.add(view);
        view.setVisible(true);
        view.requestFocus();
        this.curView = view;
    }
    /**
     * Update with data from the model.
     */
    public void update(Object observable) {
        // XXX Fill this in with the logic for updating the view when the model
        // changes.
        this.repaint();
    }
}
