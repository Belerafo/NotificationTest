package com.example.myservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationtest.MainActivity
import com.example.notificationtest.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


val CHANNEL_ID = "2"
    var image_url: String? = null
    var image_bitmap: Bitmap? = null

class MyFirebaseMessagingService : FirebaseMessagingService() {

    fun MyFirebaseMessagingService(){

    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.notification != null){

            if(remoteMessage.notification!!.imageUrl != null){
                image_url = remoteMessage.notification!!.imageUrl.toString()
                image_bitmap = getBitmapFromURL(image_url!!)
            }
            //Create and Display Notification
        showNotification(remoteMessage.notification!!.title, remoteMessage.notification!!.body)

        }
        if (!remoteMessage.data.isEmpty()) {
            val myData = remoteMessage.data
            Log.d("MYDATA", myData["key1"].toString())
            Log.d("MYDATA", myData["key2"].toString())
        }
    }

    private fun getBitmapFromURL(imageUrl: String): Bitmap? {
        return try {
            val url = URL(image_url)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    private fun showNotification(title: String?, text: String?) {
        //Create Notification Channel
        createChannel()

        val builder = NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_message)
            .setContentTitle(title)
            .setContentText(text)
            .setLargeIcon(image_bitmap)
            .setAutoCancel(true)
            .setColor(
                    resources.getColor(R.color.red))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(2, builder.build()) //Send Notification
        }
    }



    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val context = this.applicationContext
            val name = "My channel name2"
            val description = "My channel description2"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(MainActivity.CHANNEL_ID, name, importance)
            channel.description = description

            //Register the channel with the system
            //You can not change importance or the notification behaviors after this
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

}