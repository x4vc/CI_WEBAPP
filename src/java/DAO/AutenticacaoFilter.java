/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Models.Usuario;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.naming.AuthenticationException;
import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author jorgefspf
 */
public class AutenticacaoFilter extends UsernamePasswordAuthenticationFilter {
    
    /*
    private String mensagem;
 
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws BadCredentialsException {
        String login = request.getParameter("j_login");
        String senha = request.getParameter("j_senha");
        
 
        try {
            System.out.println("Entrei aqui!");
            Usuario usuario = buscarUsuario(login, senha);
            if (usuario != null) {
 
               
 
                    Collection<GrantedAuthority> regras = new ArrayList<>();
                    
                    regras.add(new SimpleGrantedAuthority(usuario.getRule()));
                    
 
                    request.getSession().setAttribute("usuarioLogado", usuario);
                    mensagem = "Bem vindo: " + usuario.getLogin();
                    return new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha(), regras);
 
                
 
            } else {
                mensagem = "Dados Incorretos";
                throw new BadCredentialsException(mensagem);
            }
 
        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }
 
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException, ServletException {
        System.out.println("Teste sucess!");
        SecurityContextHolder.getContext().setAuthentication(authResult);
        request.getSession().setAttribute("msg", mensagem);
        response.sendRedirect("index.xhtml");
    }
 
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
         System.out.println("Teste unsuccess");
        request.getSession().setAttribute("msg", mensagem);
        response.sendRedirect("login.xhtml");
    }
 
    /**
     * Metodos de acesso ao BD
     *
     */
    
    /*
    public Usuario buscarUsuario(String login, String senha) {
        System.out.println("Busquei o Usuário!");
        Usuario usuario = null;
        try {
            usuario = UsuarioDAO.getInstance().login(new Usuario(login, senha));
            
 
            
 
        } catch (NoResultException e) {
            System.out.println("DAO: Não foi encontrado resultado!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            
        }
 
        return usuario;
    }
    */
    
}
