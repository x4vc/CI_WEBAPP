/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.ExemploDAO;
import Models.Exemplo;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author tulioasc
 */
@ManagedBean(name="exemploMB")
@RequestScoped
public class ExemploMB implements Serializable{
    
    private Exemplo bean;
    
    public ExemploMB(){
        //Recuperando o parametro id para a tela de Update
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        String id= ctx.getRequestParameterMap().get("id");
        if (id != null && !id.equals("")) {
            try {
                this.bean = ExemploDAO.getInstance().selectFromId(Integer.parseInt(id));
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }else{
            this.bean= new Exemplo(); 
        }
        
    }

    public Exemplo getBean() {
        return bean;
    }

    public void setBean(Exemplo bean) {
        this.bean = bean;
    }
    
    public String persist(){
        FacesContext face = FacesContext.getCurrentInstance();
        try{
            ExemploDAO.getInstance().persist(this.bean);
            face.addMessage(null, new FacesMessage("Operação realizada com sucesso!"));
            return "listar";
        }catch(Exception e){
            face.addMessage(null, new FacesMessage( "Ocorreu um erro em sua requisição!"));
            return "listar";
        }
        
    }
}
