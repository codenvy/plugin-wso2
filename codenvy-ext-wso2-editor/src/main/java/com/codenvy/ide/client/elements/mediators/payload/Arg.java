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
package com.codenvy.ide.client.elements.mediators.payload;

import com.codenvy.ide.client.elements.AbstractEntityElement;
import com.codenvy.ide.client.elements.NameSpace;
import com.codenvy.ide.collections.Array;
import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.util.StringUtils;
import com.google.gwt.xml.client.Node;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.codenvy.ide.client.elements.NameSpace.PREFIX;
import static com.codenvy.ide.client.elements.mediators.payload.Arg.ArgType.EXPRESSION;
import static com.codenvy.ide.client.elements.mediators.payload.Arg.ArgType.VALUE;
import static com.codenvy.ide.client.elements.mediators.payload.Arg.Evaluator.XML;

/**
 * The class which describes state of Arg property of PayloadFactory mediator and also has methods for changing it. Also the class contains
 * the business  logic that allows to display serialization representation depending of the current state of element. Deserelization
 * mechanism allows to restore the condition of the element when you open ESB project after saving.
 *
 * @author Valeriy Svydenko
 * @author Dmitry Shnurenko
 * @author Andrey Plotnikov
 */
public class Arg extends AbstractEntityElement {

    public static final String ARG_SERIALIZATION_NAME = "arg";

    public static final Key<ArgType>          ARG_TYPE       = new Key<>("PayloadArgType");
    public static final Key<Evaluator>        ARG_EVALUATOR  = new Key<>("PayloadArgEvaluator");
    public static final Key<String>           ARG_EXPRESSION = new Key<>("PayloadArgExpression");
    public static final Key<String>           ARG_VALUE      = new Key<>("PayloadArgValue");
    public static final Key<Array<NameSpace>> ARG_NAMESPACES = new Key<>("PayloadArgNameSpaces");

    private static final String EVALUATOR_ATTRIBUTE_NAME  = "evaluator";
    private static final String EXPRESSION_ATTRIBUTE_NAME = "expression";
    private static final String VALUE_ATTRIBUTE_NAME      = "value";

    private final Provider<NameSpace> nameSpaceProvider;
    private final Provider<Arg>       argProvider;

    @Inject
    public Arg(Provider<NameSpace> nameSpaceProvider, Provider<Arg> argProvider) {
        this.nameSpaceProvider = nameSpaceProvider;
        this.argProvider = argProvider;

        putProperty(ARG_TYPE, VALUE);
        putProperty(ARG_EVALUATOR, XML);
        putProperty(ARG_EXPRESSION, "/default/expression");
        putProperty(ARG_VALUE, "default");
        putProperty(ARG_NAMESPACES, Collections.<NameSpace>createArray());
    }

    /** @return serialization representation of element attributes */
    @Nonnull
    private String serializeAttributes() {
        Evaluator evaluator = getProperty(ARG_EVALUATOR);

        if (evaluator == null) {
            return "";
        }

        Map<String, String> prop = new LinkedHashMap<>();

        if (EXPRESSION.equals(getProperty(ARG_TYPE))) {
            prop.put(EVALUATOR_ATTRIBUTE_NAME, evaluator.getValue());
            prop.put(EXPRESSION_ATTRIBUTE_NAME, getProperty(ARG_EXPRESSION));
        } else {
            prop.put(VALUE_ATTRIBUTE_NAME, getProperty(ARG_VALUE));
        }

        return convertNameSpaceToXMLFormat(getProperty(ARG_NAMESPACES)) + convertAttributesToXMLFormat(prop);
    }

    /** @return serialization representation of element */
    @Nonnull
    public String serialize() {
        return '<' + ARG_SERIALIZATION_NAME + ' ' + serializeAttributes() + "/>\n";
    }

    /** {@inheritDoc} */
    @Override
    protected void applyAttribute(@Nonnull String attributeName, @Nonnull String attributeValue) {
        switch (attributeName) {
            case EVALUATOR_ATTRIBUTE_NAME:
                putProperty(ARG_EVALUATOR, Evaluator.valueOf(attributeValue));
                break;

            case EXPRESSION_ATTRIBUTE_NAME:
                putProperty(ARG_EXPRESSION, attributeValue);
                putProperty(ARG_TYPE, EXPRESSION);
                break;

            case VALUE_ATTRIBUTE_NAME:
                putProperty(ARG_VALUE, attributeValue);
                break;

            default:
                applyNameSpaces(attributeName, attributeValue);
        }
    }

    /**
     * Applies attributes from xml representation to current element, when it is deserializing.
     *
     * @param node
     *         node for which need to apply attributes
     */
    public void applyAttributes(@Nonnull Node node) {
        readXMLAttributes(node);
    }

    private void applyNameSpaces(@Nonnull String attributeName, @Nonnull String attributeValue) {
        Array<NameSpace> nameSpaces = getProperty(ARG_NAMESPACES);

        if (!StringUtils.startsWith(PREFIX, attributeName, true) || nameSpaces == null) {
            return;
        }

        String name = StringUtils.trimStart(attributeName, PREFIX + ':');

        NameSpace nameSpace = nameSpaceProvider.get();

        nameSpace.setPrefix(name);
        nameSpace.setUri(attributeValue);

        nameSpaces.add(nameSpace);
    }

    /** @return copy of element */
    @Nonnull
    public Arg copy() {
        return argProvider.get();
    }

    public enum ArgType {
        VALUE("Value"), EXPRESSION("Expression");

        public static final String TYPE_NAME = "ArgType";

        private final String value;

        ArgType(@Nonnull String value) {
            this.value = value;
        }

        @Nonnull
        public String getValue() {
            return value;
        }

        @Nonnull
        public static ArgType getItemByValue(@Nonnull String value) {
            if ("Value".equals(value)) {
                return VALUE;
            } else {
                return EXPRESSION;
            }
        }
    }

    public enum Evaluator {
        XML("xml"), JSON("json");

        public static final String TYPE_NAME = "Evaluator";

        private final String value;

        Evaluator(@Nonnull String value) {
            this.value = value;
        }

        @Nonnull
        public String getValue() {
            return value;
        }

        @Nonnull
        public static Evaluator getItemByValue(@Nonnull String value) {
            if ("xml".equals(value)) {
                return XML;
            } else {
                return JSON;
            }
        }
    }
}