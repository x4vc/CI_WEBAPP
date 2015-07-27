/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Models.Despacho;
import java.io.Serializable;

/**
 *
 * @author jorgefspf
 */
public class DespachoDAO extends GenericDAO<Despacho, Integer> implements Serializable {
    
    private static DespachoDAO instance;
    
    
    
    public static DespachoDAO getInstance() {
        if (instance == null) {
            instance = new DespachoDAO();
        }
        return instance;
    }
    
    @Override
    public Class<Despacho> getEntityClass() {
        return Despacho.class;
    }
    
    
}
