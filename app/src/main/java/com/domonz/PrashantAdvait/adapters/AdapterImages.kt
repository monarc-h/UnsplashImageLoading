package com.domonz.PrashantAdvait.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.domonz.PrashantAdvait.DataModels.UnsplashPhoto
import com.domonz.PrashantAdvait.databinding.ItemImageBinding
import com.domonz.PrashantAdvait.utils.Constants.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class AdapterImages(var context: Context, var _data:MutableSet<UnsplashPhoto>):RecyclerView.Adapter<AdapterImages.ViewHolder>() {
    class ViewHolder(var binding:ItemImageBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemImageBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val image = _data.toList()[position]
            CoroutineScope(Dispatchers.IO).launch {
                Log.i(TAG, "Current thread ${Thread.currentThread().name}")
                val bitmap = downloadAndCacheBitmap(context, image.urls.thumbUrl)
                withContext(Dispatchers.Main) {
                    Log.i(TAG, "Current thread in the main dispatcher: ${Thread.currentThread().name}")
                    if(bitmap == null){
                        error.visibility = View.VISIBLE
                    }else {
                        error.visibility = View.GONE
                        imageView.setImageBitmap(bitmap)
                    }
                }
            }
            //Glide.with(context).load(image.urls.thumbUrl).override(Target.SIZE_ORIGINAL).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView)

        }
    }

    override fun getItemCount(): Int {
        return _data.size
    }

/*    private fun downloadBitmap(imageUrl: String): Bitmap? {
        return try {
            val conn = URL(imageUrl).openConnection()
            conn.connect()
            val inputStream = conn.getInputStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            bitmap
        } catch (e: Exception) {
            Log.e(TAG, "Exception $e")
            null
        }
    }*/

    fun downloadAndCacheBitmap(context: Context, imageUrl: String): Bitmap? {
        try {
            // Generate a unique filename for caching
            val fileName = imageUrl.hashCode().toString() + ".png"
            val file = File(context.cacheDir, fileName)

            // Check if the image is cached
            if (file.exists()) {
                Log.i(TAG, "Image found in cache: $fileName")
                return BitmapFactory.decodeFile(file.absolutePath)
            }

            // If not cached, download the image
            val conn = URL(imageUrl).openConnection() as HttpURLConnection
            conn.connect()
            val inputStream = conn.inputStream

            // Save the downloaded image to cache
            val outputStream = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            outputStream.close()
            inputStream.close()

            Log.i(TAG, "Image downloaded and cached: $fileName")

            // Return the downloaded bitmap
            return BitmapFactory.decodeFile(file.absolutePath)
        } catch (e: IOException) {
            Log.e(TAG, "Error downloading image: ${e.message}")
            return null
        }
    }
}