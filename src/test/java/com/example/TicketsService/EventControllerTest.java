package com.example.TicketsService;

import com.example.TicketsService.Mapper.EventMapper;
import com.example.TicketsService.dto.request.CreateEventRequest;
import com.example.TicketsService.dto.response.EventResponse;
import com.example.TicketsService.dto.response.MessageResponse;
import com.example.TicketsService.model.EventEntity;
import com.example.TicketsService.repository.EventRepository;
import com.example.TicketsService.security.service.UserDetailsImpl;
import com.example.TicketsService.service.EventService;
import com.example.TicketsService.validate.EventValidator;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventValidator eventValidator;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private EventRepository eventRepository;

    @Test
    void createEvents_shouldCreateEventsAndReturnCreatedResponse(){
        CreateEventRequest request = createEventRequest();
        UserDetailsImpl userDetails = createUserDetails();
        mockSecurityContext(userDetails);

        List<EventEntity> events = createEventsEntites(request, userDetails);
        Mockito.when(eventMapper.mapToEvents(request, userDetails)).thenReturn(events);


        ResponseEntity<?> response = eventService.createEvents(request);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Created event successfully", ((MessageResponse) Objects.requireNonNull(response.getBody())).getMessage());

        Mockito.verify(eventValidator, Mockito.times(1)).validate(request);
        Mockito.verify(eventMapper, Mockito.times(1)).mapToEvents(request, userDetails);
        Mockito.verify(eventRepository, Mockito.times(1)).saveAll(events);
    }

//    @Test
//    void createEvents_shouldReturnBadRequestResponse_whenValidationFails() {
//
//    }

    @Test
    void getEventById_shouldReturnEvent_whenEventExists() {
        CreateEventRequest request = createEventRequest();
        UserDetailsImpl userDetails = createUserDetails();
        mockSecurityContext(userDetails);

        String eventId = "123456789123456789123456";
        EventEntity event = createEventEntity(request, userDetails);
        event.setId(new ObjectId(eventId));;

        EventResponse expectedResponse = createEventResponse(event);

        Mockito.when(eventRepository.findById(new ObjectId(eventId))).thenReturn(Optional.of(event));
        Mockito.when(eventMapper.toResponse(event)).thenReturn(expectedResponse);

        EventResponse result = eventService.getEventById(eventId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(event.getId(), result.getId());

        Mockito.verify(eventRepository, Mockito.times(1)).findById(new ObjectId(eventId));
        Mockito.verify(eventMapper, Mockito.times(1)).toResponse(event);
    }

    private EventResponse createEventResponse(EventEntity event) {
        EventResponse response = new EventResponse();
        response.setId(event.getId().toHexString());
        response.setDate(event.getDate());
        response.setArtistName(event.getArtistName());
        response.setPrice(event.getPrice());
        response.setTicketsNumber(event.getTicketsNumber());
        response.setTicketsBought(event.getTicketsBought());
        response.setLocation(event.getLocation());
        response.setCity(event.getCity());
        response.setPostcode(event.getPostcode());
        response.setRegon(event.getRegon());
        response.setStreet(event.getStreet());
        response.setCreatedBy(event.getCreatedBy());
        response.setName(event.getName());
        response.setDescription(event.getDescription());
        return response;
    }


    private EventEntity createEventEntity(CreateEventRequest request, UserDetailsImpl userDetails){
        EventEntity event = new EventEntity();
        event.setDate(request.getDates().get(0));
        event.setArtistName(request.getArtistName());
        event.setPrice(request.getPrice());
        event.setTicketsNumber(request.getTicketsNumber());
        event.setTicketsBought(0);
        event.setLocation(request.getLocation());
        event.setCity(request.getCity());
        event.setPostcode(request.getPostcode());
        event.setRegon(request.getRegon());
        event.setStreet(request.getStreet());
        event.setCreatedBy(userDetails.getId().toHexString());
        event.setName(request.getName());
        event.setDescription(request.getDescription());
        return event;
    }

    private List<EventEntity> createEventsEntites(CreateEventRequest request, UserDetailsImpl userDetails){
        List<EventEntity> events = new ArrayList<>();
        EventEntity event = createEventEntity(request, userDetails);
        events.add(event);
        return events;
    }

    private void mockSecurityContext(UserDetailsImpl userDetails) {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);
    }

    private CreateEventRequest createEventRequest() {
        CreateEventRequest request = new CreateEventRequest();
        LocalDateTime dateTime = LocalDateTime.of(2024, 3, 1, 18, 30, 0);
        Date date = Date.from(dateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());
        List<Date> dateList = Collections.singletonList(date);
        List<String> stringNameList = Arrays.asList("Krzysztof", "Jan");

        request.setArtistName(stringNameList);
        request.setDates(dateList);
        request.setPrice(10.0);
        request.setTicketsNumber(100);
        request.setLocation("Bar pod mikrofonem");
        request.setCity("Toruń");
        request.setPostcode("87-100");
        request.setRegon("kujawsko-pomorskie");
        request.setStreet("Starówkowa");
        request.setDescription("Wielki miks 3 wyjątkowych standuperów, którzy pokażą wspólny program");

        return request;
    }

    private UserDetailsImpl createUserDetails() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
        return new UserDetailsImpl(new ObjectId("111111111111111111111111"), "john.doe@example.com", "password", authorities);
    }
}
