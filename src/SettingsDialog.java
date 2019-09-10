import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SettingsDialog extends JDialog implements ActionListener{
    JButton okButton, cancleButton;
    Model model;
    static final int FPS_MIN = 10;
    static final int FPS_MAX = 60;
    static final int FPS_INIT = 30;    //initial frames per second

    static final int SS_MIN = 1;
    static final int SS_MAX = 10;
    static final int SS_INIT = 2;    //initial frames per second

    int fps,ss;

    private JLabel ssText, fpsText;

    public SettingsDialog(JFrame parent, Model model) {
        super(parent, "Game Settings", true);

        this.fps = 30;
        this.ss = 1;
        this.ssText = new JLabel("2");
        this.fpsText = new JLabel("30");

        JSlider fpsSlider = new JSlider(JSlider.HORIZONTAL,
                FPS_MIN, FPS_MAX, FPS_INIT);
        JSlider ssSlider = new JSlider(JSlider.HORIZONTAL,
                SS_MIN, SS_MAX, SS_INIT);

        fpsSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                fps = source.getValue();
                fpsText.setText(Integer.toString(fps));
            }
        });

        ssSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                ss = source.getValue();
                ssText.setText(Integer.toString(ss));
            }
        });

        //Turn on labels at major tick marks.
        fpsSlider.setMajorTickSpacing(10);
        fpsSlider.setMinorTickSpacing(5);
        fpsSlider.setPaintTicks(true);
        fpsSlider.setPaintLabels(true);

        ssSlider.setMajorTickSpacing(1);
        ssSlider.setPaintTicks(true);
        ssSlider.setPaintLabels(true);

        this.model = model;

        if (parent != null) {
            Dimension parentSize = parent.getSize();
            Point p = parent.getLocation();
            setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
        }

        JPanel panel = new JPanel(new FormLayout());

        panel.add(new JLabel("Scroll Speed: "));
        //panel.add(ssText);
        panel.add(ssSlider);

        panel.add(new JLabel("Frames Per Second: "));
        //panel.add(fpsText);
        panel.add(fpsSlider);

        this.add(panel, BorderLayout.NORTH);

        JPanel buttonPane = new JPanel();
        this.okButton = new JButton("OK");
        this.cancleButton = new JButton("Cancel");

        buttonPane.add(cancleButton);
        buttonPane.add(okButton);

        cancleButton.addActionListener(this);
        okButton.addActionListener(this);

        this.add(buttonPane, BorderLayout.SOUTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
        requestFocus();
    }
    public void actionPerformed(ActionEvent e) {
        Object evt = e.getSource();
        if(evt == this.okButton){
            model.setFPS(fps);
            model.setSS(ss);
            JOptionPane.showMessageDialog(this,
                    "Success! Settings Changed.");

        }
        setVisible(false);
        dispose();

    }
}