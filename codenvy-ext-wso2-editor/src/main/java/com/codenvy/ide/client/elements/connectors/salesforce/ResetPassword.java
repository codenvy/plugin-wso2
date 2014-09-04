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
package com.codenvy.ide.client.elements.connectors.salesforce;

import com.codenvy.ide.client.EditorResources;
import com.codenvy.ide.client.elements.Branch;
import com.codenvy.ide.client.elements.NameSpace;
import com.codenvy.ide.client.managers.MediatorCreatorsManager;
import com.codenvy.ide.collections.Array;
import com.codenvy.ide.collections.Collections;
import com.google.gwt.xml.client.Node;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

import static com.codenvy.ide.client.elements.connectors.salesforce.AbstractSalesForceConnector.ParameterEditorType.Inline;
import static com.codenvy.ide.client.elements.connectors.salesforce.AbstractSalesForceConnector.ParameterEditorType
        .NamespacedPropertyEditor;

/**
 * The Class describes ResetPassword connector for Salesforce group connectors. Also the class contains the business logic
 * that allows to display serialization representation depending of the current state of element. Deserelization mechanism allows to
 * restore the condition of the element when you open ESB project after saving.
 *
 * @author Valeriy Svydenko
 */
public class ResetPassword extends AbstractSalesForceConnector {

    public static final String ELEMENT_NAME       = "ResetPassword";
    public static final String SERIALIZATION_NAME = "salesforce.resetPassword";
    public static final String USER_ID            = "userId";

    private static final List<String> PROPERTIES = Arrays.asList(USER_ID);

    private String              userId;
    private String              userIdExpr;
    private ParameterEditorType parameterEditorType;
    private Array<NameSpace>    userIdNameSpaces;

    @Inject
    public ResetPassword(EditorResources resources, Provider<Branch> branchProvider, MediatorCreatorsManager mediatorCreatorsManager) {
        super(ELEMENT_NAME, ELEMENT_NAME, SERIALIZATION_NAME, PROPERTIES, false, true, resources, branchProvider, mediatorCreatorsManager);

        userId = "";
        userIdExpr = "";

        parameterEditorType = Inline;

        userIdNameSpaces = Collections.createArray();
    }

    /** {@inheritDoc} */
    @Nonnull
    @Override
    protected String serializeProperties() {
        return Inline.equals(parameterEditorType) ? convertPropertiesToXml(userId)
                                                  : convertPropertiesToXml(userIdExpr);
    }

    private String convertPropertiesToXml(@Nonnull String userId) {
        StringBuilder result = new StringBuilder("");

        if (!userId.isEmpty()) {
            result.append('<').append(USER_ID).append('>').append(userId).append("</").append(USER_ID).append('>');
        }

        return result.toString();
    }

    /** {@inheritDoc} */
    @Override
    public void applyProperty(@Nonnull Node node) {
        String nodeName = node.getNodeName();
        String nodeValue = node.getChildNodes().item(0).getNodeValue();

        boolean isInline = Inline.equals(parameterEditorType);

        switch (nodeName) {
            case USER_ID:
                if (isInline) {
                    userId = nodeValue;
                } else {
                    userIdExpr = nodeValue;

                    parameterEditorType = NamespacedPropertyEditor;
                }
                break;
        }
    }

    @Nonnull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@Nonnull String batchSize) {
        this.userId = batchSize;
    }

    @Nonnull
    public Array<NameSpace> getUserIdNameSpaces() {
        return userIdNameSpaces;
    }

    public void setUserIdNameSpaces(@Nonnull Array<NameSpace> userIdNameSpaces) {
        this.userIdNameSpaces = userIdNameSpaces;
    }

    @Nonnull
    public ParameterEditorType getParameterEditorType() {
        return parameterEditorType;
    }

    public void setParameterEditorType(@Nonnull ParameterEditorType parameterEditorType) {
        this.parameterEditorType = parameterEditorType;
    }

    @Nonnull
    public String getUserIdExpr() {
        return userIdExpr;
    }

    public void setUserIdExpr(@Nonnull String userIdExpr) {
        this.userIdExpr = userIdExpr;
    }
}