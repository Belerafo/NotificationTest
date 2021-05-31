package com.example.notificationtest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationtest.databinding.ActivityMainBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Create Notification channel
        createChannel()

        myRegistrationToken()

        binding.buyBtn.setOnClickListener {
            val numberOfPineapple = binding.weirdPineapple.text.toString()

            //Subscribe to Topic - Firebase Cloud Massaging
            subscribeToDiscount(Integer.parseInt(numberOfPineapple))

            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("weird_pineapple", numberOfPineapple)
            val pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.pineapple_owl)

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle("Pineapple")
                .setContentText("You just bought $numberOfPineapple Weird Pineapple!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setLargeIcon(bitmap)
                .addAction(R.mipmap.ic_launcher, "Get bonus!", pendingIntent)
                .setColor(
                        resources.getColor(R.color.red))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this)) {
                notify(1, builder.build()) //Send Notification
            }
        }
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val context = this.applicationContext
            val name = "My channel name"
            val description = "My channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description

            //Register the channel with the system
            //You can not change importance or the notification behaviors after this
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "1"

    }

    private fun myRegistrationToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener OnCompleteListener@{ task ->
                if (!task.isSuccessful) {
                    Log.w("TAG", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val token: String = task.result.toString()
                // Log and toast
                Toast.makeText(this@MainActivity, token, Toast.LENGTH_SHORT).show()
                Log.d("Token", token)
            }
    }


    private fun subscribeToDiscount(pineapple: Int) {
        if (pineapple <= 50) {
            FirebaseMessaging.getInstance().subscribeToTopic("small_discount")
                    .addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Toast.makeText(this@MainActivity, "Failed to Subscribe to Small Discount", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@MainActivity, "Successfully Subscribed to Small Discount!", Toast.LENGTH_SHORT).show()
                        }
                    }
        } else {
            FirebaseMessaging.getInstance().subscribeToTopic("huge_discount")
                    .addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Toast.makeText(this@MainActivity, "Failed to Subscribe to Huge Discount", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@MainActivity, "Successfully Subscribed to Huge Discount!", Toast.LENGTH_SHORT).show()
                        }
                    }
        }
    }
}



