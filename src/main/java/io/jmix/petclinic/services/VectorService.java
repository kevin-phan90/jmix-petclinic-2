package io.jmix.petclinic.services;


import io.jmix.core.FileRef;
import io.jmix.core.FileStorage;
import io.jmix.petclinic.response.ImageVectorEmbeddingResponse;
import io.jmix.petclinic.utils.SimpleMultipartFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class VectorService {

    @Autowired
    private WebClient webClientVector;

    @Autowired
    private FileStorage fileStorage;

    public List<Float> getEmbeddingVector(FileRef image) throws Exception {
        final String formFieldName = "file";
        InputStreamResource resource = new InputStreamResource(fileStorage.openStream(image));
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setContentDisposition(ContentDisposition.formData().filename(image.getFileName()).name(formFieldName).build());
        HttpEntity<InputStreamResource> entity = new HttpEntity<>(resource, headers);
        form.add(formFieldName, entity);



        ImageVectorEmbeddingResponse imageVectorEmbeddingResponse = webClientVector.post().uri("/extract-vector")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(form))
                .retrieve().bodyToMono(ImageVectorEmbeddingResponse.class).block();

        return imageVectorEmbeddingResponse.getImage_embedding();
    }
}