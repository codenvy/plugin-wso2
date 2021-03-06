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
package com.codenvy.ide.client.propertiespanel.common.addpropertydialog;

import com.codenvy.ide.client.elements.AbstractEntityElement;

import javax.annotation.Nonnull;

/**
 * The entity which includes property, that may be modified, must implement this interface. This
 * method will be call when property is changed.
 *
 * @author Dmitry Shnurenko
 */
public interface AddPropertyCallBack<T extends AbstractEntityElement> {
    /**
     * Performs some actions when property was changed.
     *
     * @param property
     *         changed property of address end point
     */
    void onPropertyChanged(@Nonnull T property);
}