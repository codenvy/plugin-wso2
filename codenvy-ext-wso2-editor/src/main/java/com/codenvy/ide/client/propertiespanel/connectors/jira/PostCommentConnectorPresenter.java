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
import com.codenvy.ide.client.elements.connectors.jira.JiraPropertyManager;
import com.codenvy.ide.client.elements.connectors.jira.PostComment;
import com.codenvy.ide.client.managers.PropertyTypeManager;
import com.codenvy.ide.client.managers.SelectionManager;
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
import static com.codenvy.ide.client.elements.connectors.jira.PostComment.COMMENT_EXPR;
import static com.codenvy.ide.client.elements.connectors.jira.PostComment.COMMENT_INL;
import static com.codenvy.ide.client.elements.connectors.jira.PostComment.COMMENT_NS;
import static com.codenvy.ide.client.elements.connectors.jira.PostComment.ISSUE_ID_OR_KEY_EXPR;
import static com.codenvy.ide.client.elements.connectors.jira.PostComment.ISSUE_ID_OR_KEY_INL;
import static com.codenvy.ide.client.elements.connectors.jira.PostComment.ISSUE_ID_OR_KEY_NS;
import static com.codenvy.ide.client.elements.connectors.jira.PostComment.VISIBLE_ROLE_EXPR;
import static com.codenvy.ide.client.elements.connectors.jira.PostComment.VISIBLE_ROLE_INL;
import static com.codenvy.ide.client.elements.connectors.jira.PostComment.VISIBLE_ROLE_NS;


/**
 * The class provides the business logic that allows editor to react on user's action and to change state of connector
 * depending on user's changes of properties.
 *
 * @author Dmitry Shnurenko
 * @author Valeriy Svydenko
 */
public class PostCommentConnectorPresenter extends AbstractConnectorPropertiesPanelPresenter<PostComment> {

    private SimplePropertyPresenter issueIdOrKeyInl;
    private SimplePropertyPresenter commentInl;
    private SimplePropertyPresenter visibleRoleInl;

    private ComplexPropertyPresenter issueIdOrKeyExpr;
    private ComplexPropertyPresenter commentExpr;
    private ComplexPropertyPresenter visibleRoleExpr;

    @Inject
    public PostCommentConnectorPresenter(WSO2EditorLocalizationConstant locale,
                                         NameSpaceEditorPresenter nameSpacePresenter,
                                         PropertiesPanelView view,
                                         JiraPropertyManager jiraPropertyManager,
                                         ParameterPresenter parameterPresenter,
                                         PropertyTypeManager propertyTypeManager,
                                         PropertyPanelFactory propertyPanelFactory,
                                         SelectionManager selectionManager) {
        super(view,
              jiraPropertyManager,
              parameterPresenter,
              nameSpacePresenter,
              propertyTypeManager,
              locale,
              propertyPanelFactory,
              selectionManager);

        prepareView();
    }

    private void prepareView() {
        issueIdOrKeyInl = createSimpleConnectorProperty(locale.jiraIssueIdOrKey(), ISSUE_ID_OR_KEY_INL);
        commentInl = createSimpleConnectorProperty(locale.jiraComment(), COMMENT_INL);
        visibleRoleInl = createSimpleConnectorProperty(locale.jiraVisibleRole(), VISIBLE_ROLE_INL);

        issueIdOrKeyExpr = createComplexConnectorProperty(locale.jiraIssueIdOrKey(), ISSUE_ID_OR_KEY_NS, ISSUE_ID_OR_KEY_EXPR);
        commentExpr = createComplexConnectorProperty(locale.jiraComment(), COMMENT_NS, COMMENT_EXPR);
        visibleRoleExpr = createComplexConnectorProperty(locale.jiraVisibleRole(), VISIBLE_ROLE_NS, VISIBLE_ROLE_EXPR);
    }

    /** {@inheritDoc} */
    @Override
    protected void redrawPropertiesPanel() {
        boolean isVisible = INLINE.equals(element.getProperty(PARAMETER_EDITOR_TYPE));

        issueIdOrKeyInl.setVisible(isVisible);
        commentInl.setVisible(isVisible);
        visibleRoleInl.setVisible(isVisible);

        issueIdOrKeyExpr.setVisible(!isVisible);
        commentExpr.setVisible(!isVisible);
        visibleRoleExpr.setVisible(!isVisible);
    }

    /** {@inheritDoc} */
    @Override
    public void go(@Nonnull AcceptsOneWidget container) {
        super.go(container);

        issueIdOrKeyInl.setProperty(element.getProperty(ISSUE_ID_OR_KEY_INL));
        commentInl.setProperty(element.getProperty(COMMENT_INL));
        visibleRoleInl.setProperty(element.getProperty(VISIBLE_ROLE_INL));

        issueIdOrKeyExpr.setProperty(element.getProperty(ISSUE_ID_OR_KEY_EXPR));
        commentExpr.setProperty(element.getProperty(COMMENT_EXPR));
        visibleRoleExpr.setProperty(element.getProperty(VISIBLE_ROLE_EXPR));
    }
}
