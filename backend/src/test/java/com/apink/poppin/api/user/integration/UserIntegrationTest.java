//package com.apink.poppin.api.user.integration;
//
//import com.apink.poppin.api.popup.dto.PopupDTO;
//import com.apink.poppin.api.reservation.dto.PreReservationResponseDTO;
//import com.apink.poppin.api.review.dto.ReviewDto;
//import com.apink.poppin.api.user.dto.UserDto;
//import com.apink.poppin.api.user.service.UserService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.*;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class UserIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private UserDto.Response userResponse;
//    private List<PopupDTO> heartPopups;
//    private List<ReviewDto> reviews;
//    private List<PreReservationResponseDTO> preReservations;
//
//    @BeforeEach
//    void setUp() {
//        userResponse = UserDto.Response.builder()
//                .userTsid(1L)
//                .nickname("testUser")
//                .email("test@example.com")
//                .phoneNumber("1234567890")
//                .build();
//
//        heartPopups = Arrays.asList(new PopupDTO(1L, "Popup 1", LocalDate.now(), LocalDate.now().plusDays(7), 1)
//                , new PopupDTO(2L, "Popup 2", LocalDate.now(), LocalDate.now().plusDays(14), 2));
//
//        reviews = Arrays.asList(
//                ReviewDto.builder()
//                        .reviewId(1L)
//                        .popupId(1L)
//                        .userTsid(1L)
//                        .rating(4.5F)
//                        .title("Great popup!")
//                        .content("Really enjoyed my visit.")
//                        .createdAt(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
//                        .build(),
//                ReviewDto.builder()
//                        .reviewId(2L)
//                        .popupId(2L)
//                        .userTsid(1L)
//                        .rating(4.0F)
//                        .title("Nice experience")
//                        .content("Good atmosphere and products.")
//                        .createdAt(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
//                        .build()
//        );
//
//        preReservations = Arrays.asList(
//                PreReservationResponseDTO.builder()
//                        .preReservationId(1L)
//                        .userTsid(1L)
//                        .popupId(1L)
//                        .reservationDate(LocalDate.now().plusDays(1))
//                        .reservationTime(LocalTime.now().plusNanos(1))
//                        .reservationCount(2)
//                        .createdAt(LocalDate.now())
//                        .reservationStatementId(1L)
//                        .build(),
//                PreReservationResponseDTO.builder()
//                        .preReservationId(2L)
//                        .userTsid(1L)
//                        .popupId(2L)
//                        .reservationDate(LocalDate.now().plusDays(2))
//                        .reservationTime(LocalTime.now().plusNanos(2))
//                        .reservationCount(3)
//                        .createdAt(LocalDate.now())
//                        .reservationStatementId(1L)
//                        .build()
//        );
//    }
//
//    @Test
//    @WithMockUser(username = "1")
//    void testFindUser() throws Exception {
//        when(userService.findUser(anyLong())).thenReturn(userResponse);
//
//        mockMvc.perform(get("/api/users/me"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userTsid").value(1L))
//                .andExpect(jsonPath("$.nickname").value("testUser"))
//                .andExpect(jsonPath("$.email").value("test@example.com"))
//                .andExpect(jsonPath("$.phoneNumber").value("1234567890"));
//
//        verify(userService, times(1)).findUser(1L);
//    }
//
//    @Test
//    @WithMockUser(username = "1")
//    void testUpdateUser() throws Exception {
//        UserDto.Put updateUserDto = UserDto.Put.builder()
//                .userTsid(1L)
//                .nickname("updatedNickname")
//                .img("new_image.jpg")
//                .build();
//
//        doNothing().when(userService).updateUser(any(UserDto.Put.class));
//
//        mockMvc.perform(put("/api/users/me")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateUserDto)))
//                .andExpect(status().isOk());
//
//        verify(userService, times(1)).updateUser(any(UserDto.Put.class));
//    }
//
//    @Test
//    @WithMockUser(username = "1")
//    void testDeleteUser() throws Exception {
//        doNothing().when(userService).deleteUser(anyLong());
//
//        mockMvc.perform(delete("/api/users/me"))
//                .andExpect(status().isOk());
//
//        verify(userService, times(1)).deleteUser(1L);
//    }
//
//    @Test
//    @WithMockUser(username = "1")
//    void testFindHeartPopup() throws Exception {
//        when(userService.findHeartPopup()).thenReturn(heartPopups);
//
//        mockMvc.perform(get("/api/users/me/popups/heart"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].popupId").value(1L))
//                .andExpect(jsonPath("$[0].name").value("Popup 1"))
//                .andExpect(jsonPath("$[1].popupId").value(2L))
//                .andExpect(jsonPath("$[1].name").value("Popup 2"));
//
//        verify(userService, times(1)).findHeartPopup();
//    }
//
//    @Test
//    @WithMockUser(username = "1")
//    void testFindReviews() throws Exception {
//        when(userService.findReviews()).thenReturn(reviews);
//
//        mockMvc.perform(get("/api/users/me/popups/reviews"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].reviewId").value(1L))
//                .andExpect(jsonPath("$[0].popupId").value(1L))
//                .andExpect(jsonPath("$[1].reviewId").value(2L))
//                .andExpect(jsonPath("$[1].popupId").value(2L));
//
//        verify(userService, times(1)).findReviews();
//    }
//
//    @Test
//    @WithMockUser(username = "1")
//    void testFindPreReservations() throws Exception {
//        when(userService.findPreReservations()).thenReturn(preReservations);
//
//        mockMvc.perform(get("/api/users/me/popups/pre-reservations"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].preReservationId").value(1L))
//                .andExpect(jsonPath("$[0].popupId").value(1L))
//                .andExpect(jsonPath("$[1].preReservationId").value(2L))
//                .andExpect(jsonPath("$[1].popupId").value(2L));
//
//        verify(userService, times(1)).findPreReservations();
//    }
//
//    @Test
//    @WithMockUser(username = "1")
//    void testFindPreReservation() throws Exception {
//        when(userService.findPreReservation(anyLong())).thenReturn(preReservations.get(0));
//
//        mockMvc.perform(get("/api/users/me/popups/pre-reservations/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.preReservationId").value(1L))
//                .andExpect(jsonPath("$.popupId").value(1L))
//                .andExpect(jsonPath("$.reservationCount").value(2));
//
//        verify(userService, times(1)).findPreReservation(1L);
//    }
//
//    @Test
//    @WithMockUser(username = "1")
//    void testFindCancelledReservation() throws Exception {
//        when(userService.findCancelledPreReservations()).thenReturn(preReservations);
//
//        mockMvc.perform(get("/api/users/me/popups/cancelled-reservation"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].preReservationId").value(1L))
//                .andExpect(jsonPath("$[0].popupId").value(1L))
//                .andExpect(jsonPath("$[1].preReservationId").value(2L))
//                .andExpect(jsonPath("$[1].popupId").value(2L));
//
//        verify(userService, times(1)).findCancelledPreReservations();
//    }
//}