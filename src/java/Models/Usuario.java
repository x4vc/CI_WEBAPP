/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findById", query = "SELECT u FROM Usuario u WHERE u.id = :id"),
    @NamedQuery(name = "Usuario.findByLogin", query = "SELECT u FROM Usuario u WHERE u.login = :login"),
    @NamedQuery(name = "Usuario.findBySenha", query = "SELECT u FROM Usuario u WHERE u.senha = :senha"),
    @NamedQuery(name = "Usuario.findByNome", query = "SELECT u FROM Usuario u WHERE u.nome = :nome"),
    @NamedQuery(name = "Usuario.findByAtivo", query = "SELECT u FROM Usuario u WHERE u.ativo = :ativo")})
public class Usuario implements Serializable, BaseEntity {
    @Column(name = "authority")
    private String authority;
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "login",unique = true)
    private String login;
    @Column(name = "senha")
    private String senha;
    @Column(name = "nome")
    private String nome;
    @Column(name = "ativo")
    private Boolean ativo;
    @Column(name = "assinatura")
    private String assinatura;
    @ManyToMany(mappedBy = "usuarioList", fetch = FetchType.LAZY)
    private List<UnidadeOrganizacional> unidadeOrganizacionalList;
    @OneToMany(mappedBy = "autor", fetch = FetchType.LAZY)
    private List<ComunicacaoInterna> comunicacaoInternaList;
    @JoinColumn(name = "uo", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private UnidadeOrganizacional uo;
    

    public Usuario() {
        
    }

    public Usuario(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    
    
    public Usuario(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @XmlTransient
    public List<UnidadeOrganizacional> getUnidadeOrganizacionalList() {
        return unidadeOrganizacionalList;
    }

    public void setUnidadeOrganizacionalList(List<UnidadeOrganizacional> unidadeOrganizacionalList) {
        this.unidadeOrganizacionalList = unidadeOrganizacionalList;
    }

    @XmlTransient
    public List<ComunicacaoInterna> getComunicacaoInternaList() {
        return comunicacaoInternaList;
    }

    public void setComunicacaoInternaList(List<ComunicacaoInterna> comunicacaoInternaList) {
        this.comunicacaoInternaList = comunicacaoInternaList;
    }

    public UnidadeOrganizacional getUo() {
        return uo;
    }

    public void setUo(UnidadeOrganizacional uo) {
        this.uo = uo;
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
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Models.Usuario[ id=" + id + " ]";
    }


    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(String assinatura) {
        this.assinatura = assinatura;
    }
    
    
    
}
