/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author tulioasc
 */
@ManagedBean
@ApplicationScoped
public class Parametros {
    
    private final String nomeAplicacao = "ci";
    private final String aplicacao = "Comunicação Interna";    
    private final String logo = "/ci/images/logo_transalvador_transparente.png";
    private final String logoPMS = "/ci/images/logo_pms_transparente.png";
    private final String logoPreenchida = "/ci/images/logo_transalvador_preto.png";
    private final String logoPMSPreenchida = "/ci/images/logo_pms_preto.png";
    private final String rodape = "Todos os direitos reservados TRANSALVADOR - Superintendência de Trânsito do Salvador\n"; 
    private final String endereco = "Av. Vale dos Barris, nº 501 - Barris - CEP: 40070-055"; 
    private final String versão = "Versão: 1.0";
    private final String logoAreaADM = "/ci/images/adm.png";

    public String getAplicacao() {
        return aplicacao;
    }

    public String getLogo() {
        return logo;
    }

    public String getRodape() {
        return rodape;
    }

    public String getVersão() {
        return versão;
    }

    public String getLogoPMS() {
        return logoPMS;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getLogoPreenchida() {
        return logoPreenchida;
    }

    public String getLogoPMSPreenchida() {
        return logoPMSPreenchida;
    }

    public String getLogoAreaADM() {
        return logoAreaADM;
    }

    public String getNomeAplicacao() {
        return nomeAplicacao;
    }
    
    
    
    
    
}


