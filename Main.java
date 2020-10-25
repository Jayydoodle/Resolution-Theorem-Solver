package Project2.src;

import java.io.File;
import java.util.*;

/** Thorem-Solver
** 
**  @author Jason Allen
**
**  Accepts a file containing a knowledge base and a goal clause, and attempts to perform
**  resolution.  Can parse any number of knowledge base and goal clause pairs as long as
**  the file presented is in a similar format to 'test-prover.lisp'.  The default behavior
**  is to read from 'test-prover.lisp' when the program begins, but you may provide your 
**  own file to read from by entering -1 as your selection and entering the file name when
**  prompted.  The result from each test will be printed to a file Solutions_'testNum'_'type'.txt
**
*/
public class Main {

    public static void main(String[] args) {

        run();
    }
    public static void run(){

        String originalFileName = "test-prover.lisp";
        String fileName = originalFileName;
        Scanner input = new Scanner(System.in);

        while(true){

            DataBase dBase = LispParser.parse(fileName);
            System.out.print("Enter the number of the test to run (Available tests: "+ dBase.size()+" | 0 to quit | -1 to change file): ");
            int[] selections = {0, 0};

            try {
                selections[0] = input.nextInt();
            } catch (Exception e) {
        System.out.println("invalid input");
                input.nextLine();
                continue;
            } 

            switch(selections[0]){

                case 0:
                    input.close();
                    return;
                case -1:
                    input.nextLine();

                    System.out.print("Enter file name: ");
                    String newFile = input.nextLine();
                    
                    File tmpDir = new File(newFile);

                    if(tmpDir.exists())
                        fileName = newFile;
                    else{
                System.out.println("File Not Found");
                        fileName = originalFileName;
                    }
                    continue;
                default: 
                    if(selections[0] < -1 || selections[0] > dBase.size()){
                System.out.println("Invalid Selection");
                        continue;
                    }
            }
            System.out.print("Enter 1 for DFS or 2 for BFS: ");

            try {
                selections[1] = input.nextInt();
            } catch (Exception e) {
                
                input.nextLine();
        System.out.println("invalid input");
                input.nextLine();
                continue;
            } 

            if(selections[1] < 1 || selections[1] > 2){
        System.out.println("Invalid Selection");
                continue;
            }

            for(int i = 0; i < 20; i++)
        System.out.println();

            runUnification(dBase, selections[0] - 1, selections[1]);
        }
    }

    public static void runUnification(DataBase dBase, int index, int selection){

        KnowledgeBase kb = dBase.getKB(index);
        OpenList ol = dBase.getOL(index);
        BindingMap bm = dBase.getBM(index);

        System.out.println("\nKnowledge Base:\n" + kb.toString() 
        + "\nGoal: " + ol.toString());
        
        switch(selection){
            case 1:
                DFSUnification(kb, ol, bm);
                break; 

            case 2:
                BFSUnification(kb, ol, bm);
                break;
        }
        SolutionSet.printSolutionSet(index + 1, selection, bm, kb);
        bm.reset(ol);
    }
    
    /** ================================ DFSUnification ======================================
    Performs unification based on a depth-first search.  Starts with an Open List (stack) 
    containing only the original goal clause.  Pops the first clause from the stack, then
    expands it by performing unification against every clause in the knowledge base.  If a
    Unify is successful, a new clause is created that is the concatenation of the remnants
    of the open list clause and the knowledge base clause.  This clause is then added to the 
    open list.  Algorithm continues until it reaches the empty clause and an empty stack, 
    signaling a successful unification.

     @param kbase the knowledge base to query
     @param openList the open list to use for the queue
     @param bindings the list of bindings between variables
     @return true or false for successful unification

    **/ 

    public static boolean DFSUnification(KnowledgeBase kBase, OpenList openList, BindingMap bindings) {
        
        OpenStack openStack = new OpenStack(openList);
        int iterations = 0;

        while (!openStack.isEmpty()) {

            Clause currentClause = openStack.removeClause();

            if(iterations > 300){
                final int num = iterations;
                openStack.getStack().removeIf(n -> (n.getCurrentDepth() > (num / 4)));
            }
            
            //System.err.println("Knowledge Base:\n" + kBase.toString());
            //System.err.println("Current Clause: " + currentClause.toString() + "\n");

            for (int i = 0; i < kBase.size(); i++) {

                Clause newClause = Unify(kBase.getClause(i), currentClause, bindings);
                
                if(newClause != null){
                    if(!bindings.isEmpty()){
                        
                       ArrayList<Clause> clausesToAdd = TraceBindings(newClause, bindings);

                       if(clausesToAdd.size() > 0)
                                for(Clause c : clausesToAdd){
                                    openStack.addClause(c);
                                   //System.err.println("\nthis is the new clause: " + c.toString() + "\n");
                                }
                        else{
                          //System.err.println("\nthis is the new clause: " + newClause.toString() + "\n");
                           openStack.addClause(newClause);
                        }
                    }
                }
            }
            iterations++;
        }
System.out.println("Bindings: \n" + bindings.printBindings() + "\n");

        return false;
    }

    /** ================================ BFSUnification ======================================
    Performs unification based on a breadth-first search.  Starts with an Open List (queue) 
    containing only the original goal clause.  Pops the clause from the queue, then
    expands it by performing unification against every clause in the knowledge base.  If a
    Unify is successful, a new clause is created that is the concatenation of the remnants
    of the open list clause and the knowledge base clause.  This clause is then added to the 
    open list.  Algorithm continues until it reaches the empty clause, signaling a successful
    unification.

    BFS version maintains the depth of the tree, and assigns it to the clauses at that depth.
    Clauses are then inserted into a priority queue by depth.

     @param kbase the knowledge base to query
     @param openList the open list to use for the queue
     @param bindings the list of bindings between variables
     @return true or false for successful unification

    **/ 

    public static boolean BFSUnification(KnowledgeBase kBase, OpenList openList, BindingMap bindings) {
        
        OpenQueue openQueue = new OpenQueue(openList);

        while (!openQueue.isEmpty()) {

            Clause currentClause = openQueue.removeClause();
            
           //System.err.println("Knowledge Base:\n" + kBase.toString());
           //System.err.println("Current Clause: " + currentClause.toString() + "\n");

            if(currentClause.isEmpty()){

        System.out.println("Bindings: \n" + bindings.printBindings());

                return true;
            }
            if(currentClause.getCurrentDepth() == openQueue.getCurrentDepth()){
                openQueue.incrementDepth();
            }
            for (int i = 0; i < kBase.size(); i++) {

                Clause newClause = Unify(kBase.getClause(i), currentClause, bindings);
                
                if(newClause != null){
                    if(!bindings.isEmpty()){
                        
                       ArrayList<Clause> clausesToAdd = TraceBindings(newClause, bindings);

                       if(clausesToAdd.size() > 0)
                            for(Clause c : clausesToAdd){
                                    
                            openQueue.addClause(c, openQueue.getCurrentDepth());
                           //System.err.println("\nthis is the new clause: " + c.toString() + "\n");

                            }
                        else{
                            
                            openQueue.addClause(newClause, openQueue.getCurrentDepth());
                           //System.err.println("\nthis is the new clause: " + newClause.toString() + "\n");
                        }
                    }
                }
            }
        }
        return false;
    }

    /** ================================ Unify =========================================
    Attempts to perform unification on two given clauses.  If the unification is not
    successful or not possible, returns null.  If the unification is successful, returns
    the new clause that is a concatenation of the two unified clauses

     @param kbClause the clause coming from the knowledge base
     @param currentClause->olClause the clause coming from the open list
     @param bindings the list of bindings between variables
     @return returns a new clause or null

    **/

    public static Clause Unify(Clause clauseToCheck, Clause currentClause, BindingMap bindings) {

        // must perform a deep copy because we modify the clause during substitution.  If the
        // original clause is modified it cannot be reused in subsequent comparisions against the kb
        Clause olClause = currentClause.clone(true); 
        Clause kbClause = clauseToCheck.clone(false);

        Predicate olPred = olClause.getFirstPredicate();
        Predicate kbPred = kbClause.getFirstPredicate();
        
        // if names are not the same, cannot unify
        if(olPred == null || !kbPred.getName().equals(olPred.getName())) 
            return null;

        // ensure variables are unique before unification
        Uniquify(kbClause, bindings);
        
        // if number of variables are not the same, cannot unify
        if(kbPred.getList().size() == olPred.getList().size()){
            
            ArrayList<Binding> bindingList = new ArrayList<>();
            bindingList = UnifyVariables(kbPred, olPred, bindingList, 0);

            if(bindingList != null){

                bindings.addBindings(bindingList);
                Clause newClause = Clause.union(kbClause, olClause);
                return newClause;
            }
        }
        return null;
    }

    /** ================================ UnifyVariables =============================================
    Recursively unify each associated variable in two predicates.  If the unification is allowed,
    add the resulting binding between the pair of variables to a list that will be returned.  
    The function calls itself recursively until the two predicates are equal, meaning the unification
    is complete.

     @param kbPred the predicate coming from the knowledge base
     @param olPred the clause coming from the open list
     @param bindingList the list of bindings we keep adding to
     @param index the current index 
     @return returns null if the unification fails, the list of bindings if successful, or
     itself with index incremented + 1

    **/

    public static ArrayList<Binding> UnifyVariables(Predicate kbPred, Predicate olPred, ArrayList<Binding> bindingList, int index){

        if(index >= kbPred.getList().size())
            return null;

        String olVar = olPred.getVariable(index);
        String kbVar = kbPred.getVariable(index);

        // only try to bind if they are not already equal, and you cannot replace a constant with a variable 
        if(!kbVar.equals(olVar) && Predicate.isNotConstant(olVar)){
                
            Binding newBinding = new Binding(olVar, kbPred.getVariable(index));
            bindingList.add(newBinding);
            //System.err.println(kbPred + " " + olPred + "\n" + newBinding.toString());
            olPred.substitute(index, kbPred.getVariable(index)); // substitute the old variable for its new binding
            //System.err.println(kbPred + " " + olPred +"\n");
        }

        if(kbPred.toString().equals(olPred.toString())) // if equal, unification is done
            return bindingList;
        
        return UnifyVariables(kbPred, olPred, bindingList, index + 1);
    }

    /** ================================ TraceBindings =============================================
    Searches every variable in every predicate inside of a clause to see if it has any pre-existing
    bindings.  If bindings to that variable are found, substitutes that variable with it's binding

     @param clause the clause to check
     @param bindings the list of bindings

    **/
    public static ArrayList<Clause> TraceBindings(Clause clause, BindingMap bindings){
        
        ArrayList<Binding> bindingList = new ArrayList<>();
        ArrayList<Clause> clauseList = new ArrayList<>();

        for(int i = 0; i < clause.size(); i++){

            Predicate predToCheck = clause.getPredicate(i);

            for(int j = 0; j < predToCheck.size(); j++){
                
                String currentVar = predToCheck.getVariable(j);
                Set<String> bindingSet = bindings.getBindingSet(currentVar);
                
                if(bindingSet != null){

                    for(String b: bindingSet){

                        Binding newBinding = new Binding(currentVar, b);
                        if(!bindingList.contains(newBinding)){
                            
                            bindingList.add(newBinding);
                        }
                    }
                }
            }
        }
        for(int i = 0; i < bindingList.size(); i++){

            Binding currentBinding = bindingList.get(i);
            Clause newClause = clause.clone(true);

            for(int k = 0; k < newClause.size(); k++){

                Predicate predToCheck = newClause.getPredicate(k);
    
                for(int j = 0; j < predToCheck.size(); j++){
                    
                    String currentVar = predToCheck.getVariable(j);

                    if(currentVar.equals(currentBinding.getOriginal())){
                        
                      //System.err.println("substituting: " + currentVar + " <- " + currentBinding.getSubstitution());
                        predToCheck.substitute(j, currentBinding.getSubstitution());
                    }
                }
            }
            clauseList.add(newClause);
        }
        return clauseList;
    }

    /** ================================ Uniquify =============================================
    Makes every variable inside of a clause unique if it is not already.  Each variable is set
    to a hash map in the BindingMap data structure, where the variable name is mapped to a value
    that represents the number of duplicates of that particular variable.  This number is used
    to append to the end of a variable to make it unique.

        we use a map inside of this function to keep track of whether a variable has
        been updated during a round of uniquify.  If a clause has the format
                    (aunt ?x ?y) (sister ?x ?z) (mother ?x ?w) 
                            we want to get the format
                    (aunt ?x1 ?y) (sister ?x1 ?z) (mother ?x1 ?w) 
                                        not 
                    (aunt ?x1 ?y) (sister ?x2 ?z) (mother ?x3 ?w)
        
        so uniquify must only operate on a particular variable once

     @param clause the clause to search
     @param bindings the list of bindings

    **/

    public static void Uniquify(Clause clause, BindingMap bindings){

        Map<String, Boolean> updateList = new HashMap<>();

        for(int i = 0; i < clause.size(); i++){

            Predicate predToCheck = clause.getPredicate(i);
            
            for(int j = 0; j < predToCheck.size(); j++){
                
                String currentVar = predToCheck.getVariable(j);

                if(bindings.hasDuplicates(currentVar)){
                    
                    boolean wasUpdated = updateList.containsKey(currentVar);

                    if(!wasUpdated){
                        bindings.incrementIteration(currentVar);
                        updateList.put(currentVar, true);
                    }
                    int increment = bindings.getIteration(currentVar);
                    String newVar = currentVar.concat(Integer.toString(increment));
                    clause.getPredicate(i).substitute(j, newVar);
                }
            }
        }
    }
}