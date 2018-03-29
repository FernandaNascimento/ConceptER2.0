/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.concepter.model.beans;

import br.com.concepter.model.enuns.TipoObrigatoriedadeEnum;


public class Relacao {
    
    private String cardinalidade;
    private TipoObrigatoriedadeEnum obrigatoriedade;

    public Relacao() {
    }
    
    public Relacao(String cardinalidade, TipoObrigatoriedadeEnum obrigatoriedade) {
        this.cardinalidade = cardinalidade;
        this.obrigatoriedade = obrigatoriedade;
    }
    
    public String getCardinalidade() {
        return cardinalidade;
    }

    public void setCardinalidade(String cardinalidade) {
        this.cardinalidade = cardinalidade;
    }
  
    public TipoObrigatoriedadeEnum getObrigatoriedade() {
        return obrigatoriedade;
    }

    public void setObrigatoriedade(TipoObrigatoriedadeEnum obrigatoriedade) {
        this.obrigatoriedade = obrigatoriedade;
    }

    
}
