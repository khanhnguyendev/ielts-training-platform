package com.ryan.ieltsplatform.service;

import com.ryan.ieltsplatform.constant.MessageConstants;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

/**
 * Service for managing and formatting API messages.
 * Handles dynamic message formatting with parameters.
 */
@Service
public class MessageService {

    /**
     * Format a message with parameters using MessageFormat.
     * 
     * @param pattern The message pattern with placeholders
     * @param arguments The arguments to substitute
     * @return Formatted message
     */
    public String formatMessage(String pattern, Object... arguments) {
        return MessageFormat.format(pattern, arguments);
    }

    /**
     * Get a success message.
     * 
     * @param messageKey The message key from MessageConstants.Success
     * @param arguments Optional arguments for formatting
     * @return Formatted success message
     */
    public String getSuccessMessage(String messageKey, Object... arguments) {
        return formatMessage(messageKey, arguments);
    }

    /**
     * Get an error message.
     * 
     * @param messageKey The message key from MessageConstants.Error
     * @param arguments Optional arguments for formatting
     * @return Formatted error message
     */
    public String getErrorMessage(String messageKey, Object... arguments) {
        return formatMessage(messageKey, arguments);
    }

    /**
     * Get personalized hello message.
     * 
     * @param name The name to include in the message
     * @return Formatted personalized hello message
     */
    public String getPersonalizedHelloMessage(String name) {
        return formatMessage(MessageConstants.Success.PERSONALIZED_HELLO, name);
    }

    /**
     * Get validation failed message.
     * 
     * @param fieldCount Number of fields that failed validation
     * @return Formatted validation failed message
     */
    public String getValidationFailedMessage(int fieldCount) {
        return formatMessage(MessageConstants.Error.VALIDATION_FAILED, fieldCount);
    }

    /**
     * Get name too long message.
     * 
     * @return Formatted name too long message
     */
    public String getNameTooLongMessage() {
        return formatMessage(MessageConstants.Error.NAME_TOO_LONG, MessageConstants.Validation.MAX_NAME_LENGTH);
    }

    /**
     * Get user not found message.
     * 
     * @param userId The user ID that was not found
     * @return Formatted user not found message
     */
    public String getUserNotFoundMessage(String userId) {
        return formatMessage(MessageConstants.Error.USER_NOT_FOUND, userId);
    }

    /**
     * Get email already exists message.
     * 
     * @param email The email that already exists
     * @return Formatted email already exists message
     */
    public String getEmailAlreadyExistsMessage(String email) {
        return formatMessage(MessageConstants.Error.EMAIL_ALREADY_EXISTS, email);
    }
}
