package io.jmix.petclinic.view.pet.pet;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.streams.DownloadHandler;
import io.jmix.core.FileRef;
import io.jmix.core.FileStorage;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.propertyfilter.PropertyFilter;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.petclinic.entity.owner.Owner;
import io.jmix.petclinic.entity.pet.Pet;

import io.jmix.petclinic.entity.pet.PetType;
import io.jmix.petclinic.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.OutputStream;

@Route(value = "pets", layout = MainView.class)
@ViewController("petclinic_Pet.list")
@ViewDescriptor("pet-list-view.xml")
@LookupComponent("petsDataGrid")
@DialogMode(width = "50em")
public class PetListView extends StandardListView<Pet> {

    @ViewComponent
    private PropertyFilter<String> identificationNumberFilter;
    @ViewComponent
    private PropertyFilter<PetType> typeFilter;
    @ViewComponent
    private PropertyFilter<Owner> ownerFilter;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private FileStorage fileStorage;


    @Subscribe("clearFilterAction")
    public void onClearFilterAction(final ActionPerformedEvent event) {
        identificationNumberFilter.clear();
        typeFilter.clear();
        ownerFilter.clear();
    }

    @Supply(to = "petsDataGrid.picture", subject = "renderer")
    private Renderer<Pet> petsDataGridPictureRenderer() {
        return new ComponentRenderer<>(pet -> {
            FileRef fileRef = pet.getAvatar();
            if (fileRef != null) {
                Image image = uiComponents.create(Image.class);
                image.setWidth("30px");
                image.setHeight("30px");
                DownloadHandler handler = event -> {
                    try (OutputStream out = event.getOutputStream()) {
                        out.write(fileStorage.openStream(fileRef).readAllBytes());
                    }
                };
                image.setSrc(handler);
                image.setClassName("user-picture");

                return image;
            } else {
                return null;
            }
        }); 
    }

}
