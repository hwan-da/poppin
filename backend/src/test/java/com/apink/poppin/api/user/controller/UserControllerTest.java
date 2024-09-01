//package com.apink.poppin.api.user.controller;
//
//import com.apink.poppin.api.user.dto.UserDto;
//import com.apink.poppin.api.user.service.UserService;
//import com.apink.poppin.api.popup.dto.PopupDTO;
//import com.apink.poppin.api.review.dto.ReviewDto;
//import com.apink.poppin.api.reservation.dto.PreReservationResponseDTO;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(MockitoExtension.class)
//class UserControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private UserService userService;
//
//    @Mock
//    private Authentication authentication;
//
//    @InjectMocks
//    private UserController userController;
//
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//        objectMapper = new ObjectMapper();
//
//        // SecurityContext 모의 설정
//        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }
//
//    @Test
//    void checkNickname_whenNicknameAvailable_shouldReturnNotFound() throws Exception {
//        String nickname = "testUser";
//        doNothing().when(userService).verifyNicknameAvailable(nickname);
//
//        mockMvc.perform(get("/api/users/{nickname}/check", nickname))
//                .andExpect(status().isNotFound());
//
//        verify(userService).verifyNicknameAvailable(nickname);
//    }
//
//    @Test
//    void findUser_shouldReturnUserInfo() throws Exception {
//        UserDto.Response userResponse = UserDto.Response.builder()
//                .userTsid(1L)
//                .nickname("testUser")
//                .email("test@example.com")
//                .build();
//
//        when(userService.findUser(1L)).thenReturn(userResponse);
//
//        mockMvc.perform(get("/api/users/me"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userTsid").value(1L))
//                .andExpect(jsonPath("$.nickname").value("testUser"))
//                .andExpect(jsonPath("$.email").value("test@example.com"));
//
//        verify(userService).findUser(1L);
//    }
//
//    @Test
//    void updateUser_shouldUpdateUserInfo() throws Exception {
//        UserDto.Put userDto = UserDto.Put.builder()
//                .userTsid(1L)
//                .nickname("updatedUser")
//                .img("newImage.jpg")
//                .build();
//
//        doNothing().when(userService).updateUser(any(UserDto.Put.class));
//
//        mockMvc.perform(put("/api/users/me")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userDto)))
//                .andExpect(status().isOk());
//
//        verify(userService).updateUser(any(UserDto.Put.class));
//    }
//
//    @Test
//    void deleteUser_shouldDeleteUser() throws Exception {
//        doNothing().when(userService).deleteUser(1L);
//
//        mockMvc.perform(delete("/api/users/me"))
//                .andExpect(status().isOk());
//
//        verify(userService).deleteUser(1L);
//    }
//
//    @Test
//    void findHeartPopup_shouldReturnHeartedPopups() throws Exception {
//        List<PopupDTO> heartedPopups = Arrays.asList(
//                new PopupDTO(1L, "Popup1", null, null, 10),
//                new PopupDTO(2L, "Popup2", null, null, 20)
//        );
//
//        when(userService.findHeartPopup()).thenReturn(heartedPopups);
//
//        mockMvc.perform(get("/api/users/me/popups/heart"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].popupId").value(1L))
//                .andExpect(jsonPath("$[0].name").value("Popup1"))
//                .andExpect(jsonPath("$[1].popupId").value(2L))
//                .andExpect(jsonPath("$[1].name").value("Popup2"));
//
//        verify(userService).findHeartPopup();
//    }
//
//    @Test
//    void findReview_shouldReturnUserReviews() throws Exception {
//        List<ReviewDto> userReviews = Arrays.asList(
//                new ReviewDto(1L, 1L, 1L, 5, "Great!", "Loved it", "thumb.jpg", null, null),
//                new ReviewDto(2L, 2L, 1L, 4, "Good", "Nice experience", "thumb2.jpg", null, null)
//        );
//
//        when(userService.findReviews()).thenReturn(userReviews);
//
//        mockMvc.perform(get("/api/users/me/popups/reviews"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].reviewId").value(1L))
//                .andExpect(jsonPath("$[0].title").value("Great!"))
//                .andExpect(jsonPath("$[1].reviewId").value(2L))
//                .andExpect(jsonPath("$[1].title").value("Good"));
//
//        verify(userService).findReviews();
//    }
//
//    @Test
//    void findPreReservations_shouldReturnUserPreReservations() throws Exception {
//        List<PreReservationResponseDTO> preReservations = Arrays.asList(
//                PreReservationResponseDTO.builder().preReservationId(1L).popupId(1L).build(),
//                PreReservationResponseDTO.builder().preReservationId(2L).popupId(2L).build()
//        );
//
//        when(userService.findPreReservations()).thenReturn(preReservations);
//
//        mockMvc.perform(get("/api/users/me/popups/pre-reservations"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].preReservationId").value(1L))
//                .andExpect(jsonPath("$[0].popupId").value(1L))
//                .andExpect(jsonPath("$[1].preReservationId").value(2L))
//                .andExpect(jsonPath("$[1].popupId").value(2L));
//
//        verify(userService).findPreReservations();
//    }
//
//    @Test
//    void findPreReservation_shouldReturnSpecificPreReservation() throws Exception {
//        PreReservationResponseDTO preReservation = PreReservationResponseDTO.builder()
//                .preReservationId(1L)
//                .popupId(1L)
//                .reservationDate(LocalDate.parse("2024-05-01"))
//                .build();
//
//        when(userService.findPreReservation(1L)).thenReturn(preReservation);
//
//        mockMvc.perform(get("/api/users/me/popups/pre-reservations/{prereservation_id}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.preReservationId").value(1L))
//                .andExpect(jsonPath("$.popupId").value(1L))
//                .andExpect(jsonPath("$.reservationDate").value("2024-05-01"));
//
//        verify(userService).findPreReservation(1L);
//    }
//
//    @Test
//    void findCancelledReservation_shouldReturnCancelledReservations() throws Exception {
//        List<PreReservationResponseDTO> cancelledReservations = Arrays.asList(
//                PreReservationResponseDTO.builder().preReservationId(1L).popupId(1L).reservationStatementId(4L).build(),
//                PreReservationResponseDTO.builder().preReservationId(2L).popupId(2L).reservationStatementId(4L).build()
//        );
//
//        when(userService.findCancelledPreReservations()).thenReturn(cancelledReservations);
//
//        mockMvc.perform(get("/api/users/me/popups/cancelled-reservation"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].preReservationId").value(1L))
//                .andExpect(jsonPath("$[0].popupId").value(1L))
//                .andExpect(jsonPath("$[0].reservationStatementId").value(4L))
//                .andExpect(jsonPath("$[1].preReservationId").value(2L))
//                .andExpect(jsonPath("$[1].popupId").value(2L))
//                .andExpect(jsonPath("$[1].reservationStatementId").value(4L));
//
//        verify(userService).findCancelledPreReservations();
//    }
//}