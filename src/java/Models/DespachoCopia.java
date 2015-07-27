/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;



import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorgefspf
 */
@Entity
@Table(name = "despacho_copia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DespachoCopia.findAll", query = "SELECT d FROM DespachoCopia d"),
 
    @NamedQuery(name = "DespachoCopia.findByLido", query = "SELECT d FROM DespachoCopia d WHERE d.lido = :lido")})
public class DespachoCopia implements Serializable {
    @Basic(optional = false)
    
    @Column(name = "arquivado")
    private boolean arquivado;
    @Column(name = "data_recebimento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataRecebimento;
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    private static final long serialVersionUID = 1L;
   
    @Column(name = "lido")
    private Boolean lido = false;
    @JoinColumn(name = "despacho", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Despacho despacho;
    @JoinColumn(name = "unidade_organizacional", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UnidadeOrganizacional unidadeOrganizacional;

    public DespachoCopia() {
        this.despacho = new Despacho();
        this.unidadeOrganizacional = new UnidadeOrganizacional();
    }

   

    public Boolean getLido() {
        return lido;
    }

    public void setLido(Boolean lido) {
        this.lido = lido;
    }

    public Despacho getDespacho() {
        return despacho;
    }

    public void setDespacho(Despacho despacho) {
        this.despacho = despacho;
    }

    public UnidadeOrganizacional getUnidadeOrganizacional() {
        return unidadeOrganizacional;
    }

    public void setUnidadeOrganizacional(UnidadeOrganizacional unidadeOrganizacional) {
        this.unidadeOrganizacional = unidadeOrganizacional;
    }

    

    public DespachoCopia(Integer id) {
        this.id = id;
    }

   
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    
    
    @Override
    public String toString() {
        return "Models.DespachoCopia[ id=" + id + " ]";
    }

    public Date getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(Date dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    
    @Transient
    public String getVisualizado(){
        if(this.lido == true){
            return "#009900";
        }
        
        return "#000000";
    }

    public boolean getArquivado() {
        return arquivado;
    }

    public void setArquivado(boolean arquivado) {
        this.arquivado = arquivado;
    }
    
    
}
