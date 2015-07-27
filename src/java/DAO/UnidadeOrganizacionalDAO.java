/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Models.UnidadeOrganizacional;

import java.io.Serializable;


/**
 *
 * @author jorgefspf
 */
public class UnidadeOrganizacionalDAO extends GenericDAO<UnidadeOrganizacional, Long> implements Serializable {

    private static UnidadeOrganizacionalDAO instance;
    
    @Override
    public Class<UnidadeOrganizacional> getEntityClass() {
        return UnidadeOrganizacional.class;
    }

    
    
    
     public static UnidadeOrganizacionalDAO getInstance() {
        if (instance == null) {
            instance = new UnidadeOrganizacionalDAO();
        }
        return instance;
    }

    
    
}
