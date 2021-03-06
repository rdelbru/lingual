/*
 * Copyright (c) 2007-2013 Concurrent, Inc. All Rights Reserved.
 *
 * Project and contact information: http://www.cascading.org/
 *
 * This file is part of the Cascading project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package cascading.lingual.optiq;

import net.hydromatic.optiq.rules.java.EnumerableConvention;
import org.eigenbase.rel.RelNode;
import org.eigenbase.rel.convert.ConverterRule;
import org.eigenbase.relopt.volcano.AbstractConverter;

/**
 *
 */
class CascadingEnumerableConverterRule extends ConverterRule
  {
  public static final CascadingEnumerableConverterRule INSTANCE = new CascadingEnumerableConverterRule();

  public CascadingEnumerableConverterRule()
    {
    super( AbstractConverter.class, Cascading.CONVENTION, EnumerableConvention.INSTANCE, "Convert Cascading rels to Enumerable" );
    }

  @Override
  public boolean isGuaranteed()
    {
    return true;
    }

  @Override
  public RelNode convert( RelNode rel )
    {
    if( !rel.getTraitSet().contains( Cascading.CONVENTION ) )
      return null;

    return new CascadingEnumerableRel( rel.getCluster(), rel.getTraitSet().replace( getOutConvention() ), rel );
    }
  }
