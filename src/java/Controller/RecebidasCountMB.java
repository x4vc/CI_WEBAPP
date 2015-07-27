/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.ComunicacaoInternaDAO;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author jorgefspf
 */
@ManagedBean
@ViewScoped
public class RecebidasCountMB implements Serializable {

    /**
     * Creates a new instance of RecebidasCountMB
     */
    
    private int countRecebidas = 0;
    
    public RecebidasCountMB() {
        
        
        
    }
    
    public int verificarNaoLidas(long uo){
        System.out.println("**********************");
        countRecebidas = ComunicacaoInternaDAO.getInstance().countCIsRecebidas(uo);
        return countRecebidas;
    }

    
    
    
    
}
