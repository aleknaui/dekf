package dekf;

import java.io.*;
import java.util.ArrayList;

import goldengine.java.*;

/*
 * Licensed Material - Property of Matthew Hawkins (hawkini@4email.net) 
 */
 
public class DekfParser2 implements GPMessageConstants
{
 
    private interface SymbolConstants 
    {
       final int SYMBOL_EOF               =  0;  // (EOF)
       final int SYMBOL_ERROR             =  1;  // (Error)
       final int SYMBOL_WHITESPACE        =  2;  // (Whitespace)
       final int SYMBOL_COMMENTEND        =  3;  // (Comment End)
       final int SYMBOL_COMMENTLINE       =  4;  // (Comment Line)
       final int SYMBOL_COMMENTSTART      =  5;  // (Comment Start)
       final int SYMBOL_MINUS             =  6;  // '-'
       final int SYMBOL_EXCLAM            =  7;  // '!'
       final int SYMBOL_EXCLAMEQ          =  8;  // '!='
       final int SYMBOL_PERCENT           =  9;  // '%'
       final int SYMBOL_AMPAMP            = 10;  // '&&'
       final int SYMBOL_LPARAN            = 11;  // '('
       final int SYMBOL_RPARAN            = 12;  // ')'
       final int SYMBOL_TIMES             = 13;  // '*'
       final int SYMBOL_COMMA             = 14;  // ','
       final int SYMBOL_DOT               = 15;  // '.'
       final int SYMBOL_DIV               = 16;  // '/'
       final int SYMBOL_SEMI              = 17;  // ';'
       final int SYMBOL_LBRACKET          = 18;  // '['
       final int SYMBOL_RBRACKET          = 19;  // ']'
       final int SYMBOL_LBRACE            = 20;  // '{'
       final int SYMBOL_PIPEPIPE          = 21;  // '||'
       final int SYMBOL_RBRACE            = 22;  // '}'
       final int SYMBOL_PLUS              = 23;  // '+'
       final int SYMBOL_LT                = 24;  // '<'
       final int SYMBOL_LTEQ              = 25;  // '<='
       final int SYMBOL_EQ                = 26;  // '='
       final int SYMBOL_EQEQ              = 27;  // '=='
       final int SYMBOL_GT                = 28;  // '>'
       final int SYMBOL_GTEQ              = 29;  // '>='
       final int SYMBOL_BOOLEAN           = 30;  // boolean
       final int SYMBOL_CHAR              = 31;  // char
       final int SYMBOL_CHARACTER         = 32;  // character
       final int SYMBOL_CLASS             = 33;  // class
       final int SYMBOL_ELSE              = 34;  // else
       final int SYMBOL_FALSE             = 35;  // false
       final int SYMBOL_ID                = 36;  // id
       final int SYMBOL_IF                = 37;  // if
       final int SYMBOL_INT               = 38;  // int
       final int SYMBOL_NUM               = 39;  // num
       final int SYMBOL_PROGRAM           = 40;  // Program
       final int SYMBOL_RETURN            = 41;  // return
       final int SYMBOL_STRUCT            = 42;  // struct
       final int SYMBOL_TRUE              = 43;  // true
       final int SYMBOL_VOID              = 44;  // void
       final int SYMBOL_WHILE             = 45;  // while
       final int SYMBOL_ARG               = 46;  // <arg>
       final int SYMBOL_ARG1              = 47;  // <arg1>
       final int SYMBOL_ARG2              = 48;  // <arg2>
       final int SYMBOL_BLOCK             = 49;  // <block>
       final int SYMBOL_BLOCKL            = 50;  // <blockL>
       final int SYMBOL_BOOL_LITERAL      = 51;  // <bool_literal>
       final int SYMBOL_CHAR_LITERAL      = 52;  // <char_literal>
       final int SYMBOL_COND_OP           = 53;  // <cond_op>
       final int SYMBOL_DECLARATION       = 54;  // <declaration>
       final int SYMBOL_DECLARATIONK      = 55;  // <declarationK>
       final int SYMBOL_EQ_OP             = 56;  // <eq_op>
       final int SYMBOL_EXPRESSION        = 57;  // <expression>
       final int SYMBOL_EXPRESSIONL       = 58;  // <expressionL>
       final int SYMBOL_INT_LITERAL       = 59;  // <int_literal>
       final int SYMBOL_LITERAL           = 60;  // <literal>
       final int SYMBOL_LOCATION          = 61;  // <location>
       final int SYMBOL_LOCATION1         = 62;  // <location1>
       final int SYMBOL_LOCATION2         = 63;  // <location2>
       final int SYMBOL_METHODCALL        = 64;  // <methodCall>
       final int SYMBOL_METHODDECHEADER   = 65;  // <methodDecHeader>
       final int SYMBOL_METHODDECLARATION = 66;  // <methodDeclaration>
       final int SYMBOL_METHODTYPE        = 67;  // <methodType>
       final int SYMBOL_MUL_OP            = 68;  // <mul_op>
       final int SYMBOL_MULTIEXP          = 69;  // <multiExp>
       final int SYMBOL_NEGADOREXP        = 70;  // <negadorExp>
       final int SYMBOL_PARAMETER         = 71;  // <parameter>
       final int SYMBOL_PARAMETER1        = 72;  // <parameter1>
       final int SYMBOL_PARAMETER2        = 73;  // <parameter2>
       final int SYMBOL_PARAMETERTYPE     = 74;  // <parameterType>
       final int SYMBOL_PROGRAM2          = 75;  // <Program>
       final int SYMBOL_REL_OP            = 76;  // <rel_op>
       final int SYMBOL_RELEXP            = 77;  // <relExp>
       final int SYMBOL_STATEMENT         = 78;  // <statement>
       final int SYMBOL_STATEMENTK        = 79;  // <statementK>
       final int SYMBOL_STRUCTDECHEADER   = 80;  // <structDecHeader>
       final int SYMBOL_STRUCTDECLARATION = 81;  // <structDeclaration>
       final int SYMBOL_SUMA_OP           = 82;  // <suma_op>
       final int SYMBOL_SUMAEXP           = 83;  // <sumaExp>
       final int SYMBOL_VAL               = 84;  // <val>
       final int SYMBOL_VARDECLARATION    = 85;  // <varDeclaration>
       final int SYMBOL_VARDECLARATION1   = 86;  // <varDeclaration1>
       final int SYMBOL_VARDECLARATIONK   = 87;  // <varDeclarationK>
    };

    private interface RuleConstants
    {
       final int RULE_PROGRAM_CLASS_PROGRAM_LBRACE_RBRACE   =  0;  // <Program> ::= class Program '{' <declarationK> '}'
       final int RULE_DECLARATIONK                          =  1;  // <declarationK> ::= <declaration> <declarationK>
       final int RULE_DECLARATIONK2                         =  2;  // <declarationK> ::= 
       final int RULE_DECLARATION                           =  3;  // <declaration> ::= <structDeclaration>
       final int RULE_DECLARATION2                          =  4;  // <declaration> ::= <varDeclaration>
       final int RULE_DECLARATION3                          =  5;  // <declaration> ::= <methodDeclaration>
       final int RULE_VARDECLARATION_ID_SEMI                =  6;  // <varDeclaration> ::= <methodType> id <varDeclaration1> ';'
       final int RULE_VARDECLARATION1_LBRACKET_NUM_RBRACKET =  7;  // <varDeclaration1> ::= '[' num ']'
       final int RULE_VARDECLARATION1                       =  8;  // <varDeclaration1> ::= 
       final int RULE_STRUCTDECLARATION_LBRACE_RBRACE       =  9;  // <structDeclaration> ::= <structDecHeader> '{' <varDeclarationK> '}'
       final int RULE_STRUCTDECHEADER_STRUCT_ID             = 10;  // <structDecHeader> ::= struct id
       final int RULE_VARDECLARATIONK                       = 11;  // <varDeclarationK> ::= <varDeclaration> <varDeclarationK>
       final int RULE_VARDECLARATIONK2                      = 12;  // <varDeclarationK> ::= 
       final int RULE_METHODTYPE_INT                        = 13;  // <methodType> ::= int
       final int RULE_METHODTYPE_CHAR                       = 14;  // <methodType> ::= char
       final int RULE_METHODTYPE_BOOLEAN                    = 15;  // <methodType> ::= boolean
       final int RULE_METHODTYPE_VOID                       = 16;  // <methodType> ::= void
       final int RULE_METHODTYPE_STRUCT_ID                  = 17;  // <methodType> ::= struct id
       final int RULE_METHODTYPE                            = 18;  // <methodType> ::= <structDeclaration>
       final int RULE_METHODDECLARATION                     = 19;  // <methodDeclaration> ::= <methodDecHeader> <block>
       final int RULE_METHODDECHEADER_ID_LPARAN_RPARAN      = 20;  // <methodDecHeader> ::= <methodType> id '(' <parameter1> ')'
       final int RULE_PARAMETER1                            = 21;  // <parameter1> ::= <parameter2>
       final int RULE_PARAMETER12                           = 22;  // <parameter1> ::= 
       final int RULE_PARAMETER2                            = 23;  // <parameter2> ::= <parameter>
       final int RULE_PARAMETER2_COMMA                      = 24;  // <parameter2> ::= <parameter2> ',' <parameter>
       final int RULE_PARAMETER_ID                          = 25;  // <parameter> ::= <parameterType> id
       final int RULE_PARAMETER_ID_LBRACKET_RBRACKET        = 26;  // <parameter> ::= <parameterType> id '[' ']'
       final int RULE_PARAMETERTYPE_INT                     = 27;  // <parameterType> ::= int
       final int RULE_PARAMETERTYPE_CHAR                    = 28;  // <parameterType> ::= char
       final int RULE_PARAMETERTYPE_BOOLEAN                 = 29;  // <parameterType> ::= boolean
       final int RULE_BLOCK_LBRACE_RBRACE                   = 30;  // <block> ::= '{' <varDeclarationK> <statementK> '}'
       final int RULE_STATEMENTK                            = 31;  // <statementK> ::= <statement> <statementK>
       final int RULE_STATEMENTK2                           = 32;  // <statementK> ::= 
       final int RULE_STATEMENT                             = 33;  // <statement> ::= <block>
       final int RULE_STATEMENT_IF_LPARAN_RPARAN            = 34;  // <statement> ::= if '(' <expression> ')' <block> <blockL>
       final int RULE_STATEMENT_WHILE_LPARAN_RPARAN         = 35;  // <statement> ::= while '(' <expression> ')' <block>
       final int RULE_STATEMENT_RETURN_SEMI                 = 36;  // <statement> ::= return <expressionL> ';'
       final int RULE_STATEMENT_EQ                          = 37;  // <statement> ::= <location> '=' <expression>
       final int RULE_STATEMENT_SEMI                        = 38;  // <statement> ::= <expressionL> ';'
       final int RULE_BLOCKL_ELSE                           = 39;  // <blockL> ::= else <block>
       final int RULE_BLOCKL                                = 40;  // <blockL> ::= 
       final int RULE_EXPRESSIONL                           = 41;  // <expressionL> ::= <expression>
       final int RULE_EXPRESSIONL2                          = 42;  // <expressionL> ::= 
       final int RULE_EXPRESSION                            = 43;  // <expression> ::= <relExp> <cond_op> <expression>
       final int RULE_EXPRESSION2                           = 44;  // <expression> ::= <relExp>
       final int RULE_RELEXP                                = 45;  // <relExp> ::= <sumaExp> <rel_op> <relExp>
       final int RULE_RELEXP2                               = 46;  // <relExp> ::= <sumaExp>
       final int RULE_SUMAEXP                               = 47;  // <sumaExp> ::= <multiExp> <suma_op> <sumaExp>
       final int RULE_SUMAEXP2                              = 48;  // <sumaExp> ::= <multiExp>
       final int RULE_MULTIEXP                              = 49;  // <multiExp> ::= <negadorExp> <mul_op> <multiExp>
       final int RULE_MULTIEXP2                             = 50;  // <multiExp> ::= <negadorExp>
       final int RULE_NEGADOREXP_MINUS                      = 51;  // <negadorExp> ::= '-' <val>
       final int RULE_NEGADOREXP_EXCLAM                     = 52;  // <negadorExp> ::= '!' <val>
       final int RULE_NEGADOREXP                            = 53;  // <negadorExp> ::= <val>
       final int RULE_COND_OP_AMPAMP                        = 54;  // <cond_op> ::= '&&'
       final int RULE_COND_OP_PIPEPIPE                      = 55;  // <cond_op> ::= '||'
       final int RULE_REL_OP_LTEQ                           = 56;  // <rel_op> ::= '<='
       final int RULE_REL_OP_LT                             = 57;  // <rel_op> ::= '<'
       final int RULE_REL_OP_GT                             = 58;  // <rel_op> ::= '>'
       final int RULE_REL_OP_GTEQ                           = 59;  // <rel_op> ::= '>='
       final int RULE_REL_OP                                = 60;  // <rel_op> ::= <eq_op>
       final int RULE_EQ_OP_EQEQ                            = 61;  // <eq_op> ::= '=='
       final int RULE_EQ_OP_EXCLAMEQ                        = 62;  // <eq_op> ::= '!='
       final int RULE_SUMA_OP_PLUS                          = 63;  // <suma_op> ::= '+'
       final int RULE_SUMA_OP_MINUS                         = 64;  // <suma_op> ::= '-'
       final int RULE_MUL_OP_TIMES                          = 65;  // <mul_op> ::= '*'
       final int RULE_MUL_OP_DIV                            = 66;  // <mul_op> ::= '/'
       final int RULE_MUL_OP_PERCENT                        = 67;  // <mul_op> ::= '%'
       final int RULE_VAL                                   = 68;  // <val> ::= <location>
       final int RULE_VAL2                                  = 69;  // <val> ::= <methodCall>
       final int RULE_VAL3                                  = 70;  // <val> ::= <literal>
       final int RULE_VAL_LPARAN_RPARAN                     = 71;  // <val> ::= '(' <expression> ')'
       final int RULE_LOCATION                              = 72;  // <location> ::= <location1>
       final int RULE_LOCATION_DOT                          = 73;  // <location> ::= <location1> '.' <location>
       final int RULE_LOCATION1_ID                          = 74;  // <location1> ::= id <location2>
       final int RULE_LOCATION2_LBRACKET_RBRACKET           = 75;  // <location2> ::= '[' <expression> ']'
       final int RULE_LOCATION2                             = 76;  // <location2> ::= 
       final int RULE_METHODCALL_ID_LPARAN_RPARAN           = 77;  // <methodCall> ::= id '(' <arg1> ')'
       final int RULE_ARG1                                  = 78;  // <arg1> ::= <arg2>
       final int RULE_ARG12                                 = 79;  // <arg1> ::= 
       final int RULE_ARG2                                  = 80;  // <arg2> ::= <arg>
       final int RULE_ARG2_COMMA                            = 81;  // <arg2> ::= <arg2> ',' <arg>
       final int RULE_ARG                                   = 82;  // <arg> ::= <expression>
       final int RULE_LITERAL                               = 83;  // <literal> ::= <int_literal>
       final int RULE_LITERAL2                              = 84;  // <literal> ::= <char_literal>
       final int RULE_LITERAL3                              = 85;  // <literal> ::= <bool_literal>
       final int RULE_INT_LITERAL_NUM                       = 86;  // <int_literal> ::= num
       final int RULE_CHAR_LITERAL_CHARACTER                = 87;  // <char_literal> ::= character
       final int RULE_BOOL_LITERAL_TRUE                     = 88;  // <bool_literal> ::= true
       final int RULE_BOOL_LITERAL_FALSE                    = 89;  // <bool_literal> ::= false
    };

   private static BufferedReader buffR;

    /***************************************************************
     * This class will run the engine, and needs a file called config.dat
     * in the current directory. This file should contain two lines,
     * The first should be the absolute path name to the .cgt file, the second
     * should be the source file you wish to parse.
     * @param args Array of arguments.
     ***************************************************************/
    public static Reduction parse(String textToParse)
    {
    	
    	// Lista de reducciones y tokens que forman parte de la reducción actual.
    	ArrayList partes = new ArrayList();
    	
    	String compiledGrammar = new File("src/dekf/Dekf.cgt").getAbsolutePath();
        /*
        try
        {
            buffR = new BufferedReader(new FileReader(new File("./config.dat")));
            compiledGrammar = buffR.readLine();
            textToParse = buffR.readLine();

            buffR.close();
        }
        catch(FileNotFoundException fnfex)
        {
            System.out.println("Config File was not found.\n\n" +
                               "Please place it in the current directory.");
            System.exit(1);
        }
        catch(IOException ioex)
        {
           System.out.println("An error occured while reading config.dat.\n\n" +
                              "Please re-try ensuring the file can be read.");
           System.exit(1);
        }
 		//*/
        GOLDParser parser = new GOLDParser();

       try
       {
          parser.loadCompiledGrammar(compiledGrammar);
          parser.openFile(textToParse);
       }
       catch(ParserException parse)
       {
          System.out.println("**PARSER ERROR**\n" + parse.toString());
          System.exit(1);
       }

       boolean done = false;
       int response = -1;

       while(!done)
       {
          try
            {
                  response = parser.parse();
            }
            catch(ParserException parse)
            {
                System.out.println("**PARSER ERROR**\n" + parse.toString());
                System.exit(1);
            }

            switch(response)
            {
               case gpMsgTokenRead:
                   /* A token was read by the parser. The Token Object can be accessed
                      through the CurrentToken() property:  Parser.CurrentToken */
            	   if( parser.currentToken().getKind() == SymbolConstants.SYMBOL_ID ){
            		   System.out.println("ID: " + parser.currentToken().getText() );
            	   }
                   break;

               case gpMsgReduction:
                   /* This message is returned when a rule was reduced by the parse engine.
                      The CurrentReduction property is assigned a Reduction object
                      containing the rule and its related tokens. You can reassign this
                      property to your own customized class. If this is not the case,
                      this message can be ignored and the Reduction object will be used
                      to store the parse tree.  */

                      switch(parser.currentReduction().getParentRule().getTableIndex())
                      {
                         case RuleConstants.RULE_PROGRAM_CLASS_PROGRAM_LBRACE_RBRACE:
                            //<Program> ::= class Program '{' <declarationK> '}'
                            break;
                         case RuleConstants.RULE_DECLARATIONK:
                            //<declarationK> ::= <declaration> <declarationK>
                            break;
                         case RuleConstants.RULE_DECLARATIONK2:
                            //<declarationK> ::= 
                            break;
                         case RuleConstants.RULE_DECLARATION:
                            //<declaration> ::= <structDeclaration>
                            break;
                         case RuleConstants.RULE_DECLARATION2:
                            //<declaration> ::= <varDeclaration>
                            break;
                         case RuleConstants.RULE_DECLARATION3:
                            //<declaration> ::= <methodDeclaration>
                            break;
                         case RuleConstants.RULE_VARDECLARATION_ID_SEMI:
                            //<varDeclaration> ::= <methodType> id <varDeclaration1> ';'
                            break;
                         case RuleConstants.RULE_VARDECLARATION1_LBRACKET_NUM_RBRACKET:
                            //<varDeclaration1> ::= '[' num ']'
                            break;
                         case RuleConstants.RULE_VARDECLARATION1:
                            //<varDeclaration1> ::= 
                            break;
                         case RuleConstants.RULE_STRUCTDECLARATION_LBRACE_RBRACE:
                            //<structDeclaration> ::= <structDecHeader> '{' <varDeclarationK> '}'
                            break;
                         case RuleConstants.RULE_STRUCTDECHEADER_STRUCT_ID:
                            //<structDecHeader> ::= struct id
                            break;
                         case RuleConstants.RULE_VARDECLARATIONK:
                            //<varDeclarationK> ::= <varDeclaration> <varDeclarationK>
                            break;
                         case RuleConstants.RULE_VARDECLARATIONK2:
                            //<varDeclarationK> ::= 
                            break;
                         case RuleConstants.RULE_METHODTYPE_INT:
                            //<methodType> ::= int
                            break;
                         case RuleConstants.RULE_METHODTYPE_CHAR:
                            //<methodType> ::= char
                            break;
                         case RuleConstants.RULE_METHODTYPE_BOOLEAN:
                            //<methodType> ::= boolean
                            break;
                         case RuleConstants.RULE_METHODTYPE_VOID:
                            //<methodType> ::= void
                            break;
                         case RuleConstants.RULE_METHODTYPE_STRUCT_ID:
                            //<methodType> ::= struct id
                            break;
                         case RuleConstants.RULE_METHODTYPE:
                            //<methodType> ::= <structDeclaration>
                            break;
                         case RuleConstants.RULE_METHODDECLARATION:
                            //<methodDeclaration> ::= <methodDecHeader> <block>
                            break;
                         case RuleConstants.RULE_METHODDECHEADER_ID_LPARAN_RPARAN:
                            //<methodDecHeader> ::= <methodType> id '(' <parameter1> ')'
                            break;
                         case RuleConstants.RULE_PARAMETER1:
                            //<parameter1> ::= <parameter2>
                            break;
                         case RuleConstants.RULE_PARAMETER12:
                            //<parameter1> ::= 
                            break;
                         case RuleConstants.RULE_PARAMETER2:
                            //<parameter2> ::= <parameter>
                            break;
                         case RuleConstants.RULE_PARAMETER2_COMMA:
                            //<parameter2> ::= <parameter2> ',' <parameter>
                            break;
                         case RuleConstants.RULE_PARAMETER_ID:
                            //<parameter> ::= <parameterType> id
                            break;
                         case RuleConstants.RULE_PARAMETER_ID_LBRACKET_RBRACKET:
                            //<parameter> ::= <parameterType> id '[' ']'
                            break;
                         case RuleConstants.RULE_PARAMETERTYPE_INT:
                            //<parameterType> ::= int
                            break;
                         case RuleConstants.RULE_PARAMETERTYPE_CHAR:
                            //<parameterType> ::= char
                            break;
                         case RuleConstants.RULE_PARAMETERTYPE_BOOLEAN:
                            //<parameterType> ::= boolean
                            break;
                         case RuleConstants.RULE_BLOCK_LBRACE_RBRACE:
                            //<block> ::= '{' <varDeclarationK> <statementK> '}'
                            break;
                         case RuleConstants.RULE_STATEMENTK:
                            //<statementK> ::= <statement> <statementK>
                            break;
                         case RuleConstants.RULE_STATEMENTK2:
                            //<statementK> ::= 
                            break;
                         case RuleConstants.RULE_STATEMENT:
                            //<statement> ::= <block>
                            break;
                         case RuleConstants.RULE_STATEMENT_IF_LPARAN_RPARAN:
                            //<statement> ::= if '(' <expression> ')' <block> <blockL>
                            break;
                         case RuleConstants.RULE_STATEMENT_WHILE_LPARAN_RPARAN:
                            //<statement> ::= while '(' <expression> ')' <block>
                            break;
                         case RuleConstants.RULE_STATEMENT_RETURN_SEMI:
                            //<statement> ::= return <expressionL> ';'
                            break;
                         case RuleConstants.RULE_STATEMENT_EQ:
                            //<statement> ::= <location> '=' <expression>
                            break;
                         case RuleConstants.RULE_STATEMENT_SEMI:
                            //<statement> ::= <expressionL> ';'
                            break;
                         case RuleConstants.RULE_BLOCKL_ELSE:
                            //<blockL> ::= else <block>
                            break;
                         case RuleConstants.RULE_BLOCKL:
                            //<blockL> ::= 
                            break;
                         case RuleConstants.RULE_EXPRESSIONL:
                            //<expressionL> ::= <expression>
                            break;
                         case RuleConstants.RULE_EXPRESSIONL2:
                            //<expressionL> ::= 
                            break;
                         case RuleConstants.RULE_EXPRESSION:
                            //<expression> ::= <relExp> <cond_op> <expression>
                            break;
                         case RuleConstants.RULE_EXPRESSION2:
                            //<expression> ::= <relExp>
                            break;
                         case RuleConstants.RULE_RELEXP:
                            //<relExp> ::= <sumaExp> <rel_op> <relExp>
                            break;
                         case RuleConstants.RULE_RELEXP2:
                            //<relExp> ::= <sumaExp>
                            break;
                         case RuleConstants.RULE_SUMAEXP:
                            //<sumaExp> ::= <multiExp> <suma_op> <sumaExp>
                            break;
                         case RuleConstants.RULE_SUMAEXP2:
                            //<sumaExp> ::= <multiExp>
                            break;
                         case RuleConstants.RULE_MULTIEXP:
                            //<multiExp> ::= <negadorExp> <mul_op> <multiExp>
                            break;
                         case RuleConstants.RULE_MULTIEXP2:
                            //<multiExp> ::= <negadorExp>
                            break;
                         case RuleConstants.RULE_NEGADOREXP_MINUS:
                            //<negadorExp> ::= '-' <val>
                            break;
                         case RuleConstants.RULE_NEGADOREXP_EXCLAM:
                            //<negadorExp> ::= '!' <val>
                            break;
                         case RuleConstants.RULE_NEGADOREXP:
                            //<negadorExp> ::= <val>
                            break;
                         case RuleConstants.RULE_COND_OP_AMPAMP:
                            //<cond_op> ::= '&&'
                            break;
                         case RuleConstants.RULE_COND_OP_PIPEPIPE:
                            //<cond_op> ::= '||'
                            break;
                         case RuleConstants.RULE_REL_OP_LTEQ:
                            //<rel_op> ::= '<='
                            break;
                         case RuleConstants.RULE_REL_OP_LT:
                            //<rel_op> ::= '<'
                            break;
                         case RuleConstants.RULE_REL_OP_GT:
                            //<rel_op> ::= '>'
                            break;
                         case RuleConstants.RULE_REL_OP_GTEQ:
                            //<rel_op> ::= '>='
                            break;
                         case RuleConstants.RULE_REL_OP:
                            //<rel_op> ::= <eq_op>
                            break;
                         case RuleConstants.RULE_EQ_OP_EQEQ:
                            //<eq_op> ::= '=='
                            break;
                         case RuleConstants.RULE_EQ_OP_EXCLAMEQ:
                            //<eq_op> ::= '!='
                            break;
                         case RuleConstants.RULE_SUMA_OP_PLUS:
                            //<suma_op> ::= '+'
                            break;
                         case RuleConstants.RULE_SUMA_OP_MINUS:
                            //<suma_op> ::= '-'
                            break;
                         case RuleConstants.RULE_MUL_OP_TIMES:
                            //<mul_op> ::= '*'
                            break;
                         case RuleConstants.RULE_MUL_OP_DIV:
                            //<mul_op> ::= '/'
                            break;
                         case RuleConstants.RULE_MUL_OP_PERCENT:
                            //<mul_op> ::= '%'
                            break;
                         case RuleConstants.RULE_VAL:
                            //<val> ::= <location>
                            break;
                         case RuleConstants.RULE_VAL2:
                            //<val> ::= <methodCall>
                            break;
                         case RuleConstants.RULE_VAL3:
                            //<val> ::= <literal>
                            break;
                         case RuleConstants.RULE_VAL_LPARAN_RPARAN:
                            //<val> ::= '(' <expression> ')'
                            break;
                         case RuleConstants.RULE_LOCATION:
                            //<location> ::= <location1>
                            break;
                         case RuleConstants.RULE_LOCATION_DOT:
                            //<location> ::= <location1> '.' <location>
                            break;
                         case RuleConstants.RULE_LOCATION1_ID:
                            //<location1> ::= id <location2>
                            break;
                         case RuleConstants.RULE_LOCATION2_LBRACKET_RBRACKET:
                            //<location2> ::= '[' <expression> ']'
                            break;
                         case RuleConstants.RULE_LOCATION2:
                            //<location2> ::= 
                            break;
                         case RuleConstants.RULE_METHODCALL_ID_LPARAN_RPARAN:
                            //<methodCall> ::= id '(' <arg1> ')'
                            break;
                         case RuleConstants.RULE_ARG1:
                            //<arg1> ::= <arg2>
                            break;
                         case RuleConstants.RULE_ARG12:
                            //<arg1> ::= 
                            break;
                         case RuleConstants.RULE_ARG2:
                            //<arg2> ::= <arg>
                            break;
                         case RuleConstants.RULE_ARG2_COMMA:
                            //<arg2> ::= <arg2> ',' <arg>
                            break;
                         case RuleConstants.RULE_ARG:
                            //<arg> ::= <expression>
                            break;
                         case RuleConstants.RULE_LITERAL:
                            //<literal> ::= <int_literal>
                            break;
                         case RuleConstants.RULE_LITERAL2:
                            //<literal> ::= <char_literal>
                            break;
                         case RuleConstants.RULE_LITERAL3:
                            //<literal> ::= <bool_literal>
                            break;
                         case RuleConstants.RULE_INT_LITERAL_NUM:
                            //<int_literal> ::= num
                            break;
                         case RuleConstants.RULE_CHAR_LITERAL_CHARACTER:
                            //<char_literal> ::= character
                            break;
                         case RuleConstants.RULE_BOOL_LITERAL_TRUE:
                            //<bool_literal> ::= true
                            break;
                         case RuleConstants.RULE_BOOL_LITERAL_FALSE:
                            //<bool_literal> ::= false
                            break;
                      }

                      //Parser.Reduction = //Object you created to store the rule

                      ///* ************************************** log file
                      Reduction myRed = parser.currentReduction();
                      System.out.println(myRed.getParentRule().getText());
                      // ************************************** end log
                      //*/
                      break;

                  case gpMsgAccept:
                      /* The program was accepted by the parsing engine */

                      /* ************************************** log file
                      System.out.println("gpMsgAccept");
                      // ************************************** end log
                      //*/

                      done = true;

                      break;

                  case gpMsgLexicalError:
                      /* Place code here to handle a illegal or unrecognized token
                             To recover, pop the token from the stack: Parser.PopInputToken */

                      // ************************************** log file
                      System.out.println("Línea " + parser.currentLineNumber() + " - Error Léxico: " + parser.popInputToken());
                      // ************************************** end log

                      break;

                  case gpMsgNotLoadedError:
                      /* Load the Compiled Grammar Table file first. */

                      // ************************************** log file
                      System.out.println("Load the Compiled Grammar Table file first.");
                      // ************************************** end log

                      done = true;

                      break;

                  case gpMsgSyntaxError:
                      /* This is a syntax error: the source has produced a token that was
                             not expected by the LALR State Machine. The expected tokens are stored
                             into the Tokens() list. To recover, push one of the
                                expected tokens onto the parser's input queue (the first in this case):
                             You should limit the number of times this type of recovery can take
                             place. */

                      done = true;
                     
                      // ************************************** log file
                      System.out.print("Línea " + parser.currentLineNumber() + " - Error sintáctico: ");
                      // ************************************** end log

                      Token theTok = parser.currentToken();
                      System.out.println("Token not expected: " + (String)theTok.getData());

                      break;

                  case gpMsgCommentError:
                      /* The end of the input was reached while reading a comment.
                               This is caused by a comment that was not terminated */

                      // ************************************** log file
                      System.out.println("The end of the input was reached while reading a comment. This is caused by a comment that was not terminated");
                      // ************************************** end log

                      done = true;

                                break;

                  case gpMsgInternalError:
                      /* Something horrid happened inside the parser. You cannot recover */

                      // ************************************** log file
                      System.out.println("gpMsgInternalError");
                      // ************************************** end log

                      done = true;

                      break;
            }
        }
       try
       {
             parser.closeFile();
             if( response == gpMsgAccept )
           	  return parser.currentReduction();
             else return null;
       }
       catch(ParserException parse)
       {
           System.out.println("**PARSER ERROR**\n" + parse.toString());
           //System.exit(1);
           return null;
       }
    }
}
