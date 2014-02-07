package com.codenvy.ide.ext.wso2.client.editor;

import org.genmymodel.gmmf.common.CommandRequestEvent;
import org.genmymodel.gmmf.common.ModelWidgetCSS;
import org.genmymodel.gmmf.common.SelectModelElementEvent;
import org.genmymodel.gmmf.propertypanel.PropertyForm;
import org.genmymodel.gmmf.propertypanel.PropertyPanel;
import org.genmymodel.gmmf.ui.ModelWidget;
import org.genmymodel.gmmf.ui.tools.Toolbar;
import org.genmymodel.gmmf.ui.tools.ToolsController;

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
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

import esbdiag.properties.LogMediatorProperties;
import esbdiag.widgets.ESBDiagramToolbar;

public class GraphicEditorViewImpl extends Composite implements GraphicEditorView
{

    interface GEUIBinder extends UiBinder<Widget, GraphicEditorViewImpl> {
    };

    private static GEUIBinder binder = GWT.create(GEUIBinder.class);


    @UiField(provided = true)
    Toolbar                   toolbar;

    @UiField(provided = true)
    ModelWidget               modelWidget;

    @UiField
    PropertyPanel             propertyPanel;

    private ToolsController   toolsController;

    public GraphicEditorViewImpl(Diagram diagram, ModelWidgetCSS modelWidgetCss)
    {
        /* Must be local to the widget */
        EventBus diagramEventBus = new SimpleEventBus();

        this.modelWidget = new ModelWidget(diagram, diagramEventBus);

        /* the ESB-specific toolbar */
        this.toolbar = new ESBDiagramToolbar(modelWidget, diagramEventBus, modelWidgetCss);

        this.toolsController = new ToolsController(modelWidget, diagramEventBus);

        /* toolsController */
        diagramEventBus.addHandler(MouseDownEvent.getType(), toolsController);
        diagramEventBus.addHandler(MouseMoveEvent.getType(), toolsController);
        diagramEventBus.addHandler(MouseUpEvent.getType(), toolsController);
        diagramEventBus.addHandler(MouseOverEvent.getType(), toolsController);
        diagramEventBus.addHandler(MouseOutEvent.getType(), toolsController);
        diagramEventBus.addHandler(KeyDownEvent.getType(), toolsController);
        diagramEventBus.addHandler(ContextMenuEvent.getType(), toolsController);

        modelWidget.setSize(2048, 2048);
        modelWidget.loadDiagram();

        /* A handler listens every EMF command */
        diagramEventBus.addHandler(CommandRequestEvent.TYPE, new SeqEventsHandler());

        /* Bind */
        initWidget(binder.createAndBindUi(this));

        /* event for the property panel */
        diagramEventBus.addHandler(SelectModelElementEvent.TYPE, propertyPanel);
        
        /*LogMediatorProperties logProperties = GWT.create(LogMediatorProperties.class);
        propertyPanel.add(logProperties);*/
    }

	@Override
	public void addPropertyForm(PropertyForm... forms)
	{
		this.propertyPanel.add(forms);
	}
    
    

}
