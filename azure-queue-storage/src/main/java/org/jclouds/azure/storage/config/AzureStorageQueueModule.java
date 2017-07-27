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
package org.jclouds.azure.storage.config;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.inject.Provides;
import org.jclouds.azure.storage.AzureStorageQueueApi;
import org.jclouds.date.DateService;
import org.jclouds.date.TimeStamp;
import org.jclouds.json.config.GsonModule;
import org.jclouds.rest.ConfiguresHttpApi;
import org.jclouds.rest.config.HttpApiModule;

import javax.inject.Named;
import java.util.concurrent.TimeUnit;

import static org.jclouds.Constants.PROPERTY_SESSION_INTERVAL;

@ConfiguresHttpApi
public class AzureStorageQueueModule extends HttpApiModule<AzureStorageQueueApi> {
   @Override
   protected void configure() {
      super.configure();
      bind(GsonModule.DateAdapter.class).to(GsonModule.Iso8601DateAdapter.class);
   }

   @Provides
   @TimeStamp
   protected final String guiceProvideTimeStamp(@TimeStamp Supplier<String> cache) {
      return provideTimeStamp(cache);
   }

   protected String provideTimeStamp(@TimeStamp Supplier<String> cache) {
      return cache.get();
   }

   /**
    * borrowing concurrency code to ensure that caching takes place properly
    */
   @Provides
   @TimeStamp
   protected Supplier<String> provideTimeStampCache(@Named(PROPERTY_SESSION_INTERVAL) long seconds,
                                                    final DateService dateService) {
      return Suppliers.memoizeWithExpiration(new Supplier<String>() {
         @Override
         public String get() {
            return dateService.rfc822DateFormat();
         }
      }, seconds, TimeUnit.SECONDS);
   }

}
