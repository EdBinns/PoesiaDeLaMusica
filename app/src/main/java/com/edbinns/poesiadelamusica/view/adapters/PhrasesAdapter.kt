package com.edbinns.poesiadelamusica.view.adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.edbinns.poesiadelamusica.R
import com.edbinns.poesiadelamusica.databinding.FragmentPhrasesDialogBinding
import com.edbinns.poesiadelamusica.databinding.ItemPhrasesBinding
import com.edbinns.poesiadelamusica.models.Phrases
import com.edbinns.poesiadelamusica.viewmodel.PhrasesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

class PhrasesAdapter(private val bindingPhraseListener: BindingPhraseListener) : RecyclerView.Adapter<PhrasesAdapter.PhrasesViewHolder>() {

    private var phrasesList : ArrayList<Phrases> = ArrayList()

    private var context: Context? = null





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhrasesViewHolder {
        context = parent.context
        val binding = ItemPhrasesBinding.inflate(LayoutInflater.from(context), parent, false)
        return PhrasesViewHolder(binding)
    }

    override fun getItemCount(): Int = phrasesList.size

    override fun onBindViewHolder(holder: PhrasesViewHolder, position: Int) {
        val typefaceMonoglyceride =  Typeface.createFromAsset(context?.assets,"fonts/Monoglyceride.ttf")

        with(holder){
            with(phrasesList[position]){
                with(binding.tvPhrase){
                    text = phrase
                    typeface = typefaceMonoglyceride
                }
                with(binding.tvArtits){
                    text = artist
                    typeface = typefaceMonoglyceride
                }
                with(  binding.tvCantLike){
                    text = likes.toString()
                    typeface = typefaceMonoglyceride
                }

                bindingPhraseListener.setAnimInButtons(binding,this)
            }
        }
    }

    fun updateData(data: List<Phrases>) {
        phrasesList.addAll(data)
        notifyDataSetChanged()
    }

    fun updateItem(item: Phrases) {
        for (i in 0 until phrasesList.size) {
            val phrase = phrasesList[i]
            if (phrase.uid == item.uid) {
                phrasesList[i] = item
            }
        }
        notifyDataSetChanged()
    }

    fun deleteData(){
        phrasesList.clear()
        notifyDataSetChanged()
    }




   inner  class PhrasesViewHolder( val binding : ItemPhrasesBinding): RecyclerView.ViewHolder(binding.root)
}