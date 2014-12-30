package com.clemble.casino.server.template;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.apache.lucene.util.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by mavarazy on 12/30/14.
 */
public class MustacheTemplateService implements TemplateService {

    final private LoadingCache<String, Template> CACHE = CacheBuilder.newBuilder().build(new CacheLoader<String, Template>(){

        @Override
        public Template load(String key) throws Exception {
            ClassPathResource tmplResource = new ClassPathResource("/templates/" + key + ".hbs");
            InputStream is = tmplResource.getInputStream();
            try {
                return Mustache.compiler().compile(new InputStreamReader(is));
            } finally {
                is.close();
            }
        }

    });

    @Override
    public String produce(String key, Map<String, String> params) {
        try {
            // Step 1. Fetching template
            Template template = CACHE.get(key);
            // Step 2. Execute template
            return template.execute(params);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
