/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
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
package org.jclouds.openstack.nova.v1_1.features;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jclouds.openstack.domain.Resource;
import org.jclouds.openstack.filters.AuthenticateRequest;
import org.jclouds.openstack.nova.v1_1.NovaClient;
import org.jclouds.openstack.nova.v1_1.domain.RebootType;
import org.jclouds.openstack.nova.v1_1.domain.Server;
import org.jclouds.openstack.nova.v1_1.options.CreateServerOptions;
import org.jclouds.openstack.nova.v1_1.options.RebuildServerOptions;
import org.jclouds.rest.annotations.ExceptionParser;
import org.jclouds.rest.annotations.MapBinder;
import org.jclouds.rest.annotations.Payload;
import org.jclouds.rest.annotations.PayloadParam;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.SelectJson;
import org.jclouds.rest.annotations.SkipEncoding;
import org.jclouds.rest.annotations.Unwrap;
import org.jclouds.rest.functions.ReturnEmptySetOnNotFoundOr404;
import org.jclouds.rest.functions.ReturnFalseOnNotFoundOr404;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Provides asynchronous access to Server via their REST API.
 * <p/>
 * 
 * @see ServerClient
 * @see <a href="http://docs.openstack.org/api/openstack-compute/1.1/content/Servers-d1e2073.html"
 *      />
 * @author Adrian Cole
 */
@SkipEncoding({ '/', '=' })
@RequestFilters(AuthenticateRequest.class)
public interface ServerAsyncClient {

   /**
    * @see ServerClient#listServers
    */
   @GET
   @SelectJson("servers")
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/servers")
   @ExceptionParser(ReturnEmptySetOnNotFoundOr404.class)
   ListenableFuture<Set<Resource>> listServers();

   /**
    * @see ServerClient#listServersInDetail
    */
   @GET
   @SelectJson("servers")
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/servers/detail")
   @ExceptionParser(ReturnEmptySetOnNotFoundOr404.class)
   ListenableFuture<Set<Server>> listServersInDetail();

   /**
    * @see ServerClient#getServer
    */
   @GET
   @SelectJson("server")
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/servers/{id}")
   @ExceptionParser(ReturnNullOnNotFoundOr404.class)
   ListenableFuture<Server> getServer(@PathParam("id") String id);


    /**
     * @see NovaClient#deleteServer
     */
    @DELETE
    @Consumes
    @ExceptionParser(ReturnFalseOnNotFoundOr404.class)
    @Path("/servers/{id}")
    ListenableFuture<Boolean> deleteServer(@PathParam("id") String id);

   /**
    * @see NovaClient#rebootServer
    */
   @POST
   @Path("/servers/{id}/action")
   @Consumes
   @Produces(MediaType.APPLICATION_JSON)
   @Payload("%7B\"reboot\":%7B\"type\":\"{type}\"%7D%7D")
   ListenableFuture<Void> rebootServer(@PathParam("id") String id, @PayloadParam("type") RebootType rebootType);

   /**
    * @see NovaClient#resizeServer
    */
   @POST
   @Path("/servers/{id}/action")
   @Consumes
   @Produces(MediaType.APPLICATION_JSON)
   @Payload("%7B\"resize\":%7B\"flavorId\":{flavorId}%7D%7D")
   ListenableFuture<Void> resizeServer(@PathParam("id") String id, @PayloadParam("flavorId") String flavorId);

   /**
    * @see NovaClient#confirmResizeServer
    */
   @POST
   @Path("/servers/{id}/action")
   @Consumes
   @Produces(MediaType.APPLICATION_JSON)
   @Payload("{\"confirmResize\":null}")
   ListenableFuture<Void> confirmResizeServer(@PathParam("id") String id);

   /**
    * @see NovaClient#revertResizeServer
    */
   @POST
   @Path("/servers/{id}/action")
   @Consumes
   @Produces(MediaType.APPLICATION_JSON)
   @Payload("{\"revertResize\":null}")
   ListenableFuture<Void> revertResizeServer(@PathParam("id") String id);

   /**
    * @see NovaClient#createServer
    */
   @POST
   @Unwrap
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/servers")
   @MapBinder(CreateServerOptions.class)
   ListenableFuture<Server> createServer(@PayloadParam("name") String name, @PayloadParam("imageRef") String imageRef,
                                         @PayloadParam("flavorRef") String flavorRef, CreateServerOptions... options);

   /**
    * @see NovaClient#rebuildServer
    */
   @POST
   @Path("/servers/{id}/action")
   @Consumes
   @MapBinder(RebuildServerOptions.class)
   ListenableFuture<Void> rebuildServer(@PathParam("id") String id, RebuildServerOptions... options);


   /**
    * @see NovaClient#changeAdminPass
    */
   @POST
   @Path("/servers/{id}/action")
   @Consumes
   @Produces(MediaType.APPLICATION_JSON)
   @Payload("%7B\"changePassword\":%7B\"adminPass\":\"{adminPass}\"%7D%7D")
   ListenableFuture<Void> changeAdminPass(@PathParam("id") String id, @PayloadParam("adminPass") String adminPass);

   /**
    * @see NovaClient#renameServer
    */
   @PUT
   @Path("/servers/{id}")
   @Consumes
   @Produces(MediaType.APPLICATION_JSON)
   @Payload("%7B\"server\":%7B\"name\":\"{name}\"%7D%7D")
   ListenableFuture<Void> renameServer(@PathParam("id") String id, @PayloadParam("name") String newName);
}
