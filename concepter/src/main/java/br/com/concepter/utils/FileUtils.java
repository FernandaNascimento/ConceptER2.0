/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.concepter.utils;

import org.w3c.dom.Document;

import com.mxgraph.io.mxCodec;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import java.awt.HeadlessException;
import java.io.IOException;
/**
 *
 * @author AllanMagnum
 */
public class FileUtils {

    public static void loadGraph( mxGraphComponent graphComponent, String fileName) {
        try {
            mxGraph graph = graphComponent.getGraph();
            Document document = mxXmlUtils.parseXml( mxUtils.readFile( fileName));
            mxCodec codec = new mxCodec( document);
            codec.decode( document.getDocumentElement(), graph.getModel());
        } catch( Exception ex) {
            throw new RuntimeException( ex);
        }
    }

    public static void saveGraph( mxGraphComponent graphComponent, String fileName) {
        try {
            mxGraph graph = graphComponent.getGraph();
            mxCodec codec = new mxCodec();
            String xml = mxXmlUtils.getXml(codec.encode(graph.getModel()));
            mxUtils.writeFile(xml, fileName);
        } catch( IOException | HeadlessException ex) {
            throw new RuntimeException( ex);
        }
    }

}
