/*==============================================================================
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
==============================================================================*/
package eu.delving.x3ml.engine;

import com.hp.hpl.jena.rdf.model.Resource;
import org.w3c.dom.Node;
import static eu.delving.x3ml.engine.X3ML.RangeElement;

/**
 * The range entity handled here. Resolution delegated.
 *
 * @author Gerald de Jong &lt;gerald@delving.eu&gt;
 * @author Nikos Minadakis &lt;minadakn@ics.forth.gr&gt;
 * @author Yannis Marketakis &lt;marketak@ics.forth.gr&gt;
 */
public class Range extends GeneratorContext {

    public final Path path;
    public final RangeElement range;
    public EntityResolver rangeResolver;

    public Range(Root.Context context, Path path, RangeElement range, Node node, int index) {
        super(context, path, node, index);
        this.path = path;
        this.range = range;
    }

    public boolean resolve() {
        if (conditionFails(range.target_node.condition, this)) {
            return false;
        }
        rangeResolver = new EntityResolver(context.output(), range.target_node.entityElement, this);
        if(path.property == null){  //this happens only when using the MERGE facility */
            return rangeResolver.resolve(0,0,Domain.domainNode);
        }else{
            return rangeResolver.resolve(0,0,null);
        }
    }

    public void link() {
        path.link();
        if (rangeResolver.hasResources()) {
            rangeResolver.link();
            for (Resource lastResource : path.lastResources) {
                for (Resource resolvedResource : rangeResolver.resources) {
                    if(path.lastProperty==null){    //this happens only when using the MERGE facility
                        break;
                    }else{
                        lastResource.addProperty(path.lastProperty, resolvedResource);
                    }
                }
            }
        } else if (rangeResolver.hasLiteral()) {
            for (Resource lastResource : path.lastResources) {
                if(path.lastProperty==null){    //this happens only when using the MERGE facility
                        break;
                    }else{
                        lastResource.addLiteral(path.lastProperty, rangeResolver.literal);
                }
            }
        }
    }
}
