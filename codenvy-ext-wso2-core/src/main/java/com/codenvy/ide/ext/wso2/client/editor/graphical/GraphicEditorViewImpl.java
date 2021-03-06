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
package com.codenvy.ide.ext.wso2.client.editor.graphical;

import com.codenvy.ide.client.editor.WSO2Editor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Provides graphical representation of wso2 editor window.
 *
 * @author Andrey Plotnikov
 */
public class GraphicEditorViewImpl extends Composite implements GraphicEditorView {

    interface GraphicEditorViewImplUiBinder extends UiBinder<Widget, GraphicEditorViewImpl> {
    }

    private static final GraphicEditorViewImplUiBinder UI_BINDER = GWT.create(GraphicEditorViewImplUiBinder.class);

    @UiField
    SimpleLayoutPanel editor;

    @Inject
    public GraphicEditorViewImpl(@Assisted WSO2Editor wso2Editor) {
        initWidget(UI_BINDER.createAndBindUi(this));

        wso2Editor.go(editor);
    }

    /** {@inheritDoc} */
    @Override
    public void setDelegate(ActionDelegate delegate) {
        // do nothing for now
    }

}