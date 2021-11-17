package com.edbinns.poesiadelamusica.view.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
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
import com.edbinns.poesiadelamusica.services.firebase.FirestoreService
import com.edbinns.poesiadelamusica.services.repositorys.PhrasesRespository
import com.edbinns.poesiadelamusica.usecases.GetListPhrasesUseCase
import com.edbinns.poesiadelamusica.usecases.GetPhraseUpdate
import com.edbinns.poesiadelamusica.usecases.SearchPhrase
import com.edbinns.poesiadelamusica.usecases.ToLiKE
import com.edbinns.poesiadelamusica.view.Utils.copyToClipboard
import com.edbinns.poesiadelamusica.view.Utils.descapitalizeAllWords
import com.edbinns.poesiadelamusica.view.Utils.showAnim
import com.edbinns.poesiadelamusica.view.adapters.BindingPhraseListener
import com.edbinns.poesiadelamusica.view.adapters.PhrasesAdapter
import com.edbinns.poesiadelamusica.viewmodel.FavoritesViewModel
import com.edbinns.poesiadelamusica.viewmodel.PhrasesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PhrasesDialogFragment  : DialogFragment(), BindingPhraseListener {



    private var _binding: FragmentPhrasesDialogBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var category : String
    private lateinit var favoriteViewModel: FavoritesViewModel
    private var click = false
    private var filter = ""

    private val phrasesAdapter : PhrasesAdapter by lazy {
        PhrasesAdapter(this)
    }

    private val manager : LinearLayoutManager by lazy {
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
    }

    private val phrasesViewModel:PhrasesViewModel by viewModels()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPhrasesDialogBinding.inflate(inflater,container,false)

        category = arguments?.getSerializable("category") as String

        favoriteViewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)


        binding.swipeContainer.setOnRefreshListener {
            phrasesAdapter.deleteData()
            if(filter.isEmpty())
                phrasesViewModel.setCategory(category)
            else
                phrasesViewModel.search(filter, category)
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

        searchPhrase()
        showLoader()
        phrasesViewModel.setCategory(category)
        observe()
    }

    private fun observe() {
        phrasesViewModel.getListPhases().observe(this, Observer { list ->

            phrasesAdapter.updateData(list)
            hideLoader()

        })

        phrasesViewModel.getListUpdate().observe(this, Observer { item ->
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
        observerFavoriteList(bindingItem, data)
    }

    private fun observerFavoriteList(bindingItem: ItemPhrasesBinding, data: Phrases) {
        favoriteViewModel.favoritesList.observe(this, Observer { list ->
            for (item in list) {
                if (item.uid == data.uid) {
                    bindingItem.imvFavorite.setImageResource(R.drawable.ic_bookmark)
                }
            }
        })
    }


    /**
     * Función que se ejecuta cuando el usuario quiere realizar una busqueda de algún libro
     * en especifico
     */
    private fun searchPhrase() {
        with(binding) {
            searchPhrase.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                @SuppressLint("NewApi")
                override fun onQueryTextSubmit(query: String?): Boolean {
                    hideKeyboard()
                    searchPhrase.clearFocus()
                    search(query)
                    return false
                }

                @RequiresApi(Build.VERSION_CODES.M)
                override fun onQueryTextChange(newText: String?): Boolean {
                    search(newText)
                    return true
                }
            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun search(query:String?){

        showLoader()
        phrasesAdapter.deleteData()
        //obtener lo que el mae escribe
        query?.let{
            if (query.isEmpty()) {
                filter = ""
                println("filter $filter")
                phrasesViewModel.setCategory(category)
            } else {
                filter = query
                phrasesViewModel.search( filter.descapitalizeAllWords(), category)
            }
            hideLoader()
        }

    }
    /**
     * Función que se ejecuta apenas el usuario inicia una busqueda, esta oculta el teclado
     */
    private fun hideKeyboard() {
        if (view != null) {
            //Aquí esta la magia
            val input =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            input.hideSoftInputFromWindow(requireView().windowToken, 0)
            val menu: BottomNavigationView = requireActivity().findViewById(R.id.bnvMenu)
            menu.visibility =  View.VISIBLE
        }
    }

}