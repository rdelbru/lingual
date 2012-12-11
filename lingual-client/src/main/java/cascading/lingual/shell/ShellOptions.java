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

package cascading.lingual.shell;

import java.util.List;

import cascading.lingual.common.Options;
import cascading.util.Util;
import joptsimple.OptionSpec;

/**
 *
 */
public class ShellOptions extends Options
  {
  private OptionSpec<String> schemas;
  private OptionSpec<String> resultPath;
  private OptionSpec<String> dotPath;
  private OptionSpec<String> sqlFile;


  public ShellOptions()
    {
    super();

    schemas = parser.accepts( "schemas", "root path for each schema to use" )
      .withRequiredArg().withValuesSeparatedBy( ',' );

    resultPath = parser.accepts( "resultPath", "root temp path to store results" )
      .withRequiredArg();

    dotPath = parser.accepts( "dotPath", "path to write flow dot files" )
      .withRequiredArg();

    sqlFile = parser.accepts( "sql", "file with sql commands to execute" )
      .withRequiredArg();
    }

  public String createJDBCUrl()
    {
    StringBuilder builder = new StringBuilder( "jdbc:lingual:" );

    builder.append( getPlatform() );

    if( !getSchemas().isEmpty() )
      {
      builder
        .append( ";schemas=" )
        .append( Util.join( getSchemas(), "," ) );
      }

    if( getResultPath() != null )
      {
      builder.append( ";resultPath=" ).append( getResultPath() );
      }

    if( getDotPath() != null )
      {
      builder.append( ";dotPath=" ).append( getDotPath() );
      }

    return builder.toString();
    }

  /////

  public List<String> getSchemas()
    {
    return optionSet.valuesOf( schemas );
    }

  public String getResultPath()
    {
    return optionSet.valueOf( resultPath );
    }

  public String getDotPath()
    {
    return optionSet.valueOf( dotPath );
    }

  public String getSqlFile()
    {
    return optionSet.valueOf( sqlFile );
    }

  /////

  }