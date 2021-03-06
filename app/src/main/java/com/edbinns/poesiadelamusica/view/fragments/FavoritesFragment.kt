package com.edbinns.poesiadelamusica.view.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.edbinns.poesiadelamusica.R
import com.edbinns.poesiadelamusica.databinding.FragmentFavoritesBinding
import com.edbinns.poesiadelamusica.databinding.ItemFavoriteBinding
import com.edbinns.poesiadelamusica.models.Phrases
import com.edbinns.poesiadelamusica.services.firebase.FirestoreService
import com.edbinns.poesiadelamusica.services.repositorys.PhrasesRespository
import com.edbinns.poesiadelamusica.services.room.toPhrasesList
import com.edbinns.poesiadelamusica.usecases.*
import com.edbinns.poesiadelamusica.view.Utils.copyToClipboard
import com.edbinns.poesiadelamusica.view.Utils.showAnim
import com.edbinns.poesiadelamusica.view.adapters.BindingFavoritesListener
import com.edbinns.poesiadelamusica.view.adapters.FavoritesAdapter
import com.edbinns.poesiadelamusica.viewmodel.FavoritesViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FavoritesFragment  : Fragment(), BindingFavoritesListener{

    private var _binding: FragmentFavoritesBinding? = null




    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var click = false




    private val favoritesAdapter: FavoritesAdapter by lazy {
        FavoritesAdapter(this)
    }
    private val manager : LinearLayoutManager by lazy {
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
    }


    private val favoritesViewModel: FavoritesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        binding.swipeContainerFavorites.setOnRefreshListener {
            favoritesAdapter.deleteData()
            favoritesViewModel.listenUpdate()
            observe()
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.rvFavorite) {
            layoutManager = manager
            adapter = favoritesAdapter
        }

        showLoader()
        favoritesViewModel.listenUpdate()
        observe()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun observe() {
        favoritesViewModel.favoritesList.observe(viewLifecycleOwner, Observer { list ->
            if (list.isNullOrEmpty()){
                binding.imvNotFound.visibility = View.VISIBLE
                binding.tvNotFound.visibility = View.VISIBLE
//                EMPTY_LIST.showMessage(requireView(), R.color.warning_color)
            }
            favoritesAdapter.updateData(list.toPhrasesList())
            hideLoader()
        })

        favoritesViewModel.getListUpdate().observe(viewLifecycleOwner, Observer { phrase ->
            println("observer $phrase")
            favoritesAdapter.updateItem(phrase)
            favoritesViewModel.updateFavorites(phrase)
        })
    }
    private fun showLoader(){
        binding.swipeContainerFavorites.isRefreshing = true
    }

    private  fun hideLoader(){
        binding.swipeContainerFavorites.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun bindingListener(bindingItem: ItemFavoriteBinding, data: Phrases) {
        bindingItem.imvDelete.setOnClickListener {
            favoritesViewModel.deleteFavorite(data)
            binding.animViewFav.showAnim(click,R.raw.delete)

        }
        bindingItem.tvPhraseFavorite.setOnClickListener {
            with(data) {
                phrase.copyToClipboard(requireContext(),requireView())
            }
        }
    }

}


