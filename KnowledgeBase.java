package Project2.src;

import java.util.ArrayList;

/** KnowledgeBase
** 
**  @author Jason Allen
**
**  A structure for storing a knowledge base
**  (set of clauses)
**
*/
class KnowledgeBase {

    protected ArrayList<Clause> clauses;
    protected ArrayList<String> uniqueVariables;

    public KnowledgeBase() {
        clauses = new ArrayList<Clause>();
        uniqueVariables = new ArrayList<String>();
    }

    public void addClause(Clause clause) {
        clauses.add(clause);
        addUniqueVariables(clause);
    }

    public Clause getClause(int index) {
        return clauses.get(index);
    }

    public void addUniqueVariables(Clause clause) {

        ArrayList<String> newVars = clause.getUniqueVars();

        for(int i = 0; i < clause.getUniqueVars().size(); i++){

            String newUniqueVar = newVars.get(i);
            if(!uniqueVariables.contains(newUniqueVar))
                uniqueVariables.add(newUniqueVar);
        }
    }

    public ArrayList<String> getUniqueVars() {
        return uniqueVariables;
    }

    public int size(){
        return clauses.size();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Clause s : clauses) {
            sb.append(s.toString() + "\n");
        }
        return sb.toString();
    }
}