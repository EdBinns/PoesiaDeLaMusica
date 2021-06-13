package com.edbinns.poesiadelamusica.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.edbinns.poesiadelamusica.R
import com.edbinns.poesiadelamusica.databinding.FragmentFavoritesBinding
import com.edbinns.poesiadelamusica.databinding.ItemFavoriteBinding
import com.edbinns.poesiadelamusica.models.Favorites
import com.edbinns.poesiadelamusica.models.Phrases
import com.edbinns.poesiadelamusica.network.firebase.FirestoreService
import com.edbinns.poesiadelamusica.network.repositorys.PhrasesRespository
import com.edbinns.poesiadelamusica.network.room.toPhrasesList
import com.edbinns.poesiadelamusica.usecases.*
import com.edbinns.poesiadelamusica.view.Utils.copyToClipboard
import com.edbinns.poesiadelamusica.view.Utils.showLongMessage
import com.edbinns.poesiadelamusica.view.adapters.BindingFavoritesListener
import com.edbinns.poesiadelamusica.view.adapters.FavoritesAdapter
import com.edbinns.poesiadelamusica.view.adapters.ItemClickListener
import com.edbinns.poesiadelamusica.viewmodel.FavoritesViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


class FavoritesFragment : Fragment(), ItemClickListener<Phrases>, BindingFavoritesListener{

    private var _binding: FragmentFavoritesBinding? = null




    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private val firestoreService: FirestoreService by lazy {
        FirestoreService(FirebaseFirestore.getInstance())
    }

    private val phrasesRespository: PhrasesRespository by lazy {
        PhrasesRespository(firestoreService)
    }
    private val getPhraseUpdate: GetPhraseUpdate by lazy {
        GetPhraseUpdate(phrasesRespository)
    }

    private val listenUpdate: ListenUpdate by lazy {
        ListenUpdate(phrasesRespository)
    }
    private val favoritesAdapter: FavoritesAdapter by lazy {
        FavoritesAdapter(this,this)
    }

    private val manager: LinearLayoutManager by lazy {
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
    private val favoritesViewModel: FavoritesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        binding.swipeContainerFavorites.setOnRefreshListener {
            favoritesAdapter.deleteData()
            favoritesViewModel.listenUpdate(listenUpdate)
            observe()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.rvFavorite) {
            layoutManager = manager
            adapter = favoritesAdapter
        }

        showLoader()
        favoritesViewModel.listenUpdate(listenUpdate)
        observe()
    }

    override fun onCLickListener(data: Phrases) {
       println("Favorito click : $data")
    }

    private fun observe(){
        favoritesViewModel.favoritesList.observe(viewLifecycleOwner, Observer { list ->
                println("Lista de favoritos $list")
                favoritesAdapter.updateData( list.toPhrasesList())
                hideLoader()
        })

        favoritesViewModel.getUseCase(getPhraseUpdate).observe(viewLifecycleOwner, Observer { phrase ->
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

    override fun bindingListener(binding: ItemFavoriteBinding, data: Phrases) {
        binding.fabFav.setOnClickListener {
            favoritesViewModel.deleteFavorite(data)
        }
        binding.tvPhraseFavorite.setOnClickListener {
            with(data) {
                phrase.copyToClipboard(requireContext())
            }
        }
    }

}


