/*
 * CODENVY CONFIDENTIAL
 * __________________
 * 
 *  [2012] - [2013] Codenvy, S.A. 
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.ext.wso2.client.action;

import com.codenvy.ide.annotations.NotNull;
import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.collections.Array;
import com.codenvy.ide.ext.wso2.client.LocalizationConstant;
import com.codenvy.ide.ext.wso2.client.upload.ImportFilePresenter;
import com.codenvy.ide.resources.model.Project;
import com.codenvy.ide.resources.model.Property;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import static com.codenvy.ide.api.notification.Notification.Type.ERROR;
import static com.codenvy.ide.ext.wso2.shared.Constants.ESB_CONFIGURATION_PROJECT_ID;
import static com.codenvy.ide.ext.wso2.shared.Constants.WSO2_PROJECT_ID;
import static com.codenvy.ide.resources.model.ProjectDescription.PROPERTY_MIXIN_NATURES;
import static com.codenvy.ide.resources.model.ProjectDescription.PROPERTY_PRIMARY_NATURE;

/**
 * The action for importing configuration files.
 *
 * @author Valeriy Svydenko
 */
@Singleton
public class ImportFileAction extends Action {

    private AsyncProvider<ImportFilePresenter> importFilePresenter;
    private NotificationManager                notificationManager;
    private ResourceProvider                   resourceProvider;

    @Inject
    public ImportFileAction(LocalizationConstant local,
                            AsyncProvider<ImportFilePresenter> importFilePresenter,
                            NotificationManager notificationManager,
                            ResourceProvider resourceProvider) {

        super(local.wso2ImportActionTitle(), local.wso2ImportActionDescription(), null);

        this.importFilePresenter = importFilePresenter;
        this.notificationManager = notificationManager;
        this.resourceProvider = resourceProvider;
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        importFilePresenter.get(new AsyncCallback<ImportFilePresenter>() {
            @Override
            public void onSuccess(ImportFilePresenter presenter) {
                presenter.showDialog();
            }

            @Override
            public void onFailure(Throwable caught) {
                Notification notification = new Notification(caught.getMessage(), ERROR);
                notificationManager.showNotification(notification);
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public void update(ActionEvent e) {
        boolean visible = false;

        Project activeProject = resourceProvider.getActiveProject();
        if (activeProject != null) {
            Property primaryNature = activeProject.getProperty(PROPERTY_PRIMARY_NATURE);
            Property secondaryNature = activeProject.getProperty(PROPERTY_MIXIN_NATURES);

            boolean hasPrimaryNatureValue = hasValue(WSO2_PROJECT_ID, primaryNature.getValue());
            boolean hasSecondaryNatureValue = hasValue(ESB_CONFIGURATION_PROJECT_ID, secondaryNature.getValue());

            visible = hasPrimaryNatureValue && hasSecondaryNatureValue;
        }

        e.getPresentation().setVisible(visible);
    }

    /**
     * Returns whether list of values contains the given value.
     *
     * @param value
     *         value that need to find
     * @param values
     *         list of values
     * @return <code>true</code> if the value is included in the list, and <code>false</code> if it's not
     */
    private boolean hasValue(@NotNull String value, @NotNull Array<String> values) {
        for (String val : values.asIterable()) {
            if (value.equals(val)) {
                return true;
            }
        }
        return false;
    }
}