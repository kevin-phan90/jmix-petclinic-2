package io.jmix.petclinic.services;


import io.jmix.petclinic.response.ImageVectorEmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.util.List;

@Service
public class VectorService {

    @Autowired
    private WebClient webClientVector;

    public List<Float> getEmbeddingVector(File image) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", image)
                .header(HttpHeaders.CONTENT_DISPOSITION, "form-data; name=\"file\"; filename=\"" + image.getName() + "\"");

        MultiValueMap<String, HttpEntity<?>> multipartBody = builder.build();



        ImageVectorEmbeddingResponse imageVectorEmbeddingResponse = webClientVector.post().uri("/extract-vector")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multipartBody))
                .retrieve().bodyToMono(ImageVectorEmbeddingResponse.class).block();

        return imageVectorEmbeddingResponse.getEmbedding();
    }
}