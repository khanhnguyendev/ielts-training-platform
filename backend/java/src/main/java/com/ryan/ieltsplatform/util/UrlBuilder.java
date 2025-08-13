package com.ryan.ieltsplatform.util;

import com.ryan.ieltsplatform.constant.ApiEndpoints;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Utility class for building URLs using centralized endpoints.
 * Provides methods to construct complete URLs with proper encoding.
 */
public class UrlBuilder {

    private UrlBuilder() {
        // Prevent instantiation
    }

    /**
     * Build a complete URL for an endpoint.
     * 
     * @param baseUrl The base URL (e.g., "http://localhost:8080")
     * @param endpoint The endpoint path from ApiEndpoints
     * @param pathParams The path parameters to substitute
     * @return Complete URL
     */
    public static String buildUrl(String baseUrl, String endpoint, Object... pathParams) {
        String path = ApiEndpoints.buildEndpoint(endpoint, pathParams);
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(path)
                .build()
                .toUriString();
    }

    /**
     * Build a complete URL for an endpoint with query parameters.
     * 
     * @param baseUrl The base URL (e.g., "http://localhost:8080")
     * @param endpoint The endpoint path from ApiEndpoints
     * @param pathParams The path parameters to substitute
     * @param queryParams The query parameters (key-value pairs)
     * @return Complete URL
     */
    public static String buildUrlWithQuery(String baseUrl, String endpoint, Object[] pathParams, Object... queryParams) {
        String path = ApiEndpoints.buildEndpoint(endpoint, pathParams);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl).path(path);
        
        // Add query parameters
        for (int i = 0; i < queryParams.length; i += 2) {
            if (i + 1 < queryParams.length) {
                builder.queryParam(queryParams[i].toString(), queryParams[i + 1]);
            }
        }
        
        return builder.build().toUriString();
    }

    /**
     * Build a URI object for an endpoint.
     * 
     * @param baseUrl The base URL (e.g., "http://localhost:8080")
     * @param endpoint The endpoint path from ApiEndpoints
     * @param pathParams The path parameters to substitute
     * @return URI object
     */
    public static URI buildUri(String baseUrl, String endpoint, Object... pathParams) {
        String path = ApiEndpoints.buildEndpoint(endpoint, pathParams);
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(path)
                .build()
                .toUri();
    }

    // ==================== CONVENIENCE METHODS ====================

    /**
     * Build hello endpoint URL.
     * 
     * @param baseUrl The base URL
     * @return Hello endpoint URL
     */
    public static String buildHelloUrl(String baseUrl) {
        return buildUrl(baseUrl, ApiEndpoints.Hello.GREET);
    }

    /**
     * Build personalized hello endpoint URL.
     * 
     * @param baseUrl The base URL
     * @param name The name parameter
     * @return Personalized hello endpoint URL
     */
    public static String buildHelloWithNameUrl(String baseUrl, String name) {
        return buildUrl(baseUrl, ApiEndpoints.Hello.GREET_WITH_NAME, name);
    }

    /**
     * Build user endpoint URL.
     * 
     * @param baseUrl The base URL
     * @param userId The user ID
     * @return User endpoint URL
     */
    public static String buildUserUrl(String baseUrl, String userId) {
        return buildUrl(baseUrl, ApiEndpoints.User.GET_BY_ID, userId);
    }

    /**
     * Build test endpoint URL.
     * 
     * @param baseUrl The base URL
     * @param testId The test ID
     * @return Test endpoint URL
     */
    public static String buildTestUrl(String baseUrl, String testId) {
        return buildUrl(baseUrl, ApiEndpoints.Test.GET_BY_ID, testId);
    }

    /**
     * Build question endpoint URL.
     * 
     * @param baseUrl The base URL
     * @param questionId The question ID
     * @return Question endpoint URL
     */
    public static String buildQuestionUrl(String baseUrl, String questionId) {
        return buildUrl(baseUrl, ApiEndpoints.Question.GET_BY_ID, questionId);
    }

    /**
     * Build audio endpoint URL.
     * 
     * @param baseUrl The base URL
     * @param audioId The audio ID
     * @return Audio endpoint URL
     */
    public static String buildAudioUrl(String baseUrl, String audioId) {
        return buildUrl(baseUrl, ApiEndpoints.Audio.GET_BY_ID, audioId);
    }

    /**
     * Build health check URL.
     * 
     * @param baseUrl The base URL
     * @return Health check URL
     */
    public static String buildHealthUrl(String baseUrl) {
        return buildUrl(baseUrl, ApiEndpoints.Actuator.HEALTH);
    }

    /**
     * Build Swagger UI URL.
     * 
     * @param baseUrl The base URL
     * @return Swagger UI URL
     */
    public static String buildSwaggerUrl(String baseUrl) {
        return buildUrl(baseUrl, ApiEndpoints.Documentation.SWAGGER_UI);
    }

    /**
     * Build OpenAPI documentation URL.
     * 
     * @param baseUrl The base URL
     * @return OpenAPI documentation URL
     */
    public static String buildOpenApiUrl(String baseUrl) {
        return buildUrl(baseUrl, ApiEndpoints.Documentation.OPENAPI_JSON);
    }
}
