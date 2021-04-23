package com.edbinns.poesiadelamusica.view.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.edbinns.poesiadelamusica.databinding.FragmentPhrasesDialogBinding
import com.edbinns.poesiadelamusica.models.Phrases
import com.edbinns.poesiadelamusica.network.firebase.FirestoreService
import com.edbinns.poesiadelamusica.network.repositorys.PhrasesRespository
import com.edbinns.poesiadelamusica.usecases.GetListPhrasesUseCase
import com.edbinns.poesiadelamusica.usecases.GetPhraseUpdate
import com.edbinns.poesiadelamusica.usecases.ToLiKE
import com.edbinns.poesiadelamusica.view.adapters.ItemClickListener
import com.edbinns.poesiadelamusica.view.adapters.PhrasesAdapter
import com.edbinns.poesiadelamusica.viewmodel.PhrasesViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.firestore.FirebaseFirestore


class PhrasesDialogFragment : DialogFragment(),ItemClickListener<Phrases> {

    private var _binding: FragmentPhrasesDialogBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var category : String

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

        phrasesViewModel.getUseCase(toLikeUseCase)
        binding.swipeContainer.setOnRefreshListener {
            phrasesViewModel.getUseCase(getListPhrasesUseCase,category)

            observe()
            binding.reloadButton.visibility = View.GONE
        }

        phrasesAdapter.setViewModel(phrasesViewModel)
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

    private fun observe(){
        phrasesViewModel.getListPhases().observe(this, Observer { list ->
            println("observer $list")
            phrasesAdapter.updateData(list)
            hideLoader()
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

    override fun onCLickListener(data: Phrases) {
       println("click en frase $data")
    }
}