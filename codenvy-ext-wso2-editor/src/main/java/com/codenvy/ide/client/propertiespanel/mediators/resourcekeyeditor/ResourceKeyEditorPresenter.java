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
package com.codenvy.ide.client.propertiespanel.mediators.resourcekeyeditor;

import com.google.inject.Inject;

import javax.annotation.Nonnull;

/**
 * The class provides the business logic that allows editor to react on user's action related to change of element's resource key.
 * Logic which provides the class allows add, remove and edit resource key parameter of mediator via special dialog window.
 *
 * @author Valeriy Svydenko
 * @author Dmitry Shnurenko
 */
public class ResourceKeyEditorPresenter implements ResourceKeyEditorView.ActionDelegate {

    private final ResourceKeyEditorView     view;
    private       ChangeResourceKeyCallBack changeResourceKeyCallBack;

    @Inject
    public ResourceKeyEditorPresenter(ResourceKeyEditorView view) {
        this.view = view;
        this.view.setDelegate(this);
    }

    /** {@inheritDoc} */
    @Override
    public void onOkClicked(@Nonnull String key) {
        changeResourceKeyCallBack.onFormatKeyChanged(key);

        view.closeDialog();
    }

    /**
     * Show dialog window for editing resource key.
     *
     * @param content
     *         value of resource key which need to set to special place of dialog window
     * @param callBack
     *         callback that need to be handled when resource key editing is successful
     */
    public void showDialog(@Nonnull String content, @Nonnull ChangeResourceKeyCallBack callBack) {
        changeResourceKeyCallBack = callBack;

        view.showDialog(content);
    }

    /** {@inheritDoc} */
    @Override
    public void onCancelClicked() {
        view.closeDialog();
    }

}
