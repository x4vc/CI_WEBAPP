/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import DAO.ComunicacaoInternaDAO;
import Models.ComunicacaoInterna;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author jorgefspf
 */
@FacesConverter(value = "ciConverter", forClass = ComunicacaoInterna.class)
public class CIConverter implements Converter {

     public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return ComunicacaoInternaDAO.getInstance().selectFromId((Long.valueOf(value)));
        } catch (Exception ex) {
            Logger.getLogger(SimpleEntityConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        ComunicacaoInterna ci = (ComunicacaoInterna)o;
        System.out.println("O id da ci no converter:" + ci.getId());
        if(ci == null || ci.getId() <= 0){
            return null;
        }
        return String.valueOf(ci.getId());
    }
    
}
