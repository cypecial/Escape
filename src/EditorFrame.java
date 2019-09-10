
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

public class EditorFrame extends JFrame implements Observer, ActionListener{

    private EditorModel model;

    Timer myTimer;
    JMenuBar menuBar;

    JButton load,save,clear,setDim;
    JLabel curToolLabel;

    /**
     * Create a new View.
     */
    public EditorFrame(EditorModel model) {
        // Set up the window.
        this.setTitle("Level Editor");
        this.setMinimumSize(new Dimension(800, 610));
        this.setSize(800, 610);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.setLayout(new BorderLayout());

        this.menuBar = new JMenuBar();
        this.clear = new JButton("Clear Field");
        this.load = new JButton("Load");
        this.save = new JButton("Save");
        this.setDim = new JButton("Set Dimensions");
        this.clear.addActionListener(this);
        this.load.addActionListener(this);
        this.save.addActionListener(this);
        this.setDim.addActionListener(this);

        this.curToolLabel = new JLabel("Current Tool: block");

        this.menuBar.add(this.clear);
        this.menuBar.add(this.load);
        this.menuBar.add(this.save);
        this.menuBar.add(this.setDim);
        this.menuBar.add(this.curToolLabel);

        this.add(this.menuBar, BorderLayout.NORTH);

        this.model = model;
        model.addObserver(this);
        model.editorFrame = this;

        this.myTimer = new Timer(10, this);
        this.myTimer.start();
    }

    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if(source == this.clear){
            model.gameField.clearField();
            model.addToHistory();
        }
        if(source == this.load) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
            fileChooser.setFileFilter(filter);
            fileChooser.setCurrentDirectory(new File("./savedLevels"));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                model.load(selectedFile.getAbsolutePath());
            }
        }

        if(source == this.save) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
            fileChooser.setFileFilter(filter);
            fileChooser.setCurrentDirectory(new File("./savedLevels"));
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                model.save(selectedFile.getAbsolutePath());
            }
        }
        if(source == this.setDim){
            SetDimDialog dimDialog = new SetDimDialog(this, model);
        }
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
