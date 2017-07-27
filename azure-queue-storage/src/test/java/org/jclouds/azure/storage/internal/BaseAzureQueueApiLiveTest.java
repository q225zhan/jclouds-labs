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
package org.jclouds.azure.storage.internal;

import org.jclouds.apis.ApiMetadata;
import org.jclouds.apis.BaseApiLiveTest;
import org.jclouds.azure.storage.AzureStorageQueueApi;
import org.jclouds.azure.storage.AzureStorageQueueApiMetadata;

public class BaseAzureQueueApiLiveTest extends BaseApiLiveTest<AzureStorageQueueApi>{

   protected BaseAzureQueueApiLiveTest() {
      provider = "azure-queue-storage";
   }

   static {
      System.setProperty("azure-queue-storage.identity", "jcloudsazure");
      System.setProperty("azure-queue-storage.credential", "nH+KqygOYN9cy6jcYwoqY4P77F62TWzP2c8ef+0AmespWPhK0UW/HoH8vsqhC44qLTdNgnKSqyVzbtBTaZXEpQ==");

   }

   @Override
   protected ApiMetadata createApiMetadata() {
      return new AzureStorageQueueApiMetadata();
   }
}
