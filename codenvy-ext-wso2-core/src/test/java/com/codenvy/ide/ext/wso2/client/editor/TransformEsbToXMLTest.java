/*
 * CODENVY CONFIDENTIAL
 * __________________
 * 
 * [2012] - [2013] Codenvy, S.A. 
 * All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.ext.wso2.client.editor;



import org.junit.Test;
import org.wso2.developerstudio.eclipse.gmf.esb.EsbFactory;
import org.wso2.developerstudio.eclipse.gmf.esb.EsbLink;
import org.wso2.developerstudio.eclipse.gmf.esb.EsbSequence;
import org.wso2.developerstudio.eclipse.gmf.esb.FilterElseBranch;
import org.wso2.developerstudio.eclipse.gmf.esb.FilterMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.FilterThenBranch;
import org.wso2.developerstudio.eclipse.gmf.esb.LogMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.NamespacedProperty;
import org.wso2.developerstudio.eclipse.gmf.esb.PropertyMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.Sequence;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;

/**
 * Unit for transformation a ESB EMF model into XML (http://ws.apache.org/ns/synapse)
 * @author Thomas Legrand
 */
public class TransformEsbToXMLTest extends GWTTestCase {

    public TransformEsbToXMLTest()
    {
        super();
    }
    
    /**
     * Returns the module name for GWT unit test running.
     */
    @Override
    public String getModuleName() {
        return "com.codenvy.ide.ext.wso2.WSO2";
    }

    /**
     * The main test
     */
    @Test
    public void testTransformModelToXml()
    {
        // Create an ESB sequence (EMF model conforming esb.ecore)
        EsbSequence sequence = createEsbSequence();

        // Parse the sequence to render an XML document
        Document document = parseSequence(sequence);

        System.out.println(document.toString());
    }

    /**
     * takes a (model) sequence and returns an XML document whose contents conform esb.xsd
     * 
     * @param sequence
     * @return
     */
    private Document parseSequence(EsbSequence sequence)
    {
        Document document = XMLParser.createDocument();

        assertNotNull(sequence);

        try {            
            sequence.save(document);            
            
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return document;
    }

    /**
     * Create a basic sequence
     * 
     * @return
     */
    private EsbSequence createEsbSequence()
    {
        EsbSequence sequence = EsbFactory.eINSTANCE.createEsbSequence();
        sequence.setName("sequence 1");

        LogMediator logMediator = EsbFactory.eINSTANCE.createLogMediator();
        logMediator.setDescription("log 1");
        
        PropertyMediator propertyMediator = EsbFactory.eINSTANCE.createPropertyMediator();
        propertyMediator.setDescription("property 1");

        FilterMediator filterMediator = EsbFactory.eINSTANCE.createFilterMediator();
        FilterThenBranch filterThenBranch = EsbFactory.eINSTANCE.createFilterThenBranch();
        FilterElseBranch filterElseBranch = EsbFactory.eINSTANCE.createFilterElseBranch();
        filterMediator.setThenBranch(filterThenBranch);
        filterMediator.setElseBranch(filterElseBranch);        
        filterMediator.setDescription("filter 1");
        filterMediator.setRegex("default_regex");
        NamespacedProperty namespacedProperty = EsbFactory.eINSTANCE.createNamespacedProperty();
        namespacedProperty.setPropertyName("property");
        namespacedProperty.setPropertyValue("value");
        filterMediator.setSource(namespacedProperty);

        EsbLink linkLogToProperty = EsbFactory.eINSTANCE.createEsbLink();
        linkLogToProperty.setSource(logMediator.getOutputConnector());
        linkLogToProperty.setTarget(propertyMediator.getInputConnector());

        EsbLink linkPropertyToFilter = EsbFactory.eINSTANCE.createEsbLink();
        linkPropertyToFilter.setSource(propertyMediator.getOutputConnector());
        linkPropertyToFilter.setTarget(filterMediator.getInputConnector());

        sequence.getChildMediators().add(logMediator);
        sequence.getChildMediators().add(propertyMediator);
        sequence.getChildMediators().add(filterMediator);
  
        return sequence;
    }

}
