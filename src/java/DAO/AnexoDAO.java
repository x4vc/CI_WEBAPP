/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Models.Anexo;
import java.io.Serializable;

/**
 *
 * @author jorgefspf
 */
public class AnexoDAO extends GenericDAO<Anexo, Integer> implements Serializable {
    
    private static AnexoDAO instance;
    
    
    
    public static AnexoDAO getInstance() {
        if (instance == null) {
            instance = new AnexoDAO();
        }
        return instance;
    }
    
    @Override
    public Class<Anexo> getEntityClass() {
        return Anexo.class;
    }
    
}
