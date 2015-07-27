/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Models.Usuario;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;
import util.JPAUtil;

/**
 *
 * @author aluno
 */
public class UsuarioDAO extends GenericDAO<Usuario, Long> implements Serializable {

    private static UsuarioDAO instance;

    private UsuarioDAO() {
    }

    public static UsuarioDAO getInstance() {
        if (instance == null) {
            instance = new UsuarioDAO();
        }
        return instance;
    }

    @Override
    public Class<Usuario> getEntityClass() {
        return Usuario.class;
    }

    public Usuario ifExist(String login)  {
        Usuario user = null;
        try {
            user = super.selectSingle("FROM Usuario u where u.login='" + login + "'");
        } catch (Exception ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    public Usuario login(Usuario usuario) throws Exception {
        Usuario user = super.selectSingle("FROM Usuario u WHERE u.login='" + usuario.getLogin()
                + "' AND u.senha='" + usuario.getSenha() + "'");
        return user;
    }
    
     public Usuario buscarDadosUsuario(String login) throws Exception {
        Usuario user = super.selectSingle("FROM Usuario u WHERE u.login='" + login
                + "'");
        return user;
    }
    
    public int getRow(Map<String,String> filters) throws Exception{
        String filtro ="";
        if(!filters.isEmpty()){
            filtro=" WHERE ";
            
            for(Iterator<String> it = filters.keySet().iterator(); it.hasNext();){
                String filterProperty = it.next();  
                String filterValue = filters.get(filterProperty); 
                if(filterProperty.equals("grupo.id")){
                    filterProperty = "u.id_grupo";
                }
                filtro+=filterProperty+" LIKE '%"+filterValue+"%' ";         
                if(it.hasNext()){
                    filtro+=" AND "; 
                }
            }
        }
        
        Query dynaQuery = JPAUtil.getEntityManager().createNativeQuery("SELECT COUNT(*) FROM Usuario u "
                +filtro);
        Integer count = (Integer) dynaQuery.getSingleResult();
        return count;
    }
}
