/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.azure.storage.features;

import org.jclouds.azure.storage.domain.internals.QueueResponse.ListQueueResponse;
import org.jclouds.azure.storage.filters.SharedKeyLiteAuthentication;
import org.jclouds.http.functions.ReturnTrueIf2xx;
import org.jclouds.rest.annotations.Headers;
import org.jclouds.rest.annotations.JAXBResponseParser;
import org.jclouds.rest.annotations.QueryParams;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.ResponseParser;

import javax.inject.Named;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.io.Closeable;

@Headers(keys = "x-ms-version", values = "{jclouds.api-version}")
@RequestFilters(SharedKeyLiteAuthentication.class)
public interface QueueApi extends Closeable {

   @Named("azure_storage_queue_create")
   @PUT
   @Path("/{queueName}")
   @ResponseParser(ReturnTrueIf2xx.class)
   boolean create(@PathParam("queueName") String queueName);

   @Named("azure_storage_queue_list")
   @GET
   @QueryParams(keys = "comp", values = "list")
   @JAXBResponseParser
   ListQueueResponse list();

   @Named("azure_storage_queue_delete")
   @DELETE
   @Path("/{queueName}")
   @ResponseParser(ReturnTrueIf2xx.class)
   boolean delete(@PathParam("queueName") String queueName);

}
