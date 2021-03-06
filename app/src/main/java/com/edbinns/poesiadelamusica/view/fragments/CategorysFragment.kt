package com.edbinns.poesiadelamusica.view.fragments

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.edbinns.poesiadelamusica.R
import com.edbinns.poesiadelamusica.databinding.FragmentCategorysBinding


class CategorysFragment : Fragment() {

    private var _binding: FragmentCategorysBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategorysBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val typefaceMonoglyceride =  Typeface.createFromAsset(activity?.assets,"fonts/Monoglyceride.ttf")
        binding.tvElectronic.typeface = typefaceMonoglyceride
        binding.tvPOP.typeface = typefaceMonoglyceride
        binding.tvROCK.typeface = typefaceMonoglyceride
        binding.tvRap.typeface = typefaceMonoglyceride

        listener()
    }

    private fun listener(){
        binding.cvElectro.setOnClickListener {
            changeView("Electronica")
        }
        binding.cvPOP.setOnClickListener {
            changeView("Pop")
        }
        binding.cvRAP.setOnClickListener {
            changeView("Rap")
        }
        binding.cvROCK.setOnClickListener {
            changeView("Rock")
        }
    }

    private fun changeView(category:String){
        val bundle = bundleOf("category" to category)
        findNavController().navigate(R.id.navDialogPhrases, bundle)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}