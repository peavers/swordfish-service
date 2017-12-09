package swordfish.space.notificationservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.github.jasminb.jsonapi.DeserializationFeature;
import com.github.jasminb.jsonapi.JSONAPIDocument;
import com.github.jasminb.jsonapi.ResourceConverter;
import com.github.jasminb.jsonapi.exceptions.DocumentSerializationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swordfish.space.notificationservice.domain.Notification;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class JsonTransformServiceImpl implements JsonTransformService {

    private ResourceConverter converter;

    JsonTransformServiceImpl() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);

        this.converter = new ResourceConverter(objectMapper,
                Notification.class
        );

        converter.disableDeserializationOption(DeserializationFeature.REQUIRE_RESOURCE_ID);
    }

    @Override
    public <T> T read(Class<T> cls, String payload) {
        return converter.readDocument(payload.getBytes(), cls).get();
    }

    @Override
    public <T> JSONAPIDocument<List<T>> readList(Class<T> cls, String payload) {
        return converter.readDocumentCollection(payload.getBytes(), cls);
    }

    @Override
    public String write(Object object) {
        JSONAPIDocument<Object> document = new JSONAPIDocument<>(object);

        byte[] bytes = new byte[0];
        try {
            bytes = converter.writeDocument(document);
        } catch (DocumentSerializationException e) {
            e.printStackTrace();
        }

        return new String(bytes);
    }

    @Override
    public String writeList(Iterable<?> iterable) {
        JSONAPIDocument<Collection<?>> listJSONAPIDocument = new JSONAPIDocument<>((Collection<?>) iterable);

        byte[] bytes = new byte[0];
        try {
            bytes = converter.writeDocumentCollection(listJSONAPIDocument);
        } catch (DocumentSerializationException e) {
            e.printStackTrace();
        }

        return new String(bytes);
    }
}