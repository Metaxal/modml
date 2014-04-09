import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Modifier;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

/* Creates a subMenu in which appear the classes that are instances of _classObject
 * Selecting one makes the _classloader
 */
public class ClassLoadMenu implements ActionListener {
	protected String _nom;
	protected ClassOpener _classOpener;
	protected Class _classObject;
	private Interface _interface;
	
	private JMenu _menu;
	private JMenuItem _openItem;
	private JMenuItem[] _classItems;
	
	public JMenu getMenu() { return _menu; }

	public ClassLoadMenu(String nom, ClassOpener classOpener,  Class classObject, Interface interf) {
		_nom = nom;
		_classOpener = classOpener;
		_classObject = classObject;
		_interface = interf;
		
		_menu	= new JMenu(_nom);
		_openItem = new JMenuItem("Open...");
		_openItem.addActionListener(this);
		_menu.add(_openItem);
		
		_menu.addSeparator();
		
		File f = new File(".");
		String[] fs = f.list();
		int nbNoms = 0;
		for(int i = 0; i < fs.length; i++) // compter le nombre de fichiers .class
			if(fs[i].endsWith(".class")) {
				String nomf = fs[i].substring(0, fs[i].lastIndexOf(".class"));
				if(correctClass(nomf))
					nbNoms++;
			}
		_classItems = new JRadioButtonMenuItem[nbNoms];
		// Pour grouper les RadioButton
	    ButtonGroup group = new ButtonGroup();			
		for(int i = 0, j = 0; i < fs.length; i++)
			if(fs[i].endsWith(".class")) {
				String nomf = fs[i].substring(0, fs[i].lastIndexOf(".class"));
				if(correctClass(nomf)) {
					_classItems[j] = new JRadioButtonMenuItem(nomf);
					_classItems[j].addActionListener(this);
					_menu.add(_classItems[j]);
					group.add(_classItems[j]);
					j++;
				}
			}
	}
	
	public boolean correctClass(String classe) {
		try {
			Class cls = Class.forName(classe);
			return _classObject.isAssignableFrom(cls) &&
				!cls.isInterface() &&
				!Modifier.isAbstract(cls.getModifiers()) &&
				Modifier.isPublic(cls.getModifiers());
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public void actionPerformed(ActionEvent event) {
		System.out.println("ClassLoadMenu item [" + event.getActionCommand() + "] was pressed.");
		if (event.getSource() == _openItem) {		
			_interface.getFileChooser().setFileFilter(new ClassFileFilter());
			int val = _interface.getFileChooser().showOpenDialog(_interface.getFrame());
			if (val == JFileChooser.APPROVE_OPTION) {
				File file = _interface.getFileChooser().getSelectedFile();
				System.out.println("File " + file.getName() + " opened");
				openClass(file.getName());
			}
			return;
		}
		for(int i = 0; i < _classItems.length; i++){
			if(event.getSource() == _classItems[i]) {
				openClass(_classItems[i].getText()+".class");
				return;
			}
		}
	}
	
	public void openClass(String filename) {
		Class c;
		JLabel statusBar = _interface.getStatusBar();
		try {
			String name = filename.substring(0, filename.indexOf('.'));
			System.out.println("name="+name);
			c = Class.forName(name);
			if( !_classObject.isAssignableFrom(c))
				statusBar.setText("Cannot load " + filename + ": class is not of the correct type");
			else {
				_classOpener.classOpened(c.newInstance(), this);
				statusBar.setText("Class " + name + " loaded");
			}
		} catch (ClassNotFoundException e) {
			statusBar.setText("Cannot load " + filename + ": class not found");
			e.printStackTrace();
		} catch (InstantiationException e) {
			statusBar.setText("Cannot load " + filename + ": bad instanciation");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			statusBar.setText("Cannot load " + filename + ": illegal access");
			e.printStackTrace();
		}
	}
	

}
