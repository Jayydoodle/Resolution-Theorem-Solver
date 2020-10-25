package Project2.src;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

/** DataBase
** 
**  @author Jason Allen
**
**  A structure for storing an open list
**  (set of unified goal clauses)
**
**  The open list essentially functions as a 
**  queue for all the 'successor' nodes generated
**  by expansion.  The remains of unified clauses
**  are added to the open list.
**
*/
class OpenList {

    protected PriorityQueue<Clause> clauseQueue;
    protected ArrayList<String> uniqueVariables;
    protected ArrayList<Clause> clauseList;
    protected int currentDepth;

    public OpenList() {
        clauseList = new ArrayList<Clause>();
        uniqueVariables = new ArrayList<String>();
        currentDepth = 0;
    }

    public void addClause(Clause clause) {

        clause.setCurrentDepth(0);
        clause.setPredicatesNegated();
        clauseList.add(clause);
        addUniqueVariables(clause);
    }

    public void addUniqueVariable(String var) {

        this.uniqueVariables.add(var);
    }

    public void addUniqueVariables(Clause clause) {

        ArrayList<String> newVars = clause.getUniqueVars();

        for (int i = 0; i < newVars.size(); i++) {

            String newUniqueVar = newVars.get(i);
            if (!uniqueVariables.contains(newUniqueVar))
                uniqueVariables.add(newUniqueVar);
        }
    }

    public ArrayList<String> getUniqueVars() {
        return uniqueVariables;
    }

    public int getCurrentDepth() {
        return currentDepth;
    }

    public void incrementDepth() {
        this.currentDepth++;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Clause s : clauseList) {
            sb.append(s.toString() + "\n");
        }
        return sb.toString();
    }
}

class OpenQueue extends OpenList {

    private PriorityQueue<Clause> clauseQueue;

    public OpenQueue(OpenList openList) {

        super();
        this.uniqueVariables = openList.uniqueVariables;
        clauseQueue = new PriorityQueue<Clause>();
        clauseQueue.addAll(openList.clauseList);
    }

    public void addClause(Clause clause, int priority) {

        clause.setCurrentDepth(priority);
        clause.setPredicatesNegated();
        clauseQueue.add(clause);
    }

    public Clause getClause() {
        return clauseQueue.peek();
    }

    public Clause removeClause() {
        return clauseQueue.remove();
    }

    public boolean isEmpty() {
        return clauseQueue.isEmpty();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Clause s : clauseQueue) {
            sb.append(s.toString() + "\n");
        }
        return sb.toString();
    }
}

class OpenStack extends OpenList{

    private Stack<Clause> clauseStack;

    public OpenStack(OpenList openList) {

        super();
        this.uniqueVariables = openList.uniqueVariables;
        clauseStack = new Stack<Clause>();
        clauseStack.addAll(openList.clauseList);
    }

    public Stack<Clause> getStack(){
        return clauseStack;
    }

    public void addClause(Clause clause) {

        currentDepth++;
        clause.setPredicatesNegated();
        clause.setCurrentDepth(currentDepth);
        clauseStack.push(clause);
    }

    public Clause getClause() {
        return clauseStack.peek();
    }

    public Clause removeClause() {
        return clauseStack.pop();
    }

    public boolean isEmpty() {
        return clauseStack.isEmpty();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Clause s : clauseStack) {
            sb.append(s.toString() + "\n");
        }
        return sb.toString();
    }
}