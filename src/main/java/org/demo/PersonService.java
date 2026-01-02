package org.demo;

import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PersonService
{
    public static void main(String[] args)
    {
        var app = Javalin.create( config ->
                config.bundledPlugins.enableCors(cors ->
                   cors.addRule(CorsPluginConfig.CorsRule::anyHost)))
                .get("/person/list", ctx -> ctx.json(listPersons()))
                .get("/person/{id}", ctx -> ctx.json(getPerson(ctx.pathParam("id"))))
                .post("/person", ctx -> ctx.json(addPerson(ctx.bodyAsClass(Person.class))))
                .start(7070);
    }

    record Person(String firstname, String lastname){}

    private static Person getPerson(String id) {
        return OBJECTS.getOrDefault(id, new Person(null,null));
    }

    private static Collection<Person> listPersons() {
        return OBJECTS.values();
    }

    private static String addPerson(Person p)
    {
        if(p.firstname()==null || p.firstname.isEmpty() || p.lastname == null || p.lastname.isEmpty()) {
            return "Fehler: Vorname und Zuname müssen belegt sein";
        }
        var newID= (OBJECTS.size()+1)+"";
        OBJECTS.put(newID, p);
        return "Person gespeichert";
    }

    private static Map<String,Person> OBJECTS= new HashMap<String,Person>() {{
         put("id01",new Person("Max","Mustermann"));
         put("id02",new Person("Margot","Musterfrau"));
    }};
}