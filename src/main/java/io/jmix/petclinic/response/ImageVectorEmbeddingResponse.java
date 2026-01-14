package io.jmix.petclinic.response;

import java.util.List;

public class ImageVectorEmbeddingResponse {

    private List<Float> embedding;

    public List<Float> getEmbedding() {
        return embedding;
    }

    public void setEmbedding(List<Float> embedding) {
        this.embedding = embedding;
    }
}