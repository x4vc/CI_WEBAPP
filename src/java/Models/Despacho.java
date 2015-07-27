/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jorgefspf
 */
@Entity
@Table(name = "despacho")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Despacho.findAll", query = "SELECT d FROM Despacho d"),
    @NamedQuery(name = "Despacho.findById", query = "SELECT d FROM Despacho d WHERE d.id = :id"),
    @NamedQuery(name = "Despacho.findByStatus", query = "SELECT d FROM Despacho d WHERE d.status = :status"),
    @NamedQuery(name = "Despacho.findByAutorizado", query = "SELECT d FROM Despacho d WHERE d.autorizado = :autorizado"),
    @NamedQuery(name = "Despacho.findByData", query = "SELECT d FROM Despacho d WHERE d.data = :data"),
    @NamedQuery(name = "Despacho.findByLido", query = "SELECT d FROM Despacho d WHERE d.lido = :lido"),
    @NamedQuery(name = "Despacho.findByDataRecebimento", query = "SELECT d FROM Despacho d WHERE d.dataRecebimento = :dataRecebimento")})
public class Despacho implements Serializable {
    @Column(name = "autorizado_externo")
    private Boolean autorizadoExterno = true;

    @Column(name = "data_recebimento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataRecebimento;

    @OneToMany(mappedBy = "despacho", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DespachoCopia> despachoCopiaList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "status")
    private Integer status;

    @Transient
    private String statusString;

    @Column(name = "autorizado")
    private Boolean autorizado;
    @Column(name = "data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    @Column(name = "lido")
    private Boolean lido = false;

    @Column(name = "conteudo")
    private String conteudo = "";

    @JoinColumn(name = "CI", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ComunicacaoInterna ci;
    @JoinColumn(name = "para", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private UnidadeOrganizacional para;

    public Despacho() {
        this.anexoList = new ArrayList<>();
        this.despachoCopiaList = new ArrayList<>();
    }

    @OneToMany(mappedBy = "despacho")
    private List<Anexo> anexoList;

    public String getStatusString() {
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        String dataEnvio = dt.format(this.data);

        if (status == 1) {
            statusString = "encaminhou para " + this.para.getNome() + "em " + dataEnvio;
        } else if (status == 2 || status == 5) { //status = 5 para um ci que foi arquivada pelo próximo setor
            statusString = "despachou para " + this.para.getNome() + "em " + dataEnvio;
        } else if (status == 3) {
            statusString = "desarquivou em  " + dataEnvio;
        } else if (status == 4) {
            statusString = "arquivou em " + dataEnvio;
        }

        return statusString;
    }

    public Despacho(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(Boolean autorizado) {
        this.autorizado = autorizado;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Boolean getLido() {
        return lido;
    }

    public void setLido(Boolean lido) {
        this.lido = lido;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Date getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(Date dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    @XmlTransient
    public List<Anexo> getAnexoList() {
        return anexoList;
    }

    public void setAnexoList(List<Anexo> anexoList) {
        this.anexoList = anexoList;
    }

    public ComunicacaoInterna getCi() {
        return ci;
    }

    public void setCi(ComunicacaoInterna ci) {
        this.ci = ci;
    }

    public UnidadeOrganizacional getPara() {
        return para;
    }

    public void setPara(UnidadeOrganizacional para) {
        this.para = para;
    }

    @XmlTransient
    public List<DespachoCopia> getDespachoCopiaList() {
        return despachoCopiaList;
    }

    public void setDespachoCopiaList(List<DespachoCopia> despachoCopiaList) {
        this.despachoCopiaList = despachoCopiaList;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final Despacho other = (Despacho) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Transient
    public String getVisualizado() {
        if (this.lido == true) {
            return "#009900";
        }

        return "#000000";
    }

    @Override
    public String toString() {
        return "Models.Despacho[ id=" + id + " ]";
    }

    public boolean getAutorizadoExterno() {
        return autorizadoExterno;
    }

    public void setAutorizadoExterno(boolean autorizadoExterno) {
        this.autorizadoExterno = autorizadoExterno;
    }

    

}
