/*
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codenvy.ide.client.propertiespanel.connectors.salesforce.emptyrecyclebin;

import com.codenvy.ide.client.WSO2EditorLocalizationConstant;
import com.codenvy.ide.client.elements.NameSpace;
import com.codenvy.ide.client.elements.connectors.salesforce.EmptyRecycleBin;
import com.codenvy.ide.client.elements.connectors.salesforce.GeneralPropertyManager;
import com.codenvy.ide.client.managers.PropertyTypeManager;
import com.codenvy.ide.client.propertiespanel.connectors.salesforce.base.GeneralConnectorPanelPresenter;
import com.codenvy.ide.client.propertiespanel.connectors.salesforce.base.GeneralConnectorPanelView;
import com.codenvy.ide.client.propertiespanel.connectors.salesforce.base.parameter.ParameterPresenter;
import com.codenvy.ide.client.propertiespanel.namespace.NameSpaceEditorPresenter;
import com.codenvy.ide.client.propertiespanel.propertyconfig.AddNameSpacesCallBack;
import com.codenvy.ide.collections.Array;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import javax.annotation.Nonnull;

import static com.codenvy.ide.client.elements.connectors.salesforce.AbstractSalesForceConnector.ParameterEditorType;
import static com.codenvy.ide.client.elements.connectors.salesforce.AbstractSalesForceConnector.ParameterEditorType.NamespacedPropertyEditor;

/**
 * The class provides the business logic that allows editor to react on user's action and to change state of Delete connector
 * depending on user's changes of properties.
 *
 * @author Dmitry Shnurenko
 */
public class EmptyRecycleBinPresenter extends GeneralConnectorPanelPresenter<EmptyRecycleBin> {

    private final WSO2EditorLocalizationConstant local;
    private final NameSpaceEditorPresenter       nameSpacePresenter;
    private final AddNameSpacesCallBack          subjectNameSpacesCallBack;
    private final AddNameSpacesCallBack          allOrNoneNameSpacesCallBack;

    @Inject
    public EmptyRecycleBinPresenter(WSO2EditorLocalizationConstant local,
                                    GeneralConnectorPanelView generalView,
                                    NameSpaceEditorPresenter nameSpacePresenter,
                                    ParameterPresenter parameterPresenter,
                                    GeneralPropertyManager generalPropertyManager,
                                    PropertyTypeManager propertyTypeManager) {

        super(generalView, generalPropertyManager, parameterPresenter, propertyTypeManager);

        this.local = local;

        this.nameSpacePresenter = nameSpacePresenter;

        this.subjectNameSpacesCallBack = new AddNameSpacesCallBack() {
            @Override
            public void onNameSpacesChanged(@Nonnull Array<NameSpace> nameSpaces, @Nonnull String expression) {
                element.setSubjectsNameSpaces(nameSpaces);
                element.setSubjectExpression(expression);

                EmptyRecycleBinPresenter.this.view.setSecondTextBoxValue(expression);

                notifyListeners();
            }
        };

        this.allOrNoneNameSpacesCallBack = new AddNameSpacesCallBack() {
            @Override
            public void onNameSpacesChanged(@Nonnull Array<NameSpace> nameSpaces, @Nonnull String expression) {
                element.setAllOrNoneNameSpaces(nameSpaces);
                element.setAllOrNoneExpr(expression);

                EmptyRecycleBinPresenter.this.view.setFirstTextBoxValue(expression);

                notifyListeners();
            }
        };
    }

    /** {@inheritDoc} */
    @Override
    public void onParameterEditorTypeChanged() {
        ParameterEditorType editorType = ParameterEditorType.valueOf(view.getParameterEditorType());
        element.setParameterEditorType(editorType);

        boolean isEquals = NamespacedPropertyEditor.equals(editorType);

        view.setVisibleFirstButton(isEquals);
        view.setVisibleSecondButton(isEquals);

        view.setEnableFirstTextBox(!isEquals);
        view.setEnableSecondTextBox(!isEquals);

        view.setFirstTextBoxValue(isEquals ? element.getAllOrNoneExpr() : element.getAllOrNone());
        view.setSecondTextBoxValue(isEquals ? element.getSubjectExpression() : element.getSubject());

        notifyListeners();
    }

    /** {@inheritDoc} */
    @Override
    public void onFirstTextBoxValueChanged() {
        element.setAllOrNone(view.getFirstTextBoxValue());

        notifyListeners();
    }

    /** {@inheritDoc} */
    @Override
    public void onSecondTextBoxValueChanged() {
        element.setSubject(view.getSecondTextBoxValue());

        notifyListeners();
    }

    /** {@inheritDoc} */
    @Override
    public void onFirstButtonClicked() {
        nameSpacePresenter.showWindowWithParameters(element.getAllOrNoneNameSpaces(),
                                                    allOrNoneNameSpacesCallBack,
                                                    local.connectorExpression(),
                                                    element.getAllOrNoneExpr());
    }

    /** {@inheritDoc} */
    @Override
    public void onSecondButtonClicked() {
        nameSpacePresenter.showWindowWithParameters(element.getSubjectsNameSpaces(),
                                                    subjectNameSpacesCallBack,
                                                    local.connectorExpression(),
                                                    element.getSubjectExpression());
    }

    private void redesignViewToCurrentConnector() {
        view.setVisibleFirstPanel(true);
        view.setVisibleSecondPanel(true);

        view.setFirstLabelTitle(local.connectorAllOrNone());
        view.setSecondLabelTitle(local.connectorSubjects());
    }


    /** {@inheritDoc} */
    @Override
    public void go(@Nonnull AcceptsOneWidget container) {
        super.go(container);

        redesignViewToCurrentConnector();
        onParameterEditorTypeChanged();
    }
}