package ru.itmo.chirper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.itmo.chirper.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvProfileUsername.text = "user123"
    }
}