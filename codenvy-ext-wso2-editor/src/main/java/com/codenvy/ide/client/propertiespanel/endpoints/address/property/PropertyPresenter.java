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
package com.codenvy.ide.client.propertiespanel.endpoints.address.property;

import com.codenvy.ide.client.WSO2EditorLocalizationConstant;
import com.codenvy.ide.client.elements.endpoints.address.Property;
import com.codenvy.ide.client.propertiespanel.common.addpropertydialog.AddPropertyAddressEPPresenter;
import com.codenvy.ide.client.propertiespanel.common.addpropertydialog.AddPropertyCallBack;
import com.codenvy.ide.client.propertiespanel.common.addpropertydialog.general.AddPropertyPresenter;
import com.codenvy.ide.ui.dialogs.DialogFactory;
import com.codenvy.ide.ui.dialogs.message.MessageDialog;
import com.google.inject.Inject;

import javax.annotation.Nonnull;
import java.util.List;

import static com.codenvy.ide.client.elements.endpoints.address.Property.NAME;
import static com.codenvy.ide.client.elements.endpoints.address.Property.copyEndPointPropertyList;

/**
 * The class provides the business logic that allows editor to react on user's action related to change of Address's
 * endpoint properties. Logic which provides the class allows add, remove and edit properties of Address endpoint via
 * special dialog window.
 *
 * @author Andrey Plotnikov
 * @author Dmitry Shnurenko
 * @author Valeriy Svydenko
 */
public class PropertyPresenter implements PropertyView.ActionDelegate {

    private final PropertyView                   view;
    private final AddPropertyPresenter<Property> editPropertyPresenter;
    private final AddPropertyCallBack<Property>  addCallBack;
    private final AddPropertyCallBack<Property>  editCallBack;

    private Property                  selectedProperty;
    private PropertiesChangedCallback callback;
    private List<Property>            properties;

    @Inject
    public PropertyPresenter(PropertyView view,
                             AddPropertyAddressEPPresenter editPropertyPresenter,
                             WSO2EditorLocalizationConstant locale,
                             DialogFactory dialogFactory) {
        this.view = view;
        this.view.setDelegate(this);

        this.editPropertyPresenter = editPropertyPresenter;

        final MessageDialog errorMessageDialog =
                dialogFactory.createMessageDialog(locale.errorMessage(), locale.nameAlreadyExistsError(), null);

        this.addCallBack = new AddPropertyCallBack<Property>() {
            @Override
            public void onPropertyChanged(@Nonnull Property property) {
                if (!isElementNameContained(property)) {
                    properties.add(property);

                    PropertyPresenter.this.view.setProperties(properties);

                    PropertyPresenter.this.editPropertyPresenter.hideDialog();
                } else {
                    errorMessageDialog.show();
                }
            }
        };

        this.editCallBack = new AddPropertyCallBack<Property>() {
            @Override
            public void onPropertyChanged(@Nonnull Property property) {
                int index = properties.indexOf(selectedProperty);

                String innerPropertyName = property.getProperty(NAME);
                String selectedPropertyName = selectedProperty.getProperty(NAME);

                if (innerPropertyName == null || selectedPropertyName == null) {
                    return;
                }

                if (innerPropertyName.equals(selectedPropertyName) || !isElementNameContained(property)) {
                    properties.set(index, property);

                    PropertyPresenter.this.view.setProperties(properties);

                    PropertyPresenter.this.editPropertyPresenter.hideDialog();

                } else {
                    errorMessageDialog.show();
                }
            }
        };
    }

    /**
     * Returns true if list of properties contains a property with name that already exists.
     *
     * @param property
     *         value of property which need to check
     */
    private boolean isElementNameContained(@Nonnull Property property) {
        String innerPropertyName = property.getProperty(NAME);

        if (innerPropertyName == null) {
            return false;
        }

        for (Property prop : properties) {
            if (innerPropertyName.equals(prop.getProperty(NAME))) {
                return true;
            }
        }

        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void onOkButtonClicked() {
        view.hideDialog();

        callback.onPropertiesChanged(properties);
    }

    /** {@inheritDoc} */
    @Override
    public void onCancelButtonClicked() {
        view.hideDialog();
    }

    /** {@inheritDoc} */
    @Override
    public void onAddButtonClicked() {
        editPropertyPresenter.showDialog(null, addCallBack);
    }

    /** {@inheritDoc} */
    @Override
    public void onEditButtonClicked() {
        editPropertyPresenter.showDialog(selectedProperty, editCallBack);
    }

    /** {@inheritDoc} */
    @Override
    public void onRemoveButtonClicked() {
        properties.remove(selectedProperty);
        selectedProperty = null;

        view.setProperties(properties);
    }

    /** {@inheritDoc} */
    @Override
    public void onPropertySelected(@Nonnull Property property) {
        selectedProperty = property;
    }

    public void showDialog(@Nonnull PropertiesChangedCallback innerCallback, @Nonnull List<Property> propertiesArray) {
        selectedProperty = null;

        callback = innerCallback;
        properties = copyEndPointPropertyList(propertiesArray);

        view.setProperties(properties);

        view.showDialog();
    }

    public interface PropertiesChangedCallback {
        /**
         * Performs some actions when properties was changed.
         *
         * @param properties
         *         changed list of properties
         */
        void onPropertiesChanged(@Nonnull List<Property> properties);
    }

}