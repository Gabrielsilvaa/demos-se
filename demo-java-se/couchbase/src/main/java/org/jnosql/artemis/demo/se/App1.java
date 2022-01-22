/*
 * Copyright (c) 2017 Otávio Santana and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 * You may elect to redistribute this code under either of these licenses.
 *
 * Contributors:
 *
 * Otavio Santana
 */

package org.jnosql.artemis.demo.se;


import com.couchbase.client.java.search.SearchQuery;
import com.couchbase.client.java.search.queries.MatchQuery;
import org.eclipse.jnosql.mapping.couchbase.document.CouchbaseTemplate;
import jakarta.nosql.document.DocumentQuery;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static jakarta.nosql.document.DocumentQuery.select;

public class App1 {


    public static void main(String[] args) {

        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {

            Hero ironMan = Hero.builder().withRealName("Tony Stark").withName("iron_man")
                    .withAge(34).withPowers(Collections.singleton("rich")).build();

            CouchbaseTemplate couchbaseTemplate = container.select(CouchbaseTemplate.class).get();
            couchbaseTemplate.insert(ironMan);

            DocumentQuery query = select().from("Hero").where("_id").eq("iron_man").build();
            List<Hero> heroes = couchbaseTemplate.<Hero>select(query).collect(Collectors.toList());
            System.out.println(heroes);

            MatchQuery match = SearchQuery.match("rich").field("powers");
            SearchQuery search = new SearchQuery("heroes-index", match);
            List<Hero> searchResult = couchbaseTemplate.<Hero>search(search).collect(Collectors.toList());
            System.out.println(searchResult);


        }
    }
    private App1() {
    }
}
