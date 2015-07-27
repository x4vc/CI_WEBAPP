/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.UsuarioDAO;
import Models.Usuario;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author jorgefspf
 */
@ManagedBean
@ViewScoped
public class UsuarioMB implements Serializable {

    /**
     * Creates a new instance of usuarioMB
     */
    Usuario usuario;
    List<Usuario> usuarios;
    
    
    
    

    public UsuarioMB() {
        this.usuario = new Usuario();
    }

    public void cadastrar() throws NoSuchAlgorithmException {
        FacesContext face = FacesContext.getCurrentInstance();
        if (UsuarioDAO.getInstance().ifExist(usuario.getLogin()) == null) {
            usuario.setAtivo(Boolean.TRUE);
            usuario.setSenha(this.gerarMD5(usuario.getSenha()));
            UsuarioDAO.getInstance().persist(usuario);
            usuario = new Usuario();
            usuarios = null;
            face.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Login cadastrado ", ""));

        } else {
            System.out.println("Usuário ja cadastrado!");

            face.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login já cadastrado ", "Não foi possível cadastra o usuário"));
        }

    }

    public String gerarMD5(String senha) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
        String crypto = hash.toString(16);
        if (crypto.length() % 2 != 0) {
            crypto = "0" + crypto;
        }
        return crypto;
    }

    public Usuario getUsuario() {

        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getUsuarios() throws Exception {
        if (this.usuarios == null) {
            this.usuarios = UsuarioDAO.getInstance().select();
        }
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    

}
