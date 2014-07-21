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
package com.codenvy.ide.client.elements;

import com.google.gwt.xml.client.Node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Andrey Plotnikov
 */
public class LoopBack extends RootElement {
    public static final String ELEMENT_NAME = "loopBack";

    private static final String DESCRIPTION_PROPERTY_NAME = "Description";

    private static final List<String> PROPERTIES                        = Arrays.asList(DESCRIPTION_PROPERTY_NAME);
    private static final List<String> INTERNAL_PROPERTIES               = Arrays.asList(X_PROPERTY_NAME,
                                                                                        Y_PROPERTY_NAME,
                                                                                        UUID_PROPERTY_NAME,
                                                                                        AUTO_ALIGN_PROPERTY_NAME,
                                                                                        DESCRIPTION_PROPERTY_NAME);
    private static final List<String> AVAILABLE_FOR_CONNECTION_ELEMENTS = Collections.emptyList();

    private String description;

    public LoopBack() {
        super(ELEMENT_NAME, PROPERTIES, INTERNAL_PROPERTIES);

        description = "enter_description";

        targetElements.put(Connection.CONNECTION_NAME, AVAILABLE_FOR_CONNECTION_ELEMENTS);
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    /** {@inheritDoc} */
    @Override
    @Nonnull
    protected String serializeProperties() {
        return "description=\"" + description + "\" ";
    }

    /** {@inheritDoc} */
    @Override
    public void applyProperty(@Nonnull Node node) {
        String nodeName = node.getNodeName();
        String nodeValue = node.getChildNodes().item(0).getNodeValue();

        switch (nodeName) {
            case AbstractShape.X_PROPERTY_NAME:
                setX(Integer.valueOf(nodeValue));
                break;
            case AbstractShape.Y_PROPERTY_NAME:
                setY(Integer.valueOf(nodeValue));
                break;
            case AbstractElement.UUID_PROPERTY_NAME:
                id = nodeValue;
                break;
            case AbstractShape.AUTO_ALIGN_PROPERTY_NAME:
                setAutoAlignmentParam(Boolean.valueOf(nodeValue));
                break;
            case DESCRIPTION_PROPERTY_NAME:
                description = String.valueOf(nodeValue);
                break;
        }
    }

}