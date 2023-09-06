package com.bignerdranch.example.beatbox

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class SoundViewModel(private val beatBox: BeatBox) : BaseObservable(){
    var sound: Sound? = null
    set(sound){
        field = sound
        notifyChange()  //通知绑定类，视图模型对象上所有可绑定属性都已更新
    }
    @get: Bindable
    val title: String?
    get() = sound?.name

    fun onButtonClicked(){
        sound?.let {
            beatBox.play(it)
        }
    }
}