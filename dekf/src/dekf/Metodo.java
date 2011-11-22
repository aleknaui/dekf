package dekf;

/**
 * Clase que almacena la información de un método en la tabla de símbolos
 * @author AleKnaui
 *
 */
public class Metodo {
	
	// --------------------------------------------------------------------------------
	// Atributos
	// --------------------------------------------------------------------------------
	
	private String tipo;
	private String argumentType;
	
	// --------------------------------------------------------------------------------
	// Constructor 
	// --------------------------------------------------------------------------------
	
	public Metodo( String tipo, String fullName ){
		this.tipo = tipo;
		String argumentType = fullName.substring(fullName.indexOf(':')+1,fullName.length());
		this.argumentType = argumentType;
	}
	
	// --------------------------------------------------------------------------------
	// Métodos
	// --------------------------------------------------------------------------------
	
	public String darTipo(){
		return tipo;
	}
	
	public String darArgumentType(){
		return argumentType;
	}

}
