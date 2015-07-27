/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import DAO.ComunicacaoInternaDAO;
import Models.ComunicacaoInterna;
import Models.DespachoCopia;
import Models.UnidadeOrganizacional;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author jorgefspf
 */
public class ImprimirCIPDF extends RelatorioTransalvadorPDF {

    ComunicacaoInterna ci;

    public ImprimirCIPDF(ComunicacaoInterna ci) throws Exception {
        this.ci = ComunicacaoInternaDAO.getInstance().selectFromId(ci.getId());
        super.nomeArquivo = "CI" + ci.getNumeroCI() + ".pdf";
        super.assinaturaCI = this.ci.getAssinatura();
    }

    @Override
    protected void gerarConteudo() throws Exception {
        
        ci = ComunicacaoInternaDAO.getInstance().selectFromId(ci.getId());

        Paragraph paragrafo = new Paragraph(ci.getTipoCI() + " - " + ci.getNumeroCI(), fonteH1);
        paragrafo.setAlignment(Element.ALIGN_CENTER);
        paragrafo.setSpacingBefore(10f);
        documento.add(paragrafo);
        documento.add(Chunk.NEWLINE);
        documento.add(Chunk.NEWLINE);

        //1- DADOS DO ATENDIMENTO
        //documento.add(new Phrase("De: " + ci.getUo().getNome() + "                     Número: " + ci.getNumeroCI(), fonteB));
        documento.add(new Phrase("De: " + ci.getUo().getNome(),  fonteB));
        documento.add(Chunk.NEWLINE);

        //Tipo Atendimento:
        if (ci.getDespachoList().get(0).getPara() != null) {
            documento.add(new Phrase("Para: " + ci.getDespachoList().get(0).getPara().getNome(), fonteB));
            documento.add(Chunk.NEWLINE);
        }

        if (!ci.getDespachoList().get(0).getDespachoCopiaList().isEmpty()) {
            StringBuffer strBuffer = new StringBuffer();
            for (DespachoCopia dc : ci.getDespachoList().get(0).getDespachoCopiaList()) {
                strBuffer.append(TratamentoString.tratarEspacosEmBranco(dc.getUnidadeOrganizacional().getNome()));strBuffer.append(", ");

            }
            documento.add(new Phrase("CC: " + strBuffer.toString(), fonteB));
            documento.add(Chunk.NEWLINE);

        }
        documento.add(Chunk.NEWLINE);

        documento.add(new Phrase("Assunto: " + ci.getAssunto(), fonteB));
        documento.add(Chunk.NEWLINE);
        documento.add(new Phrase("        ", fonte));
        documento.add(Chunk.NEWLINE);
        
        gerarLinhaHR();
        
        documento.add(Chunk.NEWLINE);

        documento.add(Chunk.NEWLINE);

        StringReader strReader = new StringReader(ci.getDespachoList().get(0).getConteudo());
        ArrayList p = new ArrayList();
        p = HTMLWorker.parseToList(strReader, null);

        for (int i = 0; i < p.size(); i++) {

            documento.add(((Element) p.get(i)));
        }

        documento.add(Chunk.NEWLINE);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        documento.add(new Phrase("Em: " + formatter.format(ci.getData()), fonteB));
        

        if (ci.getDespachoList().size() > 1) {
            documento.add(Chunk.NEXTPAGE);
            documento.add(Chunk.NEWLINE);
            this.gerarCabecalho();
            documento.add(Chunk.NEWLINE);
            Paragraph despachos = new Paragraph("Despachos", fonteH1);
            despachos.setAlignment(Element.ALIGN_CENTER);
            despachos.setSpacingBefore(10f);
            documento.add(despachos);
            documento.add(Chunk.NEWLINE);
            for (int i = 1; i < ci.getDespachoList().size(); i++) {
                gerarLinhaHR();
                documento.add(Chunk.NEWLINE);
                documento.add(new Phrase(TratamentoString.tratarEspacosEmBranco(ci.getDespachoList().get(i - 1).getPara().getNome()) + " " + TratamentoString.tratarEspacosEmBranco(ci.getDespachoList().get(i).getStatusString()), fonteB));
                documento.add(Chunk.NEWLINE);
                documento.add(new Phrase("Observação: ", fonteB));
                strReader = new StringReader(ci.getDespachoList().get(i).getConteudo());
                p = HTMLWorker.parseToList(strReader, null);
                for (int j = 0; j < p.size(); j++) {

                    documento.add(((Element) p.get(j)));
                }
                gerarLinhaHR();
                documento.add(Chunk.NEWLINE);

            }
        }

        
    }

    public Paragraph htmlToParagraph(String texto) throws IOException {
        StringReader strReader = new StringReader(texto);
        ArrayList p = new ArrayList();
        p = HTMLWorker.parseToList(strReader, null);
        Paragraph paragrafo = new Paragraph();
        for (int i = 0; i < p.size(); i++) {

            paragrafo.add((Element) p.get(i));
        }
        paragrafo.setFont(fonte);
        return paragrafo;

    }

}
