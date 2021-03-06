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
import com.google.gwt.xml.client.Node;
import com.google.inject.Inject;

import javax.annotation.Nonnull;

import static com.codenvy.ide.client.elements.NameSpace.PREFIX;
import static com.codenvy.ide.client.elements.mediators.payload.Format.FormatType.INLINE;

/**
 * The class which describes state of Format property of PayloadFactory mediator and also has methods for changing it. Also the class
 * contains the business  logic that allows to display serialization representation depending of the current state of element.
 * Deserelization mechanism allows to restore the condition of the element when you open ESB project after saving.
 *
 * @author Dmitry Shnurenko
 * @author Andrey Plotnikov
 * @author Valeriy Svydenko
 */
public class Format extends AbstractEntityElement {

    public static final String FORMAT_SERIALIZATION_NAME = "format";

    public static final Key<FormatType> FORMAT_TYPE   = new Key<>("PayloadFormatType");
    public static final Key<String>     FORMAT_KEY    = new Key<>("PayloadFormatKey");
    public static final Key<String>     FORMAT_INLINE = new Key<>("PayloadFormatInline");

    private static final String FORMAT_KEY_ATTRIBUTE_NAME = "key";

    @Inject
    public Format() {
        putProperty(FORMAT_TYPE, INLINE);
        putProperty(FORMAT_KEY, "default/key");
        putProperty(FORMAT_INLINE, "<inline/>");
    }

    /**
     * Apply attributes from XML node to the diagram element
     *
     * @param node
     *         XML node that need to be analyzed
     */
    public void applyAttributes(@Nonnull Node node) {
        // we should use condition because it is impossible to use hasAttributes method because it throws exception
        if (node.getAttributes().getLength() < 1) {
            return;
        }

        Node attribute = node.getAttributes().item(0);

        putProperty(FORMAT_KEY, attribute.getNodeValue());
    }

    /**
     * Apply properties from XML node to the diagram element
     *
     * @param node
     *         XML node that need to be analyzed
     */
    public void applyProperty(@Nonnull Node node) {
        if (!node.hasChildNodes()) {
            return;
        }

        putProperty(FORMAT_INLINE, convertNodeToString(node));
    }

    private String convertNodeToString(@Nonnull Node node) {
        String item = node.getChildNodes().item(0).toString();

        int indexFirst = item.indexOf(' ');
        int indexLast = item.indexOf('>');

        String tagName = item.substring(0, indexLast);

        String xmlns = tagName.substring(indexFirst, tagName.contains("/") ? indexLast - 1 : indexLast);

        return item.replace(xmlns, "");
    }

    /** @return serialization representation of element attributes */
    @Nonnull
    public String serializeAttributes() {
        return FORMAT_KEY_ATTRIBUTE_NAME + "=\"" + getProperty(FORMAT_KEY) + '"';
    }

    /** @return serialized representation of the source element */
    @Nonnull
    public String serialize(boolean isJson) {
        String formatInline = getProperty(FORMAT_INLINE);

        if (formatInline == null) {
            return "";
        }

        StringBuilder xml = new StringBuilder();

        boolean isInline = INLINE.equals(getProperty(FORMAT_TYPE));
        boolean isInlineJson = isJson && isInline;

        xml.append('<').append(FORMAT_SERIALIZATION_NAME);

        if (isInline) {
            serializeInlineFormat(xml, formatInline);

        } else {
            xml.append(' ').append(serializeAttributes()).append("/>\n");
        }

        return isInlineJson ? serializeInlineJsonFormat(formatInline) : xml.toString();
    }

    private void serializeInlineFormat(@Nonnull StringBuilder stringBuilder, @Nonnull String formatInline) {
        stringBuilder.append('>');

        int index = formatInline.indexOf(">");

        String tag = formatInline.substring(0, index);

        String tagName = formatInline.substring(0, tag.contains("/") ? index - 1 : index);
        String restString = formatInline.substring(tag.contains("/") ? index - 1 : index);

        stringBuilder.append(tagName).append(' ' + PREFIX + "=\"\"").append(restString)
                     .append("</").append(FORMAT_SERIALIZATION_NAME).append('>');
    }

    private String serializeInlineJsonFormat(@Nonnull String formatInline) {

        return '<' + FORMAT_SERIALIZATION_NAME + '>' + formatInline.replace("<", "&lt;").replace(">", "&gt;") +
               "</" + FORMAT_SERIALIZATION_NAME + '>';
    }

    public enum FormatType {
        INLINE("Inline"), REGISTRY("Registry");

        public static final String TYPE_NAME = "FormatType";

        private final String value;

        FormatType(@Nonnull String value) {
            this.value = value;
        }

        @Nonnull
        public String getValue() {
            return value;
        }

        @Nonnull
        public static FormatType getItemByValue(@Nonnull String value) {
            if ("Inline".equals(value)) {
                return INLINE;
            } else {
                return REGISTRY;
            }
        }
    }

}