package com.bignerdranch.example.beatbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.example.beatbox.databinding.ActivityMainBinding
import com.bignerdranch.example.beatbox.databinding.ListItemSoundBinding

class MainActivity : AppCompatActivity() {
    private lateinit var beatBox: BeatBox
    private lateinit var binding: ActivityMainBinding
    private var rate: Float = 1.0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beatBox = BeatBox(assets)

        // 使用 Data Binding 绑定布局
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = SoundAdapter(beatBox.sounds)
        }
        binding.sekBar.progress = 100 // 例如，设置初始进度为100
        rate = binding.sekBar.progress.div(100.0f)
        updateText(rate)



        binding.sekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // 当用户拖动 SeekBar 时实时更新
                seekBar?.let {
                    rate = seekBar.progress.div(100.0f)
                    beatBox?.setRate(rate)
                    updateText(rate)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // 用户开始拖动 SeekBar 时
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // 用户停止拖动 SeekBar 时

            }
        })


    }



    override fun onDestroy() {
        super.onDestroy()
        beatBox.release()
    }
    private inner class SoundHolder(private val binding: ListItemSoundBinding):
        RecyclerView.ViewHolder(binding.root){
            init {
                binding.viewModel = SoundViewModel(beatBox)
            }
        fun bind(sound: Sound){
            binding.apply {
                viewModel?.sound = sound
                executePendingBindings()
            }
        }

    }

    private inner class SoundAdapter(private val sounds: List<Sound>):
            RecyclerView.Adapter<SoundHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
            val binding = DataBindingUtil.inflate<ListItemSoundBinding>(
                layoutInflater,
                R.layout.list_item_sound,
                parent,
                false
            )
            return SoundHolder(binding)
        }
        override fun getItemCount() = sounds.size


        override fun onBindViewHolder(holder: SoundHolder, position: Int) {
            val sound = sounds[position]
            holder.bind(sound)
        }
    }

    private fun updateText(rate: Float){
            binding.txtViewSpeed.text = getString(R.string.playback_speed, rate.toString())
    }

}