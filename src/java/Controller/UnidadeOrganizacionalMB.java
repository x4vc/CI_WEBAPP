/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.UnidadeOrganizacionalDAO;
import Models.UnidadeOrganizacional;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jorgefspf
 */
@ManagedBean
@ApplicationScoped
public class UnidadeOrganizacionalMB implements Serializable {

    /**
     * Creates a new instance of UnidadeOrganizacionalMB
     */
    public UnidadeOrganizacionalMB() {
        unidadesOrganizacionais = new ArrayList<>();
        
        try { 
            unidadesOrganizacionais = new UnidadeOrganizacionalDAO().select(1, 1000, "nome", "ASC", null);
        } catch (Exception ex) {
            
            Logger.getLogger(UnidadeOrganizacionalMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
    }
    
    private List<UnidadeOrganizacional> unidadesOrganizacionais;

    public List<UnidadeOrganizacional> getUnidadesOrganizacionais() throws Exception {
        if(unidadesOrganizacionais == null){
            unidadesOrganizacionais = new UnidadeOrganizacionalDAO().select(1, 1000, "nome", "uoGerenciadora", null);
        }
        return unidadesOrganizacionais;
    }

    public void setUnidadesOrganizacionais(List<UnidadeOrganizacional> unidadesOrganizacionais) {
        this.unidadesOrganizacionais = unidadesOrganizacionais;
    }
    
    
    
}
