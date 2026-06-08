package com.inhatc.tripshare.data;

import com.inhatc.tripshare.R;
import com.inhatc.tripshare.model.Review;
import com.inhatc.tripshare.model.ScheduleItem;
import com.inhatc.tripshare.model.Trip;
import com.inhatc.tripshare.model.UserProfile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class MockData {

    private static final List<Trip> ALL_TRIPS = new ArrayList<>();
    private static final Map<Integer, List<Review>> REVIEWS = new HashMap<>();
    private static final Map<String, String> USERS = new HashMap<>();
    private static int nextTripId = 5;
    private static int nextReviewId = 10;

    private static UserProfile currentUser = new UserProfile(
            "traveler_유니",
            "yuni@example.com",
            "여행을 사랑하는 유니입니다 ✈️",
            12, 25, 128
    );

    static {
        USERS.put("yuni@example.com", "1234");

        List<ScheduleItem> jejuSchedule = Arrays.asList(
                new ScheduleItem(1, "09:00", "제주공항 도착",
                        "렌터카 픽업 후 여행 시작", 33.5113, 126.4930),
                new ScheduleItem(1, "12:00", "협재해수욕장",
                        "에메랄드빛 바다 감상", 33.3939, 126.2396),
                new ScheduleItem(2, "09:00", "성산일출봉",
                        "일출봉 등반 및 전망 감상", 33.4581, 126.9425),
                new ScheduleItem(2, "14:00", "섭지코지",
                        "드라마 촬영지 산책", 33.4244, 126.9278),
                new ScheduleItem(3, "10:00", "한라산 어리목",
                        "가벼운 등산 코스", 33.3617, 126.5292)
        );

        ALL_TRIPS.add(new Trip(1, "제주도 2박 3일 힐링 여행", "traveler_유니", "제주도",
                "2024.05.01", "2024.05.03",
                "제주의 아름다운 자연을 만끽하는 힐링 여행 일정입니다.",
                R.drawable.bg_trip_1, 245, 0, jejuSchedule, true, false));

        ALL_TRIPS.add(new Trip(2, "부산 맛집 투어 1박 2일", "foodie_민수", "부산",
                "2024.06.10", "2024.06.11",
                "부산의 유명 맛집을 돌아보는 미식 여행입니다.",
                R.drawable.bg_trip_2, 189, 0,
                Arrays.asList(
                        new ScheduleItem(1, "11:00", "자갈치시장", "해산물 점심", 35.0967, 129.0307),
                        new ScheduleItem(1, "15:00", "해운대 해수욕장", "해변 산책", 35.1587, 129.1604)
                ), false, true));

        ALL_TRIPS.add(new Trip(3, "서울 문화 탐방 1일 코스", "culture_지은", "서울",
                "2024.04.20", "2024.04.20",
                "서울의 대표 문화 명소를 하루에 둘러보는 코스입니다.",
                R.drawable.bg_trip_3, 156, 0,
                Arrays.asList(
                        new ScheduleItem(1, "10:00", "경복궁", "한복 체험", 37.5796, 126.9770),
                        new ScheduleItem(1, "14:00", "북촌한옥마을", "전통 마을 산책", 37.5826, 126.9831)
                ), false, false));

        ALL_TRIPS.add(new Trip(4, "강릉 바다 여행 2박 3일", "ocean_하늘", "강릉",
                "2024.07.01", "2024.07.03",
                "동해의 푸른 바다와 커피 거리를 즐기는 여행입니다.",
                R.drawable.bg_trip_4, 312, 0,
                Arrays.asList(
                        new ScheduleItem(1, "09:00", "경포대", "해변 산책", 37.7955, 128.9008),
                        new ScheduleItem(2, "11:00", "안목해변 커피거리", "카페 투어", 37.7703, 128.9463)
                ), false, true));

        addReview(new Review(1, 1, "jeju_lover", "성산일출봉 일출이 정말 아름다웠어요!", "2024.05.10", 5));
        addReview(new Review(2, 1, "trip_fan", "일정 구성이 알차서 따라가기 좋았습니다.", "2024.05.12", 4));
        addReview(new Review(3, 2, "busan_eater", "맛집 리스트가 최고예요. 해운대도 좋았어요!", "2024.06.15", 5));
    }

    private MockData() {
    }

    public static boolean registerUser(String email, String password, String nickname) {
        if (USERS.containsKey(email)) {
            return false;
        }
        USERS.put(email, password);
        currentUser = new UserProfile(nickname, email,
                "여행을 사랑하는 " + nickname + "입니다 ✈️", 0, 0, 0);
        return true;
    }

    public static boolean login(String email, String password) {
        String saved = USERS.get(email);
        if (saved != null && saved.equals(password)) {
            if (!email.equals(currentUser.getEmail())) {
                currentUser = new UserProfile(
                        email.split("@")[0], email,
                        "여행을 사랑하는 여행자입니다 ✈️",
                        getMyCreatedTrips().size(), getSavedTrips().size(), 0);
            }
            return true;
        }
        return false;
    }

    public static Trip addTrip(String title, String destination, String startDate,
                               String endDate, String description,
                               List<ScheduleItem> schedule) {
        int imageRes = pickImageRes(ALL_TRIPS.size());
        List<ScheduleItem> resolvedSchedule = resolveScheduleCoordinates(destination, schedule);

        Trip trip = new Trip(
                nextTripId++, title, currentUser.getNickname(), destination,
                startDate, endDate, description, imageRes, 0, 0,
                resolvedSchedule, true, false);
        ALL_TRIPS.add(0, trip);
        REVIEWS.put(trip.getId(), new ArrayList<>());
        return trip;
    }

    private static int pickImageRes(int index) {
        int[] images = {R.drawable.bg_trip_1, R.drawable.bg_trip_2,
                R.drawable.bg_trip_3, R.drawable.bg_trip_4};
        return images[index % images.length];
    }

    private static List<ScheduleItem> resolveScheduleCoordinates(String destination,
                                                                 List<ScheduleItem> schedule) {
        double[] base = getBaseCoordinates(destination);
        List<ScheduleItem> result = new ArrayList<>();
        for (int i = 0; i < schedule.size(); i++) {
            ScheduleItem item = schedule.get(i);
            double lat = item.getLatitude() != 0 ? item.getLatitude()
                    : base[0] + (i * 0.02);
            double lng = item.getLongitude() != 0 ? item.getLongitude()
                    : base[1] + (i * 0.02);
            result.add(new ScheduleItem(item.getDay(), item.getTime(), item.getTitle(),
                    item.getDescription(), lat, lng));
        }
        return result;
    }

    private static double[] getBaseCoordinates(String destination) {
        if (destination.contains("제주")) {
            return new double[]{33.45, 126.55};
        } else if (destination.contains("부산")) {
            return new double[]{35.15, 129.05};
        } else if (destination.contains("강릉")) {
            return new double[]{37.75, 128.90};
        } else if (destination.contains("서울")) {
            return new double[]{37.57, 126.98};
        }
        return new double[]{37.57, 126.98};
    }

    public static List<Review> getReviews(int tripId) {
        List<Review> reviews = REVIEWS.get(tripId);
        return reviews != null ? new ArrayList<>(reviews) : new ArrayList<>();
    }

    public static int getReviewCount(int tripId) {
        return getReviews(tripId).size();
    }

    public static Review addReview(int tripId, String author, String content, int rating) {
        String date = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(new Date());
        Review review = new Review(nextReviewId++, tripId, author, content, date, rating);
        addReview(review);
        return review;
    }

    private static void addReview(Review review) {
        List<Review> list = REVIEWS.computeIfAbsent(review.getTripId(), k -> new ArrayList<>());
        list.add(0, review);
    }

    public static List<Trip> getPopularTrips() {
        return ALL_TRIPS.subList(0, Math.min(3, ALL_TRIPS.size()));
    }

    public static List<Trip> getLatestTrips() {
        return new ArrayList<>(ALL_TRIPS);
    }

    public static List<Trip> getMyCreatedTrips() {
        List<Trip> result = new ArrayList<>();
        for (Trip trip : ALL_TRIPS) {
            if (trip.isMine()) {
                result.add(trip);
            }
        }
        return result;
    }

    public static List<Trip> getSavedTrips() {
        List<Trip> result = new ArrayList<>();
        for (Trip trip : ALL_TRIPS) {
            if (trip.isSaved()) {
                result.add(trip);
            }
        }
        return result;
    }

    public static Trip getTripById(int id) {
        for (Trip trip : ALL_TRIPS) {
            if (trip.getId() == id) {
                return trip;
            }
        }
        return ALL_TRIPS.get(0);
    }

    public static UserProfile getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(UserProfile user) {
        currentUser = user;
    }

    public static List<Trip> searchTrips(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getLatestTrips();
        }
        String lower = query.toLowerCase();
        List<Trip> result = new ArrayList<>();
        for (Trip trip : ALL_TRIPS) {
            if (trip.getTitle().toLowerCase().contains(lower)
                    || trip.getLocation().toLowerCase().contains(lower)
                    || trip.getAuthor().toLowerCase().contains(lower)) {
                result.add(trip);
            }
        }
        return result;
    }
}
