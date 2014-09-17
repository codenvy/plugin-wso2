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
package com.codenvy.ide.client.propertiespanel.property.complex;

import com.codenvy.ide.client.mvp.AbstractView;
import com.google.inject.ImplementedBy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The abstract representation of graphical part of the complex property. It provides an ability to change property title and property
 * value.
 *
 * @author Andrey Plotnikov
 */
@ImplementedBy(ComplexPropertyViewImpl.class)
public abstract class ComplexPropertyView extends AbstractView<ComplexPropertyView.ActionDelegate> {

    /**
     * Changes title of property on the view.
     *
     * @param title
     *         title that needs to be changed
     */
    public abstract void setTitle(@Nullable String title);

    /**
     * Changes property value on the view.
     *
     * @param property
     *         property value that need to be set
     */
    public abstract void setProperty(@Nullable String property);

    public interface ActionDelegate extends AbstractView.ActionDelegate {
        /** Performs some actions in response to a user's clicking on the 'Edit' button. */
        void onEditButtonClicked();
    }

}