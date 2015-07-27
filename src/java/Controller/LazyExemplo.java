/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.ExemploDAO;
import Models.Exemplo;
import java.io.Serializable;



/**
 *
 * @author joaoppvn
 */
public class LazyExemplo extends GenericLazyModel<Exemplo> implements Serializable {

    public LazyExemplo() {
        super(ExemploDAO.getInstance());
    }
    
    @Override
    public Class<Exemplo> getEntityClass() {
        return Exemplo.class;
    }

    @Override
    public String getDefaultSortField() {
        return "descricao";
    }
    
    
    
}
