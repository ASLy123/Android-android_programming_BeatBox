package com.bignerdranch.example.beatbox

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.SoundPool
import android.util.Log
import android.widget.SeekBar
import java.io.IOException
import java.lang.Exception

private const val TAG = "BeatBox"
private const val SOUNDS_FOLDER = "sample_sounds"   //存储声音资源文件目录名
private const val MAX_SOUNDS = 5
class BeatBox (private val assets: AssetManager){
    val sounds: List<Sound>
    private val soundPool = SoundPool.Builder() //实现音频播放功能
        .setMaxStreams(MAX_SOUNDS)              //同时播放多少个音频
        .build()
    init {
        sounds = loadSounds()
    }
    fun play(sound: Sound){         //播放音频
        sound.soundId?.let {
            soundPool.play(it, 1.0f, 1.0f, 1, 0, 1.0f)
            //音频ID、左音量、右音量、优先级（无效）、是否循环和播放速率
        }
    }

    fun release(){              //释放SoundPool
        soundPool.release()
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
            try {
                load(sound)             //载入全部音频文件
                sounds.add(sound)
            } catch (ioe:IOException){
                Log.e(TAG, "Could not load sound $filename", ioe)
            }
        }
        return sounds
    }
    private fun load(sound: Sound){     //加载音频
        val afd: AssetFileDescriptor = assets.openFd(sound.assetPath)
        val soundId = soundPool.load(afd,1)         //把音频文件载入SoundPool待播
        sound.soundId = soundId
    }
}