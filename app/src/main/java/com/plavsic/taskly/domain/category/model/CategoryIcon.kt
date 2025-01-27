package com.plavsic.taskly.domain.category.model

import com.plavsic.taskly.R

enum class CategoryIcon(val resId: Int) {
    BREAD(R.drawable.bread_category),
    BRIEFCASE(R.drawable.briefcase_category),
    SPORT(R.drawable.sport_category),
    DESIGN(R.drawable.design_category),
    EDUCATION(R.drawable.education_category),
    MEGAPHONE(R.drawable.megaphone_category),
    MUSIC(R.drawable.music_category),
    HEART(R.drawable.heartbeat_category),
    CAMERA(R.drawable.video_camera_category),
    HOME(R.drawable.home_category),
    ADD(R.drawable.add_category);


    companion object {
        fun fromName(name: String): CategoryIcon? {
            return entries.find { it.name.equals(name, ignoreCase = true) }
        }
    }
}