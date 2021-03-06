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
package com.codenvy.ide.client.elements.widgets.element;

import com.codenvy.ide.client.EditorResources;
import com.codenvy.ide.client.EditorResources.EditorCSS;
import com.codenvy.ide.client.elements.Element;
import com.codenvy.ide.client.elements.widgets.branch.BranchPresenter;
import com.codenvy.ide.client.mvp.AbstractView;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragEndEvent;
import com.google.gwt.event.dom.client.DragEndHandler;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragEvent;
import com.google.gwt.event.dom.client.DragHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.event.dom.client.HasAllDragAndDropHandlers;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.Assisted;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.gwt.dom.client.Element.DRAGGABLE_TRUE;
import static com.google.gwt.dom.client.Style.Position.ABSOLUTE;
import static com.google.gwt.dom.client.Style.Position.RELATIVE;
import static com.google.gwt.dom.client.Style.Unit.PCT;
import static com.google.gwt.dom.client.Style.Unit.PX;

/**
 * Provides a graphical representation of the diagram's element.
 *
 * @author Andrey Plotnikov
 * @author Valeriy Svydenko
 */
public class ElementViewImpl extends AbstractView<ElementView.ActionDelegate> implements ElementView,
                                                                                         HasClickHandlers,
                                                                                         HasAllMouseHandlers,
                                                                                         HasAllDragAndDropHandlers {

    @Singleton
    interface ElementViewImplUiBinder extends UiBinder<Widget, ElementViewImpl> {
    }

    private static final int BORDER_SIZE = 1;
    private static final int SHADOW_SIZE = 1;
    private static final int SHADOW_BLUR = 7;
    private static final int MARGIN      = 2 * BORDER_SIZE + 2 * SHADOW_SIZE + 2 * SHADOW_BLUR;

    @UiField
    FlowPanel   rightPanel;
    @UiField
    FlowPanel   mainPanel;
    @UiField
    SimplePanel topPanel;
    @UiField
    SimplePanel westPanel;
    @UiField(provided = true)
    final         EditorResources resources;
    private final FlowPanel       iconAndTitlePanel;

    private FlowPanel headerPanel;
    private Label     headerTitle;
    private FlowPanel iconPanel;
    private Label     iconTitle;
    private Image     icon;

    private final EditorCSS editorCSS;
    private final Element   element;

    private PopupPanel popup;

    private int     height;
    private int     width;
    private boolean isComplexMediator;
    private String  titleText;
    private boolean isIconTitleVisible;

    @Inject
    public ElementViewImpl(ElementViewImplUiBinder ourUiBinder, EditorResources resources, @Assisted Element element) {
        this.resources = resources;
        this.element = element;
        this.editorCSS = resources.editorCSS();

        this.height = DEFAULT_SIZE;
        this.width = DEFAULT_SIZE;

        initWidget(ourUiBinder.createAndBindUi(this));

        if (!element.isRoot()) {
            getElement().setDraggable(DRAGGABLE_TRUE);
        }

        bind();
        preparePopup(element);

        isComplexMediator = false;
        isIconTitleVisible = true;

        iconAndTitlePanel = createIconAndTitlePanel();
    }

    @Nonnull
    private FlowPanel createIconAndTitlePanel() {
        FlowPanel iconTitlePanel = new FlowPanel();
        iconTitlePanel.addStyleName(editorCSS.fullSize());

        headerPanel = createHeaderPanel();
        iconPanel = createIconPanel();

        iconTitlePanel.add(headerPanel);
        iconTitlePanel.add(iconPanel);

        return iconTitlePanel;
    }

    @Nonnull
    private FlowPanel createHeaderPanel() {
        FlowPanel header = new FlowPanel();
        header.addStyleName(editorCSS.elementHeaderPanel());

        Style style = header.getElement().getStyle();
        style.setWidth(100, PCT);
        style.setHeight(19, PX);

        header.add(createInnerHeaderPanel());

        return header;
    }

    @Nonnull
    private FlowPanel createInnerHeaderPanel() {
        FlowPanel innerHeaderPanel = new FlowPanel();
        innerHeaderPanel.addStyleName(editorCSS.elementHeaderInnerPanel());

        Style style = innerHeaderPanel.getElement().getStyle();
        style.setWidth(100, PCT);
        style.setHeight(19, PX);

        headerTitle = createHeaderTitle();
        innerHeaderPanel.add(headerTitle);

        return innerHeaderPanel;
    }

    @Nonnull
    private Label createHeaderTitle() {
        Label title = createTitle();

        Style style = title.getElement().getStyle();
        style.setPosition(RELATIVE);
        style.setTop(2, PX);

        return title;
    }

    @Nonnull
    private FlowPanel createIconPanel() {
        FlowPanel wrapPanel = new FlowPanel();
        wrapPanel.addStyleName(editorCSS.wrapIconAndTitlePanel());

        FlowPanel iconViewPanel = new FlowPanel();

        iconViewPanel.addStyleName(editorCSS.iconAndTitlePanel());

        iconTitle = createIconTitle();
        icon = new Image();

        iconViewPanel.add(iconTitle);
        iconViewPanel.add(icon);

        wrapPanel.add(iconViewPanel);

        return wrapPanel;
    }

    @Nonnull
    private Label createIconTitle() {
        Label title = createTitle();

        Style style = title.getElement().getStyle();
        style.setHeight(15, PX);
        style.setWidth(100, PCT);

        return title;
    }

    @Nonnull
    private Label createTitle() {
        Label title = new Label();

        title.addStyleName(editorCSS.fontStyle());
        title.addStyleName(editorCSS.titleBold());
        title.addStyleName(editorCSS.textAlign());

        return title;
    }

    private void preparePopup(@Nonnull Element element) {
        MenuBar menuBar = new MenuBar(true);

        menuBar.addStyleName(resources.editorCSS().branchBackground());
        menuBar.addStyleName(resources.editorCSS().gwtMenuItemSelected());

        MenuItem delete = new MenuItem("Delete", true, new Command() {
            @Override
            public void execute() {
                delegate.onDeleteActionClicked();
            }
        });

        menuBar.addItem(delete);
        menuBar.setAutoOpen(true);

        if (element.isPossibleToAddBranches()) {
            MenuItem amountOfBranches = new MenuItem("Number of branches", true, new Command() {
                @Override
                public void execute() {
                    delegate.onChangeNumberBranchesActionClicked();
                }
            });

            menuBar.addItem(amountOfBranches);
        }

        popup = new PopupPanel(true, true);
        popup.add(menuBar);
    }

    private void bind() {
        addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent event) {
                int nativeButton = event.getNativeButton();

                if (NativeEvent.BUTTON_RIGHT == nativeButton) {
                    delegate.onMouseRightButtonClicked(event.getClientX(), event.getClientY());
                }

                if (NativeEvent.BUTTON_LEFT == nativeButton) {
                    delegate.onMouseLeftButtonClicked();
                }

                event.stopPropagation();
            }
        });

        addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                delegate.onMouseOut();

                event.stopPropagation();
            }
        });

        addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                delegate.onMouseOver();

                event.stopPropagation();
            }
        });

        addDragStartHandler(new DragStartHandler() {
            @Override
            public void onDragStart(DragStartEvent event) {
                // this code needs for stopping moving icon in the middle of the element
                event.getDataTransfer().setDragImage(getElement(), 0, 0);

                event.stopPropagation();
            }
        });

        addDragHandler(new DragHandler() {
            @Override
            public void onDrag(DragEvent event) {
                NativeEvent nativeEvent = event.getNativeEvent();

                delegate.onElementDragged(nativeEvent.getClientX(), nativeEvent.getClientY());

                event.preventDefault();
                event.stopPropagation();
            }
        });

        addDragEndHandler(new DragEndHandler() {
            @Override
            public void onDragEnd(DragEndEvent event) {
                delegate.onDragFinished();

                event.preventDefault();
                event.stopPropagation();
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public void setTitle(@Nonnull String title) {
        titleText = title;

        updateTitle();
    }

    private void updateTitle() {
        if (headerPanel.isVisible()) {
            headerTitle.setText(titleText);
        }

        String title = isIconTitleVisible ? titleText : "";
        iconTitle.setText(title);
    }

    /** {@inheritDoc} */
    @Override
    public void setIcon(@Nullable ImageResource icon) {
        if (icon == null) {
            return;
        }

        this.icon.setResource(icon);
    }

    /** {@inheritDoc} */
    @Override
    public void addBranch(@Nonnull BranchPresenter branchPresenter) {
        if (!isComplexMediator) {
            westPanel.addStyleName(editorCSS.complexMediatorBackground());
            topPanel.addStyleName(editorCSS.complexMediatorBackground());

            isComplexMediator = true;
        }

        rightPanel.add(branchPresenter.getView());
    }

    /** {@inheritDoc} */
    @Override
    public void removeBranches() {
        isComplexMediator = false;

        westPanel.removeStyleName(editorCSS.complexMediatorBackground());
        topPanel.removeStyleName(editorCSS.complexMediatorBackground());

        rightPanel.clear();
    }

    /** {@inheritDoc} */
    @Override
    public void select() {
        mainPanel.addStyleName(editorCSS.selectedElement());
    }

    /** {@inheritDoc} */
    @Override
    public void unselect() {
        mainPanel.removeStyleName(editorCSS.selectedElement());
    }

    /** {@inheritDoc} */
    @Override
    public void selectBelowCursor(boolean isError) {
        mainPanel.addStyleName(isError ? editorCSS.selectErrorElementBelowCursor() : editorCSS.selectElementBelowCursor());
    }

    /** {@inheritDoc} */
    @Override
    public void setHorizontalHeaderPanelOrientation(boolean isHorizontal) {
        if (isHorizontal) {
            changePanelOrientation(westPanel, topPanel);
            rightPanel.removeStyleName(editorCSS.branchesContainer());
        } else {
            changePanelOrientation(topPanel, westPanel);
            rightPanel.addStyleName(editorCSS.branchesContainer());
        }
    }

    private void changePanelOrientation(@Nonnull SimplePanel shownPanel, @Nonnull SimplePanel disablePanel) {
        disablePanel.setVisible(false);

        shownPanel.setWidget(iconAndTitlePanel);
        shownPanel.setVisible(true);
    }

    /** {@inheritDoc} */
    @Override
    public void unSelectBelowCursor() {
        mainPanel.removeStyleName(editorCSS.selectElementBelowCursor());
        mainPanel.removeStyleName(editorCSS.selectErrorElementBelowCursor());
    }

    /** {@inheritDoc} */
    @Override
    public void showContextMenu(int x, int y) {
        popup.setPopupPosition(x, y);
        popup.show();
    }

    /** {@inheritDoc} */
    @Override
    public void hideContextMenu() {
        popup.hide();
    }

    /** {@inheritDoc} */
    @Override
    public void setVisibleTitleAndIcon(boolean visible) {
        topPanel.setVisible(visible);
        westPanel.setVisible(visible);
    }

    /** {@inheritDoc} */
    @Override
    public void setVisibleTitle(boolean visible) {
        isIconTitleVisible = visible;

        updateTitle();
    }

    /** {@inheritDoc} */
    @Override
    public void setVisibleHeader(boolean visible) {
        headerPanel.setVisible(visible);

        resizeIconPanel();

        updateTitle();
    }

    private void resizeIconPanel() {
        Style style = iconPanel.getElement().getStyle();

        if (headerPanel.isVisible()) {
            style.setHeight(height - 19, PX);
        } else {
            style.setHeight(100, PCT);
        }
    }

    /** {@inheritDoc} */
    @Override
    public int getHeight() {
        return height + MARGIN;
    }

    /** {@inheritDoc} */
    @Override
    public void setHeight(@Nonnegative int height) {
        this.height = height - MARGIN;
        getElement().getStyle().setHeight(height, PX);

        resizeIconPanel();
    }

    /** {@inheritDoc} */
    @Override
    public void setY(@Nonnegative int y) {
        getElement().getStyle().setTop(y, PX);
    }

    /** {@inheritDoc} */
    @Override
    public void setX(@Nonnegative int x) {
        getElement().getStyle().setLeft(x, Style.Unit.PX);
    }

    /** {@inheritDoc} */
    @Override
    public int getWidth() {
        return width + MARGIN;
    }

    /** {@inheritDoc} */
    @Override
    public void setWidth(@Nonnegative int width) {
        this.width = width - MARGIN;
        getElement().getStyle().setWidth(width, PX);

        Style rightPanelStyle = rightPanel.getElement().getStyle();

        if (element.isHorizontalOrientation()) {
            int rightPanelLeft = element.isRoot() ? 0 : DEFAULT_SIZE;
            rightPanelStyle.setLeft(rightPanelLeft, PX);
            rightPanelStyle.setTop(0, PX);
        } else {
            int rightPanelTop = element.isRoot() ? 0 : DEFAULT_SIZE;
            rightPanelStyle.setTop(rightPanelTop, PX);
            rightPanelStyle.setLeft(0, PX);
        }

        Position position = element.isRoot() ? RELATIVE : ABSOLUTE;
        rightPanelStyle.setPosition(position);
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return addDomHandler(handler, MouseDownEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
        return addDomHandler(handler, MouseMoveEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return addDomHandler(handler, MouseOutEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        return addDomHandler(handler, MouseOverEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
        return addDomHandler(handler, MouseUpEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
        return addDomHandler(handler, MouseWheelEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addDragEndHandler(DragEndHandler handler) {
        return addDomHandler(handler, DragEndEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addDragEnterHandler(DragEnterHandler handler) {
        return addDomHandler(handler, DragEnterEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addDragHandler(DragHandler handler) {
        return addDomHandler(handler, DragEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addDragLeaveHandler(DragLeaveHandler handler) {
        return addDomHandler(handler, DragLeaveEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addDragOverHandler(DragOverHandler handler) {
        return addDomHandler(handler, DragOverEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addDragStartHandler(DragStartHandler handler) {
        return addDomHandler(handler, DragStartEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addDropHandler(DropHandler handler) {
        return addDomHandler(handler, DropEvent.getType());
    }

}