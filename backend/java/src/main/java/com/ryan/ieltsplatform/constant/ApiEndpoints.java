package com.ryan.ieltsplatform.constant;

/**
 * Centralized constants for all API endpoints.
 * All endpoint paths should be defined here to avoid hardcoded URLs.
 */
public final class ApiEndpoints {

    private ApiEndpoints() {
        // Prevent instantiation
    }

    // ==================== BASE PATHS ====================
    
    public static final String API_BASE = "/api";
    public static final String ACTUATOR_BASE = "/actuator";
    public static final String SWAGGER_BASE = "/swagger-ui";
    public static final String OPENAPI_BASE = "/v3/api-docs";

    // ==================== HELLO ENDPOINTS ====================
    
    public static final class Hello {
        public static final String BASE = API_BASE + "/hello";
        public static final String GREET = BASE;
        public static final String GREET_WITH_NAME = BASE + "/{name}";
        
        // Error demonstration endpoints
        public static final String BUSINESS_ERROR = BASE + "/error/business";
        public static final String VALIDATION_ERROR = BASE + "/error/validation";
        public static final String RUNTIME_ERROR = BASE + "/error/runtime";
    }

    // ==================== USER ENDPOINTS ====================
    
    public static final class User {
        public static final String BASE = API_BASE + "/users";
        public static final String CREATE = BASE;
        public static final String GET_ALL = BASE;
        public static final String GET_BY_ID = BASE + "/{id}";
        public static final String UPDATE = BASE + "/{id}";
        public static final String DELETE = BASE + "/{id}";
        public static final String GET_PROFILE = BASE + "/profile";
        public static final String UPDATE_PROFILE = BASE + "/profile";
    }

    // ==================== AUTH ENDPOINTS ====================
    
    public static final class Auth {
        public static final String BASE = API_BASE + "/auth";
        public static final String LOGIN = BASE + "/login";
        public static final String LOGOUT = BASE + "/logout";
        public static final String REGISTER = BASE + "/register";
        public static final String REFRESH_TOKEN = BASE + "/refresh";
        public static final String FORGOT_PASSWORD = BASE + "/forgot-password";
        public static final String RESET_PASSWORD = BASE + "/reset-password";
        public static final String VERIFY_EMAIL = BASE + "/verify-email";
    }

    // ==================== IELTS TEST ENDPOINTS ====================
    
    public static final class Test {
        public static final String BASE = API_BASE + "/tests";
        public static final String CREATE = BASE;
        public static final String GET_ALL = BASE;
        public static final String GET_BY_ID = BASE + "/{id}";
        public static final String UPDATE = BASE + "/{id}";
        public static final String DELETE = BASE + "/{id}";
        public static final String START_TEST = BASE + "/{id}/start";
        public static final String SUBMIT_TEST = BASE + "/{id}/submit";
        public static final String GET_RESULTS = BASE + "/{id}/results";
    }

    // ==================== QUESTION ENDPOINTS ====================
    
    public static final class Question {
        public static final String BASE = API_BASE + "/questions";
        public static final String CREATE = BASE;
        public static final String GET_ALL = BASE;
        public static final String GET_BY_ID = BASE + "/{id}";
        public static final String UPDATE = BASE + "/{id}";
        public static final String DELETE = BASE + "/{id}";
        public static final String GET_BY_TEST = BASE + "/test/{testId}";
        public static final String GET_BY_CATEGORY = BASE + "/category/{category}";
    }

    // ==================== SCORE ENDPOINTS ====================
    
    public static final class Score {
        public static final String BASE = API_BASE + "/scores";
        public static final String CREATE = BASE;
        public static final String GET_ALL = BASE;
        public static final String GET_BY_ID = BASE + "/{id}";
        public static final String GET_BY_USER = BASE + "/user/{userId}";
        public static final String GET_BY_TEST = BASE + "/test/{testId}";
        public static final String GET_USER_STATS = BASE + "/user/{userId}/stats";
        public static final String GET_OVERALL_STATS = BASE + "/stats/overall";
    }

    // ==================== AUDIO ENDPOINTS ====================
    
    public static final class Audio {
        public static final String BASE = API_BASE + "/audio";
        public static final String UPLOAD = BASE + "/upload";
        public static final String GET_BY_ID = BASE + "/{id}";
        public static final String DELETE = BASE + "/{id}";
        public static final String GET_USER_AUDIOS = BASE + "/user/{userId}";
        public static final String PROCESS_AUDIO = BASE + "/{id}/process";
    }

    // ==================== ACTUATOR ENDPOINTS ====================
    
    public static final class Actuator {
        public static final String HEALTH = ACTUATOR_BASE + "/health";
        public static final String INFO = ACTUATOR_BASE + "/info";
        public static final String METRICS = ACTUATOR_BASE + "/metrics";
        public static final String PROMETHEUS = ACTUATOR_BASE + "/prometheus";
    }

    // ==================== DOCUMENTATION ENDPOINTS ====================
    
    public static final class Documentation {
        public static final String SWAGGER_UI = SWAGGER_BASE;
        public static final String OPENAPI_JSON = OPENAPI_BASE;
        public static final String OPENAPI_YAML = OPENAPI_BASE + ".yaml";
    }

    // ==================== UTILITY METHODS ====================
    
    /**
     * Build an endpoint with path parameters.
     * 
     * @param baseEndpoint The base endpoint (e.g., User.GET_BY_ID)
     * @param pathParams The path parameters to substitute
     * @return The endpoint with substituted parameters
     */
    public static String buildEndpoint(String baseEndpoint, Object... pathParams) {
        String endpoint = baseEndpoint;
        for (Object param : pathParams) {
            endpoint = endpoint.replaceFirst("\\{[^}]*\\}", param.toString());
        }
        return endpoint;
    }

    /**
     * Build a user endpoint with ID.
     * 
     * @param userId The user ID
     * @return The complete user endpoint
     */
    public static String getUserEndpoint(String userId) {
        return buildEndpoint(User.GET_BY_ID, userId);
    }

    /**
     * Build a test endpoint with ID.
     * 
     * @param testId The test ID
     * @return The complete test endpoint
     */
    public static String getTestEndpoint(String testId) {
        return buildEndpoint(Test.GET_BY_ID, testId);
    }

    /**
     * Build a question endpoint with ID.
     * 
     * @param questionId The question ID
     * @return The complete question endpoint
     */
    public static String getQuestionEndpoint(String questionId) {
        return buildEndpoint(Question.GET_BY_ID, questionId);
    }

    /**
     * Build an audio endpoint with ID.
     * 
     * @param audioId The audio ID
     * @return The complete audio endpoint
     */
    public static String getAudioEndpoint(String audioId) {
        return buildEndpoint(Audio.GET_BY_ID, audioId);
    }
}
