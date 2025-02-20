package bg.uniplovdiv.talkify.websocket.support;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.springframework.hateoas.mediatype.MessageResolver.DEFAULTS_ONLY;
import static org.springframework.hateoas.mediatype.hal.CurieProvider.NONE;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.hateoas.server.core.DefaultLinkRelationProvider;
import org.springframework.hateoas.server.mvc.TypeConstrainedMappingJackson2HttpMessageConverter;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;

public class CustomMessageConverter extends AbstractMessageConverter {

  @Override
  protected boolean supports(Class<?> clazz) {
    return RepresentationModel.class.isAssignableFrom(clazz);
  }

  @Override
  protected Object convertToInternal(
      Object payload, MessageHeaders headers, Object conversionHint) {

    var mapper =
        new TypeConstrainedMappingJackson2HttpMessageConverter(RepresentationModel.class)
            .getObjectMapper();
    mapper.disable(WRITE_DATES_AS_TIMESTAMPS);
    mapper.registerModule(new Jackson2HalModule());
    mapper.setHandlerInstantiator(
        new Jackson2HalModule.HalHandlerInstantiator(
            new DefaultLinkRelationProvider(), NONE, DEFAULTS_ONLY));

    try {
      return mapper.writeValueAsString(payload).getBytes();
    } catch (JsonProcessingException exception) {
      return null;
    }
  }
}
