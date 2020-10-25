package Project2.src;

import java.util.ArrayList;

/** Predicate
** 
**  @author Jason Allen
**
**  A structure for storing a predicate
**  (set of variables)
**
**  Ex. - (aunt ?x ?y)
**
** @param variableList a list of the variables (exluding predicate name)
** @param uniqueVariables a list of unique variables in the predicate
** @param isNegated boolean of if the predicate is negated
** @param name name of the predicate
**
*/

class Predicate {

    private ArrayList<String> variableList;
    private ArrayList<String> uniqueVariables;
    private boolean isNegated;
    private String name;

    public Predicate (String[] variableArray){
        
        variableList = new ArrayList<String>();
        uniqueVariables = new ArrayList<String>();

        this.name = variableArray[0];

        for(int i = 1; i < variableArray.length; i++){

            variableList.add(variableArray[i]);
            addUniqueVar(variableArray[i]);
        }

        this.isNegated = true;
    }

    /** =============== getVariable =================
    Returns variable at this index

     @param index the index to search
     @return string containing the variable

    **/ 
    public String getVariable(int index){

        return variableList.get(index);
    }

    /** =============== getList =================
    Returns the list of variables

     @return arraylist of variable strings

    **/ 
    public ArrayList<String> getList(){
        return variableList;
    }
    
    /** =============== getName =================
    Get the name of the predicate

     @return name of the predicate

    **/ 
    public String getName(){
        return name;
    }

    /** =============== containsConstants =================
    Return whether or not this Predicate contains nothing
    but constants
     
     @return true or false if all constants

    **/ 
    public boolean containsConstants(){
        for(int i = 0; i < variableList.size(); i++){
            if(Predicate.isNotConstant(variableList.get(i)))
                return false;
        }
        return true;
    }

    /** =============== isNotConstant =================
    Return whether or not the current variable is a 
    constant.  A variable is not a constant if it starts
    with a '?'
     
     @param variable the variable to check
     @return true or false if it is not a constant

    **/ 
    public static boolean isNotConstant(String variable){
        return variable.charAt(0) == '?';
    }

    /** =============== substitute =====================
    Replaces the variable at the specified index with
    a new variable.  This is used for updating traced
    bindings
     
     @param index the index of the variable to remove
     @param variable the variable to substitute

    **/
    public void substitute(int index, String variable){
        variableList.set(index, variable);
    }

    /** =============== addUniqueVar =====================
    Add to the list of unique variables, but only if the
    variable is not a constant
     
     @param variable the variable to add

    **/
    public void addUniqueVar(String variable){
        if(Predicate.isNotConstant(variable))
            uniqueVariables.add(variable);
    }

    /** =============== getUniqueVars =====================
    Returns the list of unique variables
     
     @return arraylist of unique variables

    **/
    public ArrayList<String> getUniqueVars(){
        return uniqueVariables;
    }

    /** =============== isNegated =====================
    Returns whether this predicate is negated
     
     @return true or false if negated

    **/
    public boolean isNegated() {
        return isNegated;
    }

    /** =============== setNegated =====================
    Sets whether this predicate is negated 
     
     @param isNegated true or false

    **/
    public void setNegated(boolean isNegated) {
        this.isNegated = isNegated;
    }

    /** =============== size =====================
    Get the number of variables in this predicate 
     
     @return int number of variables

    **/
    public int size(){
        return variableList.size();
    }

    public Predicate clone(){

        String[] varArray = toArray();
        Predicate newPredicate = new Predicate(varArray);

        return newPredicate;
    }

    /** =============== toArray =====================
    Converts this predicate back into array form 
     
     @return predicate in array of type string

    **/
    public String[] toArray(){
        
        String[] array = new String[variableList.size() + 1];
        array[0] = name;

        for(int i = 0; i < variableList.size(); i++){
            array[i + 1] = String.valueOf(variableList.get(i));
        }

        return array;
    }

    /** =============== print =====================
    Returns predicate to string in the format:
        (simple_sentence ?x ?y ?z ?u ?v)
                    or
        ~(simple_sentence ?x ?y ?z ?u ?v)

     @return predicate in string format

    **/ 
    public String print(){
        StringBuilder sb = new StringBuilder();
        for (String s: variableList) {
            sb.append(s.toString() + " ");
        }
        if(this.isNegated)
            return "~(" + name + " " + sb.toString().trim() + ")";
        else
            return "(" + name + " " + sb.toString().trim() + ")";
    }
    
    /** =============== toString =====================
    Returns predicate to string in the format:
        aunt ?x ?y 

     @return predicate in string format

    **/ 
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(name + " ");
        for (String s: variableList) {
            sb.append(s.toString() + " ");
        }
        return sb.toString();
    }
}