/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sugan;
import java.util.ArrayList;
//import java.util.Iterator;
import static sugan.MainJFrame.NLITERAL;
//import java.util.Arrays;
/**
 *
 * @author SuganShakya
 */
public class Section {
    private int nOne;   // Number of 1 in the binary representation of minterms
    private ArrayList<Implicant> implicantList;
    
    public Section (int nOne){
        this.nOne = nOne;        
        implicantList = new ArrayList<Implicant>();
    }
    public boolean addImplicant(Implicant impl){
        
        if (this.nOne != impl.getBitCount()){
            System.out.println("Bit Count Mismatch: "
                    + "Implicant can't be added to section");
            return false;
        }
        // Implicant already present: no need to add
        for(Implicant x:implicantList) {
            if (x.isEqual(impl)){
                System.out.printf("\n Implicant Already Present\n", nOne);
                return false;
            }
            
        }
        implicantList.add(impl);
        return true;
    }
    
    public int getSize(){
        return implicantList.size();
    }
    
    public void print(){
        System.out.printf("\n Section: - %d\n", nOne);
        for(Implicant impl:implicantList)
            System.out.println(impl.toString());                    
    }
    
    public int getnOne(){
        return this.nOne;
    }
    
    public Section combine (Section section1){
        Section bigSection = new Section(Math.min(this.nOne,section1.getnOne()));
        //int isSingleCharChange;
        for(Implicant implicant1:implicantList){
            for(Implicant implicant2:section1.implicantList){
                if(implicant1.posSingleCharChange(implicant2)!= -1){
                    Implicant newImpl = implicant1.combine(implicant2);
                    implicant1.setCheck(true);
                    implicant2.setCheck(true);
                    bigSection.addImplicant(newImpl);
                }
            }
        }
        return bigSection;
    }
    
    public ArrayList<Implicant> getImplicant(){
        return implicantList;
    }
    
    public ArrayList<Implicant> getCheckedImplicants(){
        ArrayList<Implicant> checkedImplicantList = new ArrayList<>();
        for(Implicant i:this.implicantList){
            if(i.isChecked()==false)
                checkedImplicantList.add(i);
        }
        return checkedImplicantList;
    }
}
