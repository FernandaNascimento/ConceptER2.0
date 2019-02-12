package br.com.concepter.model.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

import br.com.concepter.model.enuns.TipoAtributoEnum;
import br.com.concepter.view.AreaGrafica;


public class Atributo {


    private String id;
    
    @XmlAttribute    @XmlID               // should be unique across all entities.<br>    private String uuid;   
    public String getUuid() {
        return id; 
    }
 
    public void setUuid(UUID uuid) {
        this.id = uuid.toString();
    }
    
    private String nome;
    private TipoAtributoEnum tipoAtributo;
    
    
    private List<Atributo> atributos = new ArrayList<>();
    
    @XmlTransient
    private mxGraph grafico; 
    
    @XmlTransient
    private mxCell forma;
    
    private double pX;
    private double pY;
    private int tamanhoLargura;
    private int tamanhoAltura;
    
    public Atributo() {
    }

    public Atributo(mxGraph grafico,  TipoAtributoEnum tipoAtributo, String nome, double pX, double pY){
            this.nome = nome + AreaGrafica.getCont_atributo();
            this.grafico = grafico;
            this.pX = pX;
            this.pY = pY;
            this.tamanhoLargura = 100;
            this.tamanhoAltura = 25;
            this.tipoAtributo = tipoAtributo;
    } 
    
    public void add(Object objeto){
        String caracteristicas = "";
       
        if(tipoAtributo == TipoAtributoEnum.SIMPLES){
            caracteristicas = "fillColor=white;shape=ellipse;";
        }
        if(tipoAtributo == TipoAtributoEnum.CHAVE){
            caracteristicas = "fillColor=white;shape=ellipse;"+mxConstants.STYLE_FONTSTYLE+"="+mxConstants.FONT_UNDERLINE;
        }
        if(tipoAtributo == TipoAtributoEnum.COMPOSTO){
            caracteristicas = "fillColor=white;shape=ellipse;";
        }
        if(tipoAtributo == TipoAtributoEnum.MULTIVALORADO){
            caracteristicas = "fillColor=white;shape=doubleEllipse;";
        }
        if(tipoAtributo == TipoAtributoEnum.DERIVADO){
            caracteristicas = "fillColor=white;shape=ellipse;dashed=true;";
        }

        this.grafico.getModel().beginUpdate();
        Object atributo = null;
        Object atributo_1 = null;
        Object atributo_2 = null;
        Object parent = this.grafico.getDefaultParent();
        try{
            
            double posx = ((mxCell) objeto).getGeometry().getX();
            double posy = ((mxCell) objeto).getGeometry().getY();

            if(((posy-50)<=0) && (tipoAtributo == TipoAtributoEnum.SIMPLES)) {
            	posy = posy+80;
            } else if (((posy-130)<=0) && (tipoAtributo == TipoAtributoEnum.COMPOSTO)) {
            	posy = posy+80;
            }else {
            	posy = posy-50;
            }
            if(AreaGrafica.getMapaGraficoEntidades().containsKey(Integer.valueOf(((mxCell) objeto).getId()))) {
            	int size = AreaGrafica.getMapaGraficoEntidades().get(Integer.valueOf(((mxCell) objeto).getId())).getAtributos().size();
            	for(int i = 0; i<size; i++) {
            		Atributo at = AreaGrafica.getMapaGraficoEntidades().get(Integer.valueOf(((mxCell) objeto).getId())).getAtributos().get(i);
            		if(this.id!=at.getId() && posx==at.pX ) {
            			posx = posx + 150;
            		}
            		
            	}
            }
            
            
            atributo = this.grafico.insertVertex(parent, null, this.nome, posx, posy, this.tamanhoLargura, this.tamanhoAltura, caracteristicas);
            
            this.grafico.insertEdge(parent, null, null, atributo, objeto,"startArrow=none;endArrow=none;");

            if(tipoAtributo == TipoAtributoEnum.COMPOSTO){
            	AreaGrafica.setCont_atributo();
                double px1,px2,py;
                px2 = this.pX + 15;
                if((this.pX - 95)<=0) {
                	px1 = px2 + 110;
                } else {
                	px1 = this.pX - 95;
                }
                /*if((this.pX + 15)<=0) {
                	px2 = this.pX + 15;
                } else {
                	px2 = this.pX + 15;
                }*/
                if((this.pY-130)<=0) {
                	py = posy + 50;
                } else {
                	py = this.pY-130;
                }
                atributo_1 = this.grafico.insertVertex(parent, null, "Atributo" +  AreaGrafica.getCont_atributo(), px1, py, this.tamanhoLargura, this.tamanhoAltura, "fillColor=white;shape=ellipse;rounded=true;");
                Atributo atr_1 = new Atributo(this.grafico,  TipoAtributoEnum.SIMPLES, "Atributo" + AreaGrafica.getCont_atributo(), this.pX, this.pY);
                atr_1.setForma((mxCell) atributo_1);
                this.atributos.add(atr_1);
                
                AreaGrafica.getMapaGraficoAtributos().put( Integer.valueOf( ((mxCell) atributo_1).getId() ), atr_1 );
                
                AreaGrafica.setCont_atributo();
                atributo_2 = this.grafico.insertVertex(parent, null, "Atributo" +  AreaGrafica.getCont_atributo(), px2, py, this.tamanhoLargura, this.tamanhoAltura, "fillColor=white;shape=ellipse;rounded=true;");
                Atributo atr_2 = new Atributo(this.grafico,  TipoAtributoEnum.SIMPLES, "Atributo" +  AreaGrafica.getCont_atributo(), this.pX, this.pY);
                atr_2.setForma((mxCell) atributo_2);
                this.atributos.add(atr_2);
                AreaGrafica.getMapaGraficoAtributos().put( Integer.valueOf( ((mxCell) atributo_2).getId() ), atr_2 );
                
                this.grafico.insertEdge(parent, null, null, atributo_1, atributo,"startArrow=none;endArrow=none;");
                this.grafico.insertEdge(parent, null, null, atributo_2, atributo,"startArrow=none;endArrow=none;");
            }
        }catch (Exception e) {
			e.printStackTrace();
		}
        finally{
            this.id = ((mxCell) atributo).getId();             
            this.forma = (mxCell) atributo;
            AreaGrafica.getMapaGraficoAtributos().put( Integer.valueOf( ((mxCell) atributo).getId() ), this );
            this.grafico.getModel().endUpdate();
            this.grafico.refresh();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
  
    public TipoAtributoEnum getTipoAtributo() {
        return tipoAtributo;
    }

    public void setTipoAtributo(TipoAtributoEnum tipoAtributo) {
        this.tipoAtributo = tipoAtributo;
    }

    @XmlTransient
    public mxGraph getGrafico() {
        return grafico;
    }
    public void setGrafico(mxGraph grafico) {
        this.grafico = grafico;
    }
    @XmlTransient
    public mxCell getForma() {
        return forma;
    }
    public void setForma(mxCell forma) {
        this.forma = forma;
    }
    public double getpX() {
        return pX;
    }
    public void setpX(double pX) {
        this.pX = pX;
    }
    public double getpY() {
        return pY;
    }
    public void setpY(double pY) {
        this.pY = pY;
    }
    public int getTamanhoLargura() {
        return tamanhoLargura;
    }
    public void setTamanhoLargura(int tamanhoLargura) {
        this.tamanhoLargura = tamanhoLargura;
    }
    public int getTamanhoAltura() {
        return tamanhoAltura;
    }
    public void setTamanhoAltura(int tamanhoAltura) {
        this.tamanhoAltura = tamanhoAltura;
    }
	public List<Atributo> getAtributos() {
		return atributos;
	}

	public void setAtributos(List<Atributo> atributos) {
		this.atributos = atributos;
	}

	
    
}
