/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package org.jnosql.demo.se;


import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.mapping.document.DocumentTemplate;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import java.time.Year;
import java.util.List;
import java.util.UUID;

public class App2 {


    public static void main(String[] args) {

        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            String id = UUID.randomUUID().toString();
            DocumentTemplate template = container.select(DocumentTemplate.class).get();
            Book first = new Book(id, "Effective Java", "Joshua Bloch", Year.of(2001), 1);
            Book second = first.nextEdition(UUID.randomUUID().toString(), Year.of(2008));
            Book third = second.nextEdition(UUID.randomUUID().toString(), Year.of(2018));
            template.insert(List.of(first, second, third));
            DocumentQuery query = DocumentQuery.select().from("Book")
                    .where("title").eq("Effective Java")
                    .orderBy("year").desc().build();

            System.out.println("The Effective java editions: ");
            template.select(query).forEach(System.out::println);

            template.delete(Book.class, first.id());
            template.delete(Book.class, second.id());
            template.delete(Book.class, third.id());

        }
    }

    private App2() {
    }
}
