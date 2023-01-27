package com.uber.rocket.utils;

import com.uber.rocket.entity.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class TemplateProcessor {
    private final ResourceLoader resourceLoader;
    private final ITemplateEngine templateEngine;

    @Autowired
    public TemplateProcessor(ResourceLoader resourceLoader) {
        templateEngine = new SpringTemplateEngine();
        this.resourceLoader = resourceLoader;
    }

    private String process(Map<String, String> variables, String templateName) {
        String template = getTemplate(templateName);
        return templateEngine.process(template, getContext(variables));
    }

    public String getVariableString(Map<String, String> variables) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            builder.append(String.format("%s,%s;", entry.getKey(), entry.getValue()));

        }
        return builder.toString();
    }

    public Map<String, String> getVariableMap(String variables) {
        Map<String, String> variablesMap = new HashMap<>();
        String[] entryStrings = variables.split(";");
        for (String entryString : entryStrings) {
            String[] entry = entryString.split(",");
            if (entry.length > 1) {
                variablesMap.put(entry[0], entry[1]);
            } else throw new IllegalArgumentException("Variable string is not in correct format.");
        }
        return variablesMap;
    }

    private static Context getContext(Map<String, String> variables) {
        Context context = new Context();
        setVariables(variables, context);
        return context;
    }

    private static void setVariables(Map<String, String> variables, Context context) {
        for (Map.Entry<String, String> entry : variables.entrySet())
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

    public String process(Notification notification) {
        return process(getVariableMap(notification.getTemplateVariables()), getTemplateName(notification));
    }

    private String getTemplateName(Notification notification) {
        switch (notification.getType()) {
            case DRIVER_RIDE_REQUEST, PASSENGER_RIDE_REQUEST, RIDE_SCHEDULED -> {
                return "ride_without_driver";
            }
            case RIDE_CONFIRMED, DRIVER_ARRIVED, RIDE_REVIEW -> {
                return "ride_with_driver";
            }
            case UPDATE_DRIVER_PICTURE_REQUEST -> {
                return "update_driver_picture";
            }
            case UPDATE_DRIVER_DATA_REQUEST -> {
                return "update_driver_data";
            }
            case RIDE_CANCELED -> {
                return "ride_canceled";
            }
            case USER_BLOCKED -> {
                return "user_blocked";
            }
        }
        return null;
    }
}
