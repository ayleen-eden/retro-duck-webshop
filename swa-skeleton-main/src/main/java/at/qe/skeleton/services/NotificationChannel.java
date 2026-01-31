package at.qe.skeleton.services;


import at.qe.skeleton.model.Userx;

public interface NotificationChannel {
    NotificationChannelType getType();
    void send(Userx user, String title, String message);
}