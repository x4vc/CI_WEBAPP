/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import com.itextpdf.text.html.simpleparser.HTMLWorker;

/**
 *
 * @author joaoppvn
 */
public abstract class RelatorioTransalvadorPDF {
    protected SimpleDateFormat formatar;
    protected BaseFont base;
    protected Font fonte;
    protected Font fonteB;
    protected Font fonteH1;
    protected ExternalContext context;
    protected OutputStream outputStream;
    protected String diretorio;
    protected Document documento;
    protected PdfWriter writer;
    protected Image img;
    protected HttpServletResponse response;
    protected String nomeArquivo = "relatorio.pdf";
    protected String assinaturaCI;
    
    protected SimpleDateFormat sdfSql = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected SimpleDateFormat sdfBr = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    protected DecimalFormat decimalFmt = new DecimalFormat("0.00"); 
    
    
    public RelatorioTransalvadorPDF() throws Exception {
        inicializar();
    }
    public RelatorioTransalvadorPDF(String assinaturaCI) throws Exception {
        inicializar();
    }
    
    protected void inicializar() throws Exception {
        //Criando os objetos comuns
        formatar = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
        base = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
        fonte = new Font(base, 10f, Font.NORMAL);
        fonteB = FontFactory.getFont("Arial", 10f, Font.BOLD);
        fonteH1 = FontFactory.getFont("Arial", 12f, Font.BOLD);
        context = FacesContext.getCurrentInstance().getExternalContext();  
        response =  (HttpServletResponse)context.getResponse();
        outputStream = response.getOutputStream();
        diretorio = context.getRealPath("/images") + "/";
        
        
        //Criando o Documento
        documento = new Document();
        writer = PdfWriter.getInstance(documento, outputStream);
        documento.setPageSize(PageSize.A4);
        HeaderFooter footer = new HeaderFooter(new Phrase("Assinatura digital: " + this.assinaturaCI + 
                "                                                                                               Página ", fonte), true);
        //HeaderFooter footer = new HeaderFooter(new Phrase(this.assinaturaCI+ "                     Página ", fonte), true);
        documento.setFooter(footer);
        
        documento.open();
    }
    
    protected void gerarCabecalho() throws Exception {
        //Topo com Imagem e Texto da PMS
        //img = Image.getInstance(diretorio +"logo_pms_preto.png");
        img = Image.getInstance(diretorio +"logo_transalvador_preto.png");
        img.setAbsolutePosition(36f,757f);
        img.scaleToFit(100, 50);
        documento.add(img);
        
        //img = Image.getInstance(diretorio +"logo_transalvador_preto.png");
        img = Image.getInstance(diretorio +"logo_pms_preto.png");
        img.setAbsolutePosition(420f,768f);
        img.scaleToFit(130, 31);
        documento.add(img);
        Paragraph paragrafo = new Paragraph("", fonte);
        paragrafo.setSpacingBefore(50f);
        documento.add(paragrafo);
    }
    
    protected abstract void gerarConteudo() throws Exception;
    
    protected void finalizarPDF() throws Exception {
        response.setHeader("Content-Disposition","inline; filename=\""+nomeArquivo+"\"");
        response.setContentType("application/pdf");
        outputStream.flush();
        documento.close();
        outputStream.close(); 
        writer.close();
        FacesContext.getCurrentInstance().responseComplete();
    }
    
    /**
     * Template method para emitir o pdf
     */
    public void gerarRelatorioPDF() throws Exception {
        inicializar();
        gerarCabecalho();
        gerarConteudo();
        finalizarPDF();
    }
    
    public void gerarLinhaHR() throws Exception {
        documento.add(new Phrase("_______________________________"
                + "______________________________________________"
                + "______________________________________________"
                + "______________________________________________"
                + "________", new Font(base, 5f, Font.NORMAL)));
        documento.add(Chunk.NEWLINE);
    }
}
