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
package com.graphhopper.routing.weighting;

import com.graphhopper.routing.profiles.IntEncodedValue;
import com.graphhopper.routing.profiles.ObjectEncodedValue;
import com.graphhopper.storage.NodeAccess;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.shapes.BBox;

public interface ScriptInterface {
    double getMillisFactor(EdgeIteratorState edge, boolean reverse);

    HelperVariables getHelperVariables();

    class HelperVariables {
        public ObjectEncodedValue road_environment;
        public ObjectEncodedValue road_class;
        public IntEncodedValue toll;
        public NodeAccess nodeAccess;

        public BBox getBBox(EdgeIteratorState edge) {
            BBox bbox = BBox.createInverse(false);
            bbox.update(nodeAccess.getLatitude(edge.getBaseNode()), nodeAccess.getLongitude(edge.getBaseNode()));
            bbox.update(nodeAccess.getLatitude(edge.getAdjNode()), nodeAccess.getLongitude(edge.getAdjNode()));
            return bbox;
        }
    }
}
