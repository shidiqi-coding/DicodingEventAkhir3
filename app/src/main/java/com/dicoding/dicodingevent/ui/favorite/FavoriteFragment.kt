package com.dicoding.dicodingevent.ui.favorite

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.FavoriteEventAdapter
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.FragmentFavoriteBinding
import com.dicoding.dicodingevent.ui.ViewModelFactory

import com.dicoding.dicodingevent.ui.detail.DetailActivity

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FavoriteEventAdapter

    private val viewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        adapter = FavoriteEventAdapter { eventId ->
            val intent = Intent(requireContext() , DetailActivity::class.java).apply {
                putExtra("EVENT_ID" , eventId)
            }
            startActivity(intent)
        }

        binding.rvFavoriteEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavoriteEvents.adapter = adapter

        viewModel.getFavoritedEvents().observe(viewLifecycleOwner) { favorites ->
            favorites.let { list ->
                val items = list.map { favorite ->
                    ListEventsItem(
                        id = favorite.id.toInt() ,
                        name = favorite.name ,
                        imageLogo = favorite.mediaCover ?: "" ,
                        summary = "" ,
                        category = "" ,
                        cityName = "" ,
                        description = "" ,
                        beginTime = "" ,
                        endTime = "" ,
                        link = "" ,
                        mediaCover = favorite.mediaCover ?: "" ,
                        ownerName = "" ,
                        quota = 0 ,
                        registrants = 0
                    )
                }
                adapter.submitList(items)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
