/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Models.DespachoCopia;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jorgefspf
 */
public class DespachoCopiaDAO extends GenericDAO<DespachoCopia, Integer> implements Serializable {

    private static DespachoCopiaDAO instance;

    public static DespachoCopiaDAO getInstance() {
        if (instance == null) {
            instance = new DespachoCopiaDAO();
        }
        return instance;
    }

    @Override
    public Class<DespachoCopia> getEntityClass() {
        return DespachoCopia.class;
    }
    
   
            

    public DespachoCopia selectFromUOCI(Long uo, Long ci) {

        DespachoCopia dc = new DespachoCopia();
        try {

            dc = selectSingle("SELECT max(dc) FROM DespachoCopia dc WHERE dc.unidadeOrganizacional.id = " + uo + " and dc.despacho.ci.id = " + ci );

        } catch (Exception ex) {
            Logger.getLogger(ComunicacaoInternaDAO.class.getName()).log(Level.SEVERE, null, ex);

        }

        return dc;

    }
    
    public void arquivarTodasCopiasDespachoUO(long idDespacho, long idUO){
        
        try {
            ArrayList<DespachoCopia> copias = (ArrayList<DespachoCopia>) this.select("SELECT dc FROM DespachoCopia dc WHERE dc.unidadeOrganizacional.id = " + idUO + " and dc.despacho.id = " + idDespacho );
            
            for (DespachoCopia copia : copias) {
                copia.setArquivado(Boolean.TRUE);
                copia.setLido(Boolean.TRUE);
                DespachoCopiaDAO.getInstance().persist(copia);
            }
            
        } catch (Exception ex) {
            Logger.getLogger(DespachoCopiaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void arquivarTodasCopiasCIUO(long idCI, long idUO){
        
        try {
            ArrayList<DespachoCopia> copias = (ArrayList<DespachoCopia>) this.select("SELECT dc FROM DespachoCopia dc WHERE dc.unidadeOrganizacional.id = " + idUO + " and dc.despacho.ci.id = " + idCI );
            
            for (DespachoCopia copia : copias) {
                copia.setArquivado(Boolean.TRUE);
                copia.setLido(Boolean.TRUE);
                DespachoCopiaDAO.getInstance().persist(copia);
            }
            
        } catch (Exception ex) {
            Logger.getLogger(DespachoCopiaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
