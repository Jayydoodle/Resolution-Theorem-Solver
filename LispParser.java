package Project2.src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Stack;

/** LispParser
** 
**  @author Jason Allen
**
**  Parses lisp files into a format suitabe for the theorem-prover
**  Files must mimic the format of test-prover.lisp
**
*/
class LispParser {

    /** =============== parse =================
    Parses lisp files into a format suitabe for 
    the theorem-prover

     @param file the name of the file to parse
     @return a DataBase object

    **/ 
    public static DataBase parse(String file){

        try {
            
            BufferedReader br = new BufferedReader(new FileReader(file));
            
            String line; 
            Stack<Character> stack = new Stack<Character>();
            boolean addingToKB = false;
            
            DataBase dBase = new DataBase();
            KnowledgeBase newKnowledgeBase = new KnowledgeBase();
            OpenList newOpenList = new OpenList();
            BindingMap newBindingMap = new BindingMap();
            
            Clause newClause;
    
            while ((line = br.readLine()) != null){
    
                if(!line.isEmpty() && line.substring(1, 7).equals("defvar")){
    
                    stack.push(line.charAt(0));
                    String[] startLine = line.split(" ");
                    String lineIdentifier = startLine[1];
    
                    if(lineIdentifier.charAt(1) == 'p'){
                        addingToKB = true;
                    }
                    if(lineIdentifier.charAt(1) == 'g'){
                        addingToKB = false;
                    }
                    continue;
                }
                if(!line.isEmpty()){
                    
                    String predicateString = "";
                    newClause = new Clause();
                    
                    for(int i = 0; i < line.length(); i++){
    
                        char currentChar = line.charAt(i);
                        switch (currentChar) {
                            case '\'':
                                break;
                            case ' ':
                                if(!predicateString.isEmpty()){
                                    predicateString += currentChar;
                                }
                                break;
                            case '(':
                                stack.push(currentChar);
                                break;
                            case ')':
                                stack.pop();
                                if(!predicateString.isEmpty()){
                                        
                                    Predicate newPredicate = new Predicate(predicateString.split(" "));
                                    newClause.addPredicate(newPredicate);
                                    predicateString = "";
                                }
                                if(stack.isEmpty()){
                                    if(addingToKB){
                                        newKnowledgeBase.addClause(newClause);
                                    }
                                    else{
                                        newOpenList.addClause(newClause);
                                        dBase.add(newKnowledgeBase, newOpenList, newBindingMap);
                                       
                                        newOpenList = new OpenList();
                                        newKnowledgeBase = new KnowledgeBase();
                                        newBindingMap = new BindingMap();
                                    }
                                }
                                break;
                            default:
                                predicateString += currentChar;
                                break;
                        }
                    }
                    if(!stack.isEmpty())
                        newKnowledgeBase.addClause(newClause);
                } 
            }
            br.close();
            return dBase;
        } catch (Exception e) {
            //TODO: handle exception
        } 
        return null;
    }

}

