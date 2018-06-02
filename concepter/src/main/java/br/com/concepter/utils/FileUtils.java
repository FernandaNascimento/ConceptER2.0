/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.concepter.utils;

import java.awt.HeadlessException;

import org.w3c.dom.Document;

import com.mxgraph.io.mxCodec;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;

public class FileUtils {

    public static void loadGraph( mxGraphComponent graphComponent, String graphString) {
        try {
            mxGraph graph = graphComponent.getGraph();
            //System.out.println(mxUtils.readFile( fileName));
            Document document = mxXmlUtils.parseXml(graphString);
            mxCodec codec = new mxCodec( document);
            codec.decode( document.getDocumentElement(), graph.getModel());
        } catch( Exception ex) {
            throw new RuntimeException( ex);
        }
    }

    public static String saveGraph( mxGraphComponent graphComponent, String fileName) {
        try {
            mxGraph graph = graphComponent.getGraph();
            mxCodec codec = new mxCodec();
            return mxXmlUtils.getXml(codec.encode(graph.getModel()));
            //mxUtils.writeFile(xml, fileName);
        } catch( HeadlessException ex) {
            throw new RuntimeException( ex);
        }
    }

}
