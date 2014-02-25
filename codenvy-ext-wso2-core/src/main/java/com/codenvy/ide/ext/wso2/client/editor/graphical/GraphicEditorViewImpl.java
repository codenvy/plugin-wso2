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
package com.codenvy.ide.ext.wso2.client.editor.graphical;

import org.genmymodel.gmmf.common.SelectModelElementEvent;
import org.genmymodel.gmmf.propertypanel.PropertyPanel;
import org.genmymodel.gmmf.propertypanel.PropertyPresenter;
import org.genmymodel.gmmf.ui.ModelWidget;
import org.genmymodel.gmmf.ui.tools.Toolbar;
import org.genmymodel.gmmf.ui.tools.ToolsController;
import org.wso2.developerstudio.eclipse.gmf.esb.EsbSequence;

import com.codenvy.ide.ext.wso2.client.WSO2Resources;
import com.genmymodel.ecoreonline.graphic.Diagram;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import esbdiag.widgets.ESBDiagramToolbar;

/**
 * The implementation of {@link GraphicEditorView}.
 * 
 * @author Alexis Muller
 * @author Justin Trentesaux
 * @author Andrey Plotnikov
 */
public class GraphicEditorViewImpl extends Composite implements GraphicEditorView {

    interface GEUIBinder extends UiBinder<Widget, GraphicEditorViewImpl> {
    }

    private static GEUIBinder binder = GWT.create(GEUIBinder.class);

    @UiField(provided = true)
    Toolbar                   toolbar;
    @UiField(provided = true)
    ModelWidget               modelWidget;
    @UiField
    PropertyPanel             propertyPanel;
    @UiField(provided = true)
    WSO2Resources             res;
    
    private EventBus eventBus;

    @Inject
    public GraphicEditorViewImpl(WSO2Resources resources, EventBus eventBus) {
        this.res = resources;
        this.eventBus = eventBus;
        
        /* ModelWidget comes from GMMF framework */
        this.modelWidget = new ModelWidget(eventBus);

        /* the ESB-specific toolbar */
        this.toolbar = new ESBDiagramToolbar(modelWidget, eventBus, res.wso2Style(), res);

        // TODO need to change hard code size of panel
        modelWidget.setSize(2048, 2048);
        

        initWidget(binder.createAndBindUi(this));
    }
    
    /** {@inheritDoc} */
    @Override
    public void initModelingWidgets(EsbSequence sequence, Diagram diagram) {
        modelWidget.loadDiagram(diagram);
        
        ToolsController toolsController = new ToolsController(modelWidget, eventBus);

        /* toolsController */
        eventBus.addHandler(MouseDownEvent.getType(), toolsController);
        eventBus.addHandler(MouseMoveEvent.getType(), toolsController);
        eventBus.addHandler(MouseUpEvent.getType(), toolsController);
        eventBus.addHandler(MouseOverEvent.getType(), toolsController);
        eventBus.addHandler(MouseOutEvent.getType(), toolsController);
        eventBus.addHandler(KeyDownEvent.getType(), toolsController);
        eventBus.addHandler(ContextMenuEvent.getType(), toolsController);

        /* event for the property panel */
        eventBus.addHandler(SelectModelElementEvent.TYPE, propertyPanel);
    }


    /** {@inheritDoc} */
    @Override
    public void setDelegate(ActionDelegate delegate) {

    }

    /** {@inheritDoc} */
    @Override
    public void addPropertyForm(PropertyPresenter... forms) {
        propertyPanel.add(forms);
    }
}
