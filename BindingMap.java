package Project2.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** KnowledgeBase
** 
**  @author Jason Allen
**
**  A structure for storing information about bindings
**
**  @param bindingMap a hash map that maps a variable to its binding
**  @param bindingList a list to keep track of all the bindings
**  @param iterationMap a hash map to keep track of the number of duplicates of a var
**
*/
class BindingMap{

    private Map<String, String> bindingMap;
    private Map<String, Set<String>> bindingSets;
    private ArrayList<Binding> bindingList;
    private Map<String, Integer> iterationMap;

    public BindingMap(){

        bindingMap = new HashMap<>();
        bindingSets = new HashMap<>();
        bindingList = new ArrayList<>();
        iterationMap = new HashMap<>();
    }

    /** =============== addBinding =================
    Adds a new binding to the list of bindings, then
    map the original value as the key to it's
    substitution

     @param binding the binding to add

    **/ 
    public void addBinding(Binding binding){

        if(!bindingList.contains(binding)){

            bindingList.add(binding);

            bindingMap.put(binding.getOriginal(), binding.getSubstitution());

            if(!bindingSets.containsKey(binding.getOriginal()))
                bindingSets.put(binding.getOriginal(), new HashSet<>());

            bindingSets.get(binding.getOriginal()).add(binding.getSubstitution());

        }
    }

    /** =============== addBindings =================
    Adds a list of bindings to the master list of 
    bindings

     @param bindings list of bindings to add

    **/ 
    public void addBindings(ArrayList<Binding> bindings){

        for(Binding b : bindings){
            addBinding(b);
        }
    }

    /** =============== getBinding ==========================
    Get the binding mapped to the specified string

     @param original the original binding before substitution
     @return the substituted variable it is mapped to

    **/ 
    public String getBinding(String original){
        return bindingMap.get(original);
    }

    /** =============== getBindingSet ==========================
    Get the set of bindings mapped to the specified string

     @param original the original binding before substitution
     @return the set of bindings the variable is mapped to

    **/ 
    public Set<String> getBindingSet(String original){
        return bindingSets.get(original);
    }

    /** =============== hasBindingSet ==========================
    Check to see if a particular variable has a set of bindings

     @param original the variable to check as a key
     @return true or false if it has a set of bindings

    **/ 
    public boolean hasBindingSet(String original){
        return bindingSets.containsKey(original);
    }

    /** =============== traceBinding ==========================
    Recursively traces the path from a given variable to it's
    final substitution.  Each binding is mapped to it's parent
    binding, so the recursive calls will return null once the
    master substitution is found

    Ex. (x <- y) (y <- w)  traceBinding(x) returns w

     @param original the original variable before substitution
     @return the substituted variable of it and all it's parents

    **/ 
    public String traceBinding(String original){

        String binding = bindingMap.get(original);

        if(binding == null)
            return original;
        
        return traceBinding(binding);
    }

    /** =============== hasDuplicates ==========================
    Check to see if a particular variable is unique

     @param variable the variable to search for
     @return true or false if it already exists

    **/ 
    public boolean hasDuplicates(String variable){
        return iterationMap.containsKey(variable);
    }

    /** =============== hasTraceToConstant ==========================
    Check to see if a particular variable is unique

     @param variable the variable to search for
     @return true or false if it already exists

    **/ 
    public boolean hasTraceToConstant(String variable){

        String trace = traceBinding(variable);

        if(!Predicate.isNotConstant(trace))
            return true;

        return false;
    }

    /** =============== setInitialIterations ====================
    Set the iterationMap with all unique variables in the 
    Open List before execution of unification begins

     @param ol the open list

    **/ 
    public void setInitialIterations(OpenList ol){

        for(int i = 0; i < ol.getUniqueVars().size(); i++){

            incrementIteration(ol.getUniqueVars().get(i));
        }
    }

    /** =============== incrementIteration ====================
    If a variable is not unique, increase the iteration count
    mapped to this variable

     @param variable the variable to search the map for

    **/ 
    public void incrementIteration(String variable){

        int currentIteration;
        if(!hasDuplicates(variable)){
            currentIteration = 0;
            iterationMap.put(variable, currentIteration);
        }
        else{
            currentIteration = iterationMap.get(variable);
            currentIteration++;
            iterationMap.replace(variable, currentIteration);
        }
    }

    /** =============== getIteration ====================
    Get the current count of duplicate variables.  This
    number is used to append to duplicate variables to make 
    each one unique

     @param variable the variable to search the map for
     @return the current count of duplicate variables

    **/
    public int getIteration(String variable){
        return iterationMap.get(variable);
    }

    /** =============== isEmpty ====================
    Check if there are any bindings

     @return true or false if the list is empty

    **/
    public boolean isEmpty(){
        return bindingList.isEmpty();
    }

    public void reset(OpenList openList){
        bindingMap.clear();
        bindingList.clear();
        iterationMap.clear();
        setInitialIterations(openList);
    }
    
    /** =============== printBindings ====================
    print the list of bindings separated by newling

     @return binding list in string format

    **/
    public String printBindings(){

        StringBuilder sb = new StringBuilder();
        for (Binding b: bindingList) {
            sb.append(b.toString() + "\n");
        }
        if(sb.toString().length() == 0){
            return "null\n";
        }
        return sb.toString();
    }

    public String printBindings(String key){

        //Set<String> bindingSet = getBindingSet(key);
        StringBuilder sb = new StringBuilder();
        for (String b: bindingSets.get(key)) {
            sb.append(b + "\n");
        }
        return sb.toString();
    }
}