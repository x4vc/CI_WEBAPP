/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import DAO.DespachoDAO;
import DAO.UnidadeOrganizacionalDAO;
import java.io.Serializable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jorgefspf
 */
@Entity
@Table(name = "comunicacao_interna")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComunicacaoInterna.findAll", query = "SELECT c FROM ComunicacaoInterna c"),
    @NamedQuery(name = "ComunicacaoInterna.findById", query = "SELECT c FROM ComunicacaoInterna c WHERE c.id = :id"),
    @NamedQuery(name = "ComunicacaoInterna.findByData", query = "SELECT c FROM ComunicacaoInterna c WHERE c.data = :data"),
    @NamedQuery(name = "ComunicacaoInterna.findByNumero", query = "SELECT c FROM ComunicacaoInterna c WHERE c.numero = :numero"),
    @NamedQuery(name = "ComunicacaoInterna.findByTipo", query = "SELECT c FROM ComunicacaoInterna c WHERE c.tipo = :tipo")})
public class ComunicacaoInterna implements Serializable, BaseEntity {

    @Column(name = "numero")
    private Integer numero;

    @JoinColumn(name = "uo", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private UnidadeOrganizacional uo = new UnidadeOrganizacional();
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private long id;
    @Lob
    @Column(name = "assunto")
    private String assunto;
    @Column(name = "data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    @Lob
    @Column(name = "assinatura")
    private String assinatura;
    @Column(name = "tipo")
    private Integer tipo;
    @Lob
    @Column(name = "status")
    private String status;
    @OneToMany(mappedBy = "apensamento", fetch = FetchType.LAZY)
    private List<ComunicacaoInterna> comunicacaoInternaList;
    @JoinColumn(name = "apensamento", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ComunicacaoInterna apensamento;
    @JoinColumn(name = "autor", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario autor;
    @OneToMany(mappedBy = "ci", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Despacho> despachoList;

    public ComunicacaoInterna() {

        this.despachoList = new ArrayList<>();
    }

    public ComunicacaoInterna(long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(String assinatura) {
        this.assinatura = assinatura;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlTransient
    public List<ComunicacaoInterna> getComunicacaoInternaList() {
        return comunicacaoInternaList;
    }

    public void setComunicacaoInternaList(List<ComunicacaoInterna> comunicacaoInternaList) {
        this.comunicacaoInternaList = comunicacaoInternaList;
    }

    public ComunicacaoInterna getApensamento() {
        return apensamento;
    }

    public void setApensamento(ComunicacaoInterna apensamento) {
        this.apensamento = apensamento;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    @XmlTransient
    public List<Despacho> getDespachoList() {
        return despachoList;
    }

    public void setDespachoList(List<Despacho> despachoList) {
        this.despachoList = despachoList;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ComunicacaoInterna other = (ComunicacaoInterna) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Models.ComunicacaoInterna[ id=" + id + " ]";
    }

    public UnidadeOrganizacional getUo() {
        return uo;
    }

    public void setUo(UnidadeOrganizacional uo) {
        this.uo = uo;
    }

    public String getNumeroCI() {
        SimpleDateFormat ano = new SimpleDateFormat("yyyy");
        String numeracao = "";
        numeracao = numeracao.concat(String.format("%03d", this.uo.getId()) + " " + String.format("%05d", this.numero) + "/" + ano.format(data));

        return numeracao;

    }

    public String getTipoCI() {
        if (this.tipo == null || this.tipo == 1) {
            return "Comunicação Interna";
        } else if (this.tipo == 2) {
            return "Comunicação Circular";
        }

        return "Comunicação Interna";
    }

    public Despacho getUltimoDespacho() {
        return this.despachoList.get(this.despachoList.size() - 1);
    }

    public String gerarLogoTipoCI(long idUO) {
        String logoCI = "/ci/images/ci.png";
        String logoCICircular = "/ci/images/ci_circular.png";
        String logoCICopia = "/ci/images/ci_cc.png";
        if (tipo == CI_CIRCULAR || this.getUltimoDespacho().getPara() == null) {
            return logoCICircular;
        } else if (this.getUltimoDespacho()
                .getPara().getId() == idUO) {
            return logoCI;
        } else {
            return logoCICopia;
        }
    }

    public String labelTipoCI(long idUO) {

        if (tipo == CI_CIRCULAR || this.getUltimoDespacho().getPara() == null) {
            return "Circular";
        } else if (this.getUltimoDespacho()
                .getPara().getId() == idUO) {
            return "Encaminhada para a Unidade Organizacional";
        } else {
            return "Com Cópia para a Unidade Organizacional";
        }
    }

    @Transient
    static final int CI_CIRCULAR = 2;
    @Transient
    static final int CI_DIRECIONADA = 1;
    @Transient
    static final int CI_COPIA = 3;
    
    public Despacho ultimoDespachoOrCopiaUO(long idUO) {
        Despacho ultimoDespachoUO = new Despacho();
        int idUltimoDespacho = 0;
        
        if (despachoList.size() == 1) {
            
                idUltimoDespacho = despachoList.get(0).getId();
           

        } else {

            for (Despacho d : despachoList) {
                if (d.getPara().getId() == idUO && d.getStatus() == 1) {
                    idUltimoDespacho = d.getId();
                    System.out.println("Id Ultimo despacho: " + idUltimoDespacho);
                }else{
                    for (DespachoCopia dc : d.getDespachoCopiaList()) {
                        if(dc.getUnidadeOrganizacional().getId() == idUO){
                            idUltimoDespacho = dc.getDespacho().getId();
                        }
                    }
                }
            }

        }
        try {
            ultimoDespachoUO = DespachoDAO.getInstance().selectFromId(idUltimoDespacho);
        } catch (Exception ex) {
            Logger.getLogger(ComunicacaoInterna.class.getName()).log(Level.SEVERE, null, ex);
            return ultimoDespachoUO;
        }
        return ultimoDespachoUO;
    }
    

    public Despacho ultimoDespachoUO(long idUO) {
        Despacho ultimoDespachoUO = new Despacho();
        int idUltimoDespacho = 0;

        if (despachoList.size() == 1) {
            if(this.tipo == 2){
                idUltimoDespacho = despachoList.get(0).getId();
            }else if (despachoList.get(0).getPara().getId() == idUO) {
                idUltimoDespacho = despachoList.get(0).getId();

            } else {
                return null;
            }

        } else {

            for (Despacho d : despachoList) {
                if (d.getPara().getId() == idUO && d.getStatus() == 2) {
                    idUltimoDespacho = d.getId();
                    System.out.println("Id Ultimo despacho: " + idUltimoDespacho);
                }
            }

        }
        try {
            ultimoDespachoUO = DespachoDAO.getInstance().selectFromId(idUltimoDespacho);
            if(ultimoDespachoUO != null){
                 System.out.println("Para do último despacho: "+ultimoDespachoUO.getPara().getNome());
            }
           
        } catch (Exception ex) {
            Logger.getLogger(ComunicacaoInterna.class.getName()).log(Level.SEVERE, null, ex);
            return ultimoDespachoUO;
        }
        return ultimoDespachoUO;
    }
    
    public String ultimoEnvioUO(long idUO) {
        String ultimoDespachoUO = "--";
        if(uo.getId() == idUO){
            try {
                if(despachoList.get(0).getPara() != null){
                    ultimoDespachoUO = UnidadeOrganizacionalDAO.getInstance().selectFromId(despachoList.get(0).getPara().getId()).getNome();
                }
                
            } catch (Exception ex) {
                Logger.getLogger(ComunicacaoInterna.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (despachoList.size() == 1) {
            if (despachoList.get(0).getCi() != null) {
                
                try {
                    if(despachoList.get(0).getPara() != null){
                        ultimoDespachoUO = UnidadeOrganizacionalDAO.getInstance().selectFromId(despachoList.get(0).getPara().getId()).getNome();
                    }
                    
                } catch (Exception ex) {
                    Logger.getLogger(ComunicacaoInterna.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                
            }

        } else {
            
            boolean proximoDespacho = false;
            for (Despacho d : despachoList) {
                if(proximoDespacho){
                    if(d.getPara()!= null && d.getPara().getId() != idUO){
                        
                        ultimoDespachoUO = d.getPara().getNome();
                    }
                    
                   
                }
                if (d.getPara().getId() == idUO && (d.getStatus() == 2 || d.getStatus() == 5)) {
                    proximoDespacho = true;
                    
                   
                }
            }

        }
       
        return ultimoDespachoUO;
    }
    
    

    public int controleTipoCI(long idUO) {

        if (tipo == CI_CIRCULAR) {
            return CI_CIRCULAR;
        } else if (this.getUltimoDespacho()
                .getPara().getId() == idUO) {
            return CI_DIRECIONADA;
        } else {
            return CI_COPIA;
        }
    }
    
    public boolean lidoUO(long idUO) throws Exception{
        System.out.println("************* Id UO: " +idUO);
        Despacho despacho = DespachoDAO.getInstance().selectFromId(this.ultimoDespachoOrCopiaUO(idUO).getId());
        if(despacho.getPara() == null || despacho.getPara().getId() != idUO){
            for (DespachoCopia d : despacho.getDespachoCopiaList()) {
                if(d.getUnidadeOrganizacional().getId() == idUO){
                    return d.getLido();
                }
            }
            return false;
        }else {
            return despacho.getLido();
        }
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

}
