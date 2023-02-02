package com.uber.rocket.ride_execution;

import com.uber.rocket.entity.notification.Notification;
import com.uber.rocket.entity.notification.NotificationType;
import com.uber.rocket.utils.TemplateProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.ResourceLoader;
import org.thymeleaf.ITemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.MockitoAnnotations.openMocks;

class TemplateProcessorTest {
    @Mock
    ResourceLoader resourceLoader;
    @Mock
    ITemplateEngine templateEngine;
    @InjectMocks
    TemplateProcessor templateProcessor;
    AutoCloseable closeable;
    Notification notification;
    Map<String, String> variables;
    String variableString;

    @BeforeEach
    void setUp() {
        closeable = openMocks(this);
        variables = new HashMap<>();
        variables.put("Test1", "This is test 1.");
        variables.put("Test2", "This is test 2.");
        variableString = "Test1,This is test 1.;Test2,This is test 2.;";

        notification = new Notification();
        notification.setType(NotificationType.USER_BLOCKED);
        notification.setTemplateVariables(variableString);

    }

    @Test
    void getVariableString() {
        String result = templateProcessor.getVariableString(variables);
        assertEquals(variableString, result);
    }

    @Test
    void getVariableStringEmptyMap() {
        variables = new HashMap<>();
        String result = templateProcessor.getVariableString(variables);
        assertEquals("", result);
    }

    @Test
    void getVariableStringMapNull() {
        variables = null;
        assertThrows(NullPointerException.class, () -> {
            templateProcessor.getVariableString(variables);
        });
    }

    @Test
    void getVariableMap() {
        Map<String, String> result = templateProcessor.getVariableMap(variableString);
        assertEquals(variables, result);
    }

    @Test
    void getVariableMapEmptyString() {
        try {
            templateProcessor.getVariableMap("");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("Variable string is not in correct format.", e.getMessage());
        }
    }

    @Test
    void getVariableMapIncorrectFormatString() {
        try {
            templateProcessor.getVariableMap("fenjnbtrngtn");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("Variable string is not in correct format.", e.getMessage());
        }
    }
    @Test
    void getVariableMapNullString() {
        assertThrows(NullPointerException.class, ()->{
            templateProcessor.getVariableMap(null);
        });
    }

}