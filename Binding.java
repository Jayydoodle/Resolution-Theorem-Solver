package Project2.src;

/** Binding
** 
**  @author Jason Allen
**
**  A structure for storing a binding pair
**
**  @param original the original variable
**  @param subsitution the variable that was substituted in
**
*/
class Binding{

    private String original;
    private String substitution;

    public Binding(String original, String substitution){

        this.original = original;
        this.substitution = substitution;
    }

    public String getOriginal() {
        return this.original;
    }

    public String getSubstitution() {
        return this.substitution;
    }

    @Override
    public boolean equals(Object o) { 
  
        // If the object is compared with itself then return true   
        if (o == this) { 
            return true; 
        } 
  
        /* Check if o is an instance of Complex or not 
          "null instanceof [type]" also returns false */
        if (!(o instanceof Binding)) { 
            return false; 
        } 
          
        // typecast o to Complex so that we can compare data members  
        Binding b = (Binding) o; 
          
        if(b.getOriginal().equals(this.getOriginal()) && b.getSubstitution().equals(this.getSubstitution()))
            return true;
        else
            return false;
    } 
    
    public String toString(){
        
        return original + " <- " + substitution;
    }
}