package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import goldengine.java.*;

import com.Ostermiller.Syntax.HighlightedDocument;

import dekf.DekfParser;

public class Main extends JFrame implements ActionListener{
	
	// --------------------------------------------------------------------------------
	// Atributos
	// --------------------------------------------------------------------------------
	
	/** El documento que se está editando */
	private HighlightedDocument documento = new HighlightedDocument();
	
	/** Text Pane en el que se despliega el texto */
	private JTextPane textPane = new JTextPane(documento);
	
	/** Barra de Menú */
	private JMenuBar menuBar;
	
	/** Menús */
	private JMenu menuArchivo, menuCompilador;
	
	/** Elementos del menú */
	private JMenuItem opcNuevo, opcAbrir, opcGuardar, opcGuardarComo, opcArbolSin;
	
	/** Nombre del archivo actual. De ser un archivo nuevo se utiliza la cadena vacía. */
	private String nombreArchivo = "";
	
	/** Consola del editor */
	private RedirectedFrame consola;
	
	
	// --------------------------------------------------------------------------------
	// Constructor
	// --------------------------------------------------------------------------------
	
    /**
     * Inicializa el IDE
     */
    public Main(){
    	
        // Título
        super("dekf IDE");
        setLocation(50, 50);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Look and Feel nativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          } catch(Exception e) {
            System.out.println("Error setting native LAF: " + e);
          }

        // Barra de desplazamiento
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(620, 460));

        // Componentes
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(scrollPane, BorderLayout.CENTER);
        setContentPane(contentPane);

        // Barra de Menú
        menuBar = new JMenuBar();
        
        menuArchivo = new JMenu("Archivo");
        menuArchivo.setMnemonic( KeyEvent.VK_A );
        menuBar.add(menuArchivo);
        
        opcNuevo = new JMenuItem("Nuevo", KeyEvent.VK_N);
        opcNuevo.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_N, ActionEvent.CTRL_MASK ) );
        opcNuevo.addActionListener(this);
        opcAbrir = new JMenuItem("Abrir", KeyEvent.VK_A);
        opcAbrir.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_O, ActionEvent.CTRL_MASK ) );
        opcAbrir.addActionListener(this);
        opcGuardar = new JMenuItem("Guardar", KeyEvent.VK_G);
        opcGuardar.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_S, ActionEvent.CTRL_MASK ) );
        opcGuardar.addActionListener(this);
        opcGuardarComo = new JMenuItem("Guardar Como", KeyEvent.VK_C);
        opcGuardarComo.setAccelerator( KeyStroke.getKeyStroke("F12") );
        opcGuardarComo.addActionListener(this);
        
        menuArchivo.add(opcNuevo);
        menuArchivo.add(opcAbrir);
        menuArchivo.add(opcGuardar);
        menuArchivo.add(opcGuardarComo);
        
        menuCompilador = new JMenu("Compilador");
        menuCompilador.setMnemonic( KeyEvent.VK_C );
        menuBar.add(menuCompilador);
        
        opcArbolSin = new JMenuItem("Generar Árbol Sintáctico", KeyEvent.VK_S);
        opcArbolSin.setAccelerator( KeyStroke.getKeyStroke( "F8" ) );
        opcArbolSin.addActionListener(this);
        menuCompilador.add(opcArbolSin);
        
        setJMenuBar(menuBar);
        
        // Envía el focus siempre al textPane
        addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                // focus magic
                textPane.requestFocus();
            }
        });

        // Utiliza la coloración de Java
        documento.setHighlightStyle(HighlightedDocument.JAVA_STYLE);
        
        // Crea la consola
        consola = new RedirectedFrame(this, true, true, "Consola", 620, 200);

        // Empaca y muestra
        pack();
        setVisible(true);
        
        
    }
    
	// --------------------------------------------------------------------------------
	// Métodos
	// --------------------------------------------------------------------------------
    
    @Override
    public void dispose(){
    	if(this.promptGuardar())
    		System.exit(0);
    }

    /**
     * Inicia la aplicación
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        // create the demo
        Main frame = new Main();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem source =  (JMenuItem) e.getSource();
		if( source == opcNuevo )
			nuevo();
		else if( source == opcAbrir )
			abrir();
		else if( source == opcGuardar )
			guardar();
		else if( source == opcGuardarComo )
			guardarComo();
		else if( source == opcArbolSin )
			generarArbolSintactico();
		else
			System.out.println("WTH");
	}
	
	/**
	 * Parsea el texto en el editor y muestra ya sea
	 * los errores o el árbol.
	 */
	private void generarArbolSintactico(){
		if( promptGuardar() ){
			Reduction result = DekfParser.parse(nombreArchivo);
			if( result != null ){
				consola.setText("");
				DefaultMutableTreeNode raiz = new DefaultMutableTreeNode( "program" );
				DefaultTreeModel arbol = new DefaultTreeModel( raiz );
				crearArbol(arbol, raiz, result);
				new TreeDisplay((DefaultMutableTreeNode )arbol.getRoot());
			}
		}
	}
	
	/**
	 * Convierte una reducción y la convierte en un árbol
	 * desplegable con JTree
	 */
	public static void crearArbol(DefaultTreeModel tree, DefaultMutableTreeNode root, Reduction reduccion){
        
        for(int i=0; i<reduccion.getTokenCount(); i++)
        {
            if(reduccion.getToken(i).getKind()==0){
                 Reduction red = (Reduction)reduccion.getToken(i).getData();
                String rule = red.getParentRule().definition();
                String rule_name = reduccion.getToken(i).getName();
                //System.out.println(" + "+rule_name+ " + ."+rule+".");
     
                DefaultMutableTreeNode child = new DefaultMutableTreeNode(reduccion.getToken(i).getName());
                crearArbol(tree, child, (Reduction)reduccion.getToken(i).getData());
                tree.insertNodeInto(child, root, i);
             }
    
            else{
                String token_name = reduccion.getToken(i).getName();
                String token_lexeme = (String)reduccion.getToken(i).getData();
                String txt_in_leaf = "";
                 if(token_name.equals(token_lexeme) == true && token_name.equals("id")==false){
                    txt_in_leaf = token_name+"";
                }
                else{
                     txt_in_leaf = token_name + " = "+ token_lexeme;
                }
                //System.out.println(" - "+txt_in_leaf);
                DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(txt_in_leaf);
                 tree.insertNodeInto(leaf, root, i);
            }
        }
        
        
    }
	
	/**
	 * Crea un nuevo archivo.
	 */
	private void nuevo(){
		if(promptGuardar()){
			textPane.setText("");
			nombreArchivo = "";
		}
	}
	
	/**
	 * Permite abrir un archivo ya existente.
	 */
	private void abrir(){
		if(promptGuardar()){
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Abrir");
			chooser.setMultiSelectionEnabled(false);
			chooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
			int returnVal = chooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				textPane.setText(leerArchivo( chooser.getSelectedFile().getAbsolutePath() ));
				nombreArchivo = chooser.getSelectedFile().getAbsolutePath();
			}
		    }
	}
	
	/**
	 * Permite guardar lo que se está trabajando.
	 */
	private void guardar(){
		if( nombreArchivo.equals("") ){
			guardarComo();
			return;
		}
		if(guardarArchivo(nombreArchivo))
			JOptionPane.showMessageDialog(this, "El archivo se ha guardado exitosamente", "Guardar", JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(this, "El archivo no se pudo guardar", "Guardar", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Guarda el trabajo en un nuevo archivo.
	 */
	private void guardarComo(){
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Guardar Como");
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			if(guardarArchivo(chooser.getSelectedFile().getAbsolutePath())){
				JOptionPane.showMessageDialog(this, "El archivo se ha guardado exitosamente", "Guardar Como", JOptionPane.INFORMATION_MESSAGE);
				nombreArchivo = chooser.getSelectedFile().getAbsolutePath();
			}
			else
				JOptionPane.showMessageDialog(this, "El archivo no se pudo guardar", "Guardar Como", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	/**
	 * Evalúa si se ha modificado el documento y de ser así, se pregunta si se desea guardar.
	 * @return false Si se canceló la operación.
	 */
	private boolean promptGuardar(){
		if( ! leerArchivo( this.nombreArchivo ).equals( textPane.getText() ) ){
			int guardar = JOptionPane.showConfirmDialog(this, "Se han hecho cambios en el documento.\n¿Desea guardar los cambios?", "Cambios Realizados", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			if( guardar == JOptionPane.YES_OPTION ){
				if( nombreArchivo.equals("") ) guardarComo();
				else guardar();
			}
			else if( guardar == JOptionPane.NO_OPTION );
			else{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Método que lee el texto de un archivo y lo regresa como un String
	 * @param path La dirección del archivo a leer. "" significa que es un documento nuevo.
	 * @return El texto que contiene el archivo.
	 */
	private String leerArchivo( String path ){
		
		String textoArchivo = "";
		if( path.equals("") ) return textoArchivo;
		
		try {
			BufferedReader in = new BufferedReader( new FileReader(path) );
			String lineaActual = in.readLine();
			while( lineaActual != null ){
				textoArchivo += lineaActual + '\n';
				lineaActual = in.readLine();
			}
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error de apertura - path inválido: " + path);
		} catch (IOException e) {
			System.out.println("Error de lectura.");
		}
		
		return textoArchivo;
	}
	
	/**
	 * Método que guarda el contenido del editor en el archivo especificado por el path.
	 * @param path La dirección del archivo a guardar.
	 * @return true Si la operación se realizó exitosamente. false Si no se guardó.
	 */
	private boolean guardarArchivo( String path ){
		
		try {
			PrintWriter out = new PrintWriter( new FileWriter(path) );
			String[] lineas = textPane.getText().split("\n");
			for( String linea : lineas ){
				out.println(linea);
			}
			out.close();
			return true;
		} catch (IOException e) {
			System.out.println("Error de apertura - path inválido: " + path);
			return false;
		}
		
	}
	
}
