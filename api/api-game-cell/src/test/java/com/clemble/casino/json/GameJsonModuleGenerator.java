package com.clemble.casino.json;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;

import com.clemble.test.reflection.AnnotationReflectionUtils;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.jsontype.NamedType;

public class GameJsonModuleGenerator {

    final private static String MODULE = "GameCell";

    @Test
    public void findCandidates(){
        Collection<Class<?>> candidates = AnnotationReflectionUtils.findCandidates("com.clemble.casino.game.cell", JsonTypeName.class);
        assertNotNull(candidates);
        assertTrue(candidates.size() > 0);
        System.out.println("package com.clemble.casino.json;");
        System.out.println();
        System.out.println("import com.fasterxml.jackson.databind.Module;");
        System.out.println("import com.fasterxml.jackson.annotation.JsonTypeName;");
        System.out.println("import com.fasterxml.jackson.databind.jsontype.NamedType;");
        System.out.println("import com.fasterxml.jackson.databind.module.SimpleModule;");
        System.out.println();
        for(Class<?> candidate: candidates) {
            System.out.println("import " + candidate.getCanonicalName() + ";");
        }
        System.out.println();
        System.out.println("class " + MODULE + "JsonModule implements ClembleJsonModule {");
        System.out.println();
        System.out.println("    @Override");
        System.out.println("    public Module construct() {");
        System.out.println("        SimpleModule module = new SimpleModule(\"" + MODULE + "\");");
        for(Class<?> candidate: candidates) {
            String candidateClass = candidate.getSimpleName() + ".class";
            new NamedType(candidate, candidate.getAnnotation(JsonTypeName.class).value());
            System.out.println("        module.registerSubtypes(new NamedType(" + candidateClass + ", " + candidateClass + ".getAnnotation(JsonTypeName.class).value()));");
        }
        System.out.println("        return module;");
        System.out.println("    }");
        System.out.println();
        System.out.println("}");
    }
}
