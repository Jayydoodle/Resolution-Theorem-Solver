package Project2.src;

import java.util.ArrayList;

/** DataBase
** 
**  @author Jason Allen
**
**  A structure for storing a database
**  (set of Knowledge Bases, Open Lists, 
**  and Binding Maps)
**
*/
class DataBase{

    ArrayList<KnowledgeBase> kbs;
    ArrayList<OpenList> ols;
    ArrayList<BindingMap> bms;
    
    public DataBase(){
        kbs = new ArrayList<KnowledgeBase>();
        ols = new ArrayList<OpenList>();
        bms = new ArrayList<BindingMap>();
    }

    public KnowledgeBase getKB(int index){
        return kbs.get(index);
    }

    public OpenList getOL(int index){
        return ols.get(index);
    }

    public BindingMap getBM(int index){
        return bms.get(index);
    }

    public void add(KnowledgeBase kb, OpenList ol, BindingMap bm){
        kbs.add(kb);
        ols.add(ol);
        bm.setInitialIterations(ol);
        bms.add(bm);
    }

    public int size(){
        return kbs.size();
    }
}