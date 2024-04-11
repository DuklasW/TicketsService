package com.example.TicketsService.validate;

import com.example.TicketsService.dto.request.CreateEventRequest;
import com.example.TicketsService.model.ManagerEntity;
import com.example.TicketsService.repository.ArtistRepository;
import com.example.TicketsService.security.service.UserDetailsImpl;
import jakarta.validation.ValidationException;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import com.example.TicketsService.service.ManagerService;

@Component
public class EventValidator implements Validator {

    private final ManagerService managerService;
    private final ArtistRepository artistRepository;

    public EventValidator(ManagerService managerService, ArtistRepository artistRepository) {
        this.managerService = managerService;
        this.artistRepository = artistRepository;
    }

    @Override
    public void validate(Object object) throws ValidationException {
        if(!(object instanceof CreateEventRequest createEventRequest)) {
            throw new IllegalArgumentException("Invalid object type");
        }

        if (!createEventRequest.isValidRegon()) {
            throw new ValidationException("Error, region not in enum list");
        }

        if (createEventRequest.getDates().isEmpty()) {
            throw new ValidationException("Error, date is empty");
        }
    }

    public void validateEventCreation(CreateEventRequest request, UserDetailsImpl userDetails) throws ValidationException {
        validate(request);
        ManagerEntity manager = getManagerOrThrowException(userDetails.getId());
        validateArtistAndManager(request.getCreatedByArtist(), manager.getId());
    }

    private ManagerEntity getManagerOrThrowException(ObjectId userId) throws ValidationException {
        return managerService.getManagerByUserId(userId)
                .orElseThrow(ValidationException::new);
    }

    private void validateArtistAndManager(String artistId, ObjectId managerId) throws ValidationException {
        if (!existsByArtistIdAndManagerId(artistId, managerId)) {
            throw new ValidationException("Can't create event with this artist!");
        }
    }

    public boolean existsByArtistIdAndManagerId(String artistId, ObjectId managerId) {
        return artistRepository.existsByIdAndManagerId(new ObjectId(artistId), managerId);
    }
}
