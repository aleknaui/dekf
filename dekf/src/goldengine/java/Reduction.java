package goldengine.java;

import java.util.*;

import dekf.DekfParser;

/*
 * Licensed Material - Property of Matthew Hawkins (hawkini@barclays.net)
 *
 * GOLDParser - code ported from VB - Author Devin Cook. All rights reserved.
 *
 * No modifications to this code are allowed without the permission of the author.
 */
/**-------------------------------------------------------------------------------------------<br>
 *
 *      Source File:    Reduction.java<br>
 *
 *      Author:         Devin Cook, Matthew Hawkins<br>
 *
 *      Description:    A representation of a Reduction. An instance of this class will hold
 *						the resulting parse tree once created, and if the source file has been
 *						accepted.<br>
 *
 *
 *-------------------------------------------------------------------------------------------<br>
 *
 *      Revision List<br>
 *<pre>
 *      Author          Version         Description
 *      ------          -------         -----------
 *      MPH             1.0             First Issue</pre><br>
 *
 *-------------------------------------------------------------------------------------------<br>
 *
 *      IMPORT: java.util<br>
 *
 *-------------------------------------------------------------------------------------------<br>
 */
public class Reduction
{
    private Vector pTokens = new Vector();
    private int pTokenCount;
    private Rule pParentRule;
    private int pTag;
    
    // Atributos semánticos
    
    private String type = "";
    private String value = "";
    
    // Para la generación de código
    
    public String code = "";
    public String true_l = "";
    public String false_l = "";
    public String done_l = "";
    public int stackLength = -1;
    public String init_code = ""; 

    /***************************************************************
 	 *
 	 * setTokenCount
 	 *
 	 * This method implicitly sets the number of tokens in this
     * Reduction. If the value is 0 or less, then we clear the tokens
     * in this reduction and set the number of tokens to 0.
 	 * @param value The number of tokens in this reduction.
 	 ***************************************************************/
    public void setTokenCount(int value)
    {
        if(value < 1)
        {
            pTokens.clear();
            pTokenCount = 0;
        }
        else
        {
            pTokenCount = value;
            pTokens.setSize(value);
            for(int i=0; i<value; i++)
                pTokens.addElement(new Object());
            {
            }
        }
    }

    /***************************************************************
 	 *
 	 * getTokenCount
 	 *
 	 * This method returns the number of tokens.
 	 * @return The number of tokens
 	 ***************************************************************/
    public int getTokenCount() { return pTokenCount; }

    /***************************************************************
 	 *
 	 * getParentRule
 	 *
 	 * This method returns the rule associated with this Reduction.
 	 * @return The rule associated with this Reduction.
 	 ***************************************************************/
    public Rule getParentRule() { return pParentRule; }

    /***************************************************************
 	 *
 	 * getTag
 	 *
 	 * Will return the tag associated with this Reduction.
 	 * @return The tag associated with this Reduction.
 	 ***************************************************************/
    public int getTag() { return pTag; }

    /***************************************************************
 	 *
 	 * setParentRule
 	 *
 	 * Will set the Rule of this Reduction to the one passed in.
 	 * @param newRule The parent Rule of this Reduction.
 	 ***************************************************************/
    public void setParentRule(Rule newRule) { pParentRule = newRule; }

    /***************************************************************
 	 *
 	 * setTag
 	 *
 	 * Will set the tag of this Reduction to that passed in.
 	 * @param value The value of the tag.
 	 ***************************************************************/
    public void setTag(int value) { pTag = value; }

    /***************************************************************
 	 *
 	 * getToken
 	 *
 	 * Will retrieve a Token at the specified index. The index
     * specified must be equal or greater than 0 and less than
     * the current number of Tokens.
 	 * @param index The index of the token in this Reduction.
 	 * @return The Token at the specified index.
 	 ***************************************************************/
    public Token getToken(int index)
    {
        if((index >= 0) & (index < pTokenCount))
        {
            return (Token)pTokens.elementAt(index);
        }
        else
        {
            return null;
        }
    }

    /***************************************************************
 	 *
 	 * setToken
 	 *
 	 * Will place a Token at the specified index. It will only do this
     * if the index is greater or equal to 0, and less than the
     * token count.
 	 * @param index The index to place the token at.
     * @param value The token to set at the index.
 	 ***************************************************************/
    public void setToken(int index, Token value)
    {
        if((index >= 0) & (index < pTokenCount))
        {
            pTokens.setElementAt(value, index);
        }
    }
    
    /**
     * Crea un arreglo con la data de las diferentes partes de la reducción.
     * @return Un arreglo con objetos. Si la parte i es un no-terminal, se trata de una Reduction.  
     */
    public ArrayList getParts(){
    	ArrayList result = new ArrayList<Reduction>();
    	
    	Object last = null;
    	
    	for( int i = 0; i < getTokenCount(); i++ ){
    		Token t = getToken(i);
    		if( t.getKind()==0 ){
    			Reduction r = (Reduction) t.getData();
    			if( r.equals(last) ) continue;
    			else{
    				result.add( r );
    				last = r;
    			}
    		} else{
    			String s = (String) t.getData();
    			if( s.equals(last) ) continue;
    			else{
    				result.add( s );
    				last = s;
    			}
    		}
    	}
    	
    	return result;
    }
     
    public String toString(){
    	return pParentRule.getText();
    }
    
    public void setType( String type ){
    	this.type = type;
    }
    
    public String getType(){
    	return type;
    }
    
    public String getValue(){
    	return value;
    }
    
    public void setValue(String value){
    	this.value = value;
    }
    
}
