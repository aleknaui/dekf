package dekf;

public class Struct {
	
	// --------------------------------------------------------------------------------
	// Atributos
	// --------------------------------------------------------------------------------
	
	private String tipo;
	private SymbolTable symbolTable;
	
	// --------------------------------------------------------------------------------
	// Constructor 
	// --------------------------------------------------------------------------------
	
	public Struct( String tipo, SymbolTable symbolTable ){
		this.tipo = tipo;
	}
	
	// --------------------------------------------------------------------------------
	// Métodos
	// --------------------------------------------------------------------------------
	
	public String darTipo(){
		return tipo;
	}	
	
	public SymbolTable darSymbolTable(){
		return symbolTable;
	}

}
