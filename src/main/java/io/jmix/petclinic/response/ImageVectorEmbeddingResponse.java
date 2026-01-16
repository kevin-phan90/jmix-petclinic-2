package io.jmix.petclinic.response;

import java.util.List;

public class ImageVectorEmbeddingResponse {

    public List<Float> getImage_embedding() {
        return image_embedding;
    }

    public void setImage_embedding(List<Float> image_embedding) {
        this.image_embedding = image_embedding;
    }

    private List<Float> image_embedding;


}