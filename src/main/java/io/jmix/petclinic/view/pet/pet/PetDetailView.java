package io.jmix.petclinic.view.pet.pet;

import io.jmix.core.FileRef;
import io.jmix.core.FileStorage;
import io.jmix.flowui.component.upload.FileStorageUploadField;
import io.jmix.flowui.component.upload.receiver.FileTemporaryStorageBuffer;
import io.jmix.flowui.kit.component.upload.event.FileUploadSucceededEvent;
import io.jmix.flowui.upload.TemporaryStorage;
import io.jmix.petclinic.entity.pet.Pet;

import io.jmix.petclinic.services.VectorService;
import io.jmix.petclinic.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;


@Route(value = "pets/:id", layout = MainView.class)
@ViewController("petclinic_Pet.detail")
@ViewDescriptor("pet-detail-view.xml")
@EditedEntityContainer("petDc")
@DialogMode
public class PetDetailView extends StandardDetailView<Pet> {

    private static final Logger log = LoggerFactory.getLogger(PetDetailView.class);
    @ViewComponent
    private FileStorageUploadField avatarField;

    @Autowired
    private TemporaryStorage temporaryStorage;
    @Autowired
    private VectorService vectorService;
    @Autowired
    private FileStorage fileStorage;

    @Subscribe("avatarField")
    public void onAvatarFieldFileUploadSucceeded(final FileUploadSucceededEvent<FileStorageUploadField> event)  {
        if (event.getReceiver() instanceof FileTemporaryStorageBuffer buffer) {
            UUID fileId = buffer.getFileData().getFileInfo().getId();
            File file = temporaryStorage.getFile(fileId);

            if (file != null) {
                // get vector
                //File fileToGetVector = new File(file.getPath());
                FileRef fileRef = temporaryStorage.putFileIntoStorage(fileId, event.getFileName());
                avatarField.setValue(fileRef);


                //File fileToSend =  File.createTempFile("original-image", "."+FilenameUtils.getExtension(event.getFileName()));
                //fileToSend.deleteOnExit();
                //FileUtils.copyInputStreamToFile(fileStorage.openStream(fileRef), fileToSend);
                try {
                    List<Float> result = vectorService.getEmbeddingVector(fileRef);
                } catch (Exception ex) {
                    log.error(ex.toString());
                }

            }
        }
    }
}