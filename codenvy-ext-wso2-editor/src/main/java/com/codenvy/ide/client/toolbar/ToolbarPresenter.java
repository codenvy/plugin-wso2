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
package com.codenvy.ide.client.toolbar;

import com.codenvy.ide.client.EditorState;
import com.codenvy.ide.client.HasState;
import com.codenvy.ide.client.State;
import com.codenvy.ide.client.elements.Shape;
import com.codenvy.ide.client.mvp.AbstractPresenter;
import com.codenvy.ide.client.workspace.WorkspacePresenter;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import javax.annotation.Nonnull;

import static com.codenvy.ide.client.State.CREATING_CALL;
import static com.codenvy.ide.client.State.CREATING_CALLTEMPLATE;
import static com.codenvy.ide.client.State.CREATING_CONNECTION_SOURCE;
import static com.codenvy.ide.client.State.CREATING_ENRICH;
import static com.codenvy.ide.client.State.CREATING_FILTER;
import static com.codenvy.ide.client.State.CREATING_HEADER;
import static com.codenvy.ide.client.State.CREATING_LOG;
import static com.codenvy.ide.client.State.CREATING_LOOPBACK;
import static com.codenvy.ide.client.State.CREATING_PAYLOADFACTORY;
import static com.codenvy.ide.client.State.CREATING_PROPERTY;
import static com.codenvy.ide.client.State.CREATING_RESPOND;
import static com.codenvy.ide.client.State.CREATING_SEND;
import static com.codenvy.ide.client.State.CREATING_SEQUENCE;
import static com.codenvy.ide.client.State.CREATING_SWITCH_MEDIATOR;

/**
 * @author Andrey Plotnikov
 */
public class ToolbarPresenter extends AbstractPresenter
        implements HasState<State>, WorkspacePresenter.MainElementChangeListener, ToolbarView.ActionDelegate {

    private EditorState<State> state;

    @Inject
    public ToolbarPresenter(ToolbarView view, @Assisted EditorState<State> editorState) {
        super(view);

        this.state = editorState;
    }

    /** {@inheritDoc} */
    @Override
    public void onMainElementChanged(@Nonnull Shape element) {
        ((ToolbarView)view).showButtons(element.getComponents());
    }

    /** {@inheritDoc} */
    @Nonnull
    @Override
    public State getState() {
        return state.getState();
    }

    /** {@inheritDoc} */
    @Override
    public void setState(@Nonnull State state) {
        this.state.setState(state);
    }

    /** {@inheritDoc} */
    @Override
    public void onLogButtonClicked() {
        setState(CREATING_LOG);
    }

    /** {@inheritDoc} */
    @Override
    public void onPropertyButtonClicked() {
        setState(CREATING_PROPERTY);
    }

    /** {@inheritDoc} */
    @Override
    public void onPayloadFactoryButtonClicked() {
        setState(CREATING_PAYLOADFACTORY);
    }

    /** {@inheritDoc} */
    @Override
    public void onSendButtonClicked() {
        setState(CREATING_SEND);
    }

    /** {@inheritDoc} */
    @Override
    public void onHeaderButtonClicked() {
        setState(CREATING_HEADER);
    }

    /** {@inheritDoc} */
    @Override
    public void onRespondButtonClicked() {
        setState(CREATING_RESPOND);
    }

    /** {@inheritDoc} */
    @Override
    public void onFilterButtonClicked() {
        setState(CREATING_FILTER);
    }

    /** {@inheritDoc} */
    @Override
    public void onSwitch_mediatorButtonClicked() {
        setState(CREATING_SWITCH_MEDIATOR);
    }

    /** {@inheritDoc} */
    @Override
    public void onSequenceButtonClicked() {
        setState(CREATING_SEQUENCE);
    }

    /** {@inheritDoc} */
    @Override
    public void onEnrichButtonClicked() {
        setState(CREATING_ENRICH);
    }

    /** {@inheritDoc} */
    @Override
    public void onLoopBackButtonClicked() {
        setState(CREATING_LOOPBACK);
    }

    /** {@inheritDoc} */
    @Override
    public void onCallTemplateButtonClicked() {
        setState(CREATING_CALLTEMPLATE);
    }

    /** {@inheritDoc} */
    @Override
    public void onCallButtonClicked() {
        setState(CREATING_CALL);
    }

    /** {@inheritDoc} */
    @Override
    public void onConnectionButtonClicked() {
        setState(CREATING_CONNECTION_SOURCE);
    }

}