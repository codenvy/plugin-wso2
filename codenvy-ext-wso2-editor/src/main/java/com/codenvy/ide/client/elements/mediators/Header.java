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
package com.codenvy.ide.client.elements.mediators;

import com.codenvy.ide.client.EditorResources;
import com.codenvy.ide.client.elements.AbstractElement;
import com.codenvy.ide.client.elements.Branch;
import com.codenvy.ide.client.elements.NameSpace;
import com.codenvy.ide.client.managers.ElementCreatorsManager;
import com.google.gwt.xml.client.Node;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.codenvy.ide.client.elements.NameSpace.applyNameSpace;
import static com.codenvy.ide.client.elements.NameSpace.convertNameSpacesToXML;
import static com.codenvy.ide.client.elements.mediators.Action.REMOVE;
import static com.codenvy.ide.client.elements.mediators.Action.SET;
import static com.codenvy.ide.client.elements.mediators.Header.HeaderValueType.INLINE;
import static com.codenvy.ide.client.elements.mediators.Header.HeaderValueType.LITERAL;
import static com.codenvy.ide.client.elements.mediators.Header.ScopeType.SYNAPSE;

/**
 * The class which describes state of Header mediator and also has methods for changing it. Also the class contains the business
 * logic that allows to display serialization representation depending of the current state of element. Deserelization mechanism allows to
 * restore the condition of the element when you open ESB project after saving. For more information about Header mediator go to
 * <a href=" https://docs.wso2.com/display/ESB460/Header+Mediator"> https://docs.wso2.com/display/ESB460/Header+Mediator</a>
 *
 * @author Andrey Plotnikov
 * @author Dmitry Shnurenko
 * @author Valeriy Svydenko
 */
public class Header extends AbstractElement {
    public static final String ELEMENT_NAME       = "Header";
    public static final String SERIALIZATION_NAME = "header";

    public static final Key<Action>          ACTION        = new Key<>("HeaderAction");
    public static final Key<HeaderValueType> VALUE_TYPE    = new Key<>("HeaderValueType");
    public static final Key<ScopeType>       SCOPE         = new Key<>("HeaderScope");
    public static final Key<String>          HEADER_NAME   = new Key<>("HeaderName");
    public static final Key<String>          VALUE_LITERAL = new Key<>("HeaderValue");
    public static final Key<String>          EXPRESSION    = new Key<>("HeaderExpression");
    public static final Key<String>          INLINE_XML    = new Key<>("HeaderInlineXml");

    public static final Key<List<NameSpace>> HEADER_NAMESPACES     = new Key<>("HeaderNameSpaces");
    public static final Key<List<NameSpace>> EXPRESSION_NAMESPACES = new Key<>("HeaderExpressionNameSpaces");

    private static final String NAME_ATTRIBUTE       = "name";
    private static final String VALUE_ATTRIBUTE      = "value";
    private static final String EXPRESSION_ATTRIBUTE = "expression";
    private static final String ACTION_ATTRIBUTE     = "action";
    private static final String SCOPE_ATTRIBUTE      = "scope";

    private static final List<String> PROPERTIES = Collections.emptyList();
    private final Provider<NameSpace> nameSpaceProvider;
    private       boolean             isFirstNamespace;

    @Inject
    public Header(EditorResources resources,
                  Provider<Branch> branchProvider,
                  ElementCreatorsManager elementCreatorsManager,
                  Provider<NameSpace> nameSpaceProvider) {

        super(ELEMENT_NAME,
              ELEMENT_NAME,
              SERIALIZATION_NAME,
              PROPERTIES,
              false,
              false,
              resources.header(),
              branchProvider,
              elementCreatorsManager);

        this.nameSpaceProvider = nameSpaceProvider;

        putProperty(ACTION, SET);
        putProperty(SCOPE, SYNAPSE);
        putProperty(HEADER_NAME, "To");
        putProperty(VALUE_TYPE, LITERAL);
        putProperty(VALUE_LITERAL, "header_value");
        putProperty(EXPRESSION, "/default/expression");
        putProperty(INLINE_XML, "");
        putProperty(HEADER_NAMESPACES, new ArrayList<NameSpace>());
        putProperty(EXPRESSION_NAMESPACES, new ArrayList<NameSpace>());
    }

    /** {@inheritDoc} */
    @Nonnull
    @Override
    protected String serializeProperties() {
        HeaderValueType valueType = getProperty(VALUE_TYPE);
        String inlineXml = getProperty(INLINE_XML);

        if (valueType != null && !valueType.equals(INLINE) || inlineXml != null && inlineXml.isEmpty() || inlineXml == null) {
            return "";
        }

        int index = inlineXml.indexOf(">");
        String tag = inlineXml.substring(0, index);

        String tagName = inlineXml.substring(0, tag.contains("/") ? index - 1 : index);
        String restString = inlineXml.substring(tag.contains("/") ? index - 1 : index);

        return tagName + " xmlns=\"\"" + restString + '\n';
    }

    /** {@inheritDoc} */
    @Override
    @Nonnull
    protected String serializeAttributes() {
        Map<String, String> attributes = new LinkedHashMap<>();
        StringBuilder result = new StringBuilder();

        List<NameSpace> headerNameSpaces = getProperty(HEADER_NAMESPACES);

        if (headerNameSpaces != null && !headerNameSpaces.isEmpty()) {
            NameSpace nameSpace = headerNameSpaces.get(headerNameSpaces.size() - 1);
            result.append(nameSpace.toString()).append(' ');
        }

        List<NameSpace> expressionNamespaces = getProperty(EXPRESSION_NAMESPACES);

        if (expressionNamespaces != null) {
            result.append(convertNameSpacesToXML(expressionNamespaces));
        }

        setDefaultAttributes(attributes);

        Action action = getProperty(ACTION);

        if (action != null && action.equals(REMOVE)) {
            attributes.remove(VALUE_ATTRIBUTE);
            attributes.remove(EXPRESSION_ATTRIBUTE);
        } else {
            attributes.remove(ACTION_ATTRIBUTE);
        }

        HeaderValueType valueType = getProperty(VALUE_TYPE);

        if (valueType != null) {
            attributes.remove(valueType.equals(HeaderValueType.EXPRESSION) ? VALUE_ATTRIBUTE : EXPRESSION_ATTRIBUTE);

            if (INLINE.equals(valueType)) {
                attributes.remove(VALUE_ATTRIBUTE);
                attributes.remove(NAME_ATTRIBUTE);
                attributes.remove(EXPRESSION_ATTRIBUTE);
                return convertAttributesToXML(attributes);
            }
        }

        return result + convertAttributesToXML(attributes);
    }

    /**
     * Sets default value of attributes of element
     *
     * @param attributes
     *         list of attributes which need to set to element by default
     */
    private void setDefaultAttributes(@Nonnull Map<String, String> attributes) {
        attributes.put(NAME_ATTRIBUTE, getProperty(HEADER_NAME));

        ScopeType scopeType = getProperty(SCOPE);
        if (scopeType != null) {
            attributes.put(SCOPE_ATTRIBUTE, scopeType.getValue());
        }

        attributes.put(VALUE_ATTRIBUTE, getProperty(VALUE_LITERAL));
        attributes.put(EXPRESSION_ATTRIBUTE, getProperty(EXPRESSION));

        Action action = getProperty(ACTION);

        if (action != null) {
            attributes.put(ACTION_ATTRIBUTE, action.getValue());
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void applyAttribute(@Nonnull String attributeName, @Nonnull String attributeValue) {
        switch (attributeName) {
            case NAME_ATTRIBUTE:
                putProperty(HEADER_NAME, attributeValue);
                break;

            case SCOPE_ATTRIBUTE:
                putProperty(SCOPE, ScopeType.getItemByValue(attributeValue));
                break;

            case VALUE_ATTRIBUTE:
                putProperty(VALUE_LITERAL, attributeValue);
                break;

            case EXPRESSION_ATTRIBUTE:
                putProperty(EXPRESSION, attributeValue);
                putProperty(VALUE_TYPE, HeaderValueType.EXPRESSION);
                break;

            case ACTION_ATTRIBUTE:
                putProperty(ACTION, Action.getItemByValue(attributeValue));
                break;

            default:
                applyNameSpaces(attributeName, attributeValue);
        }
    }

    private void applyNameSpaces(@Nonnull String attributeName, @Nonnull String attributeValue) {
        if (isFirstNamespace) {
            applyNameSpace(nameSpaceProvider, getProperty(HEADER_NAMESPACES), attributeName, attributeValue);
            isFirstNamespace = false;
        } else {
            applyNameSpace(nameSpaceProvider, getProperty(EXPRESSION_NAMESPACES), attributeName, attributeValue);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void deserialize(@Nonnull Node node) {
        isFirstNamespace = true;

        readXMLAttributes(node);

        if (node.hasChildNodes()) {
            String item = node.getChildNodes().item(0).toString();

            int indexFirst = item.indexOf(" ");
            int indexLast = item.indexOf(">");

            String tagName = item.substring(0, indexLast);

            String xmlns = tagName.substring(indexFirst, tagName.contains("/") ? indexLast - 1 : indexLast);

            String inlineXml = item.replace(xmlns, "");

            putProperty(INLINE_XML, inlineXml);
            putProperty(VALUE_TYPE, INLINE);
        }
    }

    public enum HeaderValueType {
        LITERAL, EXPRESSION, INLINE;

        public static final String TYPE_NAME = "HeaderValueType";
    }

    public enum ScopeType {
        SYNAPSE("Synapse"), TRANSPORT("transport");

        public static final String TYPE_NAME = "ScopeType";

        private final String value;

        ScopeType(@Nonnull String value) {
            this.value = value;
        }

        @Nonnull
        public String getValue() {
            return value;
        }

        @Nonnull
        public static ScopeType getItemByValue(@Nonnull String value) {
            if ("Synapse".equals(value)) {
                return SYNAPSE;
            } else {
                return TRANSPORT;
            }
        }
    }

}