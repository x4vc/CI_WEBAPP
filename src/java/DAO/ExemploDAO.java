/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;


import Models.Exemplo;
import java.io.Serializable;

/**
 *
 * @author joaoppvn
 */
public class ExemploDAO extends GenericDAO<Exemplo, Integer> implements Serializable {

    private static ExemploDAO instance;

    private ExemploDAO() {
    }

    public static ExemploDAO getInstance() {
        if (instance == null) {
            instance = new ExemploDAO();
        }
        return instance;
    }

    @Override
    public Class<Exemplo> getEntityClass() {
        return Exemplo.class;
    }
}
