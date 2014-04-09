import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

public class Interface implements ClassOpener{
    private JFrame                  _frame;
    private Menu                    _menu;
    private final JFileChooser      _fileChooser = new JFileChooser(".");
    private Viewport                _viewport;
    private JLabel                  _statusBar;
    private ObjectEngine            _objectEngine;

    public JFrame getFrame() { return _frame; }
    public Menu getMenu() { return _menu; }
    public JFileChooser getFileChooser() { return _fileChooser; }
    public Viewport getViewport() { return _viewport; }
    public JLabel getStatusBar() { return _statusBar; }
    public ObjectEngine getObjectEngine() { return _objectEngine; }

    public Interface() {
        String lookAndFeel = null;
        lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {}

        _menu = new Menu(this);
        _viewport = new Viewport(this);
        _statusBar = new JLabel("Welcome !");
        _frame = new JFrame("Module Machine Learning - ObjectEngine");

        _frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }});

        _frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e)  {
                ObjectEngine mli = getObjectEngine();
                if(mli != null)
                    mli.keyPressed(e.getKeyChar());

            }
            public void keyReleased(KeyEvent e)  {
                ObjectEngine mli = getObjectEngine();
                if(mli != null)
                    mli.keyReleased(e.getKeyChar());

            }
            public void keyTyped(KeyEvent e)  {
                ObjectEngine mli = getObjectEngine();
                if(mli != null)
                    mli.keyTyped(e.getKeyChar());

            }});


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        _frame.setBounds((screenSize.width - (screenSize.width / 2)) / 2,
                (screenSize.height - (screenSize.height / 2)) / 2,
                screenSize.width / 2,
                screenSize.height / 2);

        _frame.setJMenuBar(_menu);
        _frame.getContentPane().setLayout(new BorderLayout());
        _frame.getContentPane().add(_viewport, BorderLayout.CENTER);
        _frame.getContentPane().add(_statusBar, BorderLayout.SOUTH);
        _frame.setVisible(true);

        classOpened(new ObjectEngine(), this);
    }

    public void classOpened(Object object, Object caller) {
        if(_objectEngine != null)
            _objectEngine.terminate(); // supprimer les menus spécifiques
        _objectEngine = (ObjectEngine) object;
        _objectEngine.initInterface(this); // important !
        _objectEngine.write();
        _viewport.draw();
        String nom = object.getClass().getName();
        _frame.setTitle("Module Machine Learning - " + nom);
        _statusBar.setText("Class " + nom + " loaded");
    }

    public void redraw() {
        _viewport.draw();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Interface();
                // ici, on pourrait mettre le .class à charger: args[0]
            }
        });
    }
}
