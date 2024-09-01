//package com.apink.poppin.api.user.service;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import com.apink.poppin.api.user.dto.UserDto;
//import com.apink.poppin.api.user.entity.User;
//import com.apink.poppin.api.user.repository.UserRepository;
//import com.apink.poppin.api.reservation.dto.PreReservationResponseDTO;
//import com.apink.poppin.api.reservation.entity.PreReservation;
//import com.apink.poppin.api.reservation.repository.PreReservationRepository;
//import com.apink.poppin.api.popup.entity.Popup;
//import com.apink.poppin.api.reservation.entity.ReservationStatement;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.Optional;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceImplTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private PreReservationRepository preReservationRepository;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    @Nested
//    @DisplayName("닉네임 가용성 검증 테스트")
//    class VerifyNicknameAvailableTest {
//
//        @Test
//        @DisplayName("닉네임 사용 가능 시 성공")
//        void 닉네임_사용_가능() {
//            // given
//            String 닉네임 = "사용가능닉네임";
//            when(userRepository.existsByNickname(닉네임)).thenReturn(false);
//
//            // when & then
//            assertDoesNotThrow(() -> userService.verifyNicknameAvailable(닉네임));
//        }
//
//        @Test
//        @DisplayName("닉네임 중복 시 실패")
//        void 닉네임_중복() {
//            // given
//            String 닉네임 = "중복닉네임";
//            when(userRepository.existsByNickname(닉네임)).thenReturn(true);
//
//            // when & then
//            assertThrows(RuntimeException.class, () -> userService.verifyNicknameAvailable(닉네임));
//        }
//    }
//
//    @Nested
//    @DisplayName("사용자 조회 테스트")
//    class FindUserTest {
//
//        @Test
//        @DisplayName("사용자 조회 성공")
//        void 사용자_조회_성공() {
//            // given
//            long userTsid = 1L;
//            User mockUser = User.builder()
//                    .userTsid(userTsid)
//                    .nickname("테스트유저")
//                    .email("test@example.com")
//                    .build();
//            when(userRepository.findUserByUserTsid(userTsid)).thenReturn(Optional.of(mockUser));
//
//            // when
//            UserDto.Response response = userService.findUser(userTsid);
//
//            // then
//            assertNotNull(response);
//            assertEquals(userTsid, response.getUserTsid());
//            assertEquals("테스트유저", response.getNickname());
//            assertEquals("test@example.com", response.getEmail());
//        }
//
//        @Test
//        @DisplayName("사용자 조회 실패 - 존재하지 않는 사용자")
//        void 사용자_조회_실패_존재하지_않음() {
//            // given
//            long userTsid = 999L;
//            when(userRepository.findUserByUserTsid(userTsid)).thenReturn(Optional.empty());
//
//            // when & then
//            assertThrows(RuntimeException.class, () -> userService.findUser(userTsid));
//        }
//    }
//
//    @Nested
//    @DisplayName("사용자 정보 업데이트 테스트")
//    class UpdateUserTest {
//
//        @Test
//        @DisplayName("사용자 정보 업데이트 성공")
//        void 사용자_정보_업데이트_성공() {
//            // given
//            long userTsid = 1L;
//            UserDto.Put updateDto = UserDto.Put.builder()
//                    .userTsid(userTsid)
//                    .nickname("새닉네임")
//                    .img("new_image.jpg")
//                    .build();
//
//            User existingUser = User.builder()
//                    .userTsid(userTsid)
//                    .build();
//
//            when(userRepository.findUserByUserTsid(userTsid)).thenReturn(Optional.of(existingUser));
//            when(userRepository.save(any(User.class))).thenReturn(existingUser);
//
//            // when
//            assertDoesNotThrow(() -> userService.updateUser(updateDto));
//
//            // then
//            verify(userRepository).save(argThat(savedUser ->
//                    savedUser.getUserTsid() == userTsid &&
//                            "새닉네임".equals(savedUser.getNickname()) &&
//                            "new_image.jpg".equals(savedUser.getImg())
//            ));
//        }
//
//        @Test
//        @DisplayName("사용자 정보 업데이트 실패 - 존재하지 않는 사용자")
//        void 사용자_정보_업데이트_실패_존재하지_않음() {
//            // given
//            long userTsid = 999L;
//            UserDto.Put updateDto = UserDto.Put.builder()
//                    .userTsid(userTsid)
//                    .nickname("새닉네임")
//                    .img("new_image.jpg")
//                    .build();
//
//            when(userRepository.findUserByUserTsid(userTsid)).thenReturn(Optional.empty());
//
//            // when & then
//            assertThrows(RuntimeException.class, () -> userService.updateUser(updateDto));
//        }
//    }
//
//    @Nested
//    @DisplayName("사용자 삭제 테스트")
//    class DeleteUserTest {
//
//        @Test
//        @DisplayName("사용자 삭제 성공")
//        void 사용자_삭제_성공() {
//            // given
//            long userTsid = 1L;
//            User existingUser = User.builder()
//                    .userTsid(userTsid)
//                    .providerId("some-provider-id")
//                    .providerName("some-provider")
//                    .name("Test User")
//                    .nickname("TestNick")
//                    .email("test@example.com")
//                    .age("30")
//                    .phoneNumber("010-1234-5678")
//                    .img("test.jpg")
//                    .state(true)
//                    .build();
//
//            when(userRepository.findUserByUserTsid(userTsid)).thenReturn(Optional.of(existingUser));
//
//            // when
//            userService.deleteUser(userTsid);
//
//            // then
//            verify(userRepository).findUserByUserTsid(userTsid);
//
//            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
//            verify(userRepository).save(userCaptor.capture());
//
//            User savedUser = userCaptor.getValue();
//            assertEquals(userTsid, savedUser.getUserTsid());
//            assertEquals(savedUser.getProviderId(), "-" + existingUser.getUserTsid());
//            assertEquals(savedUser.getProviderName(), "-" + existingUser.getUserTsid());
//            assertEquals(savedUser.getName(), "-" + existingUser.getUserTsid());
//            assertEquals(savedUser.getNickname(), "-" + existingUser.getUserTsid());
//            assertEquals(savedUser.getEmail(), "-" + existingUser.getUserTsid());
//            assertEquals(savedUser.getAge(), "-" + existingUser.getUserTsid());
//            assertEquals(savedUser.getPhoneNumber(), "-" + existingUser.getUserTsid());
//            assertEquals(savedUser.getImg(), "default.img");
//            assertFalse(savedUser.isState());
//        }
//
//        @Test
//        @DisplayName("사용자 삭제 실패, 유저 미존재")
//        void 사용자_삭제_성공_유저_미존재() {
//            // Given
//            long nonExistentUserTsid = 999L;
//            when(userRepository.findUserByUserTsid(nonExistentUserTsid))
//                    .thenReturn(Optional.empty());
//
//            // When & Then
//            assertThrows(RuntimeException.class, () -> userService.deleteUser(nonExistentUserTsid));
//
//            // Verify
//            verify(userRepository, times(1)).findUserByUserTsid(nonExistentUserTsid);
//            verify(userRepository, never()).save(any(User.class));
//        }
//
//        @Test
//        @DisplayName("사용자 삭제 실패, 중간 실패")
//        void 사용자_삭제_중간에_실패() {
//            // Given
//            long existingUserTsid = 1L;
//            User existingUser = User.builder()
//                    .userTsid(existingUserTsid)
//                    .providerId("provider123")
//                    .providerName("providerName")
//                    .name("John Doe")
//                    .nickname("johndoe")
//                    .email("john@example.com")
//                    .age("30")
//                    .gender("Male")
//                    .phoneNumber("1234567890")
//                    .role("USER")
//                    .img("profile.jpg")
//                    .state(true)
//                    .build();
//
//            when(userRepository.findUserByUserTsid(existingUserTsid))
//                    .thenReturn(Optional.of(existingUser));
//            when(userRepository.save(any(User.class)))
//                    .thenThrow(new RuntimeException("Failed to save user"));
//
//            // When & Then
//            assertThrows(RuntimeException.class, () -> userService.deleteUser(existingUserTsid));
//
//            // Verify
//            verify(userRepository, times(1)).findUserByUserTsid(existingUserTsid);
//            verify(userRepository, times(1)).save(any(User.class));
//        }
//    }
//
//    @Nested
//    @DisplayName("사전 예약 조회 테스트")
//    class FindPreReservationTest {
//
//        @Test
//        @DisplayName("사전 예약 조회 성공")
//        void 사전_예약_조회_성공() {
//            // given
//            long preReservationId = 1L;
//            PreReservation mockPreReservation = PreReservation.builder()
//                    .preReservationId(preReservationId)
//                    .user(User.builder().userTsid(1L).build())
//                    .popup(Popup.builder().popupId(1L).build())
//                    .reservationDate(LocalDate.now())
//                    .reservationTime(LocalTime.parse("14:00"))
//                    .reservationCount(2)
//                    .createdAt(LocalDate.from(LocalDateTime.now()))
//                    .reservationStatement(ReservationStatement.builder().reservationStatementId(1L).build())
//                    .build();
//
//            when(preReservationRepository.findById(preReservationId)).thenReturn(Optional.of(mockPreReservation));
//
//            // when
//            PreReservationResponseDTO response = userService.findPreReservation(preReservationId);
//
//            // then
//            assertNotNull(response);
//            assertEquals(preReservationId, response.getPreReservationId());
//        }
//
//        @Test
//        @DisplayName("사전 예약 조회 실패 - 존재하지 않는 예약")
//        void 사전_예약_조회_실패_존재하지_않음() {
//            // given
//            long preReservationId = 999L;
//            when(preReservationRepository.findById(preReservationId)).thenReturn(Optional.empty());
//
//            // when & then
//            assertThrows(RuntimeException.class, () -> userService.findPreReservation(preReservationId));
//        }
//    }
//}