package bg.uniplovdiv.talkify.common.encodedid;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import bg.uniplovdiv.talkify.common.encodedid.serializers.EncodedIdDeserializer;
import bg.uniplovdiv.talkify.common.encodedid.serializers.EncodedIdSerializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@JacksonAnnotationsInside
@JsonSerialize(contentUsing = EncodedIdSerializer.class)
@JsonDeserialize(contentUsing = EncodedIdDeserializer.class)
@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
@Documented
public @interface EncodedIds {}
