package com.inhatc.tripshare.api;

import com.inhatc.tripshare.api.dto.UserDto;
import com.inhatc.tripshare.model.UserProfile;

public final class SessionManager {

    private static UserDto currentUser;

    private SessionManager() {
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static long getUserId() {
        return currentUser != null ? currentUser.id : 0;
    }

    public static void setCurrentUser(UserDto user) {
        currentUser = user;
    }

    public static UserProfile getProfile() {
        if (currentUser == null) {
            return new UserProfile("", "", "", 0, 0, 0);
        }
        return new UserProfile(
                currentUser.nickname,
                currentUser.email,
                currentUser.bio != null ? currentUser.bio : "",
                0, 0, 0);
    }

    public static void clear() {
        currentUser = null;
    }
}
