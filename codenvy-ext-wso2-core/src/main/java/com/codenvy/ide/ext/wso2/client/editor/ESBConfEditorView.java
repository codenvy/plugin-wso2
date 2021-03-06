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
package com.codenvy.ide.ext.wso2.client.editor;

import com.codenvy.ide.api.editor.AbstractEditorPresenter;
import com.codenvy.ide.api.mvp.View;
import com.codenvy.ide.jseditor.client.texteditor.EmbeddedTextEditorPresenter;
import com.google.inject.ImplementedBy;

import javax.annotation.Nonnull;

/**
 * The abstract view's representation of esb editor. It provides an ability to show all available properties of editor.
 *
 * @author Andrey Plotnikov
 * @author Valeriy Svydenko
 * @author Dmitry Shnurenko
 */
@ImplementedBy(ESBConfEditorViewImpl.class)
public interface ESBConfEditorView extends View<ESBConfEditorView.ActionDelegate> {

    /**
     * Interface defines methods of ESBConfEditor's presenter which calls from view. These methods defines
     * some actions when user click the button.
     */
    public interface ActionDelegate {

        /** Performs some actions in response to user clicked on source view button. */
        void onSourceViewButtonClicked();

        /** Performs some actions in response to user clicked on design view button. */
        void onDesignViewButtonClicked();

        /** Performs some actions in response to user clicked on dual view button. */
        void onDualViewButtonClicked();

        /** Performs some actions in response to user clicked on property button. */
        void onPropertyButtonClicked();

        /** Performs some actions in response to user clicked on palette button. */
        void onChangeToolbarVisibilityClicked();

        /** Performs some actions in response to DOM of editor is changed. */
        void onEditorDOMChanged();

        /** Performs some actions in response to user clicked on horizontal button. */
        void onHorizontalOrientationClicked();

        /** Performs some actions in response to user clicked on vertical button. */
        void onVerticalOrientationClicked();
    }

    /**
     * Sets enabling of source view button.
     *
     * @param enable
     *         <code>true</code> button is enable,<code>false</code> button is disable
     */
    void setEnableSourceViewButton(boolean enable);

    /**
     * Sets enabling of design view button.
     *
     * @param enable
     *         <code>true</code> button is enable,<code>false</code> button is disable
     */
    void setEnableDesignViewButton(boolean enable);

    /**
     * Sets enabling of dual view button.
     *
     * @param enable
     *         <code>true</code> button is enable,<code>false</code> button is disable
     */
    void setEnableDualViewButton(boolean enable);

    /** Shows panel for displaying source view of plugin. */
    void showSourceView();

    /** Shows panel for displaying design view of plugin. */
    void showDesignView();

    /** Shows panel for displaying dual view of plugin. */
    void showDualView();

    /**
     * Adds text editor widget into complex editor view.
     *
     * @param textEditor
     *         editor which provides displaying of source view
     */
    void addTextEditorWidget(@Nonnull EmbeddedTextEditorPresenter textEditor);

    /**
     * Adds graphical editor widget into complex editor view.
     *
     * @param graphicalEditor
     *         editor which provides displaying of design view
     */
    void addGraphicalEditorWidget(@Nonnull AbstractEditorPresenter graphicalEditor);

}