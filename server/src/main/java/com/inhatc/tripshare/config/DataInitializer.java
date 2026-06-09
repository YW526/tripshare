package com.inhatc.tripshare.config;

import com.inhatc.tripshare.entity.*;
import com.inhatc.tripshare.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final ReviewRepository reviewRepository;
    private final SavedTripRepository savedTripRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           TripRepository tripRepository,
                           ReviewRepository reviewRepository,
                           SavedTripRepository savedTripRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.reviewRepository = reviewRepository;
        this.savedTripRepository = savedTripRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        User yuni = createUser("yuni@example.com", "1234", "traveler_유니",
                "여행을 사랑하는 유니입니다 ✈️");
        User minsu = createUser("foodie@example.com", "1234", "foodie_민수",
                "맛집 여행을 좋아합니다");
        User jieun = createUser("culture@example.com", "1234", "culture_지은",
                "문화 탐방을 즐깁니다");
        User haneul = createUser("ocean@example.com", "1234", "ocean_하늘",
                "바다 여행러");

        Trip jeju = createTrip(yuni, "제주도 2박 3일 힐링 여행", "제주도",
                "2024-05-01", "2024-05-03",
                "제주의 아름다운 자연을 만끽하는 힐링 여행 일정입니다.", 245);
        addSchedule(jeju, 1, "09:00", "제주공항 도착", "렌터카 픽업 후 여행 시작", 33.5113, 126.4930, 0);
        addSchedule(jeju, 1, "12:00", "협재해수욕장", "에메랄드빛 바다 감상", 33.3939, 126.2396, 1);
        addSchedule(jeju, 2, "09:00", "성산일출봉", "일출봉 등반 및 전망 감상", 33.4581, 126.9425, 2);
        addSchedule(jeju, 2, "14:00", "섭지코지", "드라마 촬영지 산책", 33.4244, 126.9278, 3);
        addSchedule(jeju, 3, "10:00", "한라산 어리목", "가벼운 등산 코스", 33.3617, 126.5292, 4);
        tripRepository.save(jeju);

        Trip busan = createTrip(minsu, "부산 맛집 투어 1박 2일", "부산",
                "2024-06-10", "2024-06-11",
                "부산의 유명 맛집을 돌아보는 미식 여행입니다.", 189);
        addSchedule(busan, 1, "11:00", "자갈치시장", "해산물 점심", 35.0967, 129.0307, 0);
        addSchedule(busan, 1, "15:00", "해운대 해수욕장", "해변 산책", 35.1587, 129.1604, 1);
        tripRepository.save(busan);

        Trip seoul = createTrip(jieun, "서울 문화 탐방 1일 코스", "서울",
                "2024-04-20", "2024-04-20",
                "서울의 대표 문화 명소를 하루에 둘러보는 코스입니다.", 156);
        addSchedule(seoul, 1, "10:00", "경복궁", "한복 체험", 37.5796, 126.9770, 0);
        addSchedule(seoul, 1, "14:00", "북촌한옥마을", "전통 마을 산책", 37.5826, 126.9831, 1);
        tripRepository.save(seoul);

        Trip gangneung = createTrip(haneul, "강릉 바다 여행 2박 3일", "강릉",
                "2024-07-01", "2024-07-03",
                "동해의 푸른 바다와 커피 거리를 즐기는 여행입니다.", 312);
        addSchedule(gangneung, 1, "09:00", "경포대", "해변 산책", 37.7955, 128.9008, 0);
        addSchedule(gangneung, 2, "11:00", "안목해변 커피거리", "카페 투어", 37.7703, 128.9463, 1);
        tripRepository.save(gangneung);

        addReview(jeju, yuni, "성산일출봉 일출이 정말 아름다웠어요!", 5);
        addReview(jeju, minsu, "일정 구성이 알차서 따라가기 좋았습니다.", 4);
        addReview(busan, haneul, "맛집 리스트가 최고예요. 해운대도 좋았어요!", 5);

        savedTripRepository.save(new SavedTrip(yuni.getId(), busan.getId()));
        savedTripRepository.save(new SavedTrip(yuni.getId(), gangneung.getId()));
    }

    private User createUser(String email, String password, String nickname, String bio) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(nickname);
        user.setBio(bio);
        return userRepository.save(user);
    }

    private Trip createTrip(User user, String title, String destination,
                            String start, String end, String description, int likes) {
        Trip trip = new Trip();
        trip.setUser(user);
        trip.setTitle(title);
        trip.setDestination(destination);
        trip.setStartDate(LocalDate.parse(start));
        trip.setEndDate(LocalDate.parse(end));
        trip.setDescription(description);
        trip.setLikeCount(likes);
        return trip;
    }

    private void addSchedule(Trip trip, int day, String time, String place,
                             String desc, double lat, double lng, int order) {
        ScheduleItem item = new ScheduleItem();
        item.setDayNum(day);
        item.setScheduleTime(time);
        item.setPlaceName(place);
        item.setDescription(desc);
        item.setLatitude(BigDecimal.valueOf(lat));
        item.setLongitude(BigDecimal.valueOf(lng));
        item.setSortOrder(order);
        trip.addScheduleItem(item);
    }

    private void addReview(Trip trip, User user, String content, int rating) {
        Review review = new Review();
        review.setTrip(trip);
        review.setUser(user);
        review.setContent(content);
        review.setRating(rating);
        reviewRepository.save(review);
    }
}
