package com.domonz.PrashantAdvait

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.domonz.PrashantAdvait.DataModels.UnsplashApiRequest
import com.domonz.PrashantAdvait.DataModels.UnsplashPhoto
import com.domonz.PrashantAdvait.ViewModels.MainViewModel
import com.domonz.PrashantAdvait.adapters.AdapterImages
import com.domonz.PrashantAdvait.databinding.ActivityMainBinding
import com.domonz.PrashantAdvait.utils.Constants
import com.domonz.PrashantAdvait.utils.NetworkChangeReceiver
import com.domonz.PrashantAdvait.utils.NetworkResult
import com.domonz.PrashantAdvait.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NetworkChangeReceiver.OnNetworkChangeListener {

    lateinit var binding:ActivityMainBinding
    lateinit var mainViewModel: MainViewModel
    var currentPage = 1
    var adapterImages:AdapterImages? = null
    var imagesList:MutableSet<UnsplashPhoto> = mutableSetOf()
    private val networkChangeReceiver = NetworkChangeReceiver(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        next()
    }

    fun next(){

        binding.apply {
            binding.loadingLayout.visibility = View.VISIBLE
            mainViewModel =  ViewModelProvider(this@MainActivity)[MainViewModel::class.java]
            val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            recImages.layoutManager = layoutManager
            adapterImages = AdapterImages(this@MainActivity, imagesList)
            recImages.adapter = adapterImages

            scrollView.viewTreeObserver.addOnScrollChangedListener {
                val view = scrollView.getChildAt(scrollView.childCount - 1) as View
                val diff: Int = view.bottom - (scrollView.height + scrollView
                    .scrollY)
                if (diff == 0) {
                    getImages(currentPage)
                }
            }

        }
        bindObservers()
        getImages(currentPage)
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, intentFilter)

    }

    fun getImages(page:Int){
        runOnUiThread {
            MainScope().launch {
                val request = UnsplashApiRequest(TokenManager().getClientId(), page, 30)
                mainViewModel.getImages(request)
            }
        }
    }


    fun bindObservers(){

        mainViewModel.userResponseLiveData.observe(this@MainActivity) {
            when (it) {
                is NetworkResult.Success -> {
                    currentPage += 1
                    binding.loadingProgressbar.visibility = View.VISIBLE
                    binding.noMoreImages.visibility = View.GONE
                    binding.loadingLayout.visibility = View.GONE
                    binding.layoutError.visibility = View.GONE
                    for (i in 0 until it.data!!.size) {
                        val image = it.data[i]
                        val item = imagesList.firstOrNull { v -> v.id == image.id }
                        if (item == null) {
                            imagesList.add(image)
                            adapterImages?.notifyItemInserted(imagesList.size - 1)
                        }
                    }
                }

                is NetworkResult.PermissionError -> {
                    binding.loadingProgressbar.visibility = View.GONE
                    binding.noMoreImages.text = it.message.toString()
                    binding.noMoreImages.visibility = View.VISIBLE
                }

                is NetworkResult.Error -> {
                    binding.loadingProgressbar.visibility = View.GONE
                    binding.loadingLayout.visibility = View.GONE
                    binding.layoutError.visibility = View.VISIBLE
                    binding.errorText.text = it.message.toString()
                }

                is NetworkResult.Loading -> {
                }
            }
        }

    }



    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
    }

    override fun onNetworkConnected() {
        Log.d(Constants.TAG, "NETWORK_CONNECTED")
        getImages(currentPage)
    }

    override fun onNetworkDisconnected() {
        Log.d(Constants.TAG, "NETWORK_DISCONNECTED")
    }

}