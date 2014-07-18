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
import java.util.List;

/**
 * @author Andrey Plotnikov
 */
public class PayloadFactory extends RootElement {
    public static final String ELEMENT_NAME = "payloadFactory";

    private static final String PAYLOAD_FORMAT_PROPERTY_NAME = "PayloadFormat";
    private static final String FORMAT_PROPERTY_NAME         = "Format";
    private static final String ARGS_PROPERTY_NAME           = "Args";
    private static final String MEDIA_TYPE_PROPERTY_NAME     = "MediaType";
    private static final String DESCRIPTION_PROPERTY_NAME    = "Description";

    private static final List<String> PROPERTIES                        = Arrays.asList(PAYLOAD_FORMAT_PROPERTY_NAME,
                                                                                        FORMAT_PROPERTY_NAME,
                                                                                        ARGS_PROPERTY_NAME,
                                                                                        MEDIA_TYPE_PROPERTY_NAME,
                                                                                        DESCRIPTION_PROPERTY_NAME);
    private static final List<String> INTERNAL_PROPERTIES               = Arrays.asList("x", "y", "uuid", "autoAlign",
                                                                                        PAYLOAD_FORMAT_PROPERTY_NAME,
                                                                                        FORMAT_PROPERTY_NAME,
                                                                                        ARGS_PROPERTY_NAME,
                                                                                        MEDIA_TYPE_PROPERTY_NAME,
                                                                                        DESCRIPTION_PROPERTY_NAME);
    private static final List<String> AVAILABLE_FOR_CONNECTION_ELEMENTS = Arrays.asList(Log.ELEMENT_NAME,
                                                                                        Property.ELEMENT_NAME,
                                                                                        PayloadFactory.ELEMENT_NAME,
                                                                                        Send.ELEMENT_NAME,
                                                                                        Header.ELEMENT_NAME,
                                                                                        Respond.ELEMENT_NAME,
                                                                                        Filter.ELEMENT_NAME,
                                                                                        Switch_mediator.ELEMENT_NAME,
                                                                                        Sequence.ELEMENT_NAME,
                                                                                        Enrich.ELEMENT_NAME,
                                                                                        LoopBack.ELEMENT_NAME,
                                                                                        CallTemplate.ELEMENT_NAME,
                                                                                        Call.ELEMENT_NAME);

    private String payloadFormat;
    private String format;
    private String args;
    private String mediaType;
    private String description;

    public PayloadFactory() {
        super(ELEMENT_NAME, PROPERTIES, INTERNAL_PROPERTIES);

        payloadFormat = "inline";
        format = "inline";
        args = "enter_arguments";
        mediaType = "xml";
        description = "enter_description";

        targetElements.put(Connection.CONNECTION_NAME, AVAILABLE_FOR_CONNECTION_ELEMENTS);
    }

    @Nullable
    public String getPayloadFormat() {
        return payloadFormat;
    }

    public void setPayloadFormat(@Nullable String payloadFormat) {
        this.payloadFormat = payloadFormat;
    }

    @Nullable
    public String getFormat() {
        return format;
    }

    public void setFormat(@Nullable String format) {
        this.format = format;
    }

    @Nullable
    public String getArgs() {
        return args;
    }

    public void setArgs(@Nullable String args) {
        this.args = args;
    }

    @Nullable
    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(@Nullable String mediaType) {
        this.mediaType = mediaType;
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
        return "payloadFormat=\"" + payloadFormat + "\" " +
               "format=\"" + format + "\" " +
               "args=\"" + args + "\" " +
               "mediaType=\"" + mediaType + "\" " +
               "description=\"" + description + "\" ";
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
            case PAYLOAD_FORMAT_PROPERTY_NAME:
                payloadFormat = String.valueOf(nodeValue);
                break;
            case FORMAT_PROPERTY_NAME:
                format = String.valueOf(nodeValue);
                break;
            case ARGS_PROPERTY_NAME:
                args = String.valueOf(nodeValue);
                break;
            case MEDIA_TYPE_PROPERTY_NAME:
                mediaType = String.valueOf(nodeValue);
                break;
            case DESCRIPTION_PROPERTY_NAME:
                description = String.valueOf(nodeValue);
                break;
        }
    }

}