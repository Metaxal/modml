import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;

public class TrafficJamParameters {
    private TrafficJamEngine _engine;
    private JFrame _frame;

    final JTextField _nbCasesText;
    final JTextField _speedText;

    public TrafficJamParameters(TrafficJamEngine engine) {
        _engine = engine;
        _nbCasesText = new JTextField(10);
        _speedText = new JTextField(10);

        setTexts();

        JPanel p = new JPanel();

        p.setLayout(new GridLayout(0,2));

        p.add(new JLabel("# Columns", JLabel.LEFT));
        p.add(_nbCasesText);

        p.add(new JLabel("Playing speed", JLabel.LEFT));
        p.add(_speedText);

        JButton ok     = new JButton("  OK  ");
        JButton cancel = new JButton("Cancel");

        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    int nbCases = (new Integer(_nbCasesText.getText())).intValue();
                    _engine.initTableau(nbCases);

                    int speed = (new Integer(_speedText.getText())).intValue();
                    _engine.setPlayingSpeed(speed);
                } catch (Exception e) {}

                setTexts();

                _engine.getInterface().redraw();
                _frame.setVisible(false);
            }});

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                _frame.setVisible(false);
            }});

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(ok);
        buttonPanel.add(cancel);

        JPanel buttonSpacePanel = new JPanel();
        buttonSpacePanel.setLayout(new BorderLayout());
        buttonSpacePanel.add(new JLabel(" ", JLabel.RIGHT), BorderLayout.NORTH);
        buttonSpacePanel.add(buttonPanel, BorderLayout.CENTER);

        _frame = new JFrame("TrafficJam parameters");

        JRootPane root = _frame.getRootPane();
        root.setDefaultButton(ok);

        _frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("TrafficJam parameters closed");
            }});

        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setVgap(10);
        _frame.getContentPane().setLayout(borderLayout);

        _frame.getContentPane().add(new JLabel(" ", JLabel.RIGHT), BorderLayout.NORTH);
        _frame.getContentPane().add(p, BorderLayout.CENTER);
        _frame.getContentPane().add(buttonSpacePanel, BorderLayout.SOUTH);
        _frame.getContentPane().add(new JLabel(" ", JLabel.RIGHT), BorderLayout.EAST);
        _frame.getContentPane().add(new JLabel(" ", JLabel.RIGHT), BorderLayout.WEST);

        _frame.pack();
        _frame.setResizable(false);
        Utility.center(_frame);
    }

    public void setTexts() {
        _nbCasesText.setText("" + _engine.nbCases());
        _speedText.setText("" + _engine.playingSpeed());
    }

    public JFrame getFrame() { return _frame; }
}
