/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.AnexoDAO;
import DAO.ComunicacaoInternaDAO;
import DAO.DespachoCopiaDAO;
import DAO.DespachoDAO;
import DAO.UnidadeOrganizacionalDAO;
import DAO.UsuarioDAO;
import Models.Anexo;
import Models.ComunicacaoInterna;
import Models.Despacho;
import Models.DespachoCopia;
import Models.UnidadeOrganizacional;
import Models.Usuario;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultUploadedFile;
import org.primefaces.model.UploadedFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.FileNotFoundException;
/**
 *
 * @author jorgefspf
 */
@ManagedBean
@ViewScoped
public class ComunicacaoInternaMB implements Serializable {

    /**
     * Creates a new instance of ComunicacaoInternaMB
     */
    private ComunicacaoInterna ci;
    private Despacho despacho;

    private List<DespachoCopia> copias;

    private UploadedFile file;
    private List<UploadedFile> files;
    private Anexo anexo = new Anexo();
    private List<Anexo> anexos;
    private List<ComunicacaoInterna> listaApensamentos;
    private List<UnidadeOrganizacional> copiasDespacho = new ArrayList<>();
    private List<ComunicacaoInterna> ciDestinadasUO;
    private boolean circularParaTodos = true;
    private UnidadeOrganizacional para;
    Usuario usuario;

    public ComunicacaoInternaMB() {
        ci = new ComunicacaoInterna();
        despacho = new Despacho();
        copias = new ArrayList<>();
        files = new ArrayList<>();
        anexos = new ArrayList<>();

    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<UploadedFile> files) {
        this.files = files;
    }

    public String gerarMD5(String senha) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
        String crypto = hash.toString(16);
        if (crypto.length() % 2 != 0) {
            crypto = "0" + crypto;
        }
        return crypto;
    }
    
    public boolean verificarAutorizacaoExternaNovaCI(){
        boolean retorno = true;
        //Verifica se não é autonomo (não é gerencia nem diretoria)
        if(!this.ci.getAutor().getUo().isAutonoma()){
            //Não é da mesma gerência
            if(this.ci.getAutor().getUo().getUoGerenciadora() != this.despacho.getPara().getUoGerenciadora()){
                retorno = false;
            }
              
        }
        return retorno;
    }
    
    public boolean verificarAutorizacaoExternaNovoDespacho(){
        boolean retorno = true;
        //Verifica se não é autonomo (não é gerencia nem diretoria)
        if(!this.usuario.getUo().isAutonoma()){
            //Não é da mesma gerência
            if(despacho.getPara().getUoGerenciadora() != this.usuario.getUo().getId()){
                retorno = false;
            }
              
        }
        return retorno;
    }
    
    

    public String cadastrarCI() throws Exception {
        ci.setAssinatura(this.gerarMD5(new java.util.Date().toString() + ci.getAutor().getLogin()));
        ci.setAutor(UsuarioDAO.getInstance().buscarDadosUsuario(ci.getAutor().getLogin()));
        ci.setUo(ci.getAutor().getUo());
        long idUO = ci.getUo().getId();
        System.out.println("idUO: " + idUO);
        
        int numUltimaCI = ComunicacaoInternaDAO.getInstance().ultimaCI(idUO);
        ci.setNumero(numUltimaCI + 1);
        ci.setStatus("Encaminhada");
        ci.setData(new java.sql.Date(new java.util.Date().getTime()));
        ci.setTipo(1);
        ci = ComunicacaoInternaDAO.getInstance().persist(this.ci);
        

        //ci.getDespachoList().add(despacho);
        despacho.setData(new java.sql.Date(new java.util.Date().getTime()));
        if (ci.getAutor().getAuthority().equalsIgnoreCase("ROLE_MNG")) {
            despacho.setAutorizado(Boolean.FALSE);
        } else {
            despacho.setAutorizado(Boolean.TRUE);
        }

        despacho.setStatus(1);
        despacho.setCi(ci);
        despacho.setConteudo(despacho.getConteudo()+ "<br/>"+ ci.getAutor().getAssinatura());

        despacho = DespachoDAO.getInstance().persist(despacho);
        despacho.setAutorizadoExterno(this.verificarAutorizacaoExternaNovaCI());

        for (UnidadeOrganizacional uo : copiasDespacho) {
            DespachoCopia dc = new DespachoCopia();
            dc.setUnidadeOrganizacional(uo);
            dc.setDespacho(despacho);
            dc.setLido(Boolean.FALSE);
            DespachoCopiaDAO.getInstance().persist(dc);
        }

        //despacho.setDespachoCopiaList(copias);
        //despacho = DespachoDAO.getInstance().persist(despacho);
        for (Anexo a : anexos) {
            a.setDespacho(despacho);
            AnexoDAO.getInstance().persist(a);
        }

//        despacho.setAnexoList(anexosPersist);
//        despacho = DespachoDAO.getInstance().persist(despacho);
        this.ci = new ComunicacaoInterna();
        this.despacho = new Despacho();

        return "/listar/listarci.xhtml";
    }

    public String cadastrarCircular() throws Exception {
        System.out.println("Entrei no Cadastrar Circular");
        ci.setAssinatura(this.gerarMD5(new java.util.Date().toString() + ci.getAutor().getLogin()));
        ci.setAutor(UsuarioDAO.getInstance().buscarDadosUsuario(ci.getAutor().getLogin()));
        ci.setUo(ci.getAutor().getUo());
        long idUO = ci.getUo().getId();
        System.out.println("idUO: " + idUO);

        int numUltimaCI = ComunicacaoInternaDAO.getInstance().ultimaCI(idUO);
        ci.setNumero(numUltimaCI + 1);
        ci.setStatus("Encaminhada");
        ci.setData(new java.sql.Date(new java.util.Date().getTime()));
        ci.setTipo(2);
        ci = ComunicacaoInternaDAO.getInstance().persist(this.ci);

        //ci.getDespachoList().add(despacho);
        despacho.setData(new java.sql.Date(new java.util.Date().getTime()));
        if (ci.getAutor().getAuthority().equalsIgnoreCase("ROLE_MNG")) {
            despacho.setAutorizado(Boolean.FALSE);
        } else {
            despacho.setAutorizado(Boolean.TRUE);
        }
        despacho.setPara(null);
        despacho.setLido(Boolean.TRUE);
        despacho.setStatus(1);
        despacho.setCi(ci);

        despacho = DespachoDAO.getInstance().persist(despacho);
        System.out.println("Circular para todos: " + circularParaTodos);
        if (this.circularParaTodos) {
            System.out.println("CI para todas as Unidades Organizacionais");
            copiasDespacho = UnidadeOrganizacionalDAO.getInstance().select();
        }
        copiasDespacho.remove(ci.getUo());
        System.out.println("CI para todas as Unidades Organizacionais");

        for (UnidadeOrganizacional uo : copiasDespacho) {
            DespachoCopia dc = new DespachoCopia();
            dc.setUnidadeOrganizacional(uo);
            dc.setDespacho(despacho);
            dc.setLido(Boolean.FALSE);
            DespachoCopiaDAO.getInstance().persist(dc);
        }

        //despacho.setDespachoCopiaList(copias);
        //despacho = DespachoDAO.getInstance().persist(despacho);
        for (Anexo a : anexos) {
            a.setDespacho(despacho);
            AnexoDAO.getInstance().persist(a);
        }

//        despacho.setAnexoList(anexosPersist);
//        despacho = DespachoDAO.getInstance().persist(despacho);
        this.ci = new ComunicacaoInterna();
        this.despacho = new Despacho();

        return "/listar/listarci.xhtml";
    }

    public String uploadFile(UploadedFile file) {

// Extract file name from content-disposition header of file part
        String fileName = (file.getFileName());
        System.out.println("***** fileName: " + file.getFileName());
        
        System.out.println(System.getProperty("os.name"));
        String basePath = "C:" + File.separator + "aquivosCI" + File.separator;
        System.out.println("BasePath: " + basePath);
        File outputFilePath = new File(basePath + fileName);

// Copy uploaded file to destination path
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = file.getInputstream();
            outputStream = new FileOutputStream(outputFilePath);

            int read = 0;
            final byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(ComunicacaoInternaMB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(ComunicacaoInternaMB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return outputFilePath.getAbsolutePath();    // return to same page
    }

    public void removeUpload() {

        this.anexos.remove(this.anexo);
        FacesMessage message = new FacesMessage("Sucesso", anexo.getNome() + " foi removida.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        this.file = new DefaultUploadedFile();

    }

    public void fileUploadListener(FileUploadEvent e) {
        // Get uploaded file from the FileUploadEvent
        
        this.file = e.getFile();
        System.out.println("Uploaded File Name Is: " + file.getFileName());
        this.anexo.setEndereco(this.uploadFile(file));
        Calendar c = Calendar.getInstance();
        Date data = c.getTime();
        SimpleDateFormat f = new SimpleDateFormat("ddMMyyyyHHss");  
        this.anexo.setNome(f.format(data) + file.getFileName() );
        this.anexo.setTipo(file.getContentType());
        this.anexos.add(anexo);
        anexo = new Anexo();

        file = new DefaultUploadedFile();

        // Print out the information of the file
        //System.out.println("Uploaded File Name Is :: " + file.getFileName() + " :: Uploaded File Size :: " + file.getSize());
    }

    public ComunicacaoInterna getCi() {
        return ci;
    }

    public void setCi(ComunicacaoInterna ci) {
        System.out.println("Entrei aqui!");
        this.ci = ci;
    }

    public Despacho getDespacho() {
        return despacho;
    }

    public void setDespacho(Despacho despacho) {
        this.despacho = despacho;
    }

    public Anexo getAnexo() {
        return anexo;
    }

    public void setAnexo(Anexo anexo) {
        this.anexo = anexo;
    }

    public List<DespachoCopia> getCopias() {
        return copias;
    }

    public void setCopias(List<DespachoCopia> copias) {
        this.copias = copias;
    }

    public List<Anexo> getAnexos() {
        return anexos;
    }

    public void setAnexos(List<Anexo> anexos) {
        this.anexos = anexos;
    }

    public List<UnidadeOrganizacional> getCopiasDespacho() {
        return copiasDespacho;
    }

    public void setCopiasDespacho(List<UnidadeOrganizacional> copiasDespacho) throws Exception {
        UnidadeOrganizacional uo;
        uo = UnidadeOrganizacionalDAO.getInstance().selectFromId(this.pegarUOUsuarioLogado());
        copiasDespacho.remove(uo);
        this.copiasDespacho = copiasDespacho;
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

    public List<ComunicacaoInterna> getListaApensamentos() {

        if (listaApensamentos == null) {
            listaApensamentos = ComunicacaoInternaDAO.getInstance().listarApensamentos(this.pegarUOUsuarioLogado());
        }

        return listaApensamentos;
    }

    public void setListaApensamentos(List<ComunicacaoInterna> cisRecebidas) {
        this.listaApensamentos = cisRecebidas;
    }

    public List<ComunicacaoInterna> getCiDestinadasUO() {
        if (ciDestinadasUO == null) {
            ciDestinadasUO = ComunicacaoInternaDAO.getInstance().listarCIsRecebidas(this.pegarUOUsuarioLogado());
        }
        return ciDestinadasUO;
    }

    public void setCiDestinadasUO(List<ComunicacaoInterna> ciDestinadasUO) {
        this.ciDestinadasUO = ciDestinadasUO;
    }

    public void cadastrarCopiasDespacho() {

    }

    public void cadastrarNovoDespacho(ComunicacaoInterna c) {

        despacho.setCi(c);

        //ci.getDespachoList().add(despacho);
        despacho.setData(new java.sql.Date(new java.util.Date().getTime()));
        if (ci.getAutor().getAuthority().equalsIgnoreCase("ROLE_MNG")) {
            despacho.setAutorizado(Boolean.FALSE);
        } else {
            despacho.setAutorizado(Boolean.TRUE);
        }
        despacho.setStatus(1);
        despacho.setCi(ci);

        DespachoDAO.getInstance().persist(despacho);
        //RequestContext.getCurrentInstance().closeDialog(despacho);

    }

    public String cadastrarNovoDespacho() {
        try {
            ci = ComunicacaoInternaDAO.getInstance().selectFromId(ci.getId());
        } catch (Exception ex) {
            Logger.getLogger(ComunicacaoInternaMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        ci.getDespachoList().get(ci.getDespachoList().size() - 1).setStatus(2);
        ComunicacaoInternaDAO.getInstance().persist(ci);
        this.despacho.setCi(ci);

        //ci.getDespachoList().add(despacho);
        despacho.setData(new java.sql.Date(new java.util.Date().getTime()));
        despacho.setAutorizado(Boolean.TRUE);
        despacho.setAutorizadoExterno(Boolean.TRUE);
        despacho.setStatus(1);
        despacho.setCi(ci);
        despacho = DespachoDAO.getInstance().persist(despacho);

        for (UnidadeOrganizacional uo : copiasDespacho) {
            DespachoCopia dc = new DespachoCopia();
            dc.setUnidadeOrganizacional(uo);
            dc.setDespacho(despacho);
            dc.setLido(Boolean.FALSE);
            DespachoCopiaDAO.getInstance().persist(dc);
        }

        //despacho.setDespachoCopiaList(copias);
        //despacho = DespachoDAO.getInstance().persist(despacho);
        for (Anexo a : anexos) {
            a.setDespacho(despacho);
            AnexoDAO.getInstance().persist(a);
        }

        return "/listar/listarci?faces-redirect=true";
        //RequestContext.getCurrentInstance().closeDialog(despacho);
    }

    public String arquivarCI() {

        try {
            ci = ComunicacaoInternaDAO.getInstance().selectFromId(ci.getId());
        } catch (Exception ex) {
            Logger.getLogger(ComunicacaoInternaMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Muda Status para Despachado
        ci.getDespachoList().get(ci.getDespachoList().size() - 1).setStatus(5);
        ComunicacaoInternaDAO.getInstance().persist(ci);
        this.despacho.setCi(ci);

        //ci.getDespachoList().add(despacho);
        despacho.setPara(ci.getUltimoDespacho().getPara());
        despacho.setData(new java.sql.Date(new java.util.Date().getTime()));
        despacho.setAutorizado(Boolean.TRUE);
        despacho.setAutorizadoExterno(Boolean.TRUE);
        despacho.setStatus(4);
        despacho.setCi(ci);
        despacho = DespachoDAO.getInstance().persist(despacho);

        for (Anexo a : anexos) {
            a.setDespacho(despacho);
            AnexoDAO.getInstance().persist(a);
        }
        DespachoCopiaDAO.getInstance().arquivarTodasCopiasCIUO(ci.getId(), this.pegarUOUsuarioLogado());
        String retorno = "/listar/arquivadas.xhtml";

        return retorno;
    }

    public void viewCriarDespacho(Long idCI) {
        System.out.println("Id passado: " + idCI);
        this.ci.setId(idCI);
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("width", 1100);
        options.put("contentWidth", 1050);
        options.put("height", 750);
        options.put("contentHeight", 720);

        RequestContext.getCurrentInstance().openDialog("/criar/novodespacho", options, null);
    }

    public String novoDespacho() {
        String resultado = "";
        try {
            this.ci = ComunicacaoInternaDAO.getInstance().selectFromId(ci.getId());
            resultado = "/criar/novodespacho?faces-redirect=true&amp;includeViewParams=true";
        } catch (Exception ex) {
            Logger.getLogger(ComunicacaoInternaListarMB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultado;
    }

    public String novoArquivamento() {

        String resultado = "";
        try {
            
            this.ci = ComunicacaoInternaDAO.getInstance().selectFromId(ci.getId());
            long uoLogado = this.pegarUOUsuarioLogado();
            System.out.println("Arquivamento: usuario logado id: "+ uoLogado);
            if (this.ci.getUltimoDespacho()!= null && this.ci.getUltimoDespacho().getPara() != null && this.ci.getUltimoDespacho().getPara().getId() == uoLogado) {
                resultado = "/criar/novoarquivamento?faces-redirect=true&amp;includeViewParams=true";

            } else {
                Despacho ultimoDespacho = this.ci.ultimoDespachoUO(uoLogado);
                if(ultimoDespacho != null){
                    DespachoCopiaDAO.getInstance().arquivarTodasCopiasDespachoUO(ultimoDespacho.getId(), uoLogado);
                }else{ //buscar todas as copias que foram enviadas de uma determinada CI!
                    DespachoCopiaDAO.getInstance().arquivarTodasCopiasCIUO(this.ci.getId(), uoLogado);
                }
                
                return "/listar/arquivadas?faces-redirect=true";
            }

        } catch (Exception ex) {
            Logger.getLogger(ComunicacaoInternaListarMB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultado;
    }

    public boolean isCircularParaTodos() {
        return circularParaTodos;
    }

    public void setCircularParaTodos(boolean circularParaTodos) {
        this.circularParaTodos = circularParaTodos;
    }

    public UnidadeOrganizacional getPara() {
        return para;
    }
    /*
    public void gerarPDFCI(){
        Document document = new Document();
        String basePath = "C:" + File.separator + "temp" + File.separator;
        System.out.println("BasePath: " + basePath);
        
        try {
            PdfWriter.getInstance(document,
                new FileOutputStream("HelloWorld.pdf"));

            document.open();
            document.add(new Paragraph("A Hello World PDF document."));
            document.close(); // no need to close PDFwriter?

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    */
    

}
