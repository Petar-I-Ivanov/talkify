package bg.uniplovdiv.talkify.common.mail.model;

import static java.util.Locale.ENGLISH;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

// Messages are not localized, because they should be hint to the dev
@FieldDefaults(level = PRIVATE)
public class MailPropsBuilder {

  MailProps props;

  private MailPropsBuilder() {
    this.props = new MailProps();
  }

  public static MailPropsBuilder mailPropsBuilder() {
    return new MailPropsBuilder();
  }

  public MailPropsBuilder sendTo(String... emails) {
    requireNonNull(emails, "Mail 'sendTo' emails shouldn't be null!");
    props.sendTo.addAll(List.of(emails));
    return this;
  }

  public MailPropsBuilder subject(MailSubject subject) {
    requireNonNull(subject, "Mail 'subject' shouldn't be null!");
    props.subject = subject;
    return this;
  }

  public MailPropsBuilder template(MailTemplate template) {
    requireNonNull(template, "Mail 'template' shouldn't be null!");
    props.template = template;
    return this;
  }

  public MailPropsBuilder emailBody(String emailBody) {
    requireNonNull(emailBody, "Mail 'emailBody' shouldn't be null!");
    props.emailBody = emailBody;
    return this;
  }

  public MailPropsBuilder bcc(boolean bcc) {
    props.bcc = bcc;
    return this;
  }

  public MailPropsBuilder locale(Locale locale) {
    requireNonNull(locale, "Mail 'locale' shouldn't be null!");
    props.locale = locale;
    return this;
  }

  public MailPropsBuilder addVariable(MailVariable variable, Object value) {
    requireNonNull(variable, "Mail 'variable' key shouldn't be null!");
    requireNonNull(value, "Mail 'variable' value shouldn't be null!");
    props.variables.put(variable, value);
    return this;
  }

  public MailProps build() {
    validateProps(props);
    return props;
  }

  private static void validateProps(MailProps props) {
    if (props.sendTo.isEmpty() || props.sendTo.contains(null)) {
      throw new IllegalArgumentException(
          "Mail props 'sendTo' shouldn't be empty or contain 'null' as value!");
    }

    if (isNull(props.subject)) {
      throw new IllegalArgumentException("Mail props 'subject' shouldn't be null!");
    }

    if (isNull(props.template) && isBlank(props.emailBody)) {
      throw new IllegalArgumentException(
          "Mail props 'template' or 'emailBody' should have valid value!");
    }

    if (isNull(props.locale)) {
      throw new IllegalArgumentException("Mail props 'locale' shouldn't be null!");
    }
  }

  @Getter
  public static class MailProps {

    boolean bcc;
    Locale locale = ENGLISH;
    MailSubject subject;
    MailTemplate template;
    String emailBody;
    Set<String> sendTo = new HashSet<>();
    Map<MailVariable, Object> variables = new HashMap<>();

    public String getTemplate() {
      return String.format(template.getTemplate(), locale.getLanguage());
    }

    public Map<String, Object> getVariables() {
      return variables.entrySet().stream()
          .collect(toMap(entry -> entry.getKey().getVariable(), Entry::getValue));
    }
  }
}
