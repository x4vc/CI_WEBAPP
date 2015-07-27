/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.ComunicacaoInternaDAO;
import DAO.UsuarioDAO;
import Models.ComunicacaoInterna;
import Models.Usuario;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author jorgefspf
 */
public class LazyCIDataModel extends LazyDataModel<ComunicacaoInterna> implements Serializable {

    private List<ComunicacaoInterna> datasource;

    public LazyCIDataModel(List<ComunicacaoInterna> datasource) {
        this.datasource = datasource;
    }

    @Override
    public ComunicacaoInterna getRowData(String rowKey) {
        for (ComunicacaoInterna ci : datasource) {
            if (ci.getId().toString().equals(rowKey)) {
                return ci;
            }
        }

        return null;
    }

    @Override
    public Object getRowKey(ComunicacaoInterna car) {
        return car.getId();
    }

    @Override
    public List<ComunicacaoInterna> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        List<ComunicacaoInterna> data = new ArrayList<ComunicacaoInterna>();

        //filter
        for (ComunicacaoInterna ci : datasource) {
            boolean match = true;

            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);

                        System.out.println("Propriedade: " + filterProperty);
                        String fieldValue = "";
                        //String fieldValue = String.valueOf(car.getClass().getField(filterProperty).get(car));
                        if (filterProperty.equals("ultimoEnvioUO(usuarioBean.usuario.uo.id)")) {
                            SecurityContext context = SecurityContextHolder.getContext();
                            if (context instanceof SecurityContext) {
                                Authentication authentication = context.getAuthentication();
                                if (authentication instanceof Authentication) {

                                    try {
                                        Usuario usuario = UsuarioDAO.getInstance().buscarDadosUsuario(((User) authentication.getPrincipal()).getUsername());
                                        fieldValue = ci.ultimoEnvioUO(usuario.getUo().getId());
                                    } catch (Exception ex) {
                                        Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        }else if (filterProperty.equals("data")) {

                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            fieldValue = formatter.format(ci.getData());
                            
                            System.out.println(filterProperty +" contains " + fieldValue);

                        }else if (filterProperty.equals("ultimoDespacho.data")) {

                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            fieldValue = formatter.format(ci.getUltimoDespacho().getData());
                            
                            System.out.println(filterProperty +" contains " + fieldValue);

                        }else if (filterProperty.equals("uo.nome")) {

                            
                            fieldValue = ci.getUo().getNome();
                            
                            System.out.println(filterProperty +" contains " + fieldValue);

                        }

                        if (filterValue == null || fieldValue.toUpperCase().startsWith(filterValue.toString().toUpperCase())) {
                            match = true;
                        } else {
                            match = false;
                            break;
                        }
                    } catch (Exception e) {
                        match = false;
                    }
                }
            }

            if (match) {
                data.add(ci);
            }
        }

        //sort
        if (sortField != null) {
            //Collections.sort(data, new LazySorteCI(sortField, sortOrder));
        }

        //rowCount
        int dataSize = data.size();
        this.setRowCount(dataSize);

        //paginate
        if (dataSize > pageSize) {
            try {
                return data.subList(first, first + pageSize);
            } catch (IndexOutOfBoundsException e) {
                return data.subList(first, first + (dataSize % pageSize));
            }
        } else {
            return data;
        }
    }

}
