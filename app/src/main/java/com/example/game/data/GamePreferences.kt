package com.example.game.data

import android.content.Context
import android.content.SharedPreferences

class GamePreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("level_devil_prefs", Context.MODE_PRIVATE)

    fun isLevelUnlocked(levelId: Int): Boolean {
        if (levelId == 1) return true
        return prefs.getBoolean("level_unlocked_$levelId", false)
    }

    fun unlockLevel(levelId: Int) {
        prefs.edit().putBoolean("level_unlocked_$levelId", true).apply()
    }

    fun getLevelStars(levelId: Int): Int {
        return prefs.getInt("level_stars_$levelId", 0)
    }

    fun setLevelStars(levelId: Int, stars: Int) {
        val currentStars = getLevelStars(levelId)
        if (stars > currentStars) {
            prefs.edit().putInt("level_stars_$levelId", stars).apply()
        }
    }

    fun getBestTimeMs(levelId: Int): Long {
        return prefs.getLong("best_time_$levelId", 0L)
    }

    fun setBestTimeMs(levelId: Int, timeMs: Long) {
        val currentBest = getBestTimeMs(levelId)
        if (currentBest == 0L || timeMs < currentBest) {
            prefs.edit().putLong("best_time_$levelId", timeMs).apply()
        }
    }

    fun getTotalDeaths(): Int {
        return prefs.getInt("total_deaths", 0)
    }

    fun setTotalDeaths(deaths: Int) {
        prefs.edit().putInt("total_deaths", deaths).apply()
    }

    fun isSoundEnabled(): Boolean {
        return prefs.getBoolean("sound_enabled", true)
    }

    fun setSoundEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("sound_enabled", enabled).apply()
    }

    fun isMusicEnabled(): Boolean {
        return prefs.getBoolean("music_enabled", true)
    }

    fun setMusicEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("music_enabled", enabled).apply()
    }

    fun isAchievementUnlocked(achievementId: String): Boolean {
        return prefs.getBoolean("achievement_$achievementId", false)
    }

    fun unlockAchievement(achievementId: String) {
        prefs.edit().putBoolean("achievement_$achievementId", true).apply()
    }

    fun clearAllData() {
        prefs.edit().clear().apply()
    }
}
