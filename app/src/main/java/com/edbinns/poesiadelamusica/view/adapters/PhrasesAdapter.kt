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
import androidx.recyclerview.widget.RecyclerView
import com.edbinns.poesiadelamusica.R
import com.edbinns.poesiadelamusica.databinding.FragmentPhrasesDialogBinding
import com.edbinns.poesiadelamusica.databinding.ItemPhrasesBinding
import com.edbinns.poesiadelamusica.models.Phrases
import com.edbinns.poesiadelamusica.viewmodel.PhrasesViewModel
import java.util.ArrayList

class PhrasesAdapter(private val phrasesListener: ItemClickListener<Phrases>) : RecyclerView.Adapter<PhrasesAdapter.PhrasesViewHolder>() {

    private val phrasesList : ArrayList<Phrases> = ArrayList()

    private var context: Context? = null

    private var clicked : Boolean = false

    private var viewModel : PhrasesViewModel? = null

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim)
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.rotate_close_anim)
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim)
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim)
    }
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
                binding.fab.setOnClickListener {
                    onAddButtonClick(binding)
                }

                binding.fabLike.setOnClickListener {
                    viewModel?.toLike(this)
                }
                phrasesListener.onCLickListener(this)
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

    fun setViewModel(viewModel: PhrasesViewModel){
        this.viewModel = viewModel
    }

    private fun onAddButtonClick(binding: ItemPhrasesBinding) {
        setVisibility(clicked,binding)
        setAnimation(clicked,binding)
        setClickable(clicked,binding)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean, binding: ItemPhrasesBinding) {
        with(binding) {
            if (!clicked) {
                fabLike.startAnimation(fromBottom)
                fabSend.startAnimation(fromBottom)
                binding.fabFavorite.startAnimation(fromBottom)
                binding.fab.startAnimation(rotateOpen)
            } else {
                fabLike.startAnimation(toBottom)
                fabSend.startAnimation(toBottom)
                fabFavorite.startAnimation(toBottom)
                fab.startAnimation(rotateClose)
            }
        }

    }

    private fun setVisibility(clicked: Boolean, binding: ItemPhrasesBinding) {
        with(binding) {
            if (!clicked) {
                fabLike.visibility = View.VISIBLE
                fabSend.visibility = View.VISIBLE
                fabFavorite.visibility = View.VISIBLE
            } else {
                fabLike.visibility = View.INVISIBLE
                fabSend.visibility = View.INVISIBLE
                fabFavorite.visibility = View.INVISIBLE
            }
        }

    }

    private fun setClickable(clicked: Boolean, binding: ItemPhrasesBinding) {
        with(binding) {
            if (!clicked) {
                fabLike.isClickable = true
                fabFavorite.isClickable = true
                fabSend.isClickable = true
            } else {
                fabLike.isClickable = false
                fabFavorite.isClickable = false
                fabSend.isClickable = false
            }
        }

    }


   inner  class PhrasesViewHolder( val binding : ItemPhrasesBinding): RecyclerView.ViewHolder(binding.root)
}