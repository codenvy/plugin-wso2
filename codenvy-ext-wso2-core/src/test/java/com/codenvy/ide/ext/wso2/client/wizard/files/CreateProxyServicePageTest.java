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

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.codenvy.ide.ext.wso2.shared.Constants.PROXY_SERVICE_FOLDER_NAME;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * Here we're testing {@link CreateProxyServicePage}.
 *
 * @author Andrey Plotnikov
 */
public class CreateProxyServicePageTest extends AbstractCreateResourcePageTest {

    @BeforeMethod
    @Override
    public void setUp() throws Exception {
        page = new CreateProxyServicePage(view, locale, resourceProvider, resources, editorAgent, fileType);

        verify(locale).wizardFileProxyServiceTitle();
        verify(locale).wizardFileProxyServiceFieldsName();
        verify(resources).proxy_service_wizard();

        parentFolderName = PROXY_SERVICE_FOLDER_NAME;

        super.setUp();
    }

    @Test
    public void emptyResourceNameNoticeShouldBeShown() throws Exception {
        when(view.getResourceName()).thenReturn(EMPTY_TEXT);
        when(locale.wizardFileProxyServiceNoticeEmptyName()).thenReturn(SOME_TEXT);

        page.go(container);
        page.onValueChanged();

        assertEquals(page.getNotice(), SOME_TEXT);

        verify(locale).wizardFileProxyServiceNoticeEmptyName();
    }

    @Override
    public void onFailureMethodInCommitCallbackShouldBeExecuted() throws Exception {
        super.onFailureMethodInCommitCallbackShouldBeExecuted();

        verify(resources).proxyServiceTemplate();
        verify(view, times(2)).getResourceName();
    }

    @Override
    public void onSuccessMethodInCommitCallbackShouldBeExecuted() throws Exception {
        super.onSuccessMethodInCommitCallbackShouldBeExecuted();

        verify(resources).proxyServiceTemplate();
        verify(view, times(2)).getResourceName();
    }
}