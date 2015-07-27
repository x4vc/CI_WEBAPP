/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jorgefspf
 */
@Entity
@Table(name = "unidade_organizacional")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadeOrganizacional.findAll", query = "SELECT u FROM UnidadeOrganizacional u"),
    @NamedQuery(name = "UnidadeOrganizacional.findById", query = "SELECT u FROM UnidadeOrganizacional u WHERE u.id = :id"),
    @NamedQuery(name = "UnidadeOrganizacional.findByUoPai", query = "SELECT u FROM UnidadeOrganizacional u WHERE u.uoPai = :uoPai"),
    @NamedQuery(name = "UnidadeOrganizacional.findByUoGerenciadora", query = "SELECT u FROM UnidadeOrganizacional u WHERE u.uoGerenciadora = :uoGerenciadora")})
public class UnidadeOrganizacional implements Serializable, BaseEntity {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "unidadeOrganizacional", fetch = FetchType.LAZY)
    private List<DespachoCopia> despachoCopiaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uo", fetch = FetchType.LAZY)
    private List<ComunicacaoInterna> comunicacaoInternaList;
  
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Lob
    @Column(name = "nome")
    private String nome;
    @Column(name = "uo_pai")
    private long uoPai;
    @Column(name = "uo_gerenciadora")
    private long uoGerenciadora;
    @JoinTable(name = "uo_subordinacao", joinColumns = {
        @JoinColumn(name = "subordinacao", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "uo", referencedColumnName = "id")})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<UnidadeOrganizacional> unidadeOrganizacionalList;
    @ManyToMany(mappedBy = "unidadeOrganizacionalList", fetch = FetchType.LAZY)
    private List<UnidadeOrganizacional> unidadeOrganizacionalList1;
    @JoinTable(name = "uo_responsavel", joinColumns = {
        @JoinColumn(name = "uo", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "responsavel", referencedColumnName = "id")})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Usuario> usuarioList;
    @OneToMany(mappedBy = "para", fetch = FetchType.EAGER)
    private List<Despacho> despachoList;
    @OneToMany(mappedBy = "uo", fetch = FetchType.LAZY)
    private List<Usuario> usuarioList1;

    public UnidadeOrganizacional() {
    }

    public UnidadeOrganizacional(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getUoPai() {
        return uoPai;
    }

    public void setUoPai(long uoPai) {
        this.uoPai = uoPai;
    }

    public long getUoGerenciadora() {
        return uoGerenciadora;
    }

    public void setUoGerenciadora(Integer uoGerenciadora) {
        this.uoGerenciadora = uoGerenciadora;
    }

    @XmlTransient
    public List<UnidadeOrganizacional> getUnidadeOrganizacionalList() {
        return unidadeOrganizacionalList;
    }

    public void setUnidadeOrganizacionalList(List<UnidadeOrganizacional> unidadeOrganizacionalList) {
        this.unidadeOrganizacionalList = unidadeOrganizacionalList;
    }

    @XmlTransient
    public List<UnidadeOrganizacional> getUnidadeOrganizacionalList1() {
        return unidadeOrganizacionalList1;
    }

    public void setUnidadeOrganizacionalList1(List<UnidadeOrganizacional> unidadeOrganizacionalList1) {
        this.unidadeOrganizacionalList1 = unidadeOrganizacionalList1;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    @XmlTransient
    public List<Despacho> getDespachoList() {
        return despachoList;
    }

    public void setDespachoList(List<Despacho> despachoList) {
        this.despachoList = despachoList;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList1() {
        return usuarioList1;
    }

    public void setUsuarioList1(List<Usuario> usuarioList1) {
        this.usuarioList1 = usuarioList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UnidadeOrganizacional)) {
            return false;
        }
        UnidadeOrganizacional other = (UnidadeOrganizacional) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Models.UnidadeOrganizacional[ id=" + id + " ]";
    }

    @XmlTransient
    public List<ComunicacaoInterna> getComunicacaoInternaList() {
        return comunicacaoInternaList;
    }

    public void setComunicacaoInternaList(List<ComunicacaoInterna> comunicacaoInternaList) {
        this.comunicacaoInternaList = comunicacaoInternaList;
    }

    @XmlTransient
    public List<DespachoCopia> getDespachoCopiaList() {
        return despachoCopiaList;
    }

    public void setDespachoCopiaList(List<DespachoCopia> despachoCopiaList) {
        this.despachoCopiaList = despachoCopiaList;
    }

    @XmlTransient
    public boolean isAutonoma(){
        return this.uoGerenciadora == this.id;
    }
   
    
}
