/*
 * CODENVY CONFIDENTIAL
 * __________________
 * 
 * [2012] - [2013] Codenvy, S.A. 
 * All Rights Reserved.
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
package com.codenvy.ide.ext.wso2.client.wizard.files;

import com.codenvy.ide.api.editor.EditorAgent;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.resources.FileType;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.ui.wizard.AbstractWizardPage;
import com.codenvy.ide.collections.Array;
import com.codenvy.ide.ext.wso2.client.LocalizationConstant;
import com.codenvy.ide.ext.wso2.client.commons.WSO2AsyncCallback;
import com.codenvy.ide.ext.wso2.client.wizard.files.view.CreateResourceView;
import com.codenvy.ide.resources.model.File;
import com.codenvy.ide.resources.model.Folder;
import com.codenvy.ide.resources.model.Project;
import com.codenvy.ide.resources.model.Resource;
import com.codenvy.ide.resources.model.ResourceNameValidator;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import static com.codenvy.ide.ext.wso2.shared.Constants.MAIN_FOLDER_NAME;
import static com.codenvy.ide.ext.wso2.shared.Constants.SRC_FOLDER_NAME;
import static com.codenvy.ide.ext.wso2.shared.Constants.SYNAPSE_CONFIG_FOLDER_NAME;

/**
 * The abstract implementation of the wizard page. This page provides an ability to create WSO2 resources in a given place.
 *
 * @author Andrey Plotnikov
 */
public abstract class AbstractCreateResourcePage extends AbstractWizardPage implements CreateResourceView.ActionDelegate {

    private ResourceProvider    resourceProvider;
    private EditorAgent         editorAgent;
    private String              parentFolderName;
    private FileType            fileType;
    private Project             activeProject;
    private NotificationManager notificationManager;

    private   Folder               parentFolder;
    protected LocalizationConstant locale;
    protected CreateResourceView   view;
    protected boolean              hasSameFile;
    protected boolean              incorrectName;
    protected String               content;

    public AbstractCreateResourcePage(@NotNull CreateResourceView view,
                                      @NotNull String caption,
                                      @Nullable ImageResource icon,
                                      @NotNull LocalizationConstant locale,
                                      @NotNull ResourceProvider resourceProvider,
                                      @NotNull EditorAgent editorAgent,
                                      @NotNull String parentFolderName,
                                      @NotNull FileType fileType,
                                      @NotNull NotificationManager notificationManager) {

        super(caption, icon);

        this.view = view;
        this.resourceProvider = resourceProvider;
        this.editorAgent = editorAgent;
        this.parentFolderName = parentFolderName;
        this.fileType = fileType;
        this.view.setDelegate(this);
        this.locale = locale;
        this.notificationManager = notificationManager;
    }

    /** {@inheritDoc} */
    @Override
    public String getNotice() {
        if (incorrectName) {
            return locale.wizardFileResourceNoticeIncorrectName();
        }

        if (hasSameFile) {
            return locale.wizardFileResourceNoticeFileExists();
        }

        if (parentFolder == null) {
            return locale.wizardFileResourceNoticeParentFolderNotExists();
        }

        return null;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCompleted() {
        return !view.getResourceName().isEmpty() && !hasSameFile && !incorrectName && !(parentFolder == null);
    }

    /** {@inheritDoc} */
    @Override
    public void focusComponent() {
        view.setResourceName("");
    }

    /** {@inheritDoc} */
    @Override
    public void removeOptions() {
        // do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void go(AcceptsOneWidget container) {
        activeProject = resourceProvider.getActiveProject();

        getResourceByName(activeProject, SRC_FOLDER_NAME, new WSO2AsyncCallback<Resource>(notificationManager) {
            @Override
            public void onSuccess(Resource result) {
                getResourceByName((Folder)result, MAIN_FOLDER_NAME, new WSO2AsyncCallback<Resource>(notificationManager) {
                    @Override
                    public void onSuccess(Resource result) {
                        getResourceByName((Folder)result, SYNAPSE_CONFIG_FOLDER_NAME, new WSO2AsyncCallback<Resource>(notificationManager) {
                            @Override
                            public void onSuccess(Resource result) {
                                getResourceByName((Folder)result, parentFolderName, new WSO2AsyncCallback<Resource>(notificationManager) {
                                    @Override
                                    public void onSuccess(Resource result) {
                                        parentFolder = (Folder)result;
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

        container.setWidget(view);
    }

    /**
     * Find resource by name in parent folder
     *
     * @param parent
     *         place where child should be
     * @param name
     *         name that child should have
     * @param callback
     */
    private void getResourceByName(@NotNull Folder parent, @NotNull String name, @NotNull final AsyncCallback<Resource> callback) {
        Array<Resource> children = parent.getChildren();

        for (Resource child : children.asIterable()) {
            if (name.equals(child.getName())) {
                callback.onSuccess(child);
                return;
            }
        }

        activeProject.createFolder(parent, name, new WSO2AsyncCallback<Folder>(notificationManager) {
            @Override
            public void onSuccess(Folder result) {
                callback.onSuccess(result);
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public void onValueChanged() {
        String resourceName = view.getResourceName();

        incorrectName = !ResourceNameValidator.isFileNameValid(resourceName);
        Resource file = null;
        for (Resource child : parentFolder.getChildren().asIterable()) {
            if (getResourceNameWithExtension(resourceName).equals(child.getName())) {
                file = child;
            }
        }

        hasSameFile = file != null;

        delegate.updateControls();
    }

    /** @return resource name with extension */
    @NotNull
    private String getResourceNameWithExtension(@NotNull String resourceName) {
        return resourceName + '.' + fileType.getExtension();
    }

    /** {@inheritDoc} */
    @Override
    public void commit(@NotNull final CommitCallback callback) {
        String mimeType = fileType.getMimeTypes().get(0);
        activeProject.createFile(parentFolder, getResourceNameWithExtension(view.getResourceName()), content, mimeType,
                                 new AsyncCallback<File>() {
                                     @Override
                                     public void onSuccess(File result) {
                                         editorAgent.openEditor(result);
                                         callback.onSuccess();
                                     }

                                     @Override
                                     public void onFailure(Throwable caught) {
                                         callback.onFailure(caught);
                                     }
                                 });
    }
}