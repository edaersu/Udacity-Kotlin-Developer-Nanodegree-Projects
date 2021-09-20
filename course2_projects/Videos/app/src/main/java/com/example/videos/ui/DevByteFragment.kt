/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.videos.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videos.R
import com.example.videos.databinding.DevbyteItemBinding
import com.example.videos.databinding.FragmentDevByteBinding
import com.example.videos.domain.Video
import com.example.videos.viewmodels.DevByteViewModel

class DevByteFragment : Fragment() {

    private val viewModel: DevByteViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
     ViewModelProvider(this, DevByteViewModel.Factory(activity.application)).get(DevByteViewModel::class.java)
                
    }

    private var viewModelAdapter: DevByteAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.playlist.observe(viewLifecycleOwner, Observer<List<Video>> { videos ->
            videos?.apply {
                viewModelAdapter?.videos = videos
            }
        })
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentDevByteBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_dev_byte,
                container,
                false)
        binding.setLifecycleOwner(viewLifecycleOwner)

        binding.viewModel = viewModel

        viewModelAdapter = DevByteAdapter(VideoClick {
            val packageManager = context?.packageManager ?: return@VideoClick
            var intent = Intent(Intent.ACTION_VIEW, it.launchUri)
            if(intent.resolveActivity(packageManager) == null) {
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
            }

            startActivity(intent)
        })

        binding.root.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }

        return binding.root
    }
    private val Video.launchUri: Uri
        get() {
            val httpUri = Uri.parse(url)
            return Uri.parse("vnd.youtube:" + httpUri.getQueryParameter("v"))
        }
}

class VideoClick(val block: (Video) -> Unit) {
    fun onClick(video: Video) = block(video)
}
class DevByteAdapter(val callback: VideoClick) : RecyclerView.Adapter<DevByteViewHolder>() {

    var videos: List<Video> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevByteViewHolder {
        val withDataBinding: DevbyteItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                DevByteViewHolder.LAYOUT,
                parent,
                false)
        return DevByteViewHolder(withDataBinding)
    }

    override fun getItemCount() = videos.size
    override fun onBindViewHolder(holder: DevByteViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.video = videos[position]
            it.videoCallback = callback
        }
    }

}

class DevByteViewHolder(val viewDataBinding: DevbyteItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.devbyte_item
    }
}
