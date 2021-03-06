/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.spark.sql.catalyst.expressions

import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.codegen.CodegenFallback
import org.apache.spark.sql.catalyst.utils.SetAccumulator
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.types.IntegerType

case class AccumulateFiles(
    filesAccumulator: SetAccumulator[String],
    child: Expression) extends UnaryExpression with CodegenFallback {

  override def dataType: DataType = IntegerType
  override def nullable: Boolean = true
  override def prettyName: String = "AccumulateFiles"
  override lazy val deterministic: Boolean = false
  private val RETURN_VAL: Integer = 1

  override def eval(input: InternalRow) : Any = {
    val resultVal = child.eval(input)
    filesAccumulator.add(resultVal.toString)
    RETURN_VAL
  }
}
