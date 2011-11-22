package dekf;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class SymbolTable {

	// --------------------------------------------------------------------------------
	// Atributos
	// --------------------------------------------------------------------------------
	
	int correlative = -1;
	
	public static SymbolTable ambito;
	
	private String returnType;
	private Hashtable<String, Variable> variables;
	private Hashtable<String, Metodo> metodos;
	private Hashtable<String, Struct> structs;
	private Hashtable<String, Arreglo> arreglos;
	private Hashtable<String, SymbolTable> structTypes;
	private String tempKey;
	
	public SymbolTable parent;
	
	public boolean transparent = false;
	
	private Hashtable<String, Integer> positions = new Hashtable<String, Integer>();
	
	// --------------------------------------------------------------------------------
	// Constructores
	// --------------------------------------------------------------------------------
	
	public SymbolTable(){
		returnType = "void";
		variables = new Hashtable<String, Variable>();
		metodos = new Hashtable<String, Metodo>();
		structs = new Hashtable<String, Struct>();
		structTypes = new Hashtable<String, SymbolTable>();
		arreglos = new Hashtable<String, Arreglo>();
		
		tempKey = null;
		
		parent = null;
	}
	
	public SymbolTable( String returnType, SymbolTable parent ){
		this.returnType = returnType;
		
		variables = new Hashtable<String, Variable>();
		metodos = new Hashtable<String, Metodo>();
		structs = new Hashtable<String, Struct>();
		structTypes = new Hashtable<String, SymbolTable>();
		arreglos = new Hashtable<String, Arreglo>();
		
		tempKey = null;
		
		this.parent = parent;
	}
	
	// --------------------------------------------------------------------------------
	// Métodos
	// --------------------------------------------------------------------------------
	
	public void pop(){
		//System.out.println("pop()");
		if( parent == null ) System.out.println("One pop too many X_X");
		else ambito = parent;
	}
	
	public void set(boolean transparent){
		//System.out.println("set()");
		parent = ambito;
		ambito = this;
		ambito.transparent = transparent;
		if( transparent )
			correlative = parent.correlative;
	}
	
	public boolean varExists(){
		return false;
	}
	
	public Metodo getMethod( String fullName ){
		/*
		System.out.println("-----");
		Iterator<Map.Entry<String, Metodo>> it = metodos.entrySet().iterator();
		while (it.hasNext()) {
		Map.Entry<String, Metodo> e = (Map.Entry<String, Metodo>)it.next();
			System.out.println( e.getKey() );
		}
		//*/
		Metodo result = metodos.get(fullName);
		
		///*
		if( result != null )
			return result;
		else{
			if( parent != null ){
				return parent.getMethod(fullName);
			} else{
				return null;
			}
		}
		//*/
	}
	
	public String getReturnType(){
		return returnType;
	}
	
	public boolean addMethod( String fullname, String returnType ){
		if( getMethod( fullname ) != null ) return false;
		getGlobal().metodos.put( fullname, new Metodo(returnType, fullname) );
		return true;
	}
	
	public int nivel(){
		int contador = 0;
		for(SymbolTable i = this; i != null; i = i.parent){
			contador++;
		}
		return contador;
	}
	
	public SymbolTable getGlobal(){
		for(SymbolTable i = this; i != null; i = i.parent){
			if( i.parent == null ) return i;
		}
		System.out.println("Craso X_X");
		return null;
	}
	
	public boolean addVariable( String type, String nombre ){
		if( localTypeSearch(nombre) == null){
			if( type.startsWith("struct:") ){
				type = type.substring(7);
				SymbolTable st = globalStructTypeSearch(type);
				if( st == null ) return false;
				else{
					structs.put(nombre, new Struct( type, st ));
				}
			} else if( type.startsWith("array:") ){
				arreglos.put( nombre ,  new Arreglo(type.substring(6)) );
			} else{
				variables.put( nombre ,  new Variable(type) );
				/*
				System.out.println("-----");
				Iterator<Map.Entry<String, Variable>> it = variables.entrySet().iterator();
				while (it.hasNext()) {
				Map.Entry<String, Variable> e = (Map.Entry<String, Variable>)it.next();
					System.out.println( e.getKey() + ":" + e.getValue().darTipo() );
				}
				//*/
			}
			correlative++;
			positions.put( nombre, correlative );
			/*
			System.out.println("-----");
			Iterator<Map.Entry<String, Integer>> it = positions.entrySet().iterator();
			while (it.hasNext()) {
			Map.Entry<String, Integer> e = (Map.Entry<String, Integer>)it.next();
				System.out.println( e.getKey() + ":" + e.getValue() );
			}
			//*/
			return true;
		} else return false;
	}
	
	public String globalTypeSearch( String nombre ){
		
		String result = localTypeSearch(nombre);
		if( result == null ){
			if( parent != null ){
				return parent.globalTypeSearch(nombre);
			} else return null;
		} else return result;
	}
	
	public String localTypeSearch( String nombre ){
		Struct sFound = structs.get(nombre);
		if( sFound == null ){
			Arreglo aFound = arreglos.get(nombre);
			if( aFound == null ){
				Variable vFound = variables.get(nombre);
				if( vFound == null ) return null;
				else return vFound.darTipo();
			} else{
				return "array:" + aFound.darTipo();
			}
		} else{
			return "struct:" + sFound.darTipo();
		}
	}
	
	public SymbolTable localStructTypeSearch( String nombre ){
		return structTypes.get(nombre);
	}
	
	public SymbolTable globalStructTypeSearch( String nombre ){
		SymbolTable result = localStructTypeSearch(nombre);
		if( result == null ){
			if( parent != null ){
				return parent.globalStructTypeSearch(nombre);
			} else return null;
		} else return result;
	}
	
	public void setTempKey( String tempKey ){
		System.out.println(tempKey);
		this.tempKey = tempKey; 
	}
	
	public void clearTemp(){
		if( tempKey != null )
			structTypes.remove(tempKey);
		tempKey = null;
	}
	
	public boolean declareStruct( String nombre, SymbolTable st ){
		if( globalStructTypeSearch(nombre) == null ){
			structTypes.put(nombre, st);
			return true;
		} else return false;
	}
	
	public Integer getLocalIndex(String nombre){
		return positions.get(nombre);
	}
	
	public int getMethodIndex(String nombre){
		Integer local = getLocalIndex( nombre );
		if( local == null ){
			if( parent != null ){
				if( parent.transparent || (transparent && !parent.transparent) ){
					return parent.getMethodIndex(nombre);
				} else return -1;
			} else return -1;
		} else return local;
	}
	
}
