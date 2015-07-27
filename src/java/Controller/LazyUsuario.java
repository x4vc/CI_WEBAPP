/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.UsuarioDAO;
import Models.Usuario;
import java.io.Serializable;



/**
 *
 * @author joaoppvn
 */
public class LazyUsuario extends GenericLazyModel<Usuario> implements Serializable {

    public LazyUsuario() {
        super(UsuarioDAO.getInstance());
    }
    
    @Override
    public Class<Usuario> getEntityClass() {
        return Usuario.class;
    }

    @Override
    public String getDefaultSortField() {
        return "nome";
    }
    
    
    
}
