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
package com.codenvy.ide.client.managers;

import com.codenvy.ide.client.elements.Shape;
import com.codenvy.ide.client.mvp.AbstractView;
import com.codenvy.ide.client.propertiespanel.AbstractPropertiesPanel;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * The manager of properties panel. It provides an ability to show a needed properties panel in response to context (current selected
 * diagram element).
 *
 * @author Andrey Plotnikov
 */
public class PropertiesPanelManager implements SelectionManager.SelectionStateListener {

    private final Map<Class, Object> panels;
    private final AcceptsOneWidget   container;

    @Inject
    public PropertiesPanelManager(@Assisted AcceptsOneWidget container) {
        this.container = container;
        this.panels = new HashMap<>();
    }

    /**
     * Register a new properties panel. In case a given kind of diagram element is selected a given properties panel will be shown.
     *
     * @param diagramElement
     *         a class of diagram element that have own properties panel
     * @param panel
     *         a panel that need to be mapped to a given diagram element
     * @param <T>
     *         type of diagram element
     */
    public <T extends Shape, V extends AbstractView> void register(@Nullable Class<T> diagramElement,
                                                                   @Nonnull AbstractPropertiesPanel<T, V> panel) {
        panels.put(diagramElement, panel);
    }

    /**
     * Show properties panel for a given diagram element.
     *
     * @param shape
     *         diagram element that need to be shown in a special properties panel
     * @param <T>
     *         type of diagram element
     * @param <V>
     *         type of properties panel view
     */
    @SuppressWarnings("unchecked")
    public <T extends Shape, V extends AbstractView> void show(@Nullable T shape) {
        Object value = panels.get(shape == null ? null : shape.getClass());

        if (value != null && value instanceof AbstractPropertiesPanel) {
            AbstractPropertiesPanel<T, V> panel = (AbstractPropertiesPanel<T, V>)value;

            if (shape != null) {
                panel.setElement(shape);
            }

            panel.go(container);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onStateChanged(@Nullable Shape shape) {
        show(shape);
    }

}