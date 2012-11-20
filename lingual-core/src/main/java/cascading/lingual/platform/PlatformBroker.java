/*
 * Copyright (c) 2007-2012 Concurrent, Inc. All Rights Reserved.
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

package cascading.lingual.platform;

import java.io.IOException;
import java.util.Properties;

import cascading.flow.FlowConnector;
import cascading.flow.FlowProcess;
import cascading.flow.planner.PlatformInfo;
import cascading.lingual.catalog.SchemaCatalog;
import cascading.lingual.optiq.meta.Branch;
import cascading.tap.type.FileType;
import cascading.util.Util;

import static cascading.lingual.jdbc.Driver.SCHEMA_PROP;
import static cascading.lingual.jdbc.Driver.TABLE_PROP;

/**
 *
 */
public abstract class PlatformBroker<Config>
  {
  private Properties properties;
  private SchemaCatalog catalog;

  protected PlatformBroker()
    {
    }

  public void setProperties( Properties properties )
    {
    this.properties = properties;
    }

  public Properties getProperties()
    {
    return properties;
    }

  public abstract String getName();

  public abstract Config getConfig();

  public abstract FlowProcess<Config> getFlowProcess();

  public SchemaCatalog getCatalog()
    {
    if( catalog == null )
      catalog = loadCatalog();

    return catalog;
    }

  private SchemaCatalog loadCatalog()
    {
    SchemaCatalog catalog = createCatalog();

    if( properties.containsKey( SCHEMA_PROP ) )
      loadSchemas( catalog );

    if( properties.containsKey( TABLE_PROP ) )
      loadTables( catalog );

    return catalog;
    }

  private void loadSchemas( SchemaCatalog catalog )
    {
    try
      {
      String schemaProperty = getStringProperty( SCHEMA_PROP );
      String[] schemaIdentifiers = schemaProperty.split( "," );

      for( String schemaIdentifier : schemaIdentifiers )
        catalog.addSchemaFor( schemaIdentifier );
      }
    catch( IOException exception )
      {
      exception.printStackTrace();
      }
    }

  private void loadTables( SchemaCatalog catalog )
    {
    String tableProperty = getStringProperty( TABLE_PROP );
    String[] tableIdentifiers = tableProperty.split( "," );

    for( String tableIdentifier : tableIdentifiers )
      catalog.addTableFor( tableIdentifier );
    }

  private String getStringProperty( String propertyName )
    {
    return properties.getProperty( propertyName );
    }

  protected abstract SchemaCatalog createCatalog();

  public String[] getChildIdentifiers( String identifier ) throws IOException
    {
    return getChildIdentifiers( getFileTypeFor( identifier ) );
    }

  public abstract FileType getFileTypeFor( String identifier );

  public abstract String[] getChildIdentifiers( FileType<Config> fileType ) throws IOException;

  public PlatformInfo getPlatformInfo()
    {
    return getFlowConnector().getPlatformInfo();
    }

  public abstract FlowConnector getFlowConnector();

  public LingualFlowFactory getFlowFactory( Branch branch )
    {
    return new LingualFlowFactory( this, createName(), branch );
    }

  private String createName()
    {
    return "" + System.currentTimeMillis() + "-" + Util.createUniqueID().substring( 0, 10 );
    }
  }
