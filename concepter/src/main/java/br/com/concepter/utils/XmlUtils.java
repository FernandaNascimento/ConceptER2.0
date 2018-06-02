/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.concepter.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.mxgraph.model.mxCell;

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
            AreaGrafica.setCont_atributo(savedObject.getCont_atributo());
            AreaGrafica.setCont_entidade(savedObject.getCont_entidade());
            AreaGrafica.setCont_relacionamento(savedObject.getCont_relacionamento());
            AreaGrafica.setMapaGraficoAgregacao(savedObject.getMapaGraficoAgregacao());
            AreaGrafica.setMapaGraficoAtributos(savedObject.getMapaGraficoAtributos());
            AreaGrafica.setMapaGraficoEntidades(savedObject.getMapaGraficoEntidades());
            AreaGrafica.setMapaGraficoEspecializacao(savedObject.getMapaGraficoEspecializacao());
            AreaGrafica.setMapaGraficoRelacionamentos(savedObject.getMapaGraficoRelacionamentos());
            FileUtils.loadGraph(areaGrafica.getAreaGrafico(), savedObject.getGraph());
            
            
            areaGrafica.getAreaGrafico().getGraph().clearSelection();
            areaGrafica.getAreaGrafico().getGraph().selectAll();
            Object[] cells = areaGrafica.getAreaGrafico().getGraph().getSelectionCells();
            for (Object object : cells) {
                mxCell cell = (mxCell) object;
                if (AreaGrafica.getMapaGraficoEntidades().containsKey(Integer.valueOf(cell.getId()))) {
                	AreaGrafica.getMapaGraficoEntidades().get(Integer.valueOf(cell.getId())).setCell(cell);
                	AreaGrafica.getMapaGraficoEntidades().get(Integer.valueOf(cell.getId())).setGrafico(areaGrafica.getAreaGrafico().getGraph());
                } else if(AreaGrafica.getMapaGraficoAtributos().containsKey(Integer.valueOf(cell.getId()))) {
                	AreaGrafica.getMapaGraficoAtributos().get(Integer.valueOf(cell.getId())).setForma(cell);
                	AreaGrafica.getMapaGraficoAtributos().get(Integer.valueOf(cell.getId())).setGrafico(areaGrafica.getAreaGrafico().getGraph());
                } else if (AreaGrafica.getMapaGraficoRelacionamentos().containsKey(Integer.valueOf(cell.getId()))) {
                	AreaGrafica.getMapaGraficoRelacionamentos().get(Integer.valueOf(cell.getId())).setCell(cell);
                	AreaGrafica.getMapaGraficoRelacionamentos().get(Integer.valueOf(cell.getId())).setGrafico(areaGrafica.getAreaGrafico().getGraph());	
                } else if (AreaGrafica.getMapaGraficoAgregacao().containsKey(Integer.valueOf(cell.getId()))) {
                	AreaGrafica.getMapaGraficoAgregacao().get(Integer.valueOf(cell.getId())).setCell(cell);
                	AreaGrafica.getMapaGraficoAgregacao().get(Integer.valueOf(cell.getId())).setGrafico(areaGrafica.getAreaGrafico().getGraph());
                }
                
            }
            areaGrafica.getAreaGrafico().getGraph().clearSelection();
            
		} catch (JAXBException e) {
	            e.printStackTrace();
		}
		//return areaGrafica;

    }
 
}
