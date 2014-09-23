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
package com.codenvy.ide.client.elements.connectors.googlespreadsheet;

import com.codenvy.ide.client.EditorResources;
import com.codenvy.ide.client.elements.Branch;
import com.codenvy.ide.client.elements.NameSpace;
import com.codenvy.ide.client.elements.connectors.AbstractConnector;
import com.codenvy.ide.client.managers.ElementCreatorsManager;
import com.google.gwt.xml.client.Node;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.codenvy.ide.client.elements.connectors.AbstractConnector.ParameterEditorType.Inline;
import static com.codenvy.ide.client.elements.connectors.AbstractConnector.ParameterEditorType.NamespacedPropertyEditor;
import static java.util.Collections.emptyList;

/**
 * The Class describes CreateSpreadsheet connector for GoogleSpreadsheet group connectors. Also the class contains the business logic
 * that allows to display serialization representation depending of the current state of element. Deserelization mechanism allows to
 * restore the condition of the element when you open ESB project after saving.
 *
 * @author Valeriy Svydenko
 */
public class CreateSpreadsheet extends AbstractConnector {

    public static final String ELEMENT_NAME       = "CreateSpreadsheet";
    public static final String SERIALIZATION_NAME = "googlespreadsheet.createSpreadsheet";

    private static final String SPREADSHEET_NAME = "spreadsheetName";
    private static final String WORKSHEET_COUNT  = "worksheetCount";

    private static final List<String> PROPERTIES = Arrays.asList(SPREADSHEET_NAME, WORKSHEET_COUNT);

    private String spreadsheetName;
    private String worksheetCount;

    private String spreadsheetNameExpression;
    private String worksheetCountExpression;

    private List<NameSpace> spreadsheetNameNS;
    private List<NameSpace> worksheetCountNS;

    @Inject
    public CreateSpreadsheet(EditorResources resources,
                             Provider<Branch> branchProvider,
                             ElementCreatorsManager elementCreatorsManager) {
        super(ELEMENT_NAME,
              ELEMENT_NAME,
              SERIALIZATION_NAME,
              PROPERTIES,
              false,
              true,
              resources.googleSpreadsheetElement(),
              branchProvider,
              elementCreatorsManager);

        spreadsheetName = "";
        worksheetCount = "";

        spreadsheetNameExpression = "";
        worksheetCountExpression = "";

        spreadsheetNameNS = emptyList();
        worksheetCountNS = emptyList();
    }

    /** {@inheritDoc} */
    @Nonnull
    @Override
    protected String serializeProperties() {
        Map<String, String> properties = new LinkedHashMap<>();

        boolean isInline = parameterEditorType.equals(Inline);

        properties.put(SPREADSHEET_NAME, isInline ? spreadsheetName : spreadsheetNameExpression);
        properties.put(WORKSHEET_COUNT, isInline ? worksheetCount : worksheetCountExpression);

        return convertPropertiesToXMLFormat(properties);
    }

    /** {@inheritDoc} */
    @Override
    protected void applyProperty(@Nonnull Node node) {
        String nodeName = node.getNodeName();
        String nodeValue = node.getChildNodes().item(0).getNodeValue();
        boolean isInline = Inline.equals(parameterEditorType);

        switch (nodeName) {
            case SPREADSHEET_NAME:
                if (isInline) {
                    spreadsheetName = nodeValue;
                } else {
                    spreadsheetNameExpression = nodeValue;

                    parameterEditorType = NamespacedPropertyEditor;
                }
                break;

            case WORKSHEET_COUNT:
                if (isInline) {
                    worksheetCount = nodeValue;
                } else {
                    worksheetCountExpression = nodeValue;

                    parameterEditorType = NamespacedPropertyEditor;
                }
                break;
        }
    }

    @Nonnull
    public String getSpreadsheetName() {
        return spreadsheetName;
    }

    public void setSpreadsheetName(@Nonnull String spreadsheetName) {
        this.spreadsheetName = spreadsheetName;
    }

    @Nonnull
    public String getWorksheetCount() {
        return worksheetCount;
    }

    public void setWorksheetCount(@Nonnull String worksheetCount) {
        this.worksheetCount = worksheetCount;
    }

    @Nonnull
    public String getSpreadsheetNameExpression() {
        return spreadsheetNameExpression;
    }

    public void setSpreadsheetNameExpression(@Nonnull String spreadsheetNameExpression) {
        this.spreadsheetNameExpression = spreadsheetNameExpression;
    }

    @Nonnull
    public String getWorksheetCountExpression() {
        return worksheetCountExpression;
    }

    public void setWorksheetCountExpression(@Nonnull String worksheetCountExpression) {
        this.worksheetCountExpression = worksheetCountExpression;
    }

    @Nonnull
    public List<NameSpace> getSpreadsheetNameNS() {
        return spreadsheetNameNS;
    }

    public void setSpreadsheetNameNS(@Nonnull List<NameSpace> spreadsheetNameNS) {
        this.spreadsheetNameNS = spreadsheetNameNS;
    }

    @Nonnull
    public List<NameSpace> getWorksheetCountNS() {
        return worksheetCountNS;
    }

    public void setWorksheetCountNS(@Nonnull List<NameSpace> worksheetCountNS) {
        this.worksheetCountNS = worksheetCountNS;
    }

}