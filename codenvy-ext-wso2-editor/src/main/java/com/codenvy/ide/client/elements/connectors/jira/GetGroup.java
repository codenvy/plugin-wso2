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

/**
 * The Class describes GetGroup connector for jira group connectors. Also the class contains the business logic
 * that allows to display serialization representation depending of the current state of element. Deserelization mechanism allows to
 * restore the condition of the element when you open ESB project after saving.
 *
 * @author Dmitry Shnurenko
 * @author Vaelriy Svydenko
 */
public class GetGroup extends AbstractConnector {

    public static final String ELEMENT_NAME       = "GetGroup";
    public static final String SERIALIZATION_NAME = "jira.getGroup";

    public static final Key<String>          GROUP_NAME_INL  = new Key<>("groupNameInl");
    public static final Key<String>          GROUP_NAME_EXPR = new Key<>("groupNameExpr");
    public static final Key<List<NameSpace>> GROUP_NAME_NS   = new Key<>("groupNameNameSpace");

    private static final String GROUP_NAME = "groupName";

    private static final List<String> PROPERTIES = Arrays.asList(GROUP_NAME);

    @Inject
    public GetGroup(EditorResources resources,
                    Provider<Branch> branchProvider,
                    ElementCreatorsManager elementCreatorsManager) {
        super(ELEMENT_NAME,
              ELEMENT_NAME,
              SERIALIZATION_NAME,
              PROPERTIES,
              resources.jiraIcon(),
              branchProvider,
              elementCreatorsManager);

        putProperty(GROUP_NAME_INL, "");
        putProperty(GROUP_NAME_EXPR, "");
        putProperty(GROUP_NAME_NS, new ArrayList<NameSpace>());
    }

    /** {@inheritDoc} */
    @Nonnull
    @Override
    protected String serializeProperties() {
        Map<String, String> properties = new LinkedHashMap<>();

        boolean isInline = INLINE.equals(getProperty(PARAMETER_EDITOR_TYPE));

        properties.put(GROUP_NAME, isInline ? getProperty(GROUP_NAME_INL) : getProperty(GROUP_NAME_EXPR));

        return convertPropertiesToXMLFormat(properties);
    }

    /** {@inheritDoc} */
    @Override
    protected void applyProperty(@Nonnull Node node) {
        String nodeValue = node.getChildNodes().item(0).getNodeValue();

        if (GROUP_NAME.equals(node.getNodeName())) {
            adaptProperty(nodeValue, GROUP_NAME_INL, GROUP_NAME_EXPR);
        }
    }
}
