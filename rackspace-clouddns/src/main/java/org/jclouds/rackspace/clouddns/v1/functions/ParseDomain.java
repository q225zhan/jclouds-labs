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
package org.jclouds.rackspace.clouddns.v1.functions;

import static com.google.common.base.Preconditions.checkNotNull;

import java.beans.ConstructorProperties;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.jclouds.http.HttpResponse;
import org.jclouds.http.functions.ParseJson;
import org.jclouds.rackspace.clouddns.v1.domain.Domain;
import org.jclouds.rackspace.clouddns.v1.domain.Record;
import org.jclouds.rackspace.clouddns.v1.domain.Subdomain;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

/**
 * @author Everett Toews
 */
public class ParseDomain implements Function<HttpResponse, Domain> {

   private final ParseJson<RawDomain> json;

   @Inject
   ParseDomain(ParseJson<RawDomain> json) {
      this.json = checkNotNull(json, "json");
   }

   @Override
   public Domain apply(HttpResponse response) {
      RawDomain domainParsed = json.apply(response);

      if (domainParsed == null)
         return null;

      return domainParsed.getDomain();
   }

   /**
    * This class is here only to deal with the domain JSON format in Cloud DNS.
    */
   public static class RawDomain extends Domain {
      @ConstructorProperties({ "id", "name", "emailAddress", "comment", "created", "updated", "accountId", "ttl",
            "nameservers", "subdomains", "recordsList" })
      protected RawDomain(int id, String name, String emailAddress, String comment, Date created, Date updated,
            int accountId, int ttl, Iterable<Map<String, String>> nameservers,
            SubdomainsWithTotalEntries subdomainsWTE, RecordsWithTotalEntries recordsWTE) {
         super(id, name, emailAddress, comment, created, updated, accountId, ttl, transform(nameservers),
               transform(subdomainsWTE), transform(recordsWTE));
      }

      public Domain getDomain() {
         return from(this);
      }

      private static Set<String> transform(Iterable<Map<String, String>> nameserversNasty) {
         Builder<String> nameservers = ImmutableSet.builder();

         for (Map<String, String> nameToNameserver: nameserversNasty) {
            nameservers.add(nameToNameserver.get("name"));
         }

         return nameservers.build();
      }

      private static Set<Subdomain> transform(SubdomainsWithTotalEntries subdomainsWTE) {
         if (subdomainsWTE == null) {
            return null;
         }
         else {
            return ImmutableSet.<Subdomain> copyOf(subdomainsWTE.subdomains);
         }
      }

      private static Set<Record> transform(RecordsWithTotalEntries recordsWTE) {
         if (recordsWTE == null) {
            return null;
         }
         else {
            return ImmutableSet.<Record> copyOf(recordsWTE.records);
         }
      }

      /**
       * This class is here only to deal with the domain JSON format in Cloud DNS.
       */
      private static class SubdomainsWithTotalEntries {
         // ignore "totalEntries" in the JSON as it can just be derived form the size of subdomains
         @Named("domains")
         private Iterable<Subdomain> subdomains;

         @ConstructorProperties({ "domains" })
         protected SubdomainsWithTotalEntries(Iterable<Subdomain> subdomains) {
            this.subdomains = subdomains;
         }
      }

      /**
       * This class is here only to deal with the domain JSON format in Cloud DNS.
       */
      private static class RecordsWithTotalEntries {
         // ignore "totalEntries" in the JSON as it can just be derived form the size of records
         private Set<Record> records;

         @ConstructorProperties({ "records" })
         protected RecordsWithTotalEntries(Set<Record> records) {
            this.records = records;
         }
      }
   }
}
