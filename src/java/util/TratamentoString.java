/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author jorgefspf
 */
public class TratamentoString {

    public static final String VAZIO = "---";
    public static final int UPPER = 1;
    public static final int FIRST_UPPER = 2;
    public static final int EACH_FIRST_UPPER = 3;
    public static final int LOWER = 4;

    /**
     *
     * @param entrada String - String para tratamento
     * @param replaceVazio - Texto para substituir a string caso a mesma esteja
     * vazia, ex: "---"
     * @return String - A própria string ou replaceVazio caso a mesma esteja
     * vazia.
     */
    public static String tratarEspacosEmBranco(String entrada){
        String saida = entrada.replaceAll("  ", ""); 
        return saida;
    }
    
    public static String tratarVazio(String entrada, String replaceVazio) {
        if (entrada != null && !entrada.isEmpty()) {
            return entrada;
        } else {
            return replaceVazio == null ? VAZIO : replaceVazio;
        }
    }

    /**
     *
     * @param entrada String - String para tratamento
     * @param limite int - tamanho máximo permitido
     * @param trim String - para acrescentar no final, ex: "..."
     * @return String - String limitada.
     */
    public static String limitarTam(String entrada, int limite, String trim) {
        if (trim == null) {
            trim = "...";
        }
        if (entrada.length() > limite) {
            return entrada.substring(0, limite) + trim;
        } else {
            return entrada;
        }
    }

    /**
     * 
     * @param entrada String - String para tratamento
     * @param modoUpper int - Constante indicadora do modo de capitalização a ser aplicado, ex: TratamentoString.UPPER
     * @return 
     */
    public static String trataUpper(String entrada, int modoUpper) {
        //em princípio assume minúsculo.
        entrada = entrada.toLowerCase();
        switch (modoUpper) {
            case UPPER:
                return entrada.toUpperCase();

            case FIRST_UPPER:
                return entrada.substring(0,1).toUpperCase() + entrada.substring(1).toLowerCase();
                
            case EACH_FIRST_UPPER:
                final StringBuilder result = new StringBuilder(entrada.length());
                String[] words = entrada.split("\\s");
                for (int i = 0, l = words.length; i < l; ++i) {
                    if (i > 0) {
                        result.append(" ");
                    }
                    result.append(Character.toUpperCase(words[i].charAt(0))).append(words[i].substring(1));
                }
                return result.toString();

            case LOWER:
                return entrada;

            default:
                return entrada;
        }
    }
    
    /**
     * 
     * @param entrada String - String a ser tratado
     * @return String - String tratada caso vazia e capitalizada para UPPER.
     */
    public static String tratarString(String entrada) {
        String resultado = tratarVazio(entrada, null);
        resultado = trataUpper(resultado, UPPER);
        
        return resultado;
    }
    
}
