/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package np.sugan.quine;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
//import static sugan.MainJFrame.NLITERAL;
/**
 *
 * @author SuganShakya
 */
public class PrimeImplicant {
    private ArrayList<Implicant> primeImp;
    private ArrayList<Implicant> finalPrimeImp;
    // Minterm value and the mark
    // Mark = true => it is included in expression
    private HashMap<Integer, Boolean> mintermList;
    int nMintermSelected;
    
  
    public PrimeImplicant(int [] minterms){
        mintermList = new HashMap<Integer, Boolean>();
        for( int i: minterms){
            mintermList.put(i,false);
        }
        primeImp = new ArrayList<>();
        finalPrimeImp = new ArrayList<>();
        nMintermSelected = 0;
    }
    
    public void print(){
        
        /*
        System.out.printf("Minterm List:");
        //for()
        for(Entry<Integer, Boolean> mint: mintermList.entrySet()){
            System.out.printf("\n %d %s", mint.getKey(), mint.getValue());
        }
        System.out.println();
        */
        System.out.println("Extra Prime Implicant: ");
        for(Implicant i:primeImp)
            System.out.println(i.toString());
        
        System.out.println("Final Prime Implicant: ");
        for(Implicant i:finalPrimeImp)
            System.out.println(i.toString());
            
    }
      
    public void addImplicant(Implicant i){
        this.primeImp.add(i);
    }
    /**
    * Add all essential prime Implicant and check these minterms contained by the 
    * essential prime implicant.
    * Essential Prime Implicant: Those prime Implicant in which, 
    * there is a minterm ( at least one) which occurs only in this Implicant
    */
    
    public void buildEssentialPrimeImplicant(){    
        //int count = 0;         
        // For loop ticks all the essential prime Implicants
        for(Entry<Integer, Boolean> mint: mintermList.entrySet()){
            //System.out.printf("\n %d minterm inside buildEssential.", mint.getKey());
            int count = 0; 
            for(Implicant p:primeImp){
                 if(p.contains(mint.getKey())){
                     count++;
                 }
            }
            
            if(count == 1){  // Prime Implicant occurs for only one minterm
                mint.setValue(true);
                for(Implicant x:primeImp){
                    if(x.contains(mint.getKey())){
                        if(!finalPrimeImp.contains(x))
                              finalPrimeImp.add(x);
                        
                        //System.out.printf("\n %s added to final Prime Implicant List.",x.toString());
                    }
                }
                
            }
             
        }
        
        /**
         * Remove Essential Prime Implicant from primeImpl
         */
        for (Implicant i:finalPrimeImp){
            primeImp.remove(i);
            for(Entry<Integer, Boolean> mint: mintermList.entrySet()){
                if(i.contains(mint.getKey()))
                    mint.setValue(true);            
            }
            primeImp.remove(i);
        }                
    }
    
    public void buildFinalPrimeImplicant(){
        buildEssentialPrimeImplicant();
        //System.out.println("After buildEssentialPrimeImplicant.");
        print();
        
        while(mintermList.values().contains(false)){
            //System.out.println("Inside loop to clear all minterms.");
            addBestPrimeImplicant();
        }
        
    }
    
    public void addBestPrimeImplicant(){
        //System.out.println("Inside addBestPrimeImplicant.");
        Implicant best = primeImp.get(0);
        int max=0;
        for(Implicant impl:primeImp){
            if(impl == primeImp.get(0))
                continue;
            int count = 0;
            for(Entry<Integer, Boolean> mint: mintermList.entrySet()){
                if(mint.getValue()==false)
                    count++;
            }
            if(max < count){
                max = count;
                best = impl;
            }
        }        
        /**
         * Now best is the prime-implicant with maximum number of unchecked minterms
         */
        System.out.printf("Best Implicant: %s", best.toString());
        for(Entry<Integer, Boolean> mint: mintermList.entrySet()){
            if(mint.getValue()==false){
                if(best.contains(mint.getKey()))
                    mint.setValue(true);
            }
        }
        finalPrimeImp.add(best);
        primeImp.remove(best);
               
    }
    
    public String getFinalExpression(){
        String result = "";
        
        for(Implicant i:finalPrimeImp){
            if(i == finalPrimeImp.get(0))
                result = i.getExpressionString();
            else 
                result = result + " + "+ i.getExpressionString();
        }
        return result;
    }
   
}
