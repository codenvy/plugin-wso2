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
package com.codenvy.ide.client.propertiespanel.mediators.inline;

import javax.annotation.Nonnull;

/**
 * The entity which includes inline parameter, that may be modified, must implement this interface. This
 * method will be call when inline parameter is changed.
 *
 * @author Valeriy Svydenko
 * @author Dmitry Shnurenko
 */
public interface ChangeInlineFormatCallBack {
    /**
     * Performs some actions when inline was changed.
     *
     * @param inline
     *         changed content of inline
     */
    void onInlineChanged(@Nonnull String inline);
}
