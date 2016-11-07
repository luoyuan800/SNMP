import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LexerDistanceServlet")
public class LexerDistanceServlet extends HttpServlet {
private static final long serialVersionUID = 1L;    
//declare variables
public static String s2a;
public static String s2b;
//Start of Lexer (part1) codes//
public static enum TokenType 
{
        NUMBER("-?[0-9]+"), 
        BINARYOP("[*|/|+|-|=]"),
        KEYWORD("[abstract|assert|boolean|break|byte|case|catch|char|class|const|continue|default|do|double|else|enum|extends|false|final|finally|float|for|goto|if|implements|import|instanceof|int|interface|long|native|new|null|package|private|protected|public|return|short|static|strictfp|super|switch|synchronized|'String'|this|throw|throws|true|transient|try|void|volatile|while]+"),
        STRINGLITERAL("\"([^\\\"]|\\.)*\""), 
        IDENTIFIER("[a-zA-Z][a-zA-Z0-9]*"), 
        WHITESPACE("[ \t\f\r\n]+"), 
        OPERATOR("[~|`|!|@|#|$|%|^|&|(|)|_|{|[|}|]|||\"|:|;|'|<|>|?|,|.|<%|%>|<?|?>]");
        
        public final String pattern;
        private TokenType(String pattern)
        {
            this.pattern = pattern;
        }
    }
//token class
    public static class Token
    {
        public TokenType type;
        public String input;

        public Token(TokenType type, String input)
        {
        this.type = type;
            this.input = input;
        }

        @Override
        //output format: tokentype input
        public String toString()
        {
            return String.format("(%s %s)", type.name(), input);
        }
		
    }
    
    //store all input in an arraylist
    public static ArrayList<Token> lex(String input) {
        // The tokens to return
        ArrayList<Token> tokens = new ArrayList<Token>();
        // Lexer logic begins here
        StringBuffer tokenPatternsBuffer = new StringBuffer();
        for (TokenType tokenType : TokenType.values())
            tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
        Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));
        // Begin matching tokens
        Matcher matcher = tokenPatterns.matcher(input);
        while (matcher.find()) {
            if (matcher.group(TokenType.NUMBER.name()) != null)
            {
                tokens.add(new Token(TokenType.NUMBER, matcher.group(TokenType.NUMBER.name())));
                continue;
            }
            else if (matcher.group(TokenType.BINARYOP.name()) != null)
            {
                tokens.add(new Token(TokenType.BINARYOP, matcher.group(TokenType.BINARYOP.name())));
                continue;
            }
            else if (matcher.group(TokenType.IDENTIFIER.name()) != null)
            {
               tokens.add(new Token(TokenType.IDENTIFIER, matcher.group(TokenType.IDENTIFIER.name())));
               continue;
            }
            else if (matcher.group(TokenType.STRINGLITERAL.name()) != null)
            {
               tokens.add(new Token(TokenType.STRINGLITERAL, matcher.group(TokenType.STRINGLITERAL.name())));
               continue;
            }            
            else if (matcher.group(TokenType.KEYWORD.name()) != null)
            {
                tokens.add(new Token(TokenType.KEYWORD, matcher.group(TokenType.KEYWORD.name())));
                continue;
            }            
            else if (matcher.group(TokenType.OPERATOR.name()) != null)
            {
                tokens.add(new Token(TokenType.OPERATOR, matcher.group(TokenType.OPERATOR.name())));
                continue;
            }
            else if (matcher.group(TokenType.WHITESPACE.name()) != null)
                continue;
        }
        return tokens;  
    }

    

/* This method calculates the levenshtein distance given 2 arrays of code tokens
	** subTokens: the code submitted by students tokenized into an array of code tokens.
	** intendedTokens: the 'correct' code to be matched, tokenized into an array of code tokens.
	** returns the levenshtein distance
	** ----------------------------------------------------*/
	public int LevenshteinDistance (ArrayList<Token> input1, ArrayList<Token> input2) {                          
	//convert arraylist into array 
    Token subTokens[] = input1.toArray(new Token[input1.size()]);
    Token intendedTokens[] = input2.toArray(new Token[input2.size()]);

    //System.out.println("sub tokens = " + subTokens.length);
    //System.out.println("intended tokens = " + intendedTokens.length);	    

	int len0 = subTokens.length+1;                                                     
	int len1 = intendedTokens.length+1;  

	                                                                                    
    // the array of distances                                                       
    int[] cost = new int[len0];                                                     
    int[] newcost = new int[len0];                                                  
    int[][] op = new int[len0][len1];    //to store the operation to be performed ie insert, delete or replace
    int[] minCostIdx = new int[len1];
    // initial cost of skipping prefix in String s0                                 
    for (int i = 0; i < len0; i++) cost[i] = i;                                     
                                                                                    
    // dynamically computing the array of distances                                  
                                                                                    
    // transformation cost for each letter in s1
    
    for (int j = 1; j < len1; j++) {                                                
        // initial cost of skipping prefix in String s1                             
        newcost[0] = j;                                                             
        //int i = j;
              
        // transformation cost for each letter in s0                                
        for(int i = 1; i < len0; i++) {                                             
            // matching current letters in both strings  
        	System.out.println(" ");
        	//System.out.println(" ");
        	//System.out.println("SubToken " + subTokens[i-1]);
        	//System.out.println("IntendedTokens " + intendedTokens[j-1]);
        	
        	
            int match = (subTokens[i-1].toString().equals(intendedTokens[j-1].toString())) ? 0 : 1;             
            //System.out.println("Match " + match);
            // computing cost for each transformation                               
            int cost_replace = cost[i - 1] + match;                                 
            int cost_insert  = cost[i] + 1;                                         
            int cost_delete  = newcost[i - 1] + 1;                                  
                                                                                    
            // keep minimum cost                                                    
            newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            //System.out.format("row %d col %d cost:%d\n",i,j,newcost[i]);
            if (match==0){
            	op[i][j]=0;
            	if (j==i){
            		System.out.println("SubToken " + subTokens[i-1]);
                	System.out.println("IntendedTokens " + intendedTokens[j-1]);
            		System.out.println("match " + subTokens[i-1] + "    " +  intendedTokens[j-1] );
            	}
            }
            else { 
            	
            	if (newcost[i]==cost_insert)
	            {
	            	op[i][j]=1;
	            	//if (j==i || j==i-1)
	            	//if (subTokens.length > intendedTokens.length)
	            	if (j - 1 ==  subTokens.length)
	            	{
	            		System.out.println("SubToken " + subTokens[i-1]);
	                	System.out.println("IntendedTokens " + intendedTokens[j-1]);
	            		System.out.println("insert " +  intendedTokens[j-1]);
	            		break;
	            	}
	            } 
            	else if (newcost[i]==cost_delete) 
            	{
	            	op[i][j]=2;
	            	//if (j==i || j==i+1)
	            	//if (subTokens.length > intendedTokens.length)
	            	if (i - 1 == intendedTokens.length)
	            	{
	            		System.out.println("SubToken " + subTokens[i-1]);
	                	System.out.println("IntendedTokens " + intendedTokens[j-1]);
	            		System.out.println("delete " + subTokens[i-1]);
	            		break;
	            	}	            	
	            } 
            	else if (newcost[i]==cost_replace) 
            	{
	            	op[i][j]=3;
	            	if (j==i)
	            	{
	            		System.out.println("SubToken " + subTokens[i-1]);
	                	System.out.println("IntendedTokens " + intendedTokens[j-1]);
	            		System.out.println("replace " + subTokens[i-1] + " to " + intendedTokens[j-1]);
	            	}
	            }
            }   
        }                                                                        
        minCostIdx[j] = findMinimumCostIdx(newcost);
        
        // swap cost/newcost arrays                                                 
        int[] swap = cost; cost = newcost; newcost = swap;                          
    }                                                                               
                                                                                    
    // the distance is the cost for transforming all letters in both strings        
    return cost[len0 - 1];                                                          
}

private int findMinimumCostIdx(int[] newcost) {
	int minCost = newcost[1];
	int minCostIdx=1;
	for (int ii=1; ii<newcost.length;ii++) {
		if (newcost[ii]<=minCost) {
			minCost=newcost[ii];
			minCostIdx = ii;
		}
	}
	return minCostIdx;
}



    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
{
    //get input from fields in FinalForm.jsp
s2a = request.getParameter("s2a");
s2b = request.getParameter("s2b");

//Start of Lexer (part2) codes//
        //Create tokens, count no. of tokens and print them
    //print token at console
        ArrayList<Token> tokens1 = lex(s2a);
        for (Token token1 : tokens1)
            System.out.println(token1);
        System.out.println("There are " + tokens1.size() + " tokens");
       
        ArrayList<Token> tokens2 = lex(s2b);
        for (Token token2 : tokens2)
            System.out.println(token2);
        System.out.println("There are " + tokens2.size() + " tokens");
        
        //print in the webpage
        for (Token token1 : tokens1)
    {
        PrintWriter writer = response.getWriter();
        String htmlRespone = "<html>";
htmlRespone += "Token1:      " + (token1) + "<br>";
htmlRespone += "</html>";
writer.println(htmlRespone);
    }
        for (Token token2 : tokens2)
    {
        PrintWriter writer = response.getWriter();
        String htmlRespone = "<html>";
htmlRespone += "Token2:      " + (token2) + "<br>";
htmlRespone += "</html>";
writer.println(htmlRespone);
    }
//End of Lexer (part2) codes
        //Start Levenshtein (part 2) codes here  
        //System.out.println("distance = " + LevenshteinDistance(tokens1, tokens2));
   
        //appears in the webpage
       PrintWriter writer = response.getWriter();
String htmlRespone = "<html>";
htmlRespone += "<hr><br>";
htmlRespone += "Levenshtein Distance = " + LevenshteinDistance(tokens1, tokens2);
htmlRespone += "</html>";
writer.println(htmlRespone);
    //End Levenshtein (part 2) codes here//
}
}