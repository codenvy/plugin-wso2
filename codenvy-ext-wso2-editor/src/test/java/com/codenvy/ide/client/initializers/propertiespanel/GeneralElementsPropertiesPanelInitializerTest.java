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
package com.codenvy.ide.client.initializers.propertiespanel;

import com.codenvy.ide.client.elements.RootElement;
import com.codenvy.ide.client.managers.PropertiesPanelManager;
import com.codenvy.ide.client.propertiespanel.general.EmptyPropertiesPanelPresenter;
import com.codenvy.ide.client.propertiespanel.general.RootPropertiesPanelPresenter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

/**
 * @author Dmitry Shnurenko
 */
@RunWith(MockitoJUnitRunner.class)
public class GeneralElementsPropertiesPanelInitializerTest {

    @Mock
    private PropertiesPanelManager                    manager;
    @Mock
    private EmptyPropertiesPanelPresenter             emptyPropertiesPanel;
    @Mock
    private RootPropertiesPanelPresenter              rootPropertiesPanel;
    @InjectMocks
    private GeneralElementsPropertiesPanelInitializer initializer;

    @Test
    public void rootElementPropertiesPanelShouldBeInitialized() throws Exception {
        initializer.initialize();

        verify(manager).register(RootElement.class, rootPropertiesPanel);
    }

    @Test
    public void emptyElementPropertiesPanelShouldBeInitialized() throws Exception {
        initializer.initialize();

        verify(manager).register(null, emptyPropertiesPanel);
    }

}