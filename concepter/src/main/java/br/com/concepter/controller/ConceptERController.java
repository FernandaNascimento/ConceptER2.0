package br.com.concepter.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

import org.apache.commons.lang3.StringUtils;

import br.com.concepter.model.beans.Entidade;
import br.com.concepter.utils.FileUtils;
import br.com.concepter.utils.XmlUtils;
import br.com.concepter.view.AreaGrafica;
import br.com.concepter.view.TelaPrincipal;

public class ConceptERController {
	
	public boolean save(AreaGrafica areaGrafica, String pathFile) {

		FileUtils fileUtils = new FileUtils();
		XmlUtils xmlUtils = new XmlUtils();
		
		
		if(StringUtils.isNotEmpty(pathFile)) {
			fileUtils.saveGraph(areaGrafica.getAreaGrafico(), pathFile);
			
			List<Entidade> entidades = new ArrayList<Entidade>();
			
			for (Integer key : areaGrafica.getMapaGraficoEntidades().keySet()) {
				
				Entidade entidade = areaGrafica.getMapaGraficoEntidades().get(key);
				
				entidades.add(entidade);           
			} 
			
			try {
				FileOutputStream os = new FileOutputStream(pathFile+ ".xml");
				xmlUtils.save( entidades, os);
				return true;
			} catch (FileNotFoundException ex) {
				Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
			}			
		}

		
		return false;
	}

}
