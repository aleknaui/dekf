package dekf;

import java.io.*;
import java.util.ArrayList;

import goldengine.java.*;

/*
 * Licensed Material - Property of Matthew Hawkins (hawkini@4email.net) 
 */
 
public class DekfParser implements GPMessageConstants
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
       final int SYMBOL_ARG               = 46;  // <Arg>
       final int SYMBOL_ARGC              = 47;  // <ArgC>
       final int SYMBOL_ARGK              = 48;  // <ArgK>
       final int SYMBOL_BLOCK             = 49;  // <block>
       final int SYMBOL_BLOCKELSE         = 50;  // <BlockElse>
       final int SYMBOL_BOOL_LITERAL      = 51;  // <bool_literal>
       final int SYMBOL_CHAR_LITERAL      = 52;  // <char_literal>
       final int SYMBOL_CONDOP            = 53;  // <CondOp>
       final int SYMBOL_DECLARATION       = 54;  // <Declaration>
       final int SYMBOL_EQOP              = 55;  // <EqOp>
       final int SYMBOL_EXPRESSION        = 56;  // <Expression>
       final int SYMBOL_EXPRESSIONOPC     = 57;  // <ExpressionOpc>
       final int SYMBOL_INT_LITERAL       = 58;  // <int_literal>
       final int SYMBOL_KDECLARATION      = 59;  // <KDeclaration>
       final int SYMBOL_LITERAL           = 60;  // <Literal>
       final int SYMBOL_LOCATION          = 61;  // <Location>
       final int SYMBOL_LOCATIONMAIN      = 62;  // <LocationMain>
       final int SYMBOL_LOCATIONOPC       = 63;  // <LocationOpc>
       final int SYMBOL_METHODCALL        = 64;  // <MethodCall>
       final int SYMBOL_METHODDECHEADER   = 65;  // <MethodDecHeader>
       final int SYMBOL_METHODDECLARATION = 66;  // <MethodDeclaration>
       final int SYMBOL_MULTEXP           = 67;  // <MultExp>
       final int SYMBOL_MULTOP            = 68;  // <MultOp>
       final int SYMBOL_NEGEXP            = 69;  // <NegExp>
       final int SYMBOL_PARAMETER         = 70;  // <Parameter>
       final int SYMBOL_PARAMETERC        = 71;  // <ParameterC>
       final int SYMBOL_PARAMETERK        = 72;  // <ParameterK>
       final int SYMBOL_PARAMETERTYPE     = 73;  // <ParameterType>
       final int SYMBOL_PROGRAM2          = 74;  // <Program>
       final int SYMBOL_RELEXP            = 75;  // <RelExp>
       final int SYMBOL_RELOP             = 76;  // <RelOp>
       final int SYMBOL_STATEMENT         = 77;  // <Statement>
       final int SYMBOL_STATEMENTK        = 78;  // <StatementK>
       final int SYMBOL_STRUCTDECHEADER   = 79;  // <StructDecHeader>
       final int SYMBOL_STRUCTDECLARATION = 80;  // <StructDeclaration>
       final int SYMBOL_SUMEXP            = 81;  // <SumExp>
       final int SYMBOL_SUMOP             = 82;  // <SumOp>
       final int SYMBOL_VAL               = 83;  // <val>
       final int SYMBOL_VARDECLARATION    = 84;  // <VarDeclaration>
       final int SYMBOL_VARDECLARATIONK   = 85;  // <VarDeclarationK>
       final int SYMBOL_VARDECLARATIONOPC = 86;  // <VarDeclarationOpc>
       final int SYMBOL_VARMETHODTYPE     = 87;  // <VarMethodType>
    };

    private interface RuleConstants
    {
       final int RULE_PROGRAM_CLASS_PROGRAM_LBRACE_RBRACE     =  0;  // <Program> ::= class Program '{' <KDeclaration> '}'
       final int RULE_KDECLARATION                            =  1;  // <KDeclaration> ::= <Declaration> <KDeclaration>
       final int RULE_KDECLARATION2                           =  2;  // <KDeclaration> ::= 
       final int RULE_DECLARATION                             =  3;  // <Declaration> ::= <StructDeclaration>
       final int RULE_DECLARATION2                            =  4;  // <Declaration> ::= <VarDeclaration>
       final int RULE_DECLARATION3                            =  5;  // <Declaration> ::= <MethodDeclaration>
       final int RULE_VARDECLARATION_ID_SEMI                  =  6;  // <VarDeclaration> ::= <VarMethodType> id <VarDeclarationOpc> ';'
       final int RULE_VARDECLARATIONOPC_LBRACKET_NUM_RBRACKET =  7;  // <VarDeclarationOpc> ::= '[' num ']'
       final int RULE_VARDECLARATIONOPC                       =  8;  // <VarDeclarationOpc> ::= 
       final int RULE_STRUCTDECLARATION_LBRACE_RBRACE         =  9;  // <StructDeclaration> ::= <StructDecHeader> '{' <VarDeclarationK> '}'
       final int RULE_STRUCTDECHEADER_STRUCT_ID               = 10;  // <StructDecHeader> ::= struct id
       final int RULE_VARDECLARATIONK                         = 11;  // <VarDeclarationK> ::= <VarDeclaration> <VarDeclarationK>
       final int RULE_VARDECLARATIONK2                        = 12;  // <VarDeclarationK> ::= 
       final int RULE_VARMETHODTYPE_INT                       = 13;  // <VarMethodType> ::= int
       final int RULE_VARMETHODTYPE_CHAR                      = 14;  // <VarMethodType> ::= char
       final int RULE_VARMETHODTYPE_BOOLEAN                   = 15;  // <VarMethodType> ::= boolean
       final int RULE_VARMETHODTYPE_VOID                      = 16;  // <VarMethodType> ::= void
       final int RULE_VARMETHODTYPE_STRUCT_ID                 = 17;  // <VarMethodType> ::= struct id
       final int RULE_VARMETHODTYPE                           = 18;  // <VarMethodType> ::= <StructDeclaration>
       final int RULE_METHODDECLARATION                       = 19;  // <MethodDeclaration> ::= <MethodDecHeader> <block>
       final int RULE_METHODDECHEADER_ID_LPARAN_RPARAN        = 20;  // <MethodDecHeader> ::= <VarMethodType> id '(' <ParameterK> ')'
       final int RULE_PARAMETERK                              = 21;  // <ParameterK> ::= <ParameterC>
       final int RULE_PARAMETERK2                             = 22;  // <ParameterK> ::= 
       final int RULE_PARAMETERC                              = 23;  // <ParameterC> ::= <Parameter>
       final int RULE_PARAMETERC_COMMA                        = 24;  // <ParameterC> ::= <ParameterC> ',' <Parameter>
       final int RULE_PARAMETER_ID                            = 25;  // <Parameter> ::= <ParameterType> id
       final int RULE_PARAMETER_ID_LBRACKET_RBRACKET          = 26;  // <Parameter> ::= <ParameterType> id '[' ']'
       final int RULE_PARAMETERTYPE_INT                       = 27;  // <ParameterType> ::= int
       final int RULE_PARAMETERTYPE_CHAR                      = 28;  // <ParameterType> ::= char
       final int RULE_PARAMETERTYPE_BOOLEAN                   = 29;  // <ParameterType> ::= boolean
       final int RULE_BLOCK_LBRACE_RBRACE                     = 30;  // <block> ::= '{' <VarDeclarationK> <StatementK> '}'
       final int RULE_STATEMENTK                              = 31;  // <StatementK> ::= <Statement> <StatementK>
       final int RULE_STATEMENTK2                             = 32;  // <StatementK> ::= 
       final int RULE_STATEMENT                               = 33;  // <Statement> ::= <block>
       final int RULE_STATEMENT_IF_LPARAN_RPARAN              = 34;  // <Statement> ::= if '(' <Expression> ')' <block> <BlockElse>
       final int RULE_STATEMENT_WHILE_LPARAN_RPARAN           = 35;  // <Statement> ::= while '(' <Expression> ')' <block>
       final int RULE_STATEMENT_RETURN_SEMI                   = 36;  // <Statement> ::= return <ExpressionOpc> ';'
       final int RULE_STATEMENT_EQ                            = 37;  // <Statement> ::= <Location> '=' <Expression>
       final int RULE_STATEMENT_SEMI                          = 38;  // <Statement> ::= <ExpressionOpc> ';'
       final int RULE_BLOCKELSE_ELSE                          = 39;  // <BlockElse> ::= else <block>
       final int RULE_BLOCKELSE                               = 40;  // <BlockElse> ::= 
       final int RULE_EXPRESSIONOPC                           = 41;  // <ExpressionOpc> ::= <Expression>
       final int RULE_EXPRESSIONOPC2                          = 42;  // <ExpressionOpc> ::= 
       final int RULE_EXPRESSION                              = 43;  // <Expression> ::= <RelExp> <CondOp> <Expression>
       final int RULE_EXPRESSION2                             = 44;  // <Expression> ::= <RelExp>
       final int RULE_RELEXP                                  = 45;  // <RelExp> ::= <SumExp> <RelOp> <RelExp>
       final int RULE_RELEXP2                                 = 46;  // <RelExp> ::= <SumExp> <EqOp> <RelExp>
       final int RULE_RELEXP3                                 = 47;  // <RelExp> ::= <SumExp>
       final int RULE_SUMEXP                                  = 48;  // <SumExp> ::= <MultExp> <SumOp> <SumExp>
       final int RULE_SUMEXP2                                 = 49;  // <SumExp> ::= <MultExp>
       final int RULE_MULTEXP                                 = 50;  // <MultExp> ::= <NegExp> <MultOp> <MultExp>
       final int RULE_MULTEXP2                                = 51;  // <MultExp> ::= <NegExp>
       final int RULE_NEGEXP_MINUS                            = 52;  // <NegExp> ::= '-' <val>
       final int RULE_NEGEXP_EXCLAM                           = 53;  // <NegExp> ::= '!' <val>
       final int RULE_NEGEXP                                  = 54;  // <NegExp> ::= <val>
       final int RULE_CONDOP_AMPAMP                           = 55;  // <CondOp> ::= '&&'
       final int RULE_CONDOP_PIPEPIPE                         = 56;  // <CondOp> ::= '||'
       final int RULE_RELOP_LTEQ                              = 57;  // <RelOp> ::= '<='
       final int RULE_RELOP_LT                                = 58;  // <RelOp> ::= '<'
       final int RULE_RELOP_GT                                = 59;  // <RelOp> ::= '>'
       final int RULE_RELOP_GTEQ                              = 60;  // <RelOp> ::= '>='
       final int RULE_EQOP_EQEQ                               = 61;  // <EqOp> ::= '=='
       final int RULE_EQOP_EXCLAMEQ                           = 62;  // <EqOp> ::= '!='
       final int RULE_SUMOP_PLUS                              = 63;  // <SumOp> ::= '+'
       final int RULE_SUMOP_MINUS                             = 64;  // <SumOp> ::= '-'
       final int RULE_MULTOP_TIMES                            = 65;  // <MultOp> ::= '*'
       final int RULE_MULTOP_DIV                              = 66;  // <MultOp> ::= '/'
       final int RULE_MULTOP_PERCENT                          = 67;  // <MultOp> ::= '%'
       final int RULE_VAL                                     = 68;  // <val> ::= <Location>
       final int RULE_VAL2                                    = 69;  // <val> ::= <MethodCall>
       final int RULE_VAL3                                    = 70;  // <val> ::= <Literal>
       final int RULE_VAL_LPARAN_RPARAN                       = 71;  // <val> ::= '(' <Expression> ')'
       final int RULE_LOCATION                                = 72;  // <Location> ::= <LocationMain>
       final int RULE_LOCATION_DOT                            = 73;  // <Location> ::= <LocationMain> '.' <Location>
       final int RULE_LOCATIONMAIN_ID                         = 74;  // <LocationMain> ::= id <LocationOpc>
       final int RULE_LOCATIONOPC_LBRACKET_RBRACKET           = 75;  // <LocationOpc> ::= '[' <Expression> ']'
       final int RULE_LOCATIONOPC                             = 76;  // <LocationOpc> ::= 
       final int RULE_METHODCALL_ID_LPARAN_RPARAN             = 77;  // <MethodCall> ::= id '(' <ArgK> ')'
       final int RULE_ARGK                                    = 78;  // <ArgK> ::= <ArgC>
       final int RULE_ARGK2                                   = 79;  // <ArgK> ::= 
       final int RULE_ARGC                                    = 80;  // <ArgC> ::= <Arg>
       final int RULE_ARGC_COMMA                              = 81;  // <ArgC> ::= <ArgC> ',' <Arg>
       final int RULE_ARG                                     = 82;  // <Arg> ::= <Expression>
       final int RULE_LITERAL                                 = 83;  // <Literal> ::= <int_literal>
       final int RULE_LITERAL2                                = 84;  // <Literal> ::= <char_literal>
       final int RULE_LITERAL3                                = 85;  // <Literal> ::= <bool_literal>
       final int RULE_INT_LITERAL_NUM                         = 86;  // <int_literal> ::= num
       final int RULE_CHAR_LITERAL_CHARACTER                  = 87;  // <char_literal> ::= character
       final int RULE_BOOL_LITERAL_TRUE                       = 88;  // <bool_literal> ::= true
       final int RULE_BOOL_LITERAL_FALSE                      = 89;  // <bool_literal> ::= false
    };

   private static BufferedReader buffR;
   
   private static int trues = 0;
   private static int falses = 0;
   private static int dones = 0;
   
   private static String genTrue(){
	   trues++;
	   return "TRUE_" + (trues-1);
   }
   
   private static String genFalse(){
	   falses++;
	   return "FALSE_" + (falses-1);
   }
   
   private static String genDone(){
	   dones++;
	   return "DONE_" + (dones-1);
   }

    /***************************************************************
     * This class will run the engine, and needs a file called config.dat
     * in the current directory. This file should contain two lines,
     * The first should be the absolute path name to the .cgt file, the second
     * should be the source file you wish to parse.
     * @param args Array of arguments.
     ***************************************************************/
    public static Reduction parse(String textToParse)
    {
    
    	String compiledGrammar = new File("src/dekf/Dekf.cgt").getAbsolutePath();
    	SymbolTable.ambito = new SymbolTable();
    	ArrayList<String> pendingMethodNames = new ArrayList<String>();
    	ArrayList<String> pendingMethodErrMsjs = new ArrayList<String>();
    	/*
    	String textToParse = "", compiledGrammar = "";

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
            	   
            	   switch( parser.currentToken().getTableIndex() ){
            	   
            	   case SymbolConstants.SYMBOL_IF:
            	   case SymbolConstants.SYMBOL_ELSE:
            	   case SymbolConstants.SYMBOL_WHILE:
            		   
            		   SymbolTable ambito = new SymbolTable( SymbolTable.ambito.getReturnType(), SymbolTable.ambito );
            		   ambito.set(true);
            		   
            		   break;
            	   
            	   }
            	   
                   break;

               case gpMsgReduction:
            	   
            	   Reduction reduction = parser.currentReduction();
            	   ArrayList children = reduction.getParts();
            	   
                   /* This message is returned when a rule was reduced by the parse engine.
                      The CurrentReduction property is assigned a Reduction object
                      containing the rule and its related tokens. You can reassign this
                      property to your own customized class. If this is not the case,
                      this message can be ignored and the Reduction object will be used
                      to store the parse tree.  */

                      switch(parser.currentReduction().getParentRule().getTableIndex())
                      {
                         case RuleConstants.RULE_PROGRAM_CLASS_PROGRAM_LBRACE_RBRACE:
                            //<Program> ::= class Program '{' <KDeclaration> '}'
                        	 if( SymbolTable.ambito.getMethod("main:") == null ) System.out.println("ERROR: El programa debe contener un método llamado main()");
                        	 reduction.setType("void");
                        	 reduction.init_code = ((Reduction)children.get(3)).init_code;
                        	 reduction.code = ".class public Program\n" +
                        	 		".super java/lang/Object\n" +
                        	 		((Reduction)children.get(3)).code + "\n" +
                        	 		".method public static <clinit>()V\n" +
                        	 		".limit locals 0\n" +
                        	 		".limit stack 1\n" +
                        	 		reduction.init_code + "\n" +
                	 				"return\n" +
                	 				".end method\n\n" +
                	 				".method <init>()V\n" +
                	 				".limit locals 1\n" +
                	 				".limit stack 1\n" +
                	 				"\n" +
                	 				"aload_0\n" +
                	 				"invokenonvirtual	java/lang/Object.<init>()V\n" +
                	 				"return\n" +
                	 				".end method";
                            break;
                         case RuleConstants.RULE_KDECLARATION:
                            //<KDeclaration> ::= <Declaration> <KDeclaration>
                        	 reduction.setType("void");
                        	 reduction.code = ((Reduction)children.get(0)).code + "\n" +
                        	 		((Reduction)children.get(1)).code;
                        	 reduction.init_code = ((Reduction)children.get(0)).init_code + "\n" +
                 	 		((Reduction)children.get(1)).init_code;
                            break;
                         case RuleConstants.RULE_KDECLARATION2:
                            //<KDeclaration> ::=
                        	 reduction.setType("void");
                            break;
                         case RuleConstants.RULE_DECLARATION:
                            //<Declaration> ::= <StructDeclaration>
                        	 reduction.setType("void");
                            break;
                         case RuleConstants.RULE_DECLARATION2:
                            //<Declaration> ::= <VarDeclaration>
                        	 reduction.setType("void");
                        	 reduction.code = ((Reduction)children.get(0)).code;
                        	 reduction.init_code = ((Reduction)children.get(0)).init_code;
                            break;
                         case RuleConstants.RULE_DECLARATION3:
                            //<Declaration> ::= <MethodDeclaration>
                        	 reduction.setType("void");
                        	 reduction.code = ((Reduction)children.get(0)).code;
                            break;
                         case RuleConstants.RULE_VARDECLARATION_ID_SEMI:
                            //<VarDeclaration> ::= <VarMethodType> id <VarDeclarationOpc> ';'                        	 
                         	 SymbolTable.ambito.clearTemp();
                        	 if( ! SymbolTable.ambito.addVariable(((Reduction)children.get(2)).getType() + ((Reduction)children.get(0)).getType(), (String)children.get(1)) ){
                        		 System.out.println(parser.currentLineNumber() + ": La variable ya se encuentra declarada.");
                        	 }
                        	 if( SymbolTable.ambito.parent == null )
                        	 	reduction.code = ".field public static " + children.get(1) + " " + ((Reduction)children.get(2)).code + ((Reduction)children.get(0)).code;
                        	 if( ((Reduction)children.get(2)).code.equals("[") )
                        		 reduction.init_code = ((Reduction)children.get(2)).init_code + ((Reduction)children.get(0)).getType() + "\n" +
                        		 		"putstatic Program/" + children.get(1) + " " + ((Reduction)children.get(2)).code + ((Reduction)children.get(0)).code;
                            break;
                         case RuleConstants.RULE_VARDECLARATIONOPC_LBRACKET_NUM_RBRACKET:
                            //<VarDeclarationOpc> ::= '[' num ']'
                        	 reduction.setType("array:");
                        	 reduction.code = "[";
                        	 reduction.init_code = "aload_0\n" +
                        	 		"ldc " + children.get(1) + "\n" +
                        	 		"anewarray ";
                            break;
                         case RuleConstants.RULE_VARDECLARATIONOPC:
                            //<VarDeclarationOpc> ::=
                        	 reduction.setType("");
                        	 //code se queda vacío
                            break;
                         case RuleConstants.RULE_STRUCTDECLARATION_LBRACE_RBRACE:
                            //<StructDeclaration> ::= <StructDecHeader> '{' <VarDeclarationK> '}'
                        	 reduction.setType( ((Reduction)children.get(0)).getType() );
                        	 {
                        		 SymbolTable st = SymbolTable.ambito;
                        		 SymbolTable.ambito.pop();
                        		 if( ! SymbolTable.ambito.declareStruct(((Reduction)children.get(0)).getType().substring(7), st) ){
                        			 System.out.println(parser.currentLineNumber() + ": El tipo de estructura " + ((Reduction)children.get(0)).getType().substring(7) + " ya ha sido declarada.");
                        		 }
                        	 }
                            break;
                         case RuleConstants.RULE_STRUCTDECHEADER_STRUCT_ID:
                            //<StructDecHeader> ::= struct id
                        	reduction.setType( "struct:" + ((String)children.get(1)) );
                        	{
                        		SymbolTable ambito = new SymbolTable(  SymbolTable.ambito.getReturnType(), SymbolTable.ambito );
                        		ambito.set(false); 
                        	}
                            break;
                         case RuleConstants.RULE_VARDECLARATIONK:
                            //<VarDeclarationK> ::= <VarDeclaration> <VarDeclarationK>
                        	 reduction.setType("void");
                        	 reduction.code = ((Reduction)children.get(0)).code + "\n" +
                        	 		((Reduction)children.get(1)).code;
                        	 reduction.init_code = ((Reduction)children.get(0)).init_code + "\n" +
                 	 		((Reduction)children.get(1)).init_code;
                            break;
                         case RuleConstants.RULE_VARDECLARATIONK2:
                            //<VarDeclarationK> ::= 
                        	 reduction.setType("void");
                            break;
                         case RuleConstants.RULE_VARMETHODTYPE_INT:
                            //<VarMethodType> ::= int
                        	 reduction.setType("int");
                        	 reduction.code = "I";
                            break;
                         case RuleConstants.RULE_VARMETHODTYPE_CHAR:
                            //<VarMethodType> ::= char
                        	 reduction.setType("char");
                        	 reduction.code = "C";
                            break;
                         case RuleConstants.RULE_VARMETHODTYPE_BOOLEAN:
                            //<VarMethodType> ::= boolean
                        	 reduction.setType("boolean");
                        	 reduction.code = "Z";
                            break;
                         case RuleConstants.RULE_VARMETHODTYPE_VOID:
                            //<VarMethodType> ::= void
                        	 reduction.setType("void");
                        	 reduction.code = "V";
                            break;
                         case RuleConstants.RULE_VARMETHODTYPE_STRUCT_ID:
                            //<VarMethodType> ::= struct id
                        	 reduction.setType( "struct:" + ((String)children.get(1)) );
                            break;
                         case RuleConstants.RULE_VARMETHODTYPE:
                            //<VarMethodType> ::= <StructDeclaration>
                        	 reduction.setType( ((Reduction)children.get(0)).getType() );
                        	 SymbolTable.ambito.setTempKey( ((Reduction)children.get(0)).getType().substring(7) );
                            break;
                         case RuleConstants.RULE_METHODDECLARATION:
                            //<MethodDeclaration> ::= <MethodDecHeader> <block>
                        	 reduction.setType("void");
                        	 reduction.code = ((Reduction)children.get(0)).code +  "\n" +
                        	 		((Reduction)children.get(1)).code + "\n" +
                        	 				"return\n" +
                	 				".end method";
                            break;
                         case RuleConstants.RULE_METHODDECHEADER_ID_LPARAN_RPARAN:
                            //<MethodDecHeader> ::= <VarMethodType> id '(' <ParameterK> ')'
                        	 
                        	 // Verifica que no haya un struct como tipo de retorno (en los parámetros, ese chequeo se realiza sintácticamente.)
                        	 if( ((Reduction)children.get(0)).getType().startsWith("struct:") ){
                        		 System.out.println(parser.currentLineNumber() + ": Un método no puede tener un struct como tipo de retorno.");
                        	 }
                        	 {
                        		 
                        		 // Ingresa un nuevo ámbito
                        		 
                        		 SymbolTable ambito = new SymbolTable( ((Reduction)children.get(0)).getType() , SymbolTable.ambito );
                        		 ambito.set(false);
                        		 
                        		 // Verifica que no haya un método con el mismo nombre. 
                        		 
                        		 String fullname = (String) children.get(1) + ":" + ((Reduction)children.get(3)).getType() ;
                        		 //System.out.println(fullname);
                        		 String returnType = ((Reduction)children.get(0)).getType();
                        		 if( ! SymbolTable.ambito.addMethod(fullname, returnType) ){
                        			 System.out.println(parser.currentLineNumber() + ": Ya se ha declarado un método con el nombre deseado.");
                        		 }
                        		 
                        		 // Ingresa los parámetros como símbolos en el ámbito.
                        		 
                        		 String[] params = ((Reduction)children.get(3)).getValue().split("¬");
                        		 String[] paramTypes = ((Reduction)children.get(3)).getType().split("¬");
                        		 
                        		 if( params.length != paramTypes.length ) System.out.println("Mal \"parsing\" de parámetros X_X");
                        		 
                        		 if( params.length > 0 && ! params[0].equals("") ){
	                        		 for( int i = 0; i < params.length; i++ ){
	                        			 // System.out.println("Added: " + paramTypes[i] + " "  + params[i]);
	                        			 if(! SymbolTable.ambito.addVariable(paramTypes[i], params[i])){
	                        				 System.out.println(parser.currentLineNumber() + ": Nombre de parámetros repetido.");
	                        			 }
	                        		 }
                        		 }
                        	 }
                        	 
                        	 // Marca como válido aunque no lo sea para seguir detectando errores.
                        	 reduction.setType("void");
                        	 reduction.code = ".method public static " + children.get(1) + "(" + ((Reduction) children.get(3)).code + ")" + ((Reduction)children.get(0)).code + "\n" +
		                        	".limit stack 99\n" +
		                 	 		// ".limit locals " + SymbolTable.ambito.correlative + "\n";
                        	 		".limit locals " + 99 + "\n";
                            break;
                         case RuleConstants.RULE_PARAMETERK:
                            //<ParameterK> ::= <ParameterC>
                        	 reduction.setType(((Reduction)children.get(0)).getType());
                        	 reduction.setValue(((Reduction)children.get(0)).getValue());
                        	 reduction.code = ((Reduction)children.get(0)).code;
                            break;
                         case RuleConstants.RULE_PARAMETERK2:
                            //<ParameterK> ::= 
                        	 reduction.setType("");
                        	 // El code se queda igual
                            break;
                         case RuleConstants.RULE_PARAMETERC:
                            //<ParameterC> ::= <Parameter>
                        	 reduction.setType(((Reduction)children.get(0)).getType());
                        	 reduction.setValue(((Reduction)children.get(0)).getValue());
                        	 reduction.code = ((Reduction)children.get(0)).code;
                            break;
                         case RuleConstants.RULE_PARAMETERC_COMMA:
                            //<ParameterC> ::= <ParameterC> ',' <Parameter>
                        	 reduction.setType(((Reduction)children.get(0)).getType() + "¬" + ((Reduction)children.get(2)).getType());
                        	 reduction.setValue(((Reduction)children.get(0)).getValue() + "¬" + ((Reduction)children.get(2)).getValue());
                        	 reduction.code = ((Reduction)children.get(0)).code + ((Reduction)children.get(2)).code;
                            break;
                         case RuleConstants.RULE_PARAMETER_ID:
                            //<Parameter> ::= <ParameterType> id
                        	 reduction.setType(((Reduction)children.get(0)).getType());
                        	 reduction.setValue((String)children.get(1));
                        	 reduction.code = ((Reduction)children.get(0)).code;
                            break;
                         case RuleConstants.RULE_PARAMETER_ID_LBRACKET_RBRACKET:
                            //<Parameter> ::= <ParameterType> id '[' ']'
                        	 reduction.setType("array:" + ((Reduction)children.get(0)).getType());
                        	 reduction.setValue((String)children.get(1));
                        	 reduction.code = "[" + ((Reduction)children.get(0)).code;
                            break;
                         case RuleConstants.RULE_PARAMETERTYPE_INT:
                            //<ParameterType> ::= int
                        	 reduction.setType("int");
                        	 reduction.code = "I";
                            break;
                         case RuleConstants.RULE_PARAMETERTYPE_CHAR:
                            //<ParameterType> ::= char
                        	 reduction.setType("char");
                        	 reduction.code = "C";
                            break;
                         case RuleConstants.RULE_PARAMETERTYPE_BOOLEAN:
                            //<ParameterType> ::= boolean
                        	 reduction.setType("boolean");
                        	 reduction.code = "Z";
                            break;
                         case RuleConstants.RULE_BLOCK_LBRACE_RBRACE:
                            //<block> ::= '{' <VarDeclarationK> <StatementK> '}'
                        	 reduction.setType("void");
                        	 SymbolTable.ambito.pop();
                        	 reduction.code = ((Reduction) children.get(1)).code + "\n" +
                 	 		((Reduction) children.get(2)).code;
                            break;
                         case RuleConstants.RULE_STATEMENTK:
                            //<StatementK> ::= <Statement> <StatementK>
                        	 reduction.setType("void");
                        	 reduction.code = ((Reduction) children.get(0)).code + "\n" +
                        	 		((Reduction) children.get(1)).code;
                            break;
                         case RuleConstants.RULE_STATEMENTK2:
                            //<StatementK> ::=
                        	 reduction.setType("void");
                        	 // El code se queda igual (vacío)
                            break;
                         case RuleConstants.RULE_STATEMENT:
                            //<Statement> ::= <block>
                        	 reduction.setType("void");
                        	 reduction.code = ((Reduction) children.get(0)).code;
                            break;
                         case RuleConstants.RULE_STATEMENT_IF_LPARAN_RPARAN:
                            //<Statement> ::= if '(' <Expression> ')' <block> <BlockElse>
                        	 if( ! ((Reduction)children.get(2)).getType().equals("boolean") ){
                        		 System.out.println(parser.currentLineNumber() + ": En una condicional debe haber una condición de tipo boolean.");
                        	 }
                        	 reduction.setType("void");
                        	 reduction.true_l = genTrue();
                        	 reduction.false_l = genFalse();
                        	 reduction.done_l = genDone();
                        	 reduction.code = ((Reduction) children.get(2)).code + "\n" +
                        	 		"ifeq " + reduction.false_l + "\n" +
        	 						((Reduction) children.get(4)).code + "\n" +
	 								"goto " + reduction.done_l + "\n" +
									reduction.false_l + ":\n" + (((Reduction) children.get(5) ).code.isEmpty() ? ("nop") : (((Reduction) children.get(5) ).code)) + "\n" +
									reduction.done_l + ":";
                            break;
                         case RuleConstants.RULE_STATEMENT_WHILE_LPARAN_RPARAN:
                            //<Statement> ::= while '(' <Expression> ')' <block>
                        	 if( ! ((Reduction)children.get(2)).getType().equals("boolean") ){
                        		 System.out.println(parser.currentLineNumber() + ": En un ciclo debe haber una condición de tipo boolean.");
                        	 }
                        	 reduction.setType("void");
                        	 reduction.true_l = genTrue();
                        	 reduction.done_l = genDone();
                        	 reduction.code = reduction.true_l + ":\n" + ((Reduction) children.get(2)).code + "\n" +
                        	 		"ifeq " + reduction.done_l + "\n" +
                	 				((Reduction) children.get(4)).code + "\n" +
        	 						"goto " + reduction.true_l + "\n" +
	 								reduction.done_l + ": \n";
                            break;
                         case RuleConstants.RULE_STATEMENT_RETURN_SEMI:
                            //<Statement> ::= return <ExpressionOpc> ';'
                        	 if( ! ((Reduction)children.get(1)).getType().equals( SymbolTable.ambito.getReturnType() ) ){
                        		 System.out.println(parser.currentLineNumber() + ": Tipo de retorno inválido. Se retorna " + ((Reduction)children.get(1)).getType() + ", pero debería ser " + SymbolTable.ambito.getReturnType());
                        	 }
                        	 reduction.setType("void");
                        	 
                        	 if( ((Reduction) children.get(1) ).code.isEmpty() )
                        	 	reduction.code = "return";
                        	 else
                        	 	reduction.code = ((Reduction) children.get(1)).code + "\n" +
                        	 			"ireturn";
                            break;
                         case RuleConstants.RULE_STATEMENT_EQ:
                            //<Statement> ::= <Location> '=' <Expression>
                        	 if( ! ((Reduction)children.get(0)).getType().equals(((Reduction)children.get(2)).getType()) ){
                        		 System.out.println(parser.currentLineNumber() + ": No se puede asignar un valor " + ((Reduction)children.get(2)).getType() + " a una variable tipo " + ((Reduction)children.get(0)).getType() );
                        	 }
                        	 if( ((Reduction) children.get(0)).code.contains("iload") ){
                        		 ((Reduction) children.get(0)).code = ((Reduction) children.get(0)).code.replace("iload", "istore");
                        		 reduction.code = ((Reduction) children.get(2)).code + "\n" +
                        		 		((Reduction) children.get(0)).code;
                        	 }
                        	 else{
                        		 reduction.code = /*((Reduction) children.get(2)).code + "\n" +*/
                 		 		((Reduction) children.get(2)).code + "\n" +
	                 		 		((Reduction) children.get(0)).code.replace("getstatic", "putstatic");
                        	 }
                            break;
                         case RuleConstants.RULE_STATEMENT_SEMI:
                            //<Statement> ::= <ExpressionOpc> ';'
                        	 reduction.setType("void");
                        	 reduction.code = ((Reduction)children.get(0)).code;
                            break;
                         case RuleConstants.RULE_BLOCKELSE_ELSE:
                            //<BlockElse> ::= else <block>
                        	 reduction.setType("void");
                        	 reduction.code = ((Reduction)children.get(1)).code;
                            break;
                         case RuleConstants.RULE_BLOCKELSE:
                            //<BlockElse> ::=
                        	 reduction.setType("void");
                        	 // El código se queda vacío
                            break;
                         case RuleConstants.RULE_EXPRESSIONOPC:
                            //<ExpressionOpc> ::= <Expression>
                        	 reduction.setType(((Reduction)children.get(0)).getType());
                        	 reduction.code = ((Reduction)children.get(0)).code;
                            break;
                         case RuleConstants.RULE_EXPRESSIONOPC2:
                            //<ExpressionOpc> ::= 
                        	 reduction.setType("void");
                        	 // El código se queda vacío
                            break;
                         case RuleConstants.RULE_EXPRESSION:
                            //<Expression> ::= <RelExp> <CondOp> <Expression>
                        	 if(! ((Reduction)children.get(0)).getType().equals("boolean") || ! ((Reduction)children.get(2)).getType().equals("boolean") ){
                        		 System.out.println( parser.currentLineNumber() + ": Ambos lados de la operación deben ser boolean." );
                        	 }
                        	 reduction.setType("boolean");
                        	 reduction.code = ((Reduction)children.get(0)).code + "\n" +
                        	 		((Reduction)children.get(2)).code + "\n" +
                        	 		((Reduction)children.get(1)).code;
                            break;
                         case RuleConstants.RULE_EXPRESSION2:
                            //<Expression> ::= <RelExp>
                        	 reduction.setType(((Reduction)children.get(0)).getType());
                        	 reduction.code = ((Reduction)children.get(0)).code;
                            break;
                         case RuleConstants.RULE_RELEXP:
                            //<RelExp> ::= <SumExp> <RelOp> <RelExp>
                        	 if( (! ((Reduction)children.get(0)).getType().equals("int")) || (! ((Reduction)children.get(2)).getType().equals("int")) ){
                        		 System.out.println( parser.currentLineNumber() + ": Ambos lados de la operación deben ser int." );
                        	 }
                        	 reduction.setType("boolean");
                        	 reduction.code = ((Reduction)children.get(0)).code + "\n" +
		          	 				((Reduction)children.get(2)).code + "\n" +
		          	 				((Reduction)children.get(1)).code + ((Reduction)children.get(1)).true_l + "\n" +
		  	 						"goto " + ((Reduction)children.get(1)).false_l + "\n" +
		          	 				((Reduction)children.get(1)).true_l + ":\niconst_1\n" +
		  	 						"goto " + ((Reduction)children.get(1)).done_l + "\n" +
		  	 						((Reduction)children.get(1)).false_l + ":\niconst_0\n" +
		  	 						((Reduction)children.get(1)).done_l + ": \n";
                            break;
                         case RuleConstants.RULE_RELEXP2:
                            //<RelExp> ::= <SumExp> <EqOp> <RelExp>
                        	 if( ( ! ((Reduction)children.get(0)).getType().equals("int") || ! ((Reduction)children.get(2)).getType().equals("int") )
                        			 && ( ! ((Reduction)children.get(0)).getType().equals("boolean") || ! ((Reduction)children.get(2)).getType().equals("boolean") )
                        					 && ( ! ((Reduction)children.get(0)).getType().equals("char") || ! ((Reduction)children.get(2)).getType().equals("char") )){
                        		 System.out.println( parser.currentLineNumber() + ": Ambos lados de la operación deben ser int, boolean o char y del mismo tipo." );
                        	 }
                        	 reduction.setType("boolean");
                        	 reduction.code = ((Reduction)children.get(0)).code + "\n" +
                 	 				((Reduction)children.get(2)).code + "\n" +
                 	 				((Reduction)children.get(1)).code + ((Reduction)children.get(1)).true_l + "\n" +
         	 						"goto " + ((Reduction)children.get(1)).false_l + "\n" +
                 	 				((Reduction)children.get(1)).true_l + ":\niconst_1\n" +
         	 						"goto " + ((Reduction)children.get(1)).done_l + "\n" +
         	 						((Reduction)children.get(1)).false_l + ":\niconst_0\n" +
         	 						((Reduction)children.get(1)).done_l + ": \n";
                            break;
                         case RuleConstants.RULE_RELEXP3:
                            //<RelExp> ::= <SumExp>
                        	 reduction.setType(((Reduction)children.get(0)).getType());
                        	 reduction.code = ((Reduction)children.get(0)).code;
                            break;
                         case RuleConstants.RULE_SUMEXP:
                            //<SumExp> ::= <MultExp> <SumOp> <SumExp>
                        	 if(! ((Reduction)children.get(0)).getType().equals("int") || ! ((Reduction)children.get(2)).getType().equals("int") ){
                        		 System.out.println( parser.currentLineNumber() + ": Ambos lados de la operación deben ser int." );
                        	 }
                        	 reduction.setType("int");
                        	 reduction.code = ((Reduction)children.get(0)).code + "\n" +
                        	 		((Reduction)children.get(2)).code + "\n" +
                        	 		((Reduction)children.get(1)).code;
                            break;
                         case RuleConstants.RULE_SUMEXP2:
                            //<SumExp> ::= <MultExp>
                        	 reduction.setType(((Reduction)children.get(0)).getType());
                        	 reduction.code = ((Reduction)children.get(0)).code;
                            break;
                         case RuleConstants.RULE_MULTEXP:
                            //<MultExp> ::= <NegExp> <MultOp> <MultExp>
                        	 if(! ((Reduction)children.get(0)).getType().equals("int") || ! ((Reduction)children.get(2)).getType().equals("int") ){
                        		 System.out.println( parser.currentLineNumber() + ": Ambos lados de la operación deben ser int." );
                        	 }
                        	 reduction.setType("int");
                        	 reduction.code = reduction.code = ((Reduction)children.get(0)).code + "\n" +
                        	 		((Reduction)children.get(2)).code + "\n" +
                        	 		((Reduction)children.get(1)).code;
                            break;
                         case RuleConstants.RULE_MULTEXP2:
                            //<MultExp> ::= <NegExp>
                        	 reduction.setType(((Reduction)children.get(0)).getType());
                        	 reduction.code = ((Reduction)children.get(0)).code;
                            break;
                         case RuleConstants.RULE_NEGEXP_MINUS:
                            //<NegExp> ::= '-' <val>
                        	 if( ! ((Reduction)children.get(1)).getType().equals("int") ){
                        		 System.out.println( parser.currentLineNumber() + ": La expresión debe ser tipo int." );
                        	 }
                        	 reduction.setType("int");
                        	 reduction.code = ((Reduction)children.get(1)).code + "\n" +
                        	 		"ineg";
                            break;
                         case RuleConstants.RULE_NEGEXP_EXCLAM:
                            //<NegExp> ::= '!' <val>
                        	 if( ! ((Reduction)children.get(1)).getType().equals("boolean") ){
                        		 System.out.println( parser.currentLineNumber() + ": La expresión debe ser tipo boolean." );
                        	 }
                        	 reduction.setType("boolean");
                        	 reduction.true_l = genTrue();
                        	 reduction.false_l = genFalse();
                        	 reduction.done_l = genDone();
                        	 reduction.code = ((Reduction) children.get(1)).code + "\n" +
                        	 		"ifeq " + reduction.true_l + "\n" +
                        	 		"goto " + reduction.false_l + "\n" +
                	 				reduction.true_l + ":\niconst_1\n" +
        	 						"goto " + reduction.done_l + "\n" +
	 								reduction.false_l + ": iconst_0\n" +
									reduction.done_l + ": \n";
                            break;
                         case RuleConstants.RULE_NEGEXP:
                            //<NegExp> ::= <val>
                        	 reduction.setType(((Reduction)children.get(0)).getType());
                        	 reduction.code = ((Reduction) children.get(0)).code;
                            break;
                         case RuleConstants.RULE_CONDOP_AMPAMP:
                            //<CondOp> ::= '&&'
                        	 reduction.setType("void");
                        	 reduction.code = "iand";
                            break;
                         case RuleConstants.RULE_CONDOP_PIPEPIPE:
                            //<CondOp> ::= '||'
                        	 reduction.setType("void");
                        	 reduction.code = "ior";
                            break;
                         case RuleConstants.RULE_RELOP_LTEQ:
                            //<RelOp> ::= '<='
                        	 reduction.setType("void");
                        	 reduction.code = "if_icmple ";
                        	 reduction.true_l = genTrue();
                        	 reduction.false_l = genFalse();
                        	 reduction.done_l = genDone();
                            break;
                         case RuleConstants.RULE_RELOP_LT:
                            //<RelOp> ::= '<'
                        	 reduction.setType("void");
                        	 reduction.code = "if_icmplt ";
                        	 reduction.true_l = genTrue();
                        	 reduction.false_l = genFalse();
                        	 reduction.done_l = genDone();
                            break;
                         case RuleConstants.RULE_RELOP_GT:
                            //<RelOp> ::= '>'
                        	 reduction.setType("void");
                        	 reduction.code = "if_icmpgt ";
                        	 reduction.true_l = genTrue();
                        	 reduction.false_l = genFalse();
                        	 reduction.done_l = genDone();
                            break;
                         case RuleConstants.RULE_RELOP_GTEQ:
                            //<RelOp> ::= '>='
                        	 reduction.setType("void");
                        	 reduction.code = "if_icmpge ";
                        	 reduction.true_l = genTrue();
                        	 reduction.false_l = genFalse();
                        	 reduction.done_l = genDone();
                            break;
                         case RuleConstants.RULE_EQOP_EQEQ:
                            //<EqOp> ::= '=='
                        	 reduction.setType("void");
                        	 reduction.code = "if_icmpeq ";
                        	 reduction.true_l = genTrue();
                        	 reduction.false_l = genFalse();
                        	 reduction.done_l = genDone();
                            break;
                         case RuleConstants.RULE_EQOP_EXCLAMEQ:
                            //<EqOp> ::= '!='
                        	 reduction.setType("void");
                        	 reduction.code = "if_icmpne ";
                        	 reduction.true_l = genTrue();
                        	 reduction.false_l = genFalse();
                        	 reduction.done_l = genDone();
                            break;
                         case RuleConstants.RULE_SUMOP_PLUS:
                            //<SumOp> ::= '+'
                        	 reduction.setType("void");
                        	 reduction.code = "iadd";
                            break;
                         case RuleConstants.RULE_SUMOP_MINUS:
                            //<SumOp> ::= '-'
                        	 reduction.setType("void");
                        	 reduction.code = "isub";
                            break;
                         case RuleConstants.RULE_MULTOP_TIMES:
                            //<MultOp> ::= '*'
                        	 reduction.setType("void");
                        	 reduction.code = "imul";
                            break;
                         case RuleConstants.RULE_MULTOP_DIV:
                            //<MultOp> ::= '/'
                        	 reduction.setType("void");
                        	 reduction.code = "idiv";
                            break;
                         case RuleConstants.RULE_MULTOP_PERCENT:
                            //<MultOp> ::= '%'
                        	 reduction.setType("void");
                        	 reduction.code = "irem";
                            break;
                         case RuleConstants.RULE_VAL:
                            //<val> ::= <Location>
                        	 reduction.setType(((Reduction)children.get(0)).getType());
                        	 reduction.code = ((Reduction)children.get(0)).code;
                        	 /*if( ! reduction.code.contains("\n") ){
                        		 reduction.code = "iload " + reduction.code;
                        	 }*/
                            break;
                         case RuleConstants.RULE_VAL2:
                            //<val> ::= <MethodCall>
                         {
                        	 String tipoMC = ((Reduction)children.get(0)).getType();
                        	 if( ! tipoMC.equals("void") ){
                        	 	reduction.setType(tipoMC);
                        	 }
                        	 else{
                        		 System.out.println(parser.currentLineNumber() + ": No se pude utilizar un método con tipo de retorno void como un valor en una expresión.");
                        		 done = true;
                        	 }
                        	 reduction.code = ((Reduction)children.get(0)).code;
                         }
                        	 
                            break;
                         case RuleConstants.RULE_VAL3:
                            //<val> ::= <Literal>
                        	 reduction.setType(((Reduction)children.get(0)).getType());
                        	 reduction.code = ((Reduction)children.get(0)).code;
                            break;
                         case RuleConstants.RULE_VAL_LPARAN_RPARAN:
                            //<val> ::= '(' <Expression> ')'
                        	 reduction.setType(((Reduction)children.get(1)).getType());
                        	 reduction.code = ((Reduction)children.get(1)).code;
                            break;
                         case RuleConstants.RULE_LOCATION:
                            //<Location> ::= <LocationMain>
                        	 reduction.setType(((Reduction)children.get(0)).getType());
                        	 reduction.code = ((Reduction)children.get(0)).code;
                            break;
                         case RuleConstants.RULE_LOCATION_DOT: // Kind of deprecated...
                            //<Location> ::= <LocationMain> '.' <Location>
                            break;
                         case RuleConstants.RULE_LOCATIONMAIN_ID:
                            //<LocationMain> ::= id <LocationOpc>
                         {
                        	 String type = SymbolTable.ambito.globalTypeSearch((String)children.get(0));
                        	 int esLocal = SymbolTable.ambito.getMethodIndex((String)children.get(0));
                        	 if( type != null ){
	                        	 if( type.startsWith("array:") ){
	                        		 if( ((Reduction)children.get(1)).getType().equals("array:") ){
	                        			 type = type.substring(6);
	                        		 }
	                        	 } else{
	                        		 if( ((Reduction)children.get(1)).getType().equals("array:") ){
	                        			 System.out.println(parser.currentLineNumber() + ": La variable " + (String)children.get(0) + " no es un arreglo, por lo que no se puede acceder la posición indicada.");
	                        		 }
	                        	 }
	                        	 /*
	                        	 if( type.startsWith("struct:") ){
	                        		 SymbolTable ambito = SymbolTable.ambito.globalStructTypeSearch( type.substring(7) );
	                        		 if( ambito == null ) System.out.println("Otro craso... X_X");
	                        		 ambito.set();
	                        	 }
	                        	 //*/
	                        	 reduction.setType( type );
	                        	 reduction.code = (esLocal == -1) ?
	                        			 ("getstatic Program/" + children.get(0) + " " +
	                        					 (type.equals("int") ? "I" :
	                        						 (type.equals("boolean") ? "Z" :
	                        							 (type.equals("char") ? "C" : "craso"))))
                    			 		:
                    			 		 ("iload " + esLocal); //TODO
	                        	/*
	                        	 reduction.code = "aload 0\n" +
             			 			((esLocal == -1) ? ("getfield " + children.get(0)) : ("iload " + esLocal)) + "\n" +
     			 					((Reduction)children.get(1)).code;
 			 					//*/
                        	 } else{
                        		 System.out.println(parser.currentLineNumber() + ": La variable " + ((String) children.get(0)) + " no se encuentra en la tabla de símbolos.");
                        		 done = true;
                        	 }
                         }
                            break;
                         case RuleConstants.RULE_LOCATIONOPC_LBRACKET_RBRACKET:
                            //<LocationOpc> ::= '[' <Expression> ']'
                        	 if( ! ((Reduction)children.get(1)).getType().equals("int") ){
                        		 System.out.println(parser.currentLineNumber() + ": Una expresión de acceso a un arreglo debe ser de tipo int.");
                        	 }
                        	 reduction.setType("array:");
                        	 reduction.code = ((Reduction) children.get(1)).code;
                            break;
                         case RuleConstants.RULE_LOCATIONOPC:
                            //<LocationOpc> ::= 
                        	 reduction.setType("");
                            break;
                         case RuleConstants.RULE_METHODCALL_ID_LPARAN_RPARAN:
                            //<MethodCall> ::= id '(' <ArgK> ')'
                         {
                        	 String fullName = (String) children.get(0) + ":" + ((Reduction)children.get(2)).getType();
                        	 //System.out.println(fullName);
                        	 Metodo metodo = SymbolTable.ambito.getMethod(fullName);
                        	 if( metodo == null ){
                        		 System.out.println(parser.currentLineNumber() + ": El método " + (String) children.get(0) + " no se encuentra en la tabla de símbolos.");
                        		 done = true;
                        	 } else{
                        		 reduction.setType( metodo.darTipo() );
                        	 }
                        	 reduction.code = ((Reduction)children.get(2)).code + "\n" +
                        	 		"invokestatic " + "Program." + (String) children.get(0) + "(";
                        	 for( String type : metodo.darArgumentType().split("¬") ){
                        		 if( type.equals("int") ) reduction.code += "I";
                            	 else if( type.equals("void") ) reduction.code += "V";
                            	 else if( type.equals("boolean") ) reduction.code += "Z";
                            	 else if( type.equals("char") ) reduction.code += "C";                        		 
                        	 }
                        	 reduction.code += ")";
                        	 if( metodo.darTipo().equals("int") ) reduction.code += "I";
                        	 else if( metodo.darTipo().equals("void") ) reduction.code += "V";
                        	 else if( metodo.darTipo().equals("boolean") ) reduction.code += "Z";
                        	 else if( metodo.darTipo().equals("char") ) reduction.code += "C";
                         }
                            break;
                         case RuleConstants.RULE_ARGK:
                            //<ArgK> ::= <ArgC>
                        	 reduction.setType(((Reduction)children.get(0)).getType());
                        	 reduction.code = ((Reduction)children.get(0)).code;
                        	 
                            break;
                         case RuleConstants.RULE_ARGK2:
                            //<ArgK> ::= 
                        	 reduction.setType("");
                        	 reduction.code="";
                            break;
                         case RuleConstants.RULE_ARGC:
                            //<ArgC> ::= <Arg>
                        	 reduction.setType(((Reduction)children.get(0)).getType());
                        	 reduction.code = ((Reduction)children.get(0)).code;
                            break;
                         case RuleConstants.RULE_ARGC_COMMA:
                            //<ArgC> ::= <ArgC> ',' <Arg>
                        	 reduction.setType(((Reduction)children.get(2)).getType() + "¬" + ((Reduction)children.get(2)).getType());
                        	 reduction.code = ((Reduction)children.get(0)).code + "\n" +
                        	 	((Reduction)children.get(2)).code;
                            break;
                         case RuleConstants.RULE_ARG:
                            //<Arg> ::= <Expression>
                        	 reduction.setType(((Reduction)children.get(0)).getType());
                        	 reduction.code = ((Reduction)children.get(0)).code;
                            break;
                         case RuleConstants.RULE_LITERAL:
                            //<Literal> ::= <int_literal>
                        	 reduction.setType("int");
                        	 reduction.code = ((Reduction)children.get(0)).code;
                            break;
                         case RuleConstants.RULE_LITERAL2:
                            //<Literal> ::= <char_literal>
                        	 reduction.setType("char");
                        	 reduction.code = ((Reduction)children.get(0)).code;
                            break;
                         case RuleConstants.RULE_LITERAL3:
                            //<Literal> ::= <bool_literal>
                        	 reduction.setType("boolean");
                        	 reduction.code = ((Reduction)children.get(0)).code;
                            break;
                         case RuleConstants.RULE_INT_LITERAL_NUM:
                            //<int_literal> ::= num
                        	 reduction.setType("int");
                        	 reduction.code="ldc " + (String) children.get(0);
                            break;
                         case RuleConstants.RULE_CHAR_LITERAL_CHARACTER:
                            //<char_literal> ::= character
                        	 reduction.setType("char");
                        	 reduction.code="bipush " +  (int) ((String) children.get(0)).charAt(1);
                            break;
                         case RuleConstants.RULE_BOOL_LITERAL_TRUE:
                            //<bool_literal> ::= true
                        	 reduction.setType("boolean");
                        	 reduction.code="iconst_1";
                            break;
                         case RuleConstants.RULE_BOOL_LITERAL_FALSE:
                            //<bool_literal> ::= false
                        	reduction.setType("boolean");
                        	reduction.code="iconst_0";
                            break;
                      }
                      
                      /*
                      if( reduction.code != "" )
                    	  System.out.println(reduction.code);
                      //*/
                    //Parser.Reduction = //Object you created to store the rule

                      /* ************************************** log file
                      Reduction myRed = parser.currentReduction();
                      System.out.println(myRed.getParentRule().getText());
                      // ************************************** end log
                      //*/
                      
                      /*
                      System.out.println("Reducción: " + reduction.getParentRule().getText());
                      for( int i = 0; i < children.size(); i++ ){
                    	  System.out.println("\t" + children.get(i).toString());
                      }
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
            if( response == gpMsgAccept ){
            	
            	{
            		PrintWriter out = null;
            		try{
            			out = new PrintWriter("Program.j");
            		} catch( Exception e ){
            			e.printStackTrace();
            		}
            		
            		for( String linea : parser.currentReduction().code.split("\n") ){
							out.println(linea);
            		}
            		try{
            			out.close();
            		} catch( Exception e ){
            			e.printStackTrace();
            		}
            	}
            	
          	  return parser.currentReduction();
            }
            else return null;
        }
        catch(ParserException parse)
        {
            System.out.println("**PARSER ERROR**\n" + parse.toString());
            // System.exit(1);
            return null;
        }
    }
}
