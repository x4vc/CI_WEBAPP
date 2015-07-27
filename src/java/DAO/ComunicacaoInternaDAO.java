/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Models.ComunicacaoInterna;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;
import util.JPAUtil;

/**
 *
 * @author jorgefspf
 */
public class ComunicacaoInternaDAO extends GenericDAO<ComunicacaoInterna, Long> implements Serializable {

    private static ComunicacaoInternaDAO instance;

    @Override
    public Class<ComunicacaoInterna> getEntityClass() {
        return ComunicacaoInterna.class;
    }

    public static ComunicacaoInternaDAO getInstance() {
        if (instance == null) {
            instance = new ComunicacaoInternaDAO();
        }
        return instance;
    }

    public int ultimaCI(long uoID) {
        int numero = 0;

        try {
            ComunicacaoInterna ci = new ComunicacaoInterna();
            ci = selectSingle("SELECT max(c) FROM ComunicacaoInterna c WHERE c.uo.id = " + uoID + " and year(c.data) = year(getdate())");
            if (ci != null) {
                numero = ci.getNumero();
            }
        } catch (Exception ex) {
            Logger.getLogger(ComunicacaoInternaDAO.class.getName()).log(Level.SEVERE, null, ex);
            numero = 0;
            return numero;

        }

        return numero;

    }

    public List<ComunicacaoInterna> listarApensamentos(long uoID) {

        List<ComunicacaoInterna> cis = new ArrayList<>();
        try {
            System.out.println("Unidade Organizacional: " + uoID);
            cis = select("SELECT distinct d.ci FROM Despacho d, DespachoCopia dc WHERE (d.para.id = " + uoID + ") or (dc.unidadeOrganizacional.id = " + uoID + " and dc.despacho.id = d.id )  ");

        } catch (Exception ex) {
            Logger.getLogger(ComunicacaoInternaDAO.class.getName()).log(Level.SEVERE, null, ex);
            cis = new ArrayList<>();
            return cis;

        }

        return cis;

    }

    public List<ComunicacaoInterna> listarCIsPendetesAutorizacao(long uoID) {

        List<ComunicacaoInterna> cis = new ArrayList<>();
        try {
            System.out.println("Unidade Organizacional: " + uoID);
            cis = select("SELECT c FROM ComunicacaoInterna c, Despacho d WHERE (c.uo.id = " + uoID + " or c.uo.uoGerenciadora = " + uoID + ")  and  (d.autorizado = 0 or d.autorizadoExterno = 0) and d.ci.id = c.id ");

        } catch (Exception ex) {
            Logger.getLogger(ComunicacaoInternaDAO.class.getName()).log(Level.SEVERE, null, ex);
            cis = new ArrayList<>();
            return cis;

        }

        return cis;

    }

    public List<ComunicacaoInterna> listarCIsEnviadas(long uoID) {

        List<ComunicacaoInterna> cis = new ArrayList<>();
        try {

            System.out.println("Unidade Organizacional: " + uoID);  //or (dc.unidadeOrganizacional = " + uoID + " and dc.despacho.ci.id = c.id ) 
            cis = select("SELECT distinct c FROM ComunicacaoInterna c, Despacho d WHERE (c.uo.id = " + uoID + ") or (d.para.id = " + uoID + " and d.status = 2 and c.id = d.ci.id) ");

        } catch (Exception ex) {
            Logger.getLogger(ComunicacaoInternaDAO.class.getName()).log(Level.SEVERE, null, ex);
            cis = new ArrayList<>();
            return cis;

        }

        return cis;

    }

    public List<ComunicacaoInterna> listarCIsArquivadas(long uoID) {

        List<ComunicacaoInterna> cis = new ArrayList<>();
        try {

            System.out.println("Unidade Organizacional: " + uoID);  //or (dc.unidadeOrganizacional = " + uoID + " and dc.despacho.ci.id = c.id ) 
            cis = select("SELECT distinct d.ci FROM Despacho d, DespachoCopia dc WHERE ( d.para.id = " + uoID + " and d.status = 4 ) or (dc.arquivado = 1 and dc.unidadeOrganizacional = " + uoID + " and dc.despacho.id = d.id) ");

        } catch (Exception ex) {
            Logger.getLogger(ComunicacaoInternaDAO.class.getName()).log(Level.SEVERE, null, ex);
            cis = new ArrayList<>();
            return cis;

        }

        return cis;

    }

    public List<ComunicacaoInterna> listarCIsRecebidas(long uoID) {

        List<ComunicacaoInterna> cis = new ArrayList<>();
        try {
            System.out.println("Unidade Organizacional: " + uoID);  //, DespachoCopia dc or (dc.arquivado = 0 and dc.unidadeOrganizacional.id = " + uoID + " and dc.despacho.ci.id = d.ci.id) 
            cis = select("SELECT distinct d.ci from Despacho d, DespachoCopia dc where d.id in (SELECT d.id from Despacho d WHERE d.para.id = " + uoID + " and d.status = 1 and d.autorizado = 1) or d.id in (SELECT dc.despacho.id from DespachoCopia dc WHERE dc.unidadeOrganizacional.id = " + uoID + " and dc.arquivado = 0)  ");

        } catch (Exception ex) {
            Logger.getLogger(ComunicacaoInternaDAO.class.getName()).log(Level.SEVERE, null, ex);
            cis = new ArrayList<>();
            return cis;

        }

        return cis;

    }
    
     public int countCIsRecebidas(long uoID) {

        int qtdCis = 0;
        try {
            System.out.println("Unidade Organizacional: " + uoID);  //, DespachoCopia dc or (dc.arquivado = 0 and dc.unidadeOrganizacional.id = " + uoID + " and dc.despacho.ci.id = d.ci.id) 
                         
            qtdCis = countRow("SELECT count(distinct d.ci)  from Despacho d, DespachoCopia dc where d.id in (SELECT d.id from Despacho d WHERE d.para.id = " + uoID + " and d.status = 1 and d.autorizado = 1 and lido = 0) or d.id in (SELECT dc.despacho.id from DespachoCopia dc WHERE dc.unidadeOrganizacional.id = " + uoID + " and dc.arquivado = 0 and lido = 0)  ");

        } catch (Exception ex) {
            Logger.getLogger(ComunicacaoInternaDAO.class.getName()).log(Level.SEVERE, null, ex);
            qtdCis = 0;
            return qtdCis;

        }

        return qtdCis;

    }
    
    

}
