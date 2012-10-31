import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

import static ch.qos.logback.classic.Level.*

appender("STDOUT", ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
	pattern = "%d [%thread] %-5level %logger{35} [%file:%line] - %msg %n"
  }
}
logger("com.lumata.lib.webscraper", DEBUG, ["STDOUT"])
root(ERROR, ["STDOUT"])
