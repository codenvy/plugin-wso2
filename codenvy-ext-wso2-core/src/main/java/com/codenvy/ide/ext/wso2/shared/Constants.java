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
package com.codenvy.ide.ext.wso2.shared;

/**
 * Contains general constants that needed on client and server side.
 *
 * @author Andrey Plotnikov
 * @author Dmitry Kuleshov
 */
public interface Constants {

    String WSO2_PROJECT_ID              = "WSO2Project";
    String ESB_CONFIGURATION_PROJECT_ID = "ESBConfigurationProject";
    /**
     * Dedicated mime type name for WSO2 ESB configuration files
     */
    String ESB_XML_MIME_TYPE = "text/xml-esb";
    /**
     * Extension name for WSO2 ESB configuration files
     */
    String ESB_XML_EXTENSION = "xml";

}