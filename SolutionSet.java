package Project2.src;

import java.io.*;
import java.util.ArrayList;

class SolutionSet {


    public SolutionSet(){

    }

    public static void printSolutionSet(int fileNumber, int selection, BindingMap bindings, KnowledgeBase kb){

        Predicate goalPredicate = kb.getClause(0).getFirstPredicate();

        ArrayList<ArrayList<String>> solutionSet = getSolutionSet(bindings, goalPredicate);

        StringBuilder sb = new StringBuilder();
        sb.append("\nKnowledgeBase: \n\n"+ kb.toString() +"\nQuery: " + goalPredicate.toString() + "\n\n");

        for(int i = 0; i < solutionSet.size(); i++){

            sb.append(goalPredicate.getVariable(i) + " -> {");

            for(int j = 0; j < solutionSet.get(i).size(); j++){
                sb.append(solutionSet.get(i).get(j));

                if(j != solutionSet.get(i).size() - 1){
                    sb.append(", ");
                }
            }
            sb.append("}" + "\n");
        }
        String filePostfix = "";
        switch(selection){
            case 1:
                filePostfix = "DFS";
                break;
            case 2:
                filePostfix = "BFS";
                break;
        }
        printToFile(fileNumber, filePostfix, sb.toString());
        System.out.println(sb.toString());
    }
    public static ArrayList<ArrayList<String>> getSolutionSet(BindingMap bindings, Predicate predToCheck){

        ArrayList<ArrayList<String>> resultStrings = new ArrayList<ArrayList<String>>();
        for(int i = 0; i < predToCheck.size(); i++){
            
            String varToCheck = predToCheck.getVariable(i);
            
            ArrayList<String> strList = new ArrayList<String>();
            
            resultStrings.add(getSetRecursively(strList, varToCheck, bindings, 0));
        }
        return resultStrings;
    }
    public static ArrayList<String> getSetRecursively(ArrayList<String> strList, String varToCheck, BindingMap bindings, int index){

        if(bindings.hasBindingSet(varToCheck)){

            ArrayList<String> mappedVals = new ArrayList<>();
            mappedVals.addAll(bindings.getBindingSet(varToCheck));
            index = 0;
            for(String s: mappedVals){
                if(!Predicate.isNotConstant(s)){

                    if(!strList.contains(s))
                        strList.add(s);
                    
                    index++;
                }
                getSetRecursively(strList, s, bindings, 0);
            }

        }
        return strList;
    }
    public static void printToFile(int fileNumber, String filePostfix, String content){
		
		PrintWriter writer = null;
		String fileName = "Solutions_" + fileNumber + "_" + filePostfix + ".txt";
		try {
			writer = new PrintWriter(fileName);
		} catch (IOException ex) {
			// Report
        }
        writer.println(content);
		writer.close();
	}
}