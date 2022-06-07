package com.example.musicplayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.databinding.FragmentNowPlayingBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class NowPlayingFragment : Fragment() {
    private var _binding : FragmentNowPlayingBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NowPlayingAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var bottomNav : BottomNavigationView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNowPlayingBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNav = (activity as MainActivity).bottomNav
        bottomNav.visibility = View.VISIBLE

        //initializing recyclerview
        recyclerView = binding.recyclerView
        setUpRecyclerView()

        //calling my tractsitem inside fragment been used
        val list = TrackDataSet.createDataset()

        adapter.differ.submitList(list)

        //navigating to second fragment
        binding.continuePlaying.root.setOnClickListener {
            findNavController().navigate(R.id.playListFragment)
        }
    }
    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = NowPlayingAdapter()
        recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}