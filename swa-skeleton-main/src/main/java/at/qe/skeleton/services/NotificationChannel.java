package at.qe.skeleton.services;


import at.qe.skeleton.model.Userx;

/**
 * Strategy interface for delivering notifications through different channels.
 *
 * This interface is used by {@link NotificationService} to dispatch notifications according to a user's preferred channels.
 */

public interface NotificationChannel {

    /**
     * Returns the channel type supported by this implementation.
     *
     * @return the notification channel type
     */
    NotificationChannelType getType();

    /**
     * Sends a notification to the specified user.
     *
     * @param user the recipient
     * @param title the notification title
     * @param message the notification message
     */
    void send(Userx user, String title, String message);
}