/*
 * CODENVY CONFIDENTIAL
 * __________________
 * 
 * [2012] - [2014] Codenvy, S.A. 
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

/**
 * @author Andrey Plotnikov
 */

import org.eclipse.emf.ecore.util.GMMUtil;
import org.wso2.developerstudio.eclipse.gmf.esb.EsbFactory;
import org.wso2.developerstudio.eclipse.gmf.esb.EsbSequence;

import com.codenvy.ide.api.editor.AbstractEditorPresenter;
import com.codenvy.ide.ext.wso2.client.WSO2Resources;
import com.genmymodel.ecoreonline.graphic.Diagram;
import com.genmymodel.ecoreonline.graphic.GraphicFactory;
import com.genmymodel.ecoreonline.graphic.Plane;
import com.genmymodel.ecoreonline.graphic.util.GraphicUtil;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import esbdiag.EsbdiagFactory;
import esbdiag.properties.LogMediatorProperties;

public class GraphicEditor extends AbstractEditorPresenter {

    private WSO2Resources     wso2Resources;
    private GraphicEditorView view;
    
    private LogMediatorProperties logProperties;

    @Inject
    public GraphicEditor(WSO2Resources wso2Resources, LogMediatorProperties logProperties) {
        this.wso2Resources = wso2Resources;
        this.logProperties = logProperties;
    }

    /** {@inheritDoc} */
    @Override
    protected void initializeEditor() {

        final EsbSequence newModel = EsbFactory.eINSTANCE.createEsbSequence();
        GMMUtil.setUUID(newModel);
        newModel.setName("NewESB");

        // Create default diagram
        final Diagram diag = EsbdiagFactory.eINSTANCE.createESBDiagram();
        final Plane plane = GraphicFactory.eINSTANCE.createPlane();
        GMMUtil.setUUID(diag);
        GMMUtil.setUUID(plane);
        diag.setPlane(plane);
        diag.setName("NewESB" + "-diag");
        diag.getPlane().setModelElement(newModel);
        GraphicUtil.addDiagram(newModel, diag);


        view = new GraphicEditorViewImpl(diag, wso2Resources.wso2GraphicalEditorStyle());

        view.addPropertyForm(logProperties);
    }

    /** {@inheritDoc} */
    @Override
    public void doSave() {
    }

    /** {@inheritDoc} */
    @Override
    public void doSaveAs() {
    }

    /** {@inheritDoc} */
    @Override
    public void activate() {

    }

    /** {@inheritDoc} */
    @Override
    public String getTitle() {
        return "ESB Editor: " + input.getFile().getName();
    }

    /** {@inheritDoc} */
    @Override
    public ImageResource getTitleImage() {
        return input.getImageResource();
    }

    /** {@inheritDoc} */
    @Override
    public String getTitleToolTip() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void go(AcceptsOneWidget container) {
        // Add the components to a panel
        container.setWidget(view);
    }
}
