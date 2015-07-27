/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.ComunicacaoInternaDAO;
import DAO.DespachoCopiaDAO;
import DAO.DespachoDAO;
import DAO.UsuarioDAO;
import Models.Anexo;
import Models.ComunicacaoInterna;
import Models.Despacho;
import Models.DespachoCopia;
import Models.UnidadeOrganizacional;
import Models.Usuario;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import util.ImprimirCIPDF;

/**
 *
 * @author jorgefspf
 */
@ManagedBean
@ViewScoped
public class ComunicacaoInternaListarMB implements Serializable {

    /**
     * Creates a new instance of ComunicacaoInternaListarMB
     */
    private LazyDataModel<ComunicacaoInterna> cisCircularesRecebidas;
    private LazyDataModel<ComunicacaoInterna> cisPendentesAprovacao;
    private LazyDataModel<ComunicacaoInterna> cisRecebidas;
    private LazyDataModel<ComunicacaoInterna> cisArquivadas;
    private LazyDataModel<ComunicacaoInterna> cisEnviadas;
    private Long idUsuarioLogado;
    Despacho despacho = new Despacho();

    ComunicacaoInterna ci;

    Usuario usuario;

    public ComunicacaoInternaListarMB() {
        this.ci = new ComunicacaoInterna();

        despacho = new Despacho();

    }

    @PostConstruct
    public void init() {
        idUsuarioLogado = this.pegarUOUsuarioLogado();
        System.out.println("Executando!");
       
        

        

        
        
    }
    
    public void updateRecebidas(){
        this.setCisRecebidas(null);
    }

    public LazyDataModel<ComunicacaoInterna> getCisRecebidas() {
        if (cisRecebidas == null) {
            cisRecebidas = new LazyCIDataModel(ComunicacaoInternaDAO.getInstance().listarCIsRecebidas(idUsuarioLogado));
            System.out.println("Carreguei as Recebidas: ------------------");
        }
        return cisRecebidas;
    }

    public void setCisRecebidas(LazyDataModel<ComunicacaoInterna> cisRecebidas) {
        this.cisRecebidas = cisRecebidas;
    }

    public LazyDataModel<ComunicacaoInterna> getCisPendentesAprovacao() {
        if (cisPendentesAprovacao == null) {
            
            cisPendentesAprovacao = new LazyCIDataModel(ComunicacaoInternaDAO.getInstance().listarCIsPendetesAutorizacao(idUsuarioLogado));
            System.out.println("Carreguei as Pendentes: ------------------");
        }
        return cisPendentesAprovacao;
    }

    public void setCisPendentesAprovacao(LazyDataModel<ComunicacaoInterna> cisPendentesAprovacao) {
        this.cisPendentesAprovacao = cisPendentesAprovacao;
    }

    public LazyDataModel<ComunicacaoInterna> getCisArquivadas() {
        if(cisArquivadas == null){
            cisArquivadas = new LazyCIDataModel(ComunicacaoInternaDAO.getInstance().listarCIsArquivadas(idUsuarioLogado));
            System.out.println("Carreguei as Enviadas: ------------------");
        }
        return cisArquivadas;
    }

    public void setCisArquivadas(LazyDataModel<ComunicacaoInterna> cisArquivadas) {
        this.cisArquivadas = cisArquivadas;
    }

    public LazyDataModel<ComunicacaoInterna> getCisEnviadas() {
        if(cisEnviadas == null){
            cisEnviadas = new LazyCIDataModel(ComunicacaoInternaDAO.getInstance().listarCIsEnviadas(idUsuarioLogado));
        }
        return cisEnviadas;
    }

    public void setCisEnviadas(LazyDataModel<ComunicacaoInterna> cisEnviadas) {
        this.cisEnviadas = cisEnviadas;
    }

    public long pegarUOUsuarioLogado() {
        if (usuario == null) {
            SecurityContext context = SecurityContextHolder.getContext();
            if (context instanceof SecurityContext) {
                Authentication authentication = context.getAuthentication();
                if (authentication instanceof Authentication) {

                    try {
                        usuario = UsuarioDAO.getInstance().buscarDadosUsuario(((User) authentication.getPrincipal()).getUsername());
                    } catch (Exception ex) {
                        Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } 

        }
        return usuario.getUo().getId();
    }

    public void informarVisualizacao(Long idCI) {

        ComunicacaoInterna ci = new ComunicacaoInterna();
        try {
            ci = ComunicacaoInternaDAO.getInstance().selectFromId(idCI);
        } catch (Exception ex) {
            Logger.getLogger(ComunicacaoInternaListarMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ci.controleTipoCI(this.pegarUOUsuarioLogado()) != 1) {
            DespachoCopia dc = new DespachoCopia();
            dc = DespachoCopiaDAO.getInstance().selectFromUOCI(this.pegarUOUsuarioLogado(), ci.getId());
            dc.setLido(Boolean.TRUE);
            dc.setDataRecebimento(new java.sql.Date(new java.util.Date().getTime()));

            DespachoCopiaDAO.getInstance().persist(dc);
        } else {
            Despacho d = new Despacho();
            try {
                d = DespachoDAO.getInstance().selectFromId(ci.getUltimoDespacho().getId());
                d.setLido(Boolean.TRUE);
                d.setDataRecebimento(new java.sql.Date(new java.util.Date().getTime()));
                DespachoDAO.getInstance().persist(d);
            } catch (Exception ex) {
                Logger.getLogger(ComunicacaoInternaListarMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String selecionar() {

        String resultado = "";
        try {
            this.ci = ComunicacaoInternaDAO.getInstance().selectFromId(ci.getId());
            resultado = "/visualizar/visualizarci?faces-redirect=true&amp;includeViewParams=true";
        } catch (Exception ex) {
            Logger.getLogger(ComunicacaoInternaListarMB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultado;
    }

    public ComunicacaoInterna getCi() {
        if (this.ci != null && this.ci.getId() != null && this.ci.getId() > 0) {
            try {
                this.ci = ComunicacaoInternaDAO.getInstance().selectFromId(ci.getId());
                return ci;
            } catch (Exception ex) {
                Logger.getLogger(ComunicacaoInternaListarMB.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return ci;
    }

    public void setCi(ComunicacaoInterna ci) {
        try {
            ci = ComunicacaoInternaDAO.getInstance().selectFromId(ci.getId());
        } catch (Exception ex) {
            Logger.getLogger(ComunicacaoInternaListarMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.ci = ci;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Despacho getDespacho() {
        return despacho;
    }

    public void setDespacho(Despacho despacho) {
        this.despacho = despacho;
    }

    public String aprovarCI() {

        this.ci.getUltimoDespacho().setStatus(1);
        this.ci.getUltimoDespacho().setAutorizado(Boolean.TRUE);
        
        if(this.ci.getUltimoDespacho().getPara().getId() != this.idUsuarioLogado){
            this.ci.getUltimoDespacho().setAutorizadoExterno(Boolean.TRUE);
        }
        
        System.out.println("ID despacho: " + this.ci.getUltimoDespacho().getId());
        despacho = this.ci.getUltimoDespacho();
        DespachoDAO.getInstance().persist(despacho);
        System.out.println("Despacho Aprovado!");
        return "/listar/listarci.xhtml";
    }

    public String cadastrarNovoDespacho() {

        despacho.setCi(this.ci);
        try {
            despacho.setPara(new UnidadeOrganizacional(para));
        } catch (Exception ex) {
            Logger.getLogger(ComunicacaoInternaListarMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        //ci.getDespachoList().add(despacho);
        despacho.setData(new java.sql.Date(new java.util.Date().getTime()));
        despacho.setAutorizado(Boolean.TRUE);
        despacho.setStatus(1);

        DespachoDAO.getInstance().persist(despacho);
        //RequestContext.getCurrentInstance().closeDialog(despacho);

        return "";

    }

    public boolean podeAprovar(int idUO) {
        
        boolean podeAprovar = false;
        //se o último despacho não tiver sido aprovado 
        System.out.println("Último Despacho ta autorizado? " + this.ci.getUltimoDespacho().getAutorizado());
        int numDespachos = this.ci.getDespachoList().size();
        if (!this.ci.getUltimoDespacho().getAutorizado()) {
            
            System.out.println("Numero de Despachos na CI: " + numDespachos);
            //se tiver mais de um despacho é preciso comparar se quem está logado é da unidade organizacional de quem recebeu o penultimo despacho 
            if (numDespachos > 1) {
                System.out.println("Foi enviado por mim?  " + this.ci.getDespachoList().get(numDespachos - 2).getPara().getId() + " == " + idUO);
                if (this.ci.getDespachoList().get(numDespachos - 2).getPara().getId() == idUO) {
                    podeAprovar = true;
                }
                

                //Se tiver somente um despach o basta verificar se a pessoa logada é da uo da origem da ci     
            } else if (this.ci.getUo().getId() == idUO) {
                System.out.println("Quem criou a CI foi minha uo?  " + this.ci.getUo().getId() + " == " + idUO);
                podeAprovar = true;
            }
        }else if(!this.ci.getUltimoDespacho().getAutorizadoExterno()){
            if(numDespachos > 1){
                if(this.ci.getDespachoList().get(numDespachos - 2).getPara().getUoGerenciadora() == idUO){
                    podeAprovar = true;
                }
            }else if (this.ci.getUo().getUoGerenciadora() == idUO) {
                System.out.println("Quem criou a CI foi minha uo?  " + this.ci.getUo().getId() + " == " + idUO);
                podeAprovar = true;
            }
        }

        return podeAprovar;

    }

    public boolean podeArquivar(long idCI) {

        try {
            this.ci = ComunicacaoInternaDAO.getInstance().selectFromId(idCI);
            long uoUser = this.pegarUOUsuarioLogado();
            Despacho d = DespachoDAO.getInstance().selectFromId(this.ci.getUltimoDespacho().getId());
            if (ci.getTipo() != 2 && d.getPara().getId() == uoUser) {
                if (this.ci.getUltimoDespacho().getStatus() == 1) {
                    return true;
                } else {
                    return false;
                }

            } else {
                DespachoCopia dc = DespachoCopiaDAO.getInstance().selectFromUOCI(uoUser, this.ci.getId());
                if (dc != null && !dc.getArquivado()) {
                    return true;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ComunicacaoInternaListarMB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    Long para;
    String conteudo = "Teste de Jorge!!!!";

    public Long getPara() {
        return para;
    }

    public void setPara(Long para) {
        this.para = para;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    private StreamedContent file;

    public void initFile(Anexo a) throws FileNotFoundException {
        System.out.println("Passei aqui!");
        String arquivo = a.getNome();
        String caminho = a.getEndereco();
        FileInputStream stream = new FileInputStream(caminho);
        file = new DefaultStreamedContent(stream, caminho, arquivo);

    }

    public StreamedContent getFile() {

        return file;
    }
    
    public void imprimirCI() throws Exception{
        ImprimirCIPDF pdfCI = new ImprimirCIPDF(this.ci);
        pdfCI.gerarRelatorioPDF();
        
    }

}
