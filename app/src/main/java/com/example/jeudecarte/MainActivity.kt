package com.example.jeudecarte

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.jeudecarte.Bataille.Bataille
import com.example.jeudecarte.HereToSlay.view.HereToSlay
import com.example.jeudecarte.databinding.GameListBinding

class MainActivity : Activity() {

    //the variable that list all xml ids
    private lateinit var binding: GameListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = GameListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //link the button to the class that manage the bataille
        binding.bataille.setOnClickListener {
            val intent = Intent(this, Bataille::class.java)
            startActivity(intent)
        }

        //link the button to the class that manage the here to slay
        binding.heretoslay.setOnClickListener {
            val intent = Intent(this, HereToSlay::class.java)
            startActivity(intent)
        }

    }
}