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
package com.codenvy.ide.client.propertiespanel.mediators.arguments;

import com.codenvy.ide.client.elements.mediators.payload.Arg;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * The entity which includes arguments, that may be modified, must implement this interface. This
 * method will be call when argument  changed.
 *
 * @author Valeriy Svydenko
 * @author Dmitry Shnurenko
 */
public interface AddArgumentCallBack {
    /**
     * Performs some actions when arguments was changed.
     *
     * @param arg
     *         changed list of properties
     */
    void onArgumentsChanged(@Nonnull List<Arg> arg);
}
