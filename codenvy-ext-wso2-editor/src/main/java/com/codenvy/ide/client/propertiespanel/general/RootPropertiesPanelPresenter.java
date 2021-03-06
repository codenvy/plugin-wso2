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
package com.codenvy.ide.client.propertiespanel.general;

import com.codenvy.ide.client.WSO2EditorLocalizationConstant;
import com.codenvy.ide.client.elements.RootElement;
import com.codenvy.ide.client.managers.PropertyTypeManager;
import com.codenvy.ide.client.managers.SelectionManager;
import com.codenvy.ide.client.propertiespanel.AbstractPropertiesPanel;
import com.codenvy.ide.client.propertiespanel.PropertiesPanelView;
import com.codenvy.ide.client.propertiespanel.PropertyPanelFactory;
import com.codenvy.ide.client.propertiespanel.property.PropertyValueChangedListener;
import com.codenvy.ide.client.propertiespanel.property.group.PropertyGroupPresenter;
import com.codenvy.ide.client.propertiespanel.property.simple.SimplePropertyPresenter;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import javax.annotation.Nonnull;

import static com.codenvy.ide.client.elements.RootElement.NAME;
import static com.codenvy.ide.client.elements.RootElement.ON_ERROR;

/**
 * The presenter that provides a business logic of root element properties panel. It provides an ability to work with all properties
 * of root element.
 *
 * @author Andrey Plotnikov
 */
public class RootPropertiesPanelPresenter extends AbstractPropertiesPanel<RootElement> {

    private SimplePropertyPresenter name;
    private SimplePropertyPresenter onError;

    @Inject
    public RootPropertiesPanelPresenter(PropertiesPanelView view,
                                        PropertyTypeManager propertyTypeManager,
                                        WSO2EditorLocalizationConstant locale,
                                        PropertyPanelFactory propertyPanelFactory,
                                        SelectionManager selectionManager) {

        super(view, propertyTypeManager, locale, propertyPanelFactory, selectionManager);

        prepareView();
    }

    private void prepareView() {
        PropertyGroupPresenter basicGroup = createGroup(locale.miscGroupTitle());

        PropertyValueChangedListener nameListener = new PropertyValueChangedListener() {
            @Override
            public void onPropertyChanged(@Nonnull String property) {
                element.putProperty(NAME, property);

                notifyListeners();
            }
        };
        name = createSimpleProperty(basicGroup, locale.rootName(), nameListener);

        PropertyValueChangedListener onErrorListener = new PropertyValueChangedListener() {
            @Override
            public void onPropertyChanged(@Nonnull String property) {
                element.putProperty(ON_ERROR, property);

                notifyListeners();
            }
        };
        onError = createSimpleProperty(basicGroup, locale.rootOnError(), onErrorListener);
    }

    /** {@inheritDoc} */
    @Override
    public void go(@Nonnull AcceptsOneWidget container) {
        super.go(container);

        name.setProperty(element.getProperty(NAME));
        onError.setProperty(element.getProperty(ON_ERROR));
    }

}