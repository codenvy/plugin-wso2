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
package com.codenvy.ide.client.elements.connectors.salesforce;

import com.codenvy.ide.client.EditorResources;
import com.codenvy.ide.client.elements.Branch;
import com.codenvy.ide.client.elements.NameSpace;
import com.codenvy.ide.client.elements.connectors.AbstractConnector;
import com.codenvy.ide.client.managers.MediatorCreatorsManager;
import com.codenvy.ide.collections.Array;
import com.codenvy.ide.collections.Collections;
import com.google.gwt.xml.client.Node;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.codenvy.ide.client.elements.connectors.AbstractConnector.ParameterEditorType.Inline;

/**
 * The Class describes Undelete connector for Salesforce group connectors. Also the class contains the business logic
 * that allows to display serialization representation depending of the current state of element. Deserelization mechanism allows to
 * restore the condition of the element when you open ESB project after saving.
 *
 * @author Dmitry Shnurenko
 * @author Valeriy Svydenko
 */
public class UnDelete extends AbstractConnector {

    public static final String ELEMENT_NAME       = "Undelete";
    public static final String SERIALIZATION_NAME = "salesforce.undelete";

    private static final String ALL_OR_NONE = "allOrNone";
    private static final String SUBJECTS    = "sobjects";

    private static final List<String> PROPERTIES = Arrays.asList(ALL_OR_NONE, SUBJECTS);

    private String           allOrNone;
    private String           subject;
    private String           allOrNoneExpr;
    private String           subjectExpression;
    private Array<NameSpace> allOrNoneNameSpaces;
    private Array<NameSpace> subjectsNameSpaces;

    @Inject
    public UnDelete(EditorResources resources, Provider<Branch> branchProvider, MediatorCreatorsManager mediatorCreatorsManager) {
        super(ELEMENT_NAME, ELEMENT_NAME, SERIALIZATION_NAME, PROPERTIES, false, true, resources, branchProvider, mediatorCreatorsManager);

        allOrNone = "";
        subject = "";
        allOrNoneExpr = "";
        subjectExpression = "";

        allOrNoneNameSpaces = Collections.createArray();
        subjectsNameSpaces = Collections.createArray();
    }

    /** {@inheritDoc} */
    @Nonnull
    @Override
    protected String serializeProperties() {
        Map<String, String> properties = new LinkedHashMap<>();

        boolean isInline = parameterEditorType.equals(Inline);

        properties.put(ALL_OR_NONE, isInline ? allOrNone : allOrNoneExpr);
        properties.put(SUBJECTS, isInline ? subject : subjectExpression);

        return convertPropertiesToXMLFormat(properties);
    }

    /** {@inheritDoc} */
    @Override
    protected void applyProperty(@Nonnull Node node) {
        String nodeName = node.getNodeName();
        String nodeValue = node.getChildNodes().item(0).getNodeValue();

        boolean isInline = Inline.equals(parameterEditorType);

        switch (nodeName) {
            case ALL_OR_NONE:
                if (isInline) {
                    allOrNone = nodeValue;
                } else {
                    allOrNoneExpr = nodeValue;
                }
                break;

            case SUBJECTS:
                if (isInline) {
                    subject = nodeValue;
                } else {
                    subjectExpression = nodeValue;
                }
                break;

        }
    }

    @Nonnull
    public String getAllOrNone() {
        return allOrNone;
    }

    public void setAllOrNone(@Nonnull String allOrNone) {
        this.allOrNone = allOrNone;
    }

    @Nonnull
    public String getSubject() {
        return subject;
    }

    public void setSubject(@Nonnull String subjects) {
        this.subject = subjects;
    }

    @Nonnull
    public Array<NameSpace> getAllOrNoneNameSpaces() {
        return allOrNoneNameSpaces;
    }

    public void setAllOrNoneNameSpaces(@Nonnull Array<NameSpace> allOrNoneNameSpaces) {
        this.allOrNoneNameSpaces = allOrNoneNameSpaces;
    }

    @Nonnull
    public Array<NameSpace> getSubjectsNameSpaces() {
        return subjectsNameSpaces;
    }

    public void setSubjectsNameSpaces(@Nonnull Array<NameSpace> subjectsNameSpaces) {
        this.subjectsNameSpaces = subjectsNameSpaces;
    }

    @Nonnull
    public String getAllOrNoneExpr() {
        return allOrNoneExpr;
    }

    public void setAllOrNoneExpr(@Nonnull String allOrNoneExpr) {
        this.allOrNoneExpr = allOrNoneExpr;
    }

    @Nonnull
    public String getSubjectExpression() {
        return subjectExpression;
    }

    public void setSubjectExpression(@Nonnull String subjectExpression) {
        this.subjectExpression = subjectExpression;
    }
}