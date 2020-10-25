package Project2.src;

import java.util.ArrayList;

/** Clause
** 
**  @author Jason Allen
**
**  A structure for storing a clause
**  (set of predicates)
**
**  Ex. - (aunt ?x ?y) (sister ?x ?z) (mother ?z ?y)
**
**  @param predicates a list containing the predicates
**  @param uniqueVariables a list containing unique variables
**  @param currentDepth the depth of a tree, or priority of this clause
**
*/
class Clause implements Comparable<Clause>, Cloneable {

    private ArrayList<Predicate> predicates;
    private ArrayList<String> uniqueVariables;
    private int currentDepth;

    public Clause() {
        predicates = new ArrayList<Predicate>();
        uniqueVariables = new ArrayList<String>();
    }
    
    /** =============== addPredicate =================
    Adds a predicate to the list of predicates.  If
    The list is empty, sets the predicate to not 
    negated to follow the format of horn clauses (first
    element positive, rest negative)

     @param predicate the predicate to add

    **/ 
    public void addPredicate(Predicate predicate){

        if(predicates.isEmpty())
            predicate.setNegated(false);

        predicates.add(predicate);
        addUniqueVariables(predicate);
    }
    /** =============== getPredicate =================
    Returns predicate at this index

     @param index the index to search
     @return the predicate object at index

    **/ 
    public Predicate getPredicate(int index){
        return predicates.get(index);
    }
    
    /** =============== getFirstPredicate =================
    Returns first predicate in the clause

     @return the predicate at first index

    **/ 
    public Predicate getFirstPredicate(){

        if(predicates.isEmpty())
            return null;
        return predicates.get(0);
    }
    
    /** =============== removeFirstPredicate =================
    Remove first predicate in the clause

    **/ 
    public void removeFirstPredicate(){
    
        if(!predicates.isEmpty())
            predicates.remove(0);
    }

    /** =============== setPredicatesNegated =================
    Negate all of the predicates in the clause

    **/ 
    public void setPredicatesNegated(){

        for (Predicate p: predicates)
            p.setNegated(true);
    }

    /** =============== addUniqueVariables =================
    Adds all of the unique variables in a predicate object
    to the master list maintained by the clause

     @param predicate the predicate to add

    **/ 
    public void addUniqueVariables(Predicate predicate){

        uniqueVariables.addAll(predicate.getUniqueVars());
    }
    
    /** =============== getUniqueVars =================
    Returns a list of unique variables in the clause

     @return list of all of the unique variables in the clause

    **/ 
    public ArrayList<String> getUniqueVars(){
        return uniqueVariables;
    }

    /** =============== setcurrentDepth =================
    Set current depth, or priority of this clause
    Used for breadth-first searches

     @param depth the depth to assign

    **/ 
    public void setCurrentDepth(int depth){
        this.currentDepth = depth;
    }

    /** =============== getcurrentDepth =================
    Returns the current depth, or priority of this clause
    Used for breadth-first searches

     @return int containing the current depth

    **/ 
    public int getCurrentDepth(){
        return currentDepth;
    }

    /** =============== isEmpty ==================
    Returns whether or not the clause is empty
    Note, empty clause means successful resolution

     @return boolean true or false if empty 

    **/ 
    public boolean isEmpty(){
        return predicates.isEmpty();
    }

    /** =============== size ==================
    Returns number of predicates in the clause

     @return int of number of predicates

    **/ 
    public int size(){
        return predicates.size();
    }

    /** =============== toString =====================
    Returns clause to string in the format:
        (aunt ?x ?y) (sister ?x ?z)

     @return clause in string format

    **/ 
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (Predicate p: predicates) {
            sb.append(p.print() + " ");
        }
        if(sb.toString().length() == 0)
            return "null";
        return sb.toString();
    }

    /** =============== compareTo =================
    Overridded compareTo method from the Comparable
    interface.  Used to create a natural ordering
    for clauses based on their depth.  Used for
    implementing PriorityQueue for BFS

     @param c2 the clause to compare against
     @return evaluation of the ordering 

    **/ 
	@Override
	public int compareTo(Clause c2) { 
        if (getCurrentDepth() > c2.getCurrentDepth()) 
            return 1; 
        else if (getCurrentDepth() < c2.getCurrentDepth()) 
            return -1; 
        else
            return 0; 
    }
    
    /** =============== clone =================
    Performs a deep copy of this clause object

     @return a clause identical to the caller

    **/ 
    public Clause clone(boolean isGoalClause){

        Clause newClause = new Clause();

        for(int i = 0; i < this.size(); i++){

            Predicate newPredicate = new Predicate(this.getPredicate(i).toArray());
            newClause.addPredicate(newPredicate);
        }

        if(isGoalClause)
            newClause.setPredicatesNegated();

        return newClause;
    }

    /** =============== union ==========================
    Takes the remnants of the 2 clauses post unification
    and concatenates them into a new clause.  

     @param c1 the clause from the knowledge base
     @param c2 the clause that was evaluated
     @return a new clause with the predicates from c1
     concatenated onto the end of c2

    **/ 
    public static Clause union(Clause c1, Clause c2){

        Clause newClause = new Clause();

        // start at 1 because index 0 was unified with
        // the other clause
        for(int i = 1; i < c2.size(); i++){
            newClause.addPredicate(c2.getPredicate(i));
        }
        for(int i = 1; i < c1.size(); i++){
            newClause.addPredicate(c1.getPredicate(i));
        }

        return newClause;
    }
}