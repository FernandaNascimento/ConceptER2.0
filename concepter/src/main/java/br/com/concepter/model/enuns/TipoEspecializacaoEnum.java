/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.concepter.model.enuns;


public enum TipoEspecializacaoEnum {
    ESPECIALIZACAO(""), DISJUNCAO("Disjunção"), SOBREPOSICAO("Sobreposição");
	
	private String name;

	private TipoEspecializacaoEnum(String name) {
		this.name = name;
	}
	
    public String toString(){
        return name;
    }

    public static String getEnumByString(String code){
        for(TipoEspecializacaoEnum e : TipoEspecializacaoEnum.values()){
            if(code == e.name) return e.name();
        }
        return null;
    }

}
