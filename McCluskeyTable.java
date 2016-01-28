/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sugan;
import java.util.ArrayList;
//import static sugan.MainJFrame.NLITERAL;
/**
 *
 * @author SuganShakya
 */
public class McCluskeyTable {
    int nSection;
    private ArrayList<Section> sectionList;
    
    public McCluskeyTable(int nSection){
        this.nSection = nSection;
        ArrayList<Section> sectionList = new ArrayList<Section>();
    }
    public void addSection(Section s){
        //int nOne = s.getnOne();
        //Section newSection = new Section(nOne);
        //newSection = s;
        if(s.getSize() > 0)
            sectionList.add(s);
        //System.out.println("s added.");
    }
     public void print(){
         for(Section s:sectionList)
             s.print();
     }
     public Section getSection(int index){
         return sectionList.get(index);
     }

    
}
