package com.bignerdranch.example.beatbox

import android.content.res.AssetManager
import android.util.Log
import java.lang.Exception

private const val TAG = "BeatBox"
private const val SOUNDS_FOLDER = "sample_sounds"   //存储声音资源文件目录名
class BeatBox (private val assets: AssetManager){
    val sounds: List<Sound>
    init {
        sounds = loadSounds()
    }

    private fun loadSounds(): List<Sound>{
        val soundNames: Array<String>
        try {
            soundNames = assets.list(SOUNDS_FOLDER)!!       //能列出指定目录下的所有文件名
//            Log.d(TAG, "Found ${soundNames.size} sounds")
//            return soundNames.asList()
        } catch (e:Exception){
            Log.e(TAG, "Could not list assets", e)
            return emptyList()
        }
        val sounds = mutableListOf<Sound>()
        soundNames.forEach { filename ->
            val assetPath = "$SOUNDS_FOLDER/$filename"
            val sound  = Sound(assetPath)
            sounds.add(sound)
        }
        return sounds
    }
}