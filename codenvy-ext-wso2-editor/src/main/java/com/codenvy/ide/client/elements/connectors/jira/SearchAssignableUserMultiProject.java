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
package com.codenvy.ide.client.elements.connectors.jira;

import com.codenvy.ide.client.EditorResources;
import com.codenvy.ide.client.elements.Branch;
import com.codenvy.ide.client.elements.NameSpace;
import com.codenvy.ide.client.elements.connectors.AbstractConnector;
import com.codenvy.ide.client.managers.ElementCreatorsManager;
import com.google.gwt.xml.client.Node;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.codenvy.ide.client.elements.connectors.AbstractConnector.ParameterEditorType.INLINE;
import static com.codenvy.ide.client.elements.connectors.AbstractConnector.ParameterEditorType.NAME_SPACED_PROPERTY_EDITOR;

/**
 * The Class describes SearchAssignableUserMultiProject connector for jira group connectors. Also the class contains the business logic
 * that allows to display serialization representation depending of the current state of element. Deserelization mechanism allows to
 * restore the condition of the element when you open ESB project after saving.
 *
 * @author Dmitry Shnurenko
 * @author Valeriy Svydenko
 */
public class SearchAssignableUserMultiProject extends AbstractConnector {

    public static final String ELEMENT_NAME       = "SearchAssignableUserMultiProject";
    public static final String SERIALIZATION_NAME = "jira.searchAssignableUserMultiProject";

    public static final Key<String> USER_NAME_INL    = new Key<>("userNameInl");
    public static final Key<String> PROJECT_KEYS_INL = new Key<>("projectKeysInl");
    public static final Key<String> START_AT_INL     = new Key<>("startAtInl");
    public static final Key<String> MAX_RESULTS_INL  = new Key<>("maxResultsInl");

    public static final Key<String> USER_NAME_EXPR    = new Key<>("userNameExpr");
    public static final Key<String> PROJECT_KEYS_EXPR = new Key<>("projectKeysExpr");
    public static final Key<String> START_AT_EXPR     = new Key<>("startAtExpr");
    public static final Key<String> MAX_RESULTS_EXPR  = new Key<>("maxResultsExpr");

    public static final Key<List<NameSpace>> USER_NAME_NS    = new Key<>("userNameSpace");
    public static final Key<List<NameSpace>> PROJECT_KEYS_NS = new Key<>("projectKeysNameSpace");
    public static final Key<List<NameSpace>> START_AT_NS     = new Key<>("startAtNameSpace");
    public static final Key<List<NameSpace>> MAX_RESULTS_NS  = new Key<>("maxResultsNameSpace");

    private static final String USER_NAME    = "username";
    private static final String PROJECT_KEYS = "projectKeys";
    private static final String START_AT     = "startAt";
    private static final String MAX_RESULTS  = "maxResults";

    private static final List<String> PROPERTIES = Arrays.asList(USER_NAME,
                                                                 PROJECT_KEYS,
                                                                 START_AT,
                                                                 MAX_RESULTS);

    @Inject
    public SearchAssignableUserMultiProject(EditorResources resources,
                                            Provider<Branch> branchProvider,
                                            ElementCreatorsManager elementCreatorsManager) {
        super(ELEMENT_NAME,
              ELEMENT_NAME,
              SERIALIZATION_NAME,
              PROPERTIES,
              resources.jiraIcon(),
              branchProvider,
              elementCreatorsManager);

        putProperty(USER_NAME_INL, "");
        putProperty(PROJECT_KEYS_INL, "");
        putProperty(START_AT_INL, "");
        putProperty(MAX_RESULTS_INL, "");

        putProperty(USER_NAME_EXPR, "");
        putProperty(PROJECT_KEYS_EXPR, "");
        putProperty(START_AT_EXPR, "");
        putProperty(MAX_RESULTS_EXPR, "");

        putProperty(USER_NAME_NS, new ArrayList<NameSpace>());
        putProperty(PROJECT_KEYS_NS, new ArrayList<NameSpace>());
        putProperty(START_AT_NS, new ArrayList<NameSpace>());
        putProperty(MAX_RESULTS_NS, new ArrayList<NameSpace>());
    }

    /** {@inheritDoc} */
    @Nonnull
    @Override
    protected String serializeProperties() {
        Map<String, String> properties = new LinkedHashMap<>();

        boolean isInline = INLINE.equals(getProperty(PARAMETER_EDITOR_TYPE));

        properties.put(USER_NAME, isInline ? getProperty(USER_NAME_INL) : getProperty(USER_NAME_EXPR));
        properties.put(PROJECT_KEYS, isInline ? getProperty(PROJECT_KEYS_INL) : getProperty(PROJECT_KEYS_EXPR));
        properties.put(START_AT, isInline ? getProperty(START_AT_INL) : getProperty(START_AT_EXPR));
        properties.put(MAX_RESULTS, isInline ? getProperty(MAX_RESULTS_INL) : getProperty(MAX_RESULTS_EXPR));

        return convertPropertiesToXMLFormat(properties);
    }

    /** {@inheritDoc} */
    @Override
    protected void applyProperty(@Nonnull Node node) {
        String nodeValue = node.getChildNodes().item(0).getNodeValue();

        switch (node.getNodeName()) {
            case USER_NAME:
                adaptProperty(nodeValue, USER_NAME_INL, USER_NAME_EXPR);
                break;

            case PROJECT_KEYS:
                adaptProperty(nodeValue, PROJECT_KEYS_INL, PROJECT_KEYS_EXPR);
                break;

            case START_AT:
                adaptProperty(nodeValue, START_AT_INL, START_AT_EXPR);
                break;

            case MAX_RESULTS:
                adaptProperty(nodeValue, MAX_RESULTS_INL, MAX_RESULTS_EXPR);
                break;

            default:
        }
    }
}