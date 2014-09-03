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
package com.codenvy.ide.client.elements;

import com.google.gwt.xml.client.Node;
import com.google.inject.ImplementedBy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * The entity that represents a diagram element branch. It can have different state. It depends on a mediator. It can be a 'Case' branch of
 * Switch mediator or 'Then' branch of Filter mediator and etc.
 *
 * @author Andrey Plotnikov
 */
@ImplementedBy(BranchImpl.class)
public interface Branch {

    /**
     * @return an unique branch identifier. All instances of branches will have an unique identifier. This means that anyone can find
     * needed branch.
     */
    @Nonnull
    String getId();

    /**
     * Set name of branch. The name will be used for serialization and deserialization like a identifier of the branch.
     *
     * @param name
     *         name that needs to be set
     */
    void setName(@Nullable String name);

    /** @return title of the branch */
    @Nullable
    String getTitle();

    /**
     * Set title of the branch. Te title will be shown in the widget.
     *
     * @param title
     *         title that needs to be set
     */
    void setTitle(@Nullable String title);

    /** @return parent of the branch */
    @Nullable
    Element getParent();

    /**
     * Set parent of branch.
     *
     * @param parent
     *         parent that needs to be added
     */
    void setParent(@Nullable Element parent);

    /**
     * Add an inner element into this one element.
     *
     * @param element
     *         element that need to be added
     */
    void addElement(@Nonnull Element element);

    /**
     * Remove a inner element from this one element.
     *
     * @param element
     *         element that need to be removed
     */
    void removeElement(@Nonnull Element element);

    /** @return a list of inner elements */
    @Nonnull
    List<Element> getElements();

    /** @return true if branch has elements */
    boolean hasElements();

    /** @return serialized representation of the branch */
    @Nonnull
    String serialize();

    /**
     * Deserialize branch.
     *
     * @param node
     *         XML node that need to be deserialized
     */
    void deserialize(@Nonnull Node node);

    /**
     * Adds XML attribute.
     *
     * @param name
     *         attribute name
     * @param value
     *         attribute value
     */
    void addAttribute(@Nonnull String name, @Nonnull String value);

    /**
     * Returns attribute value by attribute name or <code>null</code> when attribute with a given name is absent.
     *
     * @param name
     *         attribute name that needs to be found
     * @return attribute value
     */
    @Nullable
    String getAttributeByName(@Nonnull String name);

}