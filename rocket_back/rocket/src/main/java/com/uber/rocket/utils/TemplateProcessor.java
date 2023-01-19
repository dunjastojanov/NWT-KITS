package com.uber.rocket.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class TemplateProcessor {
    private final ResourceLoader resourceLoader;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public TemplateProcessor(ResourceLoader resourceLoader, SpringTemplateEngine templateEngine) {
        this.resourceLoader = resourceLoader;
        this.templateEngine = templateEngine;
    }

    public String process(Map<String, String> variables, String templateName) {
        return templateEngine.process(getTemplate(templateName), getContext(variables));
    }

    private static Context getContext(Map<String, String> variables) {
        Context context = new Context();
        setVariables(variables, context);
        return context;
    }

    private static void setVariables(Map<String, String> variables, Context context) {
        for (Map.Entry<String,String> entry : variables.entrySet())
            context.setVariable(entry.getKey(), entry.getValue());
    }

    private String getTemplate(String templateName) {
        Resource resource = getResource(templateName);
        return convertResourceToString(resource);
    }

    private static String convertResourceToString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Resource getResource(String templateName) {
        return resourceLoader.getResource(String.format("classpath:templates/%s.html", templateName));
    }

}
