package com.example.pizzamania;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundManager {
    private static MediaPlayer mediaPlayer;

    public static void playSound(Context context, int soundId) {
        // Stop & release any previous sound
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        // Create & play new sound
        mediaPlayer = MediaPlayer.create(context, soundId);
        mediaPlayer.start();
    }
}