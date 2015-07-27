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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author jorgefspf
 */
@ManagedBean
@SessionScoped
public class UsuarioBean implements Serializable {

    private Usuario usuario;

    public UsuarioBean() {

    }
    
    private String novaSenha ;
    private String confirmarSenha; 

    public void pegarUsuarioSpringer() {
        usuario = new Usuario();
        SecurityContext context = SecurityContextHolder.getContext();
        if (context instanceof SecurityContext) {
            Authentication authentication = context.getAuthentication();
            if (authentication instanceof Authentication) {

                try {
                    System.out.println("Teste de usuário: " + ((User) authentication.getPrincipal()).getUsername());
                    usuario = UsuarioDAO.getInstance().buscarDadosUsuario(((User) authentication.getPrincipal()).getUsername());

                } catch (Exception ex) {
                    Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public Usuario getUsuario() {
        if(usuario == null){
            this.pegarUsuarioSpringer();
        }
        
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }
    
    public void atualizarPerfil(){
        FacesContext face = FacesContext.getCurrentInstance();
        try {
            String email = usuario.getLogin();
            String assinatura = usuario.getAssinatura();
            usuario = UsuarioDAO.getInstance().buscarDadosUsuario(email);
            usuario.setAssinatura(assinatura);
            usuario.setLogin(email);
            UsuarioDAO.getInstance().persist(usuario);
            
            
            face.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Perfil atualizado com Sucesso ", ""));
        } catch (Exception ex) {
            face.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro ao gravar as informações ", "Desculpe"));
            Logger.getLogger(UsuarioMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alterarSenha(){
        FacesContext face = FacesContext.getCurrentInstance();
        System.out.println(novaSenha +  " confirmarSenha " + confirmarSenha  );
        if((!novaSenha.isEmpty() && !confirmarSenha.isEmpty()) && novaSenha.equals(confirmarSenha)){
            try {
                this.usuario = UsuarioDAO.getInstance().selectFromId(this.usuario.getId());
                this.usuario.setSenha(this.gerarMD5(novaSenha));
                face.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Senha Alterado com Sucesso ", ""));
            } catch (Exception ex) {
                Logger.getLogger(UsuarioMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            face.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Senha não confere", ""));
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

}
