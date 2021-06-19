package com.edbinns.poesiadelamusica.view.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.edbinns.poesiadelamusica.R
import com.edbinns.poesiadelamusica.databinding.FragmentPhrasesDialogBinding
import com.edbinns.poesiadelamusica.databinding.ItemPhrasesBinding
import com.edbinns.poesiadelamusica.models.Phrases
import com.edbinns.poesiadelamusica.network.firebase.FirestoreService
import com.edbinns.poesiadelamusica.network.repositorys.PhrasesRespository
import com.edbinns.poesiadelamusica.usecases.GetListPhrasesUseCase
import com.edbinns.poesiadelamusica.usecases.GetPhraseUpdate
import com.edbinns.poesiadelamusica.usecases.ToLiKE
import com.edbinns.poesiadelamusica.view.Utils.copyToClipboard
import com.edbinns.poesiadelamusica.view.Utils.showAnim
import com.edbinns.poesiadelamusica.view.adapters.BindingPhraseListener
import com.edbinns.poesiadelamusica.view.adapters.PhrasesAdapter
import com.edbinns.poesiadelamusica.viewmodel.FavoritesViewModel
import com.edbinns.poesiadelamusica.viewmodel.PhrasesViewModel
import com.google.firebase.firestore.FirebaseFirestore


class PhrasesDialogFragment : DialogFragment(), BindingPhraseListener {



    private var _binding: FragmentPhrasesDialogBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var category : String
    private lateinit var favoriteViewModel: FavoritesViewModel
    private var click = false

    private val firestoreService: FirestoreService by lazy {
        FirestoreService(FirebaseFirestore.getInstance())
    }



    private val phrasesRespository : PhrasesRespository by lazy {
        PhrasesRespository(firestoreService)
    }

    private val getListPhrasesUseCase : GetListPhrasesUseCase by lazy {
        GetListPhrasesUseCase(phrasesRespository)
    }

    private val toLikeUseCase : ToLiKE by lazy {
        ToLiKE(phrasesRespository)
    }

    private val getPhraseUpdate : GetPhraseUpdate by lazy {
        GetPhraseUpdate(phrasesRespository)
    }

    private val phrasesAdapter : PhrasesAdapter by lazy {
        PhrasesAdapter(this)
    }
    private val manager: LinearLayoutManager by lazy {
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
    private val phrasesViewModel:PhrasesViewModel by viewModels()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPhrasesDialogBinding.inflate(inflater,container,false)

        category = arguments?.getSerializable("category") as String

        favoriteViewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)
        phrasesViewModel.getUseCase(toLikeUseCase)

        binding.swipeContainer.setOnRefreshListener {
            phrasesAdapter.deleteData()
            phrasesViewModel.getUseCase(getListPhrasesUseCase,category)
            binding.reloadButton.visibility = View.GONE
        }


        listener()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.rvPhrases) {
            layoutManager = manager
            adapter = phrasesAdapter
        }

        showLoader()
        phrasesViewModel.getUseCase(getListPhrasesUseCase, category)
        observe()
    }

    private fun observe() {
        phrasesViewModel.getListPhases().observe(this, Observer { list ->

            phrasesAdapter.updateData(list)
            hideLoader()

        })

        phrasesViewModel.getUseCase(getPhraseUpdate).observe(this, Observer { item ->
            phrasesAdapter.updateItem(item)
            favoriteViewModel.updateFavorites(item)
        })
    }

    private fun showLoader(){
        binding.swipeContainer.isRefreshing = true
    }

    private  fun hideLoader(){
        binding.swipeContainer.isRefreshing = false
    }
    private fun listener(){
        binding.fabBack.setOnClickListener {
            closeDialog()
        }
    }
    private fun closeDialog(){
        getFragmentManager()?.beginTransaction()?.remove(this)?.commit();
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun setAnimInButtons(bindingItem :ItemPhrasesBinding, data: Phrases) {

        bindingItem.imvLIKE.setOnClickListener {
            phrasesViewModel.toLike(data)
            binding.animView.showAnim(click,R.raw.like)
        }
        bindingItem.imvFavorite.setOnClickListener {
            favoriteViewModel.addFavorite(data)
            binding.animView.showAnim(click,R.raw.bookmark)
        }
        bindingItem.tvPhrase.setOnClickListener {
            with(data) {
                phrase.copyToClipboard(requireContext(),requireView())
            }
        }
    }





}