/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Models.ComunicacaoInterna;
import java.util.Comparator;
import org.primefaces.model.SortOrder;

/**
 *
 * @author jorgefspf
 */
public class LazySorteCI implements Comparator<ComunicacaoInterna> {
    private String sortField;
     
    private SortOrder sortOrder;
     
    public LazySorteCI(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }
 
    public int compare(ComunicacaoInterna car1,ComunicacaoInterna  car2) {
        try {
            Object value1 = ComunicacaoInterna.class.getField(this.sortField).get(car1);
            Object value2 = ComunicacaoInterna.class.getField(this.sortField).get(car2);
 
            int value = ((Comparable)value1).compareTo(value2);
             
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}
