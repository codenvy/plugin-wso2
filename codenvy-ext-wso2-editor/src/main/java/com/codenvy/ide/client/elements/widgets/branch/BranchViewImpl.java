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
package com.codenvy.ide.client.elements.widgets.branch;

import com.codenvy.ide.client.EditorResources;
import com.codenvy.ide.client.elements.Branch;
import com.codenvy.ide.client.elements.widgets.branch.arrow.ArrowPresenter;
import com.codenvy.ide.client.elements.widgets.element.ElementPresenter;
import com.codenvy.ide.client.mvp.AbstractView;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.Assisted;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.gwt.dom.client.Style.Unit.PCT;
import static com.google.gwt.dom.client.Style.Unit.PX;

/**
 * Provides a graphical representation of the element's branch.
 *
 * @author Andrey Plotnikov
 * @author Dmitry Shnurenko
 * @author Valeriy Svydenko
 */
public class BranchViewImpl extends AbstractView<BranchView.ActionDelegate> implements BranchView {

    @Singleton
    interface BranchViewImplUiBinder extends UiBinder<Widget, BranchViewImpl> {
    }

    @UiField
    AbsolutePanel body;
    @UiField
    FlowPanel     focusPanel;
    @UiField
    FlowPanel     topTitlePanel;
    @UiField
    Label         topTitle;
    @UiField
    FlowPanel     westTitlePanel;
    @UiField
    Label         westTitle;
    @UiField(provided = true)
    final EditorResources resources;

    private int height;
    private int width;

    @Inject
    public BranchViewImpl(BranchViewImplUiBinder ourUiBinder, EditorResources resources, @Assisted Branch branch) {
        this.resources = resources;

        height = DEFAULT_HEIGHT;
        width = DEFAULT_WIDTH;

        initWidget(ourUiBinder.createAndBindUi(this));

        if (branch.getParent().isRoot()) {
            addStyleName(resources.editorCSS().dottedBackground());
        }

        bind();
    }

    private void bind() {
        focusPanel.addDomHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent event) {
                int nativeButton = event.getNativeButton();

                if (NativeEvent.BUTTON_LEFT == nativeButton) {
                    delegate.onMouseLeftButtonClicked(event.getRelativeX(body.getElement()), event.getRelativeY(body.getElement()));
                }

                if (NativeEvent.BUTTON_RIGHT == nativeButton) {
                    delegate.onMouseRightButtonClicked();
                }

                event.stopPropagation();
            }
        }, MouseDownEvent.getType());

        focusPanel.addDomHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent event) {
                event.stopPropagation();
            }
        }, MouseUpEvent.getType());

        focusPanel.addDomHandler(new MouseMoveHandler() {
            @Override
            public void onMouseMove(MouseMoveEvent event) {
                delegate.onMouseMoved(event.getRelativeX(body.getElement()), event.getRelativeY(body.getElement()));

                event.stopPropagation();
            }
        }, MouseMoveEvent.getType());

        focusPanel.addDomHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                delegate.onMouseOut();
            }
        }, MouseOutEvent.getType());

        focusPanel.addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_DELETE) {
                    delegate.onDeleteButtonPressed();
                }

                event.stopPropagation();
            }
        }, KeyDownEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public void setTitle(@Nullable String title) {
        topTitle.setText(title);
        westTitle.setText(title);
    }

    /** {@inheritDoc} */
    @Override
    public void addElement(@Nonnull ElementPresenter elementPresenter, @Nonnegative int x, @Nonnegative int y) {
        body.add(elementPresenter.getView(), x, y);
    }

    /** {@inheritDoc} */
    @Override
    public void addArrow(@Nonnull ArrowPresenter arrow, @Nonnegative int x, @Nonnegative int y) {
        body.add(arrow.getView(), x, y);
    }

    /** {@inheritDoc} */
    @Override
    public void clear() {
        body.clear();
    }

    /** {@inheritDoc} */
    @Override
    public void setDefaultCursor() {
        focusPanel.removeStyleName(resources.editorCSS().applyCursor());
        focusPanel.removeStyleName(resources.editorCSS().errorCursor());
    }

    /** {@inheritDoc} */
    @Override
    public void setApplyCursor() {
        focusPanel.removeStyleName(resources.editorCSS().errorCursor());
        focusPanel.addStyleName(resources.editorCSS().applyCursor());
    }

    /** {@inheritDoc} */
    @Override
    public void setErrorCursor() {
        focusPanel.removeStyleName(resources.editorCSS().applyCursor());
        focusPanel.addStyleName(resources.editorCSS().errorCursor());
    }

    /** {@inheritDoc} */
    @Override
    public int getHeight() {
        return height;
    }

    /** {@inheritDoc} */
    @Override
    public void setHeight(@Nonnegative int height) {
        this.height = height;

        getElement().getStyle().setHeight(height, PX);

        resizeBodyPanel();
    }

    /** {@inheritDoc} */
    @Override
    public int getWidth() {
        return width;
    }

    /** {@inheritDoc} */
    @Override
    public void setWidth(@Nonnegative int width) {
        this.width = width;

        getElement().getStyle().setWidth(width, PX);

        resizeBodyPanel();
    }

    /** {@inheritDoc} */
    @Override
    public void setVisibleTitle(boolean visible) {
        westTitlePanel.setVisible(visible);
        topTitlePanel.setVisible(visible);
    }

    /** {@inheritDoc} */
    @Override
    public void setVisibleTopBorder(boolean visible) {
        String style = resources.editorCSS().topBorder();

        if (visible) {
            addStyleName(style);
        } else {
            removeStyleName(style);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setVisibleLeftBorder(boolean visible) {
        String style = resources.editorCSS().leftBorder();

        if (visible) {
            addStyleName(style);
        } else {
            removeStyleName(style);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setVisibleHorizontalTitlePanel(boolean visible) {
        westTitlePanel.setVisible(visible);
        topTitlePanel.setVisible(!visible);

        resizeBodyPanel();
    }

    private void resizeBodyPanel() {
        Style style = body.getElement().getStyle();

        if (westTitlePanel.isVisible()) {
            style.setHeight(100, PCT);
            style.setWidth(width - HORIZONTAL_TITLE_WIDTH - BORDER_SIZE, PX);
        }

        if (topTitlePanel.isVisible()) {
            style.setHeight(height - VERTICAL_TITLE_WIDTH - BORDER_SIZE, PX);
            style.setWidth(100, PCT);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void applyVerticalAlign() {
        focusPanel.addStyleName(resources.editorCSS().verticalBranchAlign());
    }

    /** {@inheritDoc} */
    @Override
    public void applyHorizontalAlign() {
        focusPanel.removeStyleName(resources.editorCSS().verticalBranchAlign());
    }

    /** {@inheritDoc} */
    @Override
    public int getAbsoluteLeft() {
        return body.getAbsoluteLeft();
    }

    /** {@inheritDoc} */
    @Override
    public int getAbsoluteTop() {
        return body.getAbsoluteTop();
    }

}