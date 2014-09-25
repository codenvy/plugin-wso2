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
package com.codenvy.ide.client.propertiespanel.connectors.twitter;

import com.codenvy.ide.client.WSO2EditorLocalizationConstant;
import com.codenvy.ide.client.elements.connectors.twitter.SearchPlaces;
import com.codenvy.ide.client.elements.connectors.twitter.TwitterPropertyManager;
import com.codenvy.ide.client.inject.factories.PropertiesPanelWidgetFactory;
import com.codenvy.ide.client.managers.PropertyTypeManager;
import com.codenvy.ide.client.propertiespanel.PropertiesPanelView;
import com.codenvy.ide.client.propertiespanel.common.namespace.NameSpaceEditorPresenter;
import com.codenvy.ide.client.propertiespanel.connectors.base.AbstractConnectorPropertiesPanelPresenter;
import com.codenvy.ide.client.propertiespanel.connectors.base.parameter.ParameterPresenter;
import com.codenvy.ide.client.propertiespanel.property.complex.ComplexPropertyPresenter;
import com.codenvy.ide.client.propertiespanel.property.list.ListPropertyPresenter;
import com.codenvy.ide.client.propertiespanel.property.simple.SimplePropertyPresenter;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.annotation.Nonnull;

import static com.codenvy.ide.client.elements.connectors.AbstractConnector.PARAMETER_EDITOR_TYPE;
import static com.codenvy.ide.client.elements.connectors.AbstractConnector.ParameterEditorType.INLINE;
import static com.codenvy.ide.client.elements.connectors.twitter.SearchPlaces.ACCESS_TOKEN_EXPR;
import static com.codenvy.ide.client.elements.connectors.twitter.SearchPlaces.ACCESS_TOKEN_INL;
import static com.codenvy.ide.client.elements.connectors.twitter.SearchPlaces.ACCESS_TOKEN_NS;
import static com.codenvy.ide.client.elements.connectors.twitter.SearchPlaces.ACCESS_TOKEN_SECRET_EXPR;
import static com.codenvy.ide.client.elements.connectors.twitter.SearchPlaces.ACCESS_TOKEN_SECRET_INL;
import static com.codenvy.ide.client.elements.connectors.twitter.SearchPlaces.ACCESS_TOKEN_SECRET_NS;
import static com.codenvy.ide.client.elements.connectors.twitter.SearchPlaces.CONSUMER_KEY_EXPR;
import static com.codenvy.ide.client.elements.connectors.twitter.SearchPlaces.CONSUMER_KEY_INL;
import static com.codenvy.ide.client.elements.connectors.twitter.SearchPlaces.CONSUMER_KEY_NS;
import static com.codenvy.ide.client.elements.connectors.twitter.SearchPlaces.CONSUMER_SECRET_EXPR;
import static com.codenvy.ide.client.elements.connectors.twitter.SearchPlaces.CONSUMER_SECRET_INL;
import static com.codenvy.ide.client.elements.connectors.twitter.SearchPlaces.CONSUMER_SECRET_NS;
import static com.codenvy.ide.client.elements.connectors.twitter.SearchPlaces.LATITUDE_EXPR;
import static com.codenvy.ide.client.elements.connectors.twitter.SearchPlaces.LATITUDE_INL;
import static com.codenvy.ide.client.elements.connectors.twitter.SearchPlaces.LATITUDE_NS;
import static com.codenvy.ide.client.elements.connectors.twitter.SearchPlaces.LONGITUDE_EXPR;
import static com.codenvy.ide.client.elements.connectors.twitter.SearchPlaces.LONGITUDE_INL;
import static com.codenvy.ide.client.elements.connectors.twitter.SearchPlaces.LONGITUDE_NS;
import static com.codenvy.ide.client.elements.connectors.twitter.SearchPlaces.QUERY_EXPR;
import static com.codenvy.ide.client.elements.connectors.twitter.SearchPlaces.QUERY_INL;
import static com.codenvy.ide.client.elements.connectors.twitter.SearchPlaces.QUERY_NS;

/**
 * The class provides the business logic that allows editor to react on user's action and to change state of connector
 * depending on user's changes of properties.
 *
 * @author Dmitry Shnurenko
 * @author Valeriy Svydenko
 */
public class SearchPlacesConnectorPresenter extends AbstractConnectorPropertiesPanelPresenter<SearchPlaces> {

    private SimplePropertyPresenter consumerKey;
    private SimplePropertyPresenter consumerSecret;
    private SimplePropertyPresenter accessToken;
    private SimplePropertyPresenter accessTokenSecret;
    private SimplePropertyPresenter latitude;
    private SimplePropertyPresenter longitude;
    private SimplePropertyPresenter query;

    private ComplexPropertyPresenter consumerKeyExpr;
    private ComplexPropertyPresenter consumerSecretExpr;
    private ComplexPropertyPresenter accessTokenExpr;
    private ComplexPropertyPresenter accessTokenSecretExpr;
    private ComplexPropertyPresenter latitudeExpr;
    private ComplexPropertyPresenter longitudeExpr;
    private ComplexPropertyPresenter queryExpr;

    @Inject
    public SearchPlacesConnectorPresenter(WSO2EditorLocalizationConstant locale,
                                          NameSpaceEditorPresenter nameSpacePresenter,
                                          PropertiesPanelView view,
                                          TwitterPropertyManager twitterPropertyManager,
                                          ParameterPresenter parameterPresenter,
                                          PropertyTypeManager propertyTypeManager,
                                          PropertiesPanelWidgetFactory propertiesPanelWidgetFactory,
                                          Provider<ListPropertyPresenter> listPropertyPresenterProvider,
                                          Provider<ComplexPropertyPresenter> complexPropertyPresenterProvider,
                                          Provider<SimplePropertyPresenter> simplePropertyPresenterProvider) {
        super(view,
              twitterPropertyManager,
              parameterPresenter,
              nameSpacePresenter,
              propertyTypeManager,
              locale,
              propertiesPanelWidgetFactory,
              listPropertyPresenterProvider,
              complexPropertyPresenterProvider,
              simplePropertyPresenterProvider);

        prepareView();
    }

    private void prepareView() {
        consumerKey = createSimplePanel(locale.twitterConsumerKey(), CONSUMER_KEY_INL);
        consumerSecret = createSimplePanel(locale.twitterConsumerSecret(), CONSUMER_SECRET_INL);
        accessToken = createSimplePanel(locale.twitterAccessToken(), ACCESS_TOKEN_INL);
        accessTokenSecret = createSimplePanel(locale.twitterAccessTokenSecret(), ACCESS_TOKEN_SECRET_INL);
        latitude = createSimplePanel(locale.twitterLatitude(), LATITUDE_INL);
        longitude = createSimplePanel(locale.twitterLongitude(), LONGITUDE_INL);
        query = createSimplePanel(locale.twitterQuery(), QUERY_INL);

        consumerKeyExpr = createComplexPanel(locale.twitterConsumerKey(), CONSUMER_KEY_NS, CONSUMER_KEY_EXPR);
        consumerSecretExpr = createComplexPanel(locale.twitterConsumerSecret(), CONSUMER_SECRET_NS, CONSUMER_SECRET_EXPR);
        accessTokenExpr = createComplexPanel(locale.twitterAccessToken(), ACCESS_TOKEN_NS, ACCESS_TOKEN_EXPR);
        accessTokenSecretExpr = createComplexPanel(locale.twitterAccessTokenSecret(), ACCESS_TOKEN_SECRET_NS, ACCESS_TOKEN_SECRET_EXPR);
        latitudeExpr = createComplexPanel(locale.twitterLatitude(), LATITUDE_NS, LATITUDE_EXPR);
        longitudeExpr = createComplexPanel(locale.twitterLongitude(), LONGITUDE_NS, LONGITUDE_EXPR);
        queryExpr = createComplexPanel(locale.twitterQuery(), QUERY_NS, QUERY_EXPR);
    }

    /** {@inheritDoc} */
    @Override
    protected void redrawPropertiesPanel() {
        boolean isVisible = INLINE.equals(element.getProperty(PARAMETER_EDITOR_TYPE));

        consumerKey.setVisible(isVisible);
        consumerSecret.setVisible(isVisible);
        accessToken.setVisible(isVisible);
        accessTokenSecret.setVisible(isVisible);
        latitude.setVisible(isVisible);
        longitude.setVisible(isVisible);
        query.setVisible(isVisible);

        consumerKeyExpr.setVisible(!isVisible);
        consumerSecretExpr.setVisible(!isVisible);
        accessTokenExpr.setVisible(!isVisible);
        accessTokenSecretExpr.setVisible(!isVisible);
        latitudeExpr.setVisible(!isVisible);
        longitudeExpr.setVisible(!isVisible);
        queryExpr.setVisible(!isVisible);
    }

    /** {@inheritDoc} */
    @Override
    public void go(@Nonnull AcceptsOneWidget container) {
        super.go(container);

        consumerKey.setProperty(element.getProperty(CONSUMER_KEY_INL));
        consumerSecret.setProperty(element.getProperty(CONSUMER_SECRET_INL));
        accessToken.setProperty(element.getProperty(ACCESS_TOKEN_INL));
        accessTokenSecret.setProperty(element.getProperty(ACCESS_TOKEN_SECRET_INL));
        latitude.setProperty(element.getProperty(LATITUDE_INL));
        longitude.setProperty(element.getProperty(LONGITUDE_INL));
        query.setProperty(element.getProperty(QUERY_INL));

        consumerKey.setProperty(element.getProperty(CONSUMER_KEY_EXPR));
        consumerSecret.setProperty(element.getProperty(CONSUMER_SECRET_EXPR));
        accessToken.setProperty(element.getProperty(ACCESS_TOKEN_EXPR));
        accessTokenSecret.setProperty(element.getProperty(ACCESS_TOKEN_SECRET_EXPR));
        latitudeExpr.setProperty(element.getProperty(LATITUDE_EXPR));
        longitudeExpr.setProperty(element.getProperty(LONGITUDE_EXPR));
        queryExpr.setProperty(element.getProperty(QUERY_EXPR));
    }
}
