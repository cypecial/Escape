import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class SetDimDialog extends JDialog implements ActionListener {
    JButton okButton, cancleButton;
    EditorModel model;
    JTextField xText, yText;

    private int xInt, yInt;

    public SetDimDialog(JFrame parent, EditorModel model) {
        super(parent, "Set Game Field Dimensions", true);

        this.xInt = 50;
        this.yInt = 10;

        this.model = model;
        if (parent != null) {
            Dimension parentSize = parent.getSize();
            Point p = parent.getLocation();
            setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
        }
        JPanel panel = new JPanel();

        this.xText = new JTextField(Integer.toString(model.gameField.getDimX()),10);
        this.yText = new JTextField(Integer.toString(model.gameField.getDimY()),10);

        panel.add(new JLabel("X: "));
        panel.add(xText);
        panel.add(new JLabel("Y: "));
        panel.add(yText);

        this.add(panel);

        JPanel buttonPane = new JPanel();
        this.okButton = new JButton("OK");
        this.cancleButton = new JButton("Cancel");

        buttonPane.add(cancleButton);
        buttonPane.add(okButton);

        cancleButton.addActionListener(this);
        okButton.addActionListener(this);
        xText.addActionListener(this);
        yText.addActionListener(this);

        this.add(buttonPane, BorderLayout.SOUTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
        requestFocus();
    }
    public void actionPerformed(ActionEvent e) {
        Object evt = e.getSource();
        if(evt == this.okButton){
            String xInput = xText.getText();
            String yInput = yText.getText();
            if (OperationLogic.isNum(xInput) && OperationLogic.isNum(yInput)) {
                xInt = Integer.parseInt(xInput);
                yInt = Integer.parseInt(yInput);
                model.changeDim(xInt,yInt);
                JOptionPane.showMessageDialog(this,
                        "Success! Dimension Changed.");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid Dimensions. Try again.",
                        "Error Message",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        setVisible(false);
        dispose();
    }

}