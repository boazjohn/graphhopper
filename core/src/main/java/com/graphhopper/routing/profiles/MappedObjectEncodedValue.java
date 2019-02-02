/*
 *  Licensed to GraphHopper GmbH under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for
 *  additional information regarding copyright ownership.
 *
 *  GraphHopper GmbH licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except in
 *  compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.graphhopper.routing.profiles;

import com.graphhopper.storage.IntsRef;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class implements an ObjectEncodedValue and holds an array of IndexBased objects. It stores just the indices
 * of the used objects as an integer value.
 */
public final class MappedObjectEncodedValue extends SimpleIntEncodedValue implements ObjectEncodedValue {
    private final IndexBased[] arr;
    private final Map<String, IndexBased> map;

    public MappedObjectEncodedValue(String name, List<? extends IndexBased> values) {
        super(name, 32 - Integer.numberOfLeadingZeros(values.size()));

        arr = new IndexBased[values.size()];
        map = new ConcurrentHashMap<>(values.size());
        int counter = 0;
        for (IndexBased object : values) {
            if (counter != object.ordinal())
                throw new IllegalArgumentException("Object index does not fit " + counter + " vs " + object.ordinal());
            arr[counter] = object;
            IndexBased old = map.put(object.toString(), object);
            if (old != null)
                throw new IllegalArgumentException("Object name duplicated " + object);
            counter++;
        }
    }

    @Override
    public IndexBased[] getObjects() {
        return arr;
    }

    @Override
    public int indexOf(String name) {
        if (name == null)
            throw new NullPointerException("name cannot be null");
        IndexBased object = map.get(name);
        // return **default** if not found
        return object == null ? 0 : object.ordinal();
    }

    @Override
    public final void setObject(boolean reverse, IntsRef ref, IndexBased value) {
        int intValue = value.ordinal();
        super.setInt(reverse, ref, intValue);
    }

    @Override
    public final IndexBased getObject(boolean reverse, IntsRef ref) {
        int value = super.getInt(reverse, ref);
        return arr[value];
    }
}
