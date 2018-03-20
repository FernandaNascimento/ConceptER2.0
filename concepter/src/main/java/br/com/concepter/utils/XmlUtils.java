/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.concepter.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.mxgraph.model.mxCell;

import br.com.concepter.model.beans.Entidade;
import br.com.concepter.model.beans.SavedObject;
import br.com.concepter.view.AreaGrafica;


public class XmlUtils {
    
    public void save(SavedObject savedObject, FileOutputStream os) throws FileNotFoundException{
        try {            
            
            JAXBContext jaxbContext = JAXBContext.newInstance(SavedObject.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(savedObject, os );
            
	} catch (JAXBException e) {
            e.printStackTrace();
	}
    }

    public void load(String fileName, AreaGrafica areaGrafica){
        try {
            File file = new File(fileName);
            JAXBContext jaxbContext = JAXBContext.newInstance(SavedObject.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            SavedObject savedObject = (SavedObject)jaxbUnmarshaller.unmarshal(file);
            areaGrafica.setCont_atributo(savedObject.getCont_atributo());
            areaGrafica.setCont_entidade(savedObject.getCont_entidade());
            areaGrafica.setCont_relacionamento(savedObject.getCont_relacionamento());
            areaGrafica.setMapaGraficoAgregacao(savedObject.getMapaGraficoAgregacao());
            areaGrafica.setMapaGraficoAtributos(savedObject.getMapaGraficoAtributos());
            areaGrafica.setMapaGraficoEntidades(savedObject.getMapaGraficoEntidades());
            areaGrafica.setMapaGraficoEspecializacao(savedObject.getMapaGraficoEspecializacao());
            areaGrafica.setMapaGraficoRelacionamentos(savedObject.getMapaGraficoRelacionamentos());
            FileUtils.loadGraph(areaGrafica.getAreaGrafico(), savedObject.getGraph());
            
            
            areaGrafica.getAreaGrafico().getGraph().clearSelection();
            areaGrafica.getAreaGrafico().getGraph().selectAll();
            Object[] cells = areaGrafica.getAreaGrafico().getGraph().getSelectionCells();
            for (Object object : cells) {
                mxCell cell = (mxCell) object;
                if (areaGrafica.getMapaGraficoEntidades().containsKey(Integer.valueOf(cell.getId()))) {
                	areaGrafica.getMapaGraficoEntidades().get(Integer.valueOf(cell.getId())).setCell(cell);
                	areaGrafica.getMapaGraficoEntidades().get(Integer.valueOf(cell.getId())).setGrafico(areaGrafica.getAreaGrafico().getGraph());
                } else if(areaGrafica.getMapaGraficoAtributos().containsKey(Integer.valueOf(cell.getId()))) {
                	//areaGrafica.getMapaGraficoAtributos().get(Integer.valueOf(cell.getId())).setCell(cell);
                } else if (areaGrafica.getMapaGraficoRelacionamentos().containsKey(Integer.valueOf(cell.getId()))) {
                	areaGrafica.getMapaGraficoRelacionamentos().get(Integer.valueOf(cell.getId())).setCell(cell);
                	areaGrafica.getMapaGraficoRelacionamentos().get(Integer.valueOf(cell.getId())).setGrafico(areaGrafica.getAreaGrafico().getGraph());
                	
                }
                
            }
            areaGrafica.getAreaGrafico().getGraph().clearSelection();
            
		} catch (JAXBException e) {
	            e.printStackTrace();
		}
		//return areaGrafica;

    }
 
}
