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
package com.codenvy.ide.client.propertiespanel.connectors.jira;

import com.codenvy.ide.client.WSO2EditorLocalizationConstant;
import com.codenvy.ide.client.elements.connectors.jira.GetUserPermissions;
import com.codenvy.ide.client.elements.connectors.twitter.TwitterPropertyManager;
import com.codenvy.ide.client.managers.PropertyTypeManager;
import com.codenvy.ide.client.propertiespanel.PropertiesPanelView;
import com.codenvy.ide.client.propertiespanel.PropertyPanelFactory;
import com.codenvy.ide.client.propertiespanel.common.namespace.NameSpaceEditorPresenter;
import com.codenvy.ide.client.propertiespanel.connectors.base.AbstractConnectorPropertiesPanelPresenter;
import com.codenvy.ide.client.propertiespanel.connectors.base.parameter.ParameterPresenter;
import com.codenvy.ide.client.propertiespanel.property.complex.ComplexPropertyPresenter;
import com.codenvy.ide.client.propertiespanel.property.simple.SimplePropertyPresenter;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import javax.annotation.Nonnull;

import static com.codenvy.ide.client.elements.connectors.AbstractConnector.PARAMETER_EDITOR_TYPE;
import static com.codenvy.ide.client.elements.connectors.AbstractConnector.ParameterEditorType.INLINE;
import static com.codenvy.ide.client.elements.connectors.jira.GetUserAssignableProjects.PROJECT_KEY_EXPR;
import static com.codenvy.ide.client.elements.connectors.jira.GetUserAssignableProjects.PROJECT_KEY_INL;
import static com.codenvy.ide.client.elements.connectors.jira.GetUserAssignableProjects.PROJECT_KEY_NS;
import static com.codenvy.ide.client.elements.connectors.jira.GetUserPermissions.ISSUE_ID_EXPR;
import static com.codenvy.ide.client.elements.connectors.jira.GetUserPermissions.ISSUE_ID_INL;
import static com.codenvy.ide.client.elements.connectors.jira.GetUserPermissions.ISSUE_ID_NS;
import static com.codenvy.ide.client.elements.connectors.jira.GetUserPermissions.ISSUE_KEY_EXPR;
import static com.codenvy.ide.client.elements.connectors.jira.GetUserPermissions.ISSUE_KEY_INL;
import static com.codenvy.ide.client.elements.connectors.jira.GetUserPermissions.ISSUE_KEY_NS;
import static com.codenvy.ide.client.elements.connectors.jira.GetUserPermissions.PROJECT_ID_EXPR;
import static com.codenvy.ide.client.elements.connectors.jira.GetUserPermissions.PROJECT_ID_INL;
import static com.codenvy.ide.client.elements.connectors.jira.GetUserPermissions.PROJECT_ID_NS;

/**
 * The class provides the business logic that allows editor to react on user's action and to change state of connector
 * depending on user's changes of properties.
 *
 * @author Dmitry Shnurenko
 * @author Valeriy Svydenko
 */
public class GetUserPermissionsConnectorPresenter extends AbstractConnectorPropertiesPanelPresenter<GetUserPermissions> {

    private SimplePropertyPresenter projectKeyInl;
    private SimplePropertyPresenter projectIdInl;
    private SimplePropertyPresenter issueKeyInl;
    private SimplePropertyPresenter issueIdInl;

    private ComplexPropertyPresenter projectKeyExpr;
    private ComplexPropertyPresenter projectIdExpr;
    private ComplexPropertyPresenter issueKeyExpr;
    private ComplexPropertyPresenter issueIdExpr;

    @Inject
    public GetUserPermissionsConnectorPresenter(WSO2EditorLocalizationConstant locale,
                                                NameSpaceEditorPresenter nameSpacePresenter,
                                                PropertiesPanelView view,
                                                TwitterPropertyManager twitterPropertyManager,
                                                ParameterPresenter parameterPresenter,
                                                PropertyTypeManager propertyTypeManager,
                                                PropertyPanelFactory propertyPanelFactory) {
        super(view,
              twitterPropertyManager,
              parameterPresenter,
              nameSpacePresenter,
              propertyTypeManager,
              locale,
              propertyPanelFactory);

        prepareView();
    }

    private void prepareView() {
        projectKeyInl = createSimpleConnectorProperty(locale.jiraProjectKey(), PROJECT_KEY_INL);
        projectIdInl = createSimpleConnectorProperty(locale.jiraProjectId(), PROJECT_ID_INL);
        issueKeyInl = createSimpleConnectorProperty(locale.jiraIssueKey(), ISSUE_KEY_INL);
        issueIdInl = createSimpleConnectorProperty(locale.jiraIssueId(), ISSUE_ID_INL);

        projectKeyExpr = createComplexConnectorProperty(locale.jiraProjectKey(), PROJECT_KEY_NS, PROJECT_KEY_EXPR);
        projectIdExpr = createComplexConnectorProperty(locale.jiraProjectId(), PROJECT_ID_NS, PROJECT_ID_EXPR);
        issueKeyExpr = createComplexConnectorProperty(locale.jiraIssueKey(), ISSUE_KEY_NS, ISSUE_KEY_EXPR);
        issueIdExpr = createComplexConnectorProperty(locale.jiraIssueId(), ISSUE_ID_NS, ISSUE_ID_EXPR);
    }

    /** {@inheritDoc} */
    @Override
    protected void redrawPropertiesPanel() {
        boolean isVisible = INLINE.equals(element.getProperty(PARAMETER_EDITOR_TYPE));

        projectKeyInl.setVisible(isVisible);
        projectIdInl.setVisible(isVisible);
        issueKeyInl.setVisible(isVisible);
        issueIdInl.setVisible(isVisible);

        projectKeyExpr.setVisible(!isVisible);
        projectIdExpr.setVisible(!isVisible);
        issueKeyExpr.setVisible(!isVisible);
        issueIdExpr.setVisible(!isVisible);
    }

    /** {@inheritDoc} */
    @Override
    public void go(@Nonnull AcceptsOneWidget container) {
        super.go(container);

        projectKeyInl.setProperty(element.getProperty(PROJECT_KEY_INL));
        projectIdInl.setProperty(element.getProperty(PROJECT_ID_INL));
        issueKeyInl.setProperty(element.getProperty(ISSUE_KEY_INL));
        issueIdInl.setProperty(element.getProperty(ISSUE_ID_INL));

        projectKeyExpr.setProperty(element.getProperty(PROJECT_KEY_EXPR));
        projectIdExpr.setProperty(element.getProperty(PROJECT_ID_EXPR));
        issueKeyExpr.setProperty(element.getProperty(ISSUE_KEY_EXPR));
        issueIdExpr.setProperty(element.getProperty(ISSUE_ID_EXPR));
    }
}
