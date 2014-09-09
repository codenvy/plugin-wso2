/*
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache  License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codenvy.ide.client.editor;

import com.codenvy.ide.client.EditorState;
import com.codenvy.ide.client.State;
import com.codenvy.ide.client.elements.RootElement;
import com.codenvy.ide.client.elements.endpoints.addressendpoint.AddressEndpoint;
import com.codenvy.ide.client.elements.mediators.Call;
import com.codenvy.ide.client.elements.mediators.CallTemplate;
import com.codenvy.ide.client.elements.mediators.Filter;
import com.codenvy.ide.client.elements.mediators.Header;
import com.codenvy.ide.client.elements.mediators.LoopBack;
import com.codenvy.ide.client.elements.mediators.Property;
import com.codenvy.ide.client.elements.mediators.Respond;
import com.codenvy.ide.client.elements.mediators.Send;
import com.codenvy.ide.client.elements.mediators.Sequence;
import com.codenvy.ide.client.elements.mediators.Switch;
import com.codenvy.ide.client.elements.mediators.ValueType;
import com.codenvy.ide.client.elements.mediators.enrich.Enrich;
import com.codenvy.ide.client.elements.mediators.log.Log;
import com.codenvy.ide.client.elements.mediators.payload.PayloadFactory;
import com.codenvy.ide.client.elements.widgets.element.ElementChangedListener;
import com.codenvy.ide.client.elements.widgets.element.ElementPresenter;
import com.codenvy.ide.client.inject.EditorFactory;
import com.codenvy.ide.client.managers.MediatorCreatorsManager;
import com.codenvy.ide.client.managers.PropertiesPanelManager;
import com.codenvy.ide.client.managers.PropertyTypeManager;
import com.codenvy.ide.client.managers.SelectionManager;
import com.codenvy.ide.client.mvp.AbstractPresenter;
import com.codenvy.ide.client.propertiespanel.AbstractPropertiesPanel;
import com.codenvy.ide.client.propertiespanel.addressendpoint.AddressEndpointPropertiesPanelPresenter;
import com.codenvy.ide.client.propertiespanel.call.CallPropertiesPanelPresenter;
import com.codenvy.ide.client.propertiespanel.calltemplate.CallTemplatePropertiesPanelPresenter;
import com.codenvy.ide.client.propertiespanel.empty.EmptyPropertiesPanelPresenter;
import com.codenvy.ide.client.propertiespanel.enrich.EnrichPropertiesPanelPresenter;
import com.codenvy.ide.client.propertiespanel.filter.FilterPropertiesPanelPresenter;
import com.codenvy.ide.client.propertiespanel.header.HeaderPropertiesPanelPresenter;
import com.codenvy.ide.client.propertiespanel.log.LogPropertiesPanelPresenter;
import com.codenvy.ide.client.propertiespanel.loopback.LoopBackPropertiesPanelPresenter;
import com.codenvy.ide.client.propertiespanel.payloadfactory.PayloadFactoryPropertiesPanelPresenter;
import com.codenvy.ide.client.propertiespanel.property.PropertyPropertiesPanelPresenter;
import com.codenvy.ide.client.propertiespanel.respond.RespondPropertiesPanelPresenter;
import com.codenvy.ide.client.propertiespanel.root.RootPropertiesPanelPresenter;
import com.codenvy.ide.client.propertiespanel.send.SendPropertiesPanelPresenter;
import com.codenvy.ide.client.propertiespanel.sequence.SequencePropertiesPanelPresenter;
import com.codenvy.ide.client.propertiespanel.switchmediator.SwitchPropertiesPanelPresenter;
import com.codenvy.ide.client.toolbar.ToolbarPresenter;
import com.codenvy.ide.client.validators.ConnectionsValidator;
import com.codenvy.ide.client.validators.InnerElementsValidator;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codenvy.ide.client.State.CREATING_NOTHING;
import static com.codenvy.ide.client.elements.endpoints.addressendpoint.AddressEndpoint.AddressingVersion;
import static com.codenvy.ide.client.elements.endpoints.addressendpoint.AddressEndpoint.AddressingVersion.FINAL;
import static com.codenvy.ide.client.elements.endpoints.addressendpoint.AddressEndpoint.AddressingVersion.SUBMISSION;
import static com.codenvy.ide.client.elements.endpoints.addressendpoint.AddressEndpoint.Format;
import static com.codenvy.ide.client.elements.endpoints.addressendpoint.AddressEndpoint.Format.LEAVE_AS_IS;
import static com.codenvy.ide.client.elements.endpoints.addressendpoint.AddressEndpoint.Format.REST;
import static com.codenvy.ide.client.elements.endpoints.addressendpoint.AddressEndpoint.Format.get;
import static com.codenvy.ide.client.elements.endpoints.addressendpoint.AddressEndpoint.Format.pox;
import static com.codenvy.ide.client.elements.endpoints.addressendpoint.AddressEndpoint.Format.soap11;
import static com.codenvy.ide.client.elements.endpoints.addressendpoint.AddressEndpoint.Format.soap12;
import static com.codenvy.ide.client.elements.endpoints.addressendpoint.AddressEndpoint.Optimize;
import static com.codenvy.ide.client.elements.endpoints.addressendpoint.AddressEndpoint.Optimize.mtom;
import static com.codenvy.ide.client.elements.endpoints.addressendpoint.AddressEndpoint.Optimize.swa;
import static com.codenvy.ide.client.elements.endpoints.addressendpoint.AddressEndpoint.TimeoutAction;
import static com.codenvy.ide.client.elements.endpoints.addressendpoint.AddressEndpoint.TimeoutAction.discard;
import static com.codenvy.ide.client.elements.endpoints.addressendpoint.AddressEndpoint.TimeoutAction.fault;
import static com.codenvy.ide.client.elements.endpoints.addressendpoint.AddressEndpoint.TimeoutAction.never;
import static com.codenvy.ide.client.elements.mediators.Call.EndpointType;
import static com.codenvy.ide.client.elements.mediators.Call.EndpointType.INLINE;
import static com.codenvy.ide.client.elements.mediators.Call.EndpointType.NONE;
import static com.codenvy.ide.client.elements.mediators.Call.EndpointType.REGISTRYKEY;
import static com.codenvy.ide.client.elements.mediators.Call.EndpointType.XPATH;
import static com.codenvy.ide.client.elements.mediators.CallTemplate.AvailableTemplates;
import static com.codenvy.ide.client.elements.mediators.CallTemplate.AvailableTemplates.EMPTY;
import static com.codenvy.ide.client.elements.mediators.CallTemplate.AvailableTemplates.SDF;
import static com.codenvy.ide.client.elements.mediators.CallTemplate.AvailableTemplates.SELECT_FROM_TEMPLATE;
import static com.codenvy.ide.client.elements.mediators.Filter.ConditionType.SOURCE_AND_REGEX;
import static com.codenvy.ide.client.elements.mediators.Header.HeaderAction;
import static com.codenvy.ide.client.elements.mediators.Header.HeaderValueType;
import static com.codenvy.ide.client.elements.mediators.Header.ScopeType;
import static com.codenvy.ide.client.elements.mediators.Header.ScopeType.Synapse;
import static com.codenvy.ide.client.elements.mediators.Header.ScopeType.transport;
import static com.codenvy.ide.client.elements.mediators.Property.Action;
import static com.codenvy.ide.client.elements.mediators.Property.Action.remove;
import static com.codenvy.ide.client.elements.mediators.Property.Action.set;
import static com.codenvy.ide.client.elements.mediators.Property.DataType;
import static com.codenvy.ide.client.elements.mediators.Property.DataType.BOOLEAN;
import static com.codenvy.ide.client.elements.mediators.Property.DataType.DOUBLE;
import static com.codenvy.ide.client.elements.mediators.Property.DataType.FLOAT;
import static com.codenvy.ide.client.elements.mediators.Property.DataType.INTEGER;
import static com.codenvy.ide.client.elements.mediators.Property.DataType.LONG;
import static com.codenvy.ide.client.elements.mediators.Property.DataType.OM;
import static com.codenvy.ide.client.elements.mediators.Property.DataType.SHORT;
import static com.codenvy.ide.client.elements.mediators.Property.DataType.STRING;
import static com.codenvy.ide.client.elements.mediators.Property.Scope.AXIS2;
import static com.codenvy.ide.client.elements.mediators.Property.Scope.AXIS2_CLIENT;
import static com.codenvy.ide.client.elements.mediators.Property.Scope.OPERATION;
import static com.codenvy.ide.client.elements.mediators.Property.Scope.SYNAPSE;
import static com.codenvy.ide.client.elements.mediators.Property.Scope.TRANSPORT;
import static com.codenvy.ide.client.elements.mediators.Send.SequenceType.Default;
import static com.codenvy.ide.client.elements.mediators.Send.SequenceType.Static;
import static com.codenvy.ide.client.elements.mediators.Sequence.ReferringType;
import static com.codenvy.ide.client.elements.mediators.Sequence.ReferringType.Dynamic;
import static com.codenvy.ide.client.elements.mediators.ValueType.EXPRESSION;
import static com.codenvy.ide.client.elements.mediators.ValueType.LITERAL;
import static com.codenvy.ide.client.elements.mediators.enrich.Source.InlineType;
import static com.codenvy.ide.client.elements.mediators.enrich.Source.InlineType.RegistryKey;
import static com.codenvy.ide.client.elements.mediators.enrich.Source.InlineType.SourceXML;
import static com.codenvy.ide.client.elements.mediators.enrich.Source.SourceType;
import static com.codenvy.ide.client.elements.mediators.enrich.Source.SourceType.body;
import static com.codenvy.ide.client.elements.mediators.enrich.Source.SourceType.custom;
import static com.codenvy.ide.client.elements.mediators.enrich.Source.SourceType.envelope;
import static com.codenvy.ide.client.elements.mediators.enrich.Source.SourceType.inline;
import static com.codenvy.ide.client.elements.mediators.enrich.Source.SourceType.property;
import static com.codenvy.ide.client.elements.mediators.enrich.Target.TargetAction;
import static com.codenvy.ide.client.elements.mediators.enrich.Target.TargetAction.child;
import static com.codenvy.ide.client.elements.mediators.enrich.Target.TargetAction.replace;
import static com.codenvy.ide.client.elements.mediators.enrich.Target.TargetAction.sibling;
import static com.codenvy.ide.client.elements.mediators.enrich.Target.TargetType;
import static com.codenvy.ide.client.elements.mediators.log.Log.LogCategory;
import static com.codenvy.ide.client.elements.mediators.log.Log.LogCategory.DEBUG;
import static com.codenvy.ide.client.elements.mediators.log.Log.LogCategory.ERROR;
import static com.codenvy.ide.client.elements.mediators.log.Log.LogCategory.FATAL;
import static com.codenvy.ide.client.elements.mediators.log.Log.LogCategory.INFO;
import static com.codenvy.ide.client.elements.mediators.log.Log.LogCategory.TRACE;
import static com.codenvy.ide.client.elements.mediators.log.Log.LogCategory.WARN;
import static com.codenvy.ide.client.elements.mediators.log.Log.LogLevel;
import static com.codenvy.ide.client.elements.mediators.log.Log.LogLevel.CUSTOM;
import static com.codenvy.ide.client.elements.mediators.log.Log.LogLevel.FULL;
import static com.codenvy.ide.client.elements.mediators.log.Log.LogLevel.HEADERS;
import static com.codenvy.ide.client.elements.mediators.log.Log.LogLevel.SIMPLE;
import static com.codenvy.ide.client.elements.mediators.payload.Format.FormatType;
import static com.codenvy.ide.client.elements.mediators.payload.Format.FormatType.Inline;
import static com.codenvy.ide.client.elements.mediators.payload.Format.FormatType.Registry;
import static com.codenvy.ide.client.elements.mediators.payload.Format.MediaType;
import static com.codenvy.ide.client.elements.mediators.payload.Format.MediaType.json;
import static com.codenvy.ide.client.elements.mediators.payload.Format.MediaType.xml;

/**
 * The presenter that provides a business logic of WSO2Editor. It provides ability to configure the editor.
 *
 * @author Andrey Plotnikov
 * @author Valeriy Svydenko
 * @author Dmitry Shnurenko
 */
public class WSO2Editor extends AbstractPresenter<WSO2EditorView> implements AbstractPropertiesPanel.PropertyChangedListener,
                                                                             WSO2EditorView.ActionDelegate,
                                                                             ElementChangedListener {

    public static final String BOOLEAN_TYPE_NAME = "Boolean";

    private final ToolbarPresenter           toolbar;
    private final RootElement                rootElement;
    private final ElementPresenter           rootElementPresenter;
    private final List<EditorChangeListener> listeners;

    @Inject
    public WSO2Editor(WSO2EditorView view, EditorFactory editorFactory, RootElement rootElement) {
        super(view);

        this.listeners = new ArrayList<>();

        EditorState<State> state = new EditorState<>(CREATING_NOTHING);

        this.rootElement = rootElement;

        this.rootElementPresenter = editorFactory.createElementPresenter(state, rootElement);
        this.rootElementPresenter.addElementChangedListener(this);

        this.toolbar = editorFactory.createToolbar(state);
    }

    @Inject
    private void configurePropertiesPanelManager(EditorFactory editorFactory,
                                                 SelectionManager selectionManager,
                                                 EmptyPropertiesPanelPresenter emptyPropertiesPanelPresenter,
                                                 RootPropertiesPanelPresenter rootPropertiesPanelPresenter,
                                                 LogPropertiesPanelPresenter logPropertiesPanelPresenter,
                                                 PropertyPropertiesPanelPresenter propertyPropertiesPanelPresenter,
                                                 PayloadFactoryPropertiesPanelPresenter payloadFactoryPropertiesPanelPresenter,
                                                 SendPropertiesPanelPresenter sendPropertiesPanelPresenter,
                                                 HeaderPropertiesPanelPresenter headerPropertiesPanelPresenter,
                                                 RespondPropertiesPanelPresenter respondPropertiesPanelPresenter,
                                                 FilterPropertiesPanelPresenter filterPropertiesPanelPresenter,
                                                 SwitchPropertiesPanelPresenter switchPropertiesPanelPresenter,
                                                 SequencePropertiesPanelPresenter sequencePropertiesPanelPresenter,
                                                 EnrichPropertiesPanelPresenter enrichPropertiesPanelPresenter,
                                                 LoopBackPropertiesPanelPresenter loopBackPropertiesPanelPresenter,
                                                 CallTemplatePropertiesPanelPresenter callTemplatePropertiesPanelPresenter,
                                                 CallPropertiesPanelPresenter callPropertiesPanelPresenter,
                                                 AddressEndpointPropertiesPanelPresenter addressEndpointPropertiesPanelPresenter) {

        PropertiesPanelManager propertiesPanelManager = editorFactory.createPropertiesPanelManager(view.getPropertiesPanel());

        propertiesPanelManager.register(Log.class, logPropertiesPanelPresenter);
        logPropertiesPanelPresenter.addListener(this);

        propertiesPanelManager.register(Property.class, propertyPropertiesPanelPresenter);
        propertyPropertiesPanelPresenter.addListener(this);

        propertiesPanelManager.register(PayloadFactory.class, payloadFactoryPropertiesPanelPresenter);
        payloadFactoryPropertiesPanelPresenter.addListener(this);

        propertiesPanelManager.register(Send.class, sendPropertiesPanelPresenter);
        sendPropertiesPanelPresenter.addListener(this);

        propertiesPanelManager.register(Header.class, headerPropertiesPanelPresenter);
        headerPropertiesPanelPresenter.addListener(this);

        propertiesPanelManager.register(Respond.class, respondPropertiesPanelPresenter);
        respondPropertiesPanelPresenter.addListener(this);

        propertiesPanelManager.register(Filter.class, filterPropertiesPanelPresenter);
        filterPropertiesPanelPresenter.addListener(this);

        propertiesPanelManager.register(Switch.class, switchPropertiesPanelPresenter);
        switchPropertiesPanelPresenter.addListener(this);

        propertiesPanelManager.register(Sequence.class, sequencePropertiesPanelPresenter);
        sequencePropertiesPanelPresenter.addListener(this);

        propertiesPanelManager.register(Enrich.class, enrichPropertiesPanelPresenter);
        enrichPropertiesPanelPresenter.addListener(this);

        propertiesPanelManager.register(LoopBack.class, loopBackPropertiesPanelPresenter);
        loopBackPropertiesPanelPresenter.addListener(this);

        propertiesPanelManager.register(CallTemplate.class, callTemplatePropertiesPanelPresenter);
        callTemplatePropertiesPanelPresenter.addListener(this);

        propertiesPanelManager.register(Call.class, callPropertiesPanelPresenter);
        callPropertiesPanelPresenter.addListener(this);

        propertiesPanelManager.register(AddressEndpoint.class, addressEndpointPropertiesPanelPresenter);
        addressEndpointPropertiesPanelPresenter.addListener(this);

        propertiesPanelManager.register(RootElement.class, rootPropertiesPanelPresenter);
        rootPropertiesPanelPresenter.addListener(this);

        propertiesPanelManager.register(null, emptyPropertiesPanelPresenter);
        emptyPropertiesPanelPresenter.addListener(this);

        selectionManager.addListener(propertiesPanelManager);
    }

    @Inject
    private void configurePropertyTypeManager(PropertyTypeManager propertyTypeManager) {
        propertyTypeManager.register(EndpointType.TYPE_NAME, Arrays.asList(INLINE.name(), NONE.name(), REGISTRYKEY.name(), XPATH.name()));

        propertyTypeManager.register(LogCategory.TYPE_NAME, Arrays.asList(TRACE.name(),
                                                                          DEBUG.name(),
                                                                          INFO.name(),
                                                                          WARN.name(),
                                                                          ERROR.name(),
                                                                          FATAL.name()));

        propertyTypeManager.register(LogLevel.TYPE_NAME, Arrays.asList(SIMPLE.name(), HEADERS.name(), FULL.name(), CUSTOM.name()));

        propertyTypeManager.register(ValueType.TYPE_NAME, Arrays.asList(LITERAL.name(), EXPRESSION.name()));

        propertyTypeManager.register(DataType.TYPE_NAME, Arrays.asList(STRING.name(),
                                                                       INTEGER.name(),
                                                                       BOOLEAN.name(),
                                                                       DOUBLE.name(),
                                                                       FLOAT.name(),
                                                                       LONG.name(),
                                                                       SHORT.name(),
                                                                       OM.name()));

        propertyTypeManager.register(Property.Scope.TYPE_NAME, Arrays.asList(SYNAPSE.getValue(),
                                                                             TRANSPORT.getValue(),
                                                                             AXIS2.getValue(),
                                                                             AXIS2_CLIENT.getValue(),
                                                                             OPERATION.getValue()));

        propertyTypeManager.register(BOOLEAN_TYPE_NAME, Arrays.asList(Boolean.FALSE.toString(), Boolean.TRUE.toString()));

        propertyTypeManager.register(Action.TYPE_NAME, Arrays.asList(set.name(), remove.name()));

        propertyTypeManager.register(MediaType.TYPE_NAME, Arrays.asList(xml.name(), json.name()));

        propertyTypeManager.register(FormatType.TYPE_NAME, Arrays.asList(Inline.name(), Registry.name()));

        propertyTypeManager.register(Send.SequenceType.TYPE_NAME, Arrays.asList(Default.name(), Static.name(), Dynamic.name()));

        propertyTypeManager.register(HeaderAction.TYPE_NAME, Arrays.asList(HeaderAction.set.name(), HeaderAction.remove.name()));

        propertyTypeManager.register(HeaderValueType.TYPE_NAME, Arrays.asList(HeaderValueType.LITERAL.name(),
                                                                              HeaderValueType.EXPRESSION.name(),
                                                                              HeaderValueType.INLINE.name()));

        propertyTypeManager.register(ScopeType.TYPE_NAME, Arrays.asList(Synapse.name(), transport.name()));

        propertyTypeManager.register(Filter.ConditionType.TYPE_NAME, Arrays.asList(SOURCE_AND_REGEX.name(), XPATH.name()));

        propertyTypeManager.register(ReferringType.TYPE_NAME, Arrays.asList(ReferringType.Static.name(), Dynamic.name()));

        propertyTypeManager.register(SourceType.TYPE_NAME, Arrays.asList(custom.name(),
                                                                         envelope.name(),
                                                                         body.name(),
                                                                         property.name(),
                                                                         inline.name()));

        propertyTypeManager.register(TargetAction.TYPE_NAME, Arrays.asList(replace.name(), child.name(), sibling.name()));

        propertyTypeManager.register(TargetType.TYPE_NAME, Arrays.asList(TargetType.custom.name(),
                                                                         TargetType.envelope.name(),
                                                                         TargetType.body.name(),
                                                                         TargetType.property.name()));

        propertyTypeManager.register(AvailableTemplates.TYPE_NAME, Arrays.asList(EMPTY.getValue(),
                                                                                 SELECT_FROM_TEMPLATE.getValue(),
                                                                                 SDF.getValue()));

        propertyTypeManager.register(InlineType.INLINE_TYPE, Arrays.asList(RegistryKey.name(),
                                                                           SourceXML.name()));

        propertyTypeManager.register(Format.TYPE_NAME, Arrays.asList(LEAVE_AS_IS.name(),
                                                                     soap11.name(),
                                                                     soap12.name(),
                                                                     pox.name(),
                                                                     get.name(),
                                                                     REST.name()));

        propertyTypeManager.register(Optimize.TYPE_NAME, Arrays.asList(Optimize.LEAVE_AS_IS.name(),
                                                                       mtom.name(),
                                                                       swa.name()));

        propertyTypeManager.register(AddressingVersion.TYPE_NAME, Arrays.asList(FINAL.getValue(),
                                                                                SUBMISSION.getValue()));

        propertyTypeManager.register(TimeoutAction.TYPE_NAME, Arrays.asList(never.name(),
                                                                            discard.name(),
                                                                            fault.name()));
    }

    @Inject
    private void configureConnectionsValidator(ConnectionsValidator connectionsValidator) {
        connectionsValidator.addDisallowRules(Respond.ELEMENT_NAME, Arrays.asList(Log.ELEMENT_NAME,
                                                                                  Property.ELEMENT_NAME,
                                                                                  PayloadFactory.ELEMENT_NAME,
                                                                                  Send.ELEMENT_NAME,
                                                                                  Header.ELEMENT_NAME,
                                                                                  Respond.ELEMENT_NAME,
                                                                                  Filter.ELEMENT_NAME,
                                                                                  Switch.ELEMENT_NAME,
                                                                                  Sequence.ELEMENT_NAME,
                                                                                  Enrich.ELEMENT_NAME,
                                                                                  LoopBack.ELEMENT_NAME,
                                                                                  CallTemplate.ELEMENT_NAME,
                                                                                  Call.ELEMENT_NAME,
                                                                                  AddressEndpoint.ELEMENT_NAME));

        connectionsValidator.addDisallowRules(LoopBack.ELEMENT_NAME, Arrays.asList(Log.ELEMENT_NAME,
                                                                                   Property.ELEMENT_NAME,
                                                                                   PayloadFactory.ELEMENT_NAME,
                                                                                   Send.ELEMENT_NAME,
                                                                                   Header.ELEMENT_NAME,
                                                                                   Respond.ELEMENT_NAME,
                                                                                   Filter.ELEMENT_NAME,
                                                                                   Switch.ELEMENT_NAME,
                                                                                   Sequence.ELEMENT_NAME,
                                                                                   Enrich.ELEMENT_NAME,
                                                                                   LoopBack.ELEMENT_NAME,
                                                                                   CallTemplate.ELEMENT_NAME,
                                                                                   Call.ELEMENT_NAME,
                                                                                   AddressEndpoint.ELEMENT_NAME));

        connectionsValidator.addDisallowRules(AddressEndpoint.ELEMENT_NAME, Arrays.asList(Log.ELEMENT_NAME,
                                                                                          Property.ELEMENT_NAME,
                                                                                          PayloadFactory.ELEMENT_NAME,
                                                                                          Send.ELEMENT_NAME,
                                                                                          Header.ELEMENT_NAME,
                                                                                          Respond.ELEMENT_NAME,
                                                                                          Filter.ELEMENT_NAME,
                                                                                          Switch.ELEMENT_NAME,
                                                                                          Sequence.ELEMENT_NAME,
                                                                                          Enrich.ELEMENT_NAME,
                                                                                          LoopBack.ELEMENT_NAME,
                                                                                          CallTemplate.ELEMENT_NAME,
                                                                                          Call.ELEMENT_NAME,
                                                                                          AddressEndpoint.ELEMENT_NAME));
    }

    @Inject
    private void configureInnerElementsValidator(InnerElementsValidator innerElementsValidator) {
        innerElementsValidator.addAllowRule(Call.ELEMENT_NAME, AddressEndpoint.ELEMENT_NAME);

        innerElementsValidator.addAllowRule(Send.ELEMENT_NAME, AddressEndpoint.ELEMENT_NAME);
        
        innerElementsValidator.addDisallowRule(RootElement.ELEMENT_NAME, AddressEndpoint.ELEMENT_NAME);

        innerElementsValidator.addDisallowRule(Filter.ELEMENT_NAME, AddressEndpoint.ELEMENT_NAME);

        innerElementsValidator.addDisallowRule(Switch.ELEMENT_NAME, AddressEndpoint.ELEMENT_NAME);
    }

    @Inject
    private void configureMediatorCreatorsManager(MediatorCreatorsManager mediatorCreatorsManager,
                                                  Provider<Log> logProvider,
                                                  Provider<Enrich> enrichProvider,
                                                  Provider<Filter> filterProvider,
                                                  Provider<Header> headerProvider,
                                                  Provider<Call> callProvider,
                                                  Provider<CallTemplate> callTemplateProvider,
                                                  Provider<LoopBack> loopBackProvider,
                                                  Provider<PayloadFactory> payloadFactoryProvider,
                                                  Provider<Property> propertyProvider,
                                                  Provider<Respond> respondProvider,
                                                  Provider<Send> sendProvider,
                                                  Provider<Sequence> sequenceProvider,
                                                  Provider<Switch> switchProvider,
                                                  Provider<AddressEndpoint> addressEndpointProvider) {

        mediatorCreatorsManager.register(Log.ELEMENT_NAME, Log.SERIALIZATION_NAME, State.CREATING_LOG, logProvider);

        mediatorCreatorsManager.register(Enrich.ELEMENT_NAME, Enrich.SERIALIZATION_NAME, State.CREATING_ENRICH, enrichProvider);

        mediatorCreatorsManager.register(Filter.ELEMENT_NAME, Filter.SERIALIZATION_NAME, State.CREATING_FILTER, filterProvider);

        mediatorCreatorsManager.register(Header.ELEMENT_NAME, Header.SERIALIZATION_NAME, State.CREATING_HEADER, headerProvider);

        mediatorCreatorsManager.register(Call.ELEMENT_NAME, Call.SERIALIZATION_NAME, State.CREATING_CALL, callProvider);

        mediatorCreatorsManager.register(CallTemplate.ELEMENT_NAME,
                                         CallTemplate.SERIALIZATION_NAME,
                                         State.CREATING_CALLTEMPLATE,
                                         callTemplateProvider);

        mediatorCreatorsManager.register(LoopBack.ELEMENT_NAME, LoopBack.SERIALIZATION_NAME, State.CREATING_LOOPBACK, loopBackProvider);

        mediatorCreatorsManager.register(PayloadFactory.ELEMENT_NAME,
                                         PayloadFactory.SERIALIZATION_NAME,
                                         State.CREATING_PAYLOADFACTORY,
                                         payloadFactoryProvider);

        mediatorCreatorsManager.register(Property.ELEMENT_NAME, Property.SERIALIZATION_NAME, State.CREATING_PROPERTY, propertyProvider);

        mediatorCreatorsManager.register(Respond.ELEMENT_NAME, Respond.SERIALIZATION_NAME, State.CREATING_RESPOND, respondProvider);

        mediatorCreatorsManager.register(Send.ELEMENT_NAME, Send.SERIALIZATION_NAME, State.CREATING_SEND, sendProvider);

        mediatorCreatorsManager.register(Sequence.ELEMENT_NAME, Sequence.SERIALIZATION_NAME, State.CREATING_SEQUENCE, sequenceProvider);

        mediatorCreatorsManager.register(Switch.ELEMENT_NAME, Switch.SERIALIZATION_NAME, State.CREATING_SWITCH, switchProvider);

        mediatorCreatorsManager.register(AddressEndpoint.ELEMENT_NAME,
                                         AddressEndpoint.SERIALIZATION_NAME,
                                         State.CREATING_ADDRESS_ENDPOINT,
                                         addressEndpointProvider);
    }

    /** {@inheritDoc} */
    @Override
    public void go(@Nonnull AcceptsOneWidget container) {
        toolbar.go(view.getToolbarPanel());
        rootElementPresenter.go(view.getWorkspacePanel());

        super.go(container);
    }

    /** {@inheritDoc} */
    @Override
    public void onElementChanged() {
        notifyListeners();
    }

    /** {@inheritDoc} */
    @Override
    public void onPropertyChanged() {
        notifyListeners();
    }

    public void addListener(@Nonnull EditorChangeListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners() {
        for (EditorChangeListener listener : listeners) {
            listener.onChanged();
        }
    }

    /** @return a serialized text type of diagram */
    @Nonnull
    public String serialize() {
        return rootElement.serialize();
    }

    /**
     * Convert a text type of diagram to a graphical type.
     *
     * @param content
     *         content that need to be parsed
     */
    public void deserialize(@Nonnull String content) {
        rootElement.deserialize(content);
        rootElementPresenter.onElementChanged();
    }

    /** {@inheritDoc} */
    @Override
    public void onHidePanelButtonClicked() {
        view.setVisiblePropertyPanel(true);
    }

    /** {@inheritDoc} */
    @Override
    public void onShowPropertyButtonClicked() {
        view.setVisiblePropertyPanel(false);
    }

    public interface EditorChangeListener {

        /** Performs some actions when editor was changed. */
        void onChanged();

    }

}