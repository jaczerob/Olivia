package dev.jaczerob.olivia.common;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.builder.impl.DefaultConfigurationBuilder;
import org.apache.logging.log4j.core.config.plugins.Plugin;

import java.net.URI;

@Plugin(name = "JSONLogger", category = ConfigurationFactory.CATEGORY)
@Order(50)
public class Log4j2JSONFactory extends ConfigurationFactory {
    private static final String JSON_LAYOUT = """
            {
              "@timestamp": {
                "$resolver": "timestamp",
                "pattern": {
                  "format": "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                  "timeZone": "UTC"
                }
              },
              "log.level": {
                "$resolver": "level",
                "field": "name"
              },
              "message": {
                "$resolver": "message",
                "stringified": true
              },
              "labels": {
                "$resolver": "mdc",
                "flatten": true,
                "stringified": true
              },
              "error.type": {
                "$resolver": "exception",
                "field": "className"
              },
              "error.message": {
                "$resolver": "exception",
                "field": "message"
              },
              "error.stack_trace": {
                "$resolver": "exception",
                "field": "stackTrace",
                "stackTrace": {
                  "stringified": true
                }
              }
            }""";

    private static String getSystemProperty(final String key, final String defaultValue) {
        final String envValue = System.getenv(key);
        if (envValue != null)
            return envValue;

        return System.getProperty(key, defaultValue);
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[]{"*"};
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource configurationSource) {
        return this.getConfiguration(loggerContext, configurationSource.toString(), null);
    }

    @Override
    public Configuration getConfiguration(
            final LoggerContext loggerContext,
            final String name,
            final URI configLocation
    ) {
        final ConfigurationBuilder<BuiltConfiguration> configurationBuilder = new DefaultConfigurationBuilder<>()
                .addProperty("strict", "true")
                .setStatusLevel(Level.INFO)
                .setMonitorInterval("30")
                .setConfigurationName(name);

        final LayoutComponentBuilder layoutComponentBuilder = configurationBuilder.newLayout("JsonTemplateLayout")
                .addAttribute("eventTemplate", JSON_LAYOUT);

        final AppenderComponentBuilder consoleAppenderComponentBuilder = configurationBuilder.newAppender("Stdout", "CONSOLE")
                .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT)
                .add(layoutComponentBuilder);

        final AppenderComponentBuilder logstashAppenderComponentBuilder = configurationBuilder.newAppender("Logstash", "SOCKET")
                .addAttribute("host", getSystemProperty("LOGSTASH_HOST", "logstash"))
                .addAttribute("port", getSystemProperty("LOGSTASH_PORT", "9999"))
                .add(layoutComponentBuilder);

        final LoggerComponentBuilder projectLoggerComponentBuilder = configurationBuilder.newLogger("dev.jaczerob", Level.DEBUG)
                .addAttribute("additivity", false)
                .add(configurationBuilder.newAppenderRef("Stdout"))
                .add(configurationBuilder.newAppenderRef("Logstash"));

        final RootLoggerComponentBuilder rootLoggerComponentBuilder = configurationBuilder.newRootLogger(Level.ERROR)
                .add(configurationBuilder.newAppenderRef("Stdout"))
                .add(configurationBuilder.newAppenderRef("Logstash"));

        configurationBuilder
                .add(consoleAppenderComponentBuilder)
                .add(logstashAppenderComponentBuilder)
                .add(projectLoggerComponentBuilder)
                .add(rootLoggerComponentBuilder);

        return configurationBuilder.build();
    }
}
