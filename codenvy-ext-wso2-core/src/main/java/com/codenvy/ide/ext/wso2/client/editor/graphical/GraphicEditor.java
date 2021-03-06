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

import com.codenvy.ide.api.editor.AbstractEditorPresenter;
import com.codenvy.ide.api.editor.EditorInput;
import com.codenvy.ide.client.editor.WSO2Editor;
import com.codenvy.ide.client.inject.Injector;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import javax.annotation.Nonnull;

/**
 * The graphical editor for ESB configuration.
 *
 * @author Andrey Plotnikov
 * @author Dmitry Shnurenko
 * @author Valeriy Svydenko
 */
public class GraphicEditor extends AbstractEditorPresenter implements GraphicEditorView.ActionDelegate,
                                                                      WSO2Editor.EditorChangeListener {

    private final GraphicEditorView view;
    private final WSO2Editor        editor;

    @Inject
    public GraphicEditor(EditorViewFactory editorViewFactory, Injector editorInjector) {
        editor = editorInjector.getEditor();
        editor.addListener(this);

        this.view = editorViewFactory.getEditorView(editor);
        this.view.setDelegate(this);
    }

    /** {@inheritDoc} */
    @Override
    protected void initializeEditor() {

    }

    /** {@inheritDoc} */
    @Override
    public void doSave() {
        dirtyState = false;
    }

    /** {@inheritDoc} */
    @Override
    public void doSave(@Nonnull AsyncCallback<EditorInput> callback) {
        doSave();

        callback.onSuccess(getEditorInput());
    }

    /** {@inheritDoc} */
    @Override
    public void doSaveAs() {
    }

    /** {@inheritDoc} */
    @Override
    public void activate() {
    }

    /** {@inheritDoc} */
    @Nonnull
    @Override
    public String getTitle() {
        return input.getFile().getName();
    }

    /** {@inheritDoc} */
    @Override
    public ImageResource getTitleImage() {
        return input.getImageResource();
    }

    /** {@inheritDoc} */
    @Override
    public String getTitleToolTip() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    /** @return a serialized text type of diagram */
    @Nonnull
    public String serialize() {
        return editor.serialize();
    }

    /**
     * Convert a text type of diagram to a graphical type.
     *
     * @param content
     *         content that need to be parsed
     */
    public void deserialize(@Nonnull String content) {
        editor.deserialize(content);
    }

    /** {@inheritDoc} */
    @Override
    public void onChanged() {
        updateDirtyState(true);
    }

    /** Calls method which changes visibility of toolbar panel. */
    public void changeToolbarVisibility() {
        editor.changeToolbarPanelVisibility();
    }

    /** Calls method which changes orientation of diagram. */
    public void setHorizontalOrientation(boolean isHorizontal) {
        editor.changeDiagramOrientation(isHorizontal);
    }

    /** Changes visible state of the property panel. */
    public void changePropertyPanelVisibility() {
        editor.changePropertyPanelVisibility();
    }

    /** Calls method which changes size of workspace. */
    public void resizeEditor() {
        editor.onEditorDOMChanged();
    }

}