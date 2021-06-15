package com.edbinns.poesiadelamusica.view.adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.edbinns.poesiadelamusica.R
import com.edbinns.poesiadelamusica.databinding.ItemFavoriteBinding
import com.edbinns.poesiadelamusica.databinding.ItemPhrasesBinding
import com.edbinns.poesiadelamusica.models.Phrases
import com.edbinns.poesiadelamusica.viewmodel.PhrasesViewModel
import java.util.ArrayList

class FavoritesAdapter (private val bindingFavoritesListener: BindingFavoritesListener)  : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>(){
    private var favoritesList : ArrayList<Phrases> = ArrayList()

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesAdapter.FavoriteViewHolder {
        context = parent.context
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(context), parent, false)
        return FavoriteViewHolder(binding)
    }


    override fun getItemCount(): Int = favoritesList.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val typefaceMonoglyceride =  Typeface.createFromAsset(context?.assets,"fonts/Monoglyceride.ttf")
        with(holder){
            with(favoritesList[position]){
                with(binding.tvPhraseFavorite){
                    text = phrase
                    typeface = typefaceMonoglyceride
                }
                with(binding.tvArtitsFav){
                    text = artist
                    typeface = typefaceMonoglyceride
                }
                with(  binding.tvCantLikeFav){
                    text = likes.toString()
                    typeface = typefaceMonoglyceride
                }

                bindingFavoritesListener.bindingListener(binding,this)
            }
        }
    }
    fun updateData(data: List<Phrases>) {
        deleteData()
        favoritesList.addAll(data)
        println("size Update ${favoritesList.size}")
        notifyDataSetChanged()
    }



    fun updateItem(item: Phrases) {
        println("size ${favoritesList.size}")
        for (i in 0 until favoritesList.size) {
            val phrase = favoritesList[i]
            if (phrase.uid == item.uid) {
                favoritesList[i] = item
            }
        }
        notifyDataSetChanged()
        println("sizex2 ${favoritesList.size}")
    }
    fun deleteData(){
        favoritesList.clear()
        notifyDataSetChanged()
    }

    inner  class FavoriteViewHolder( val binding : ItemFavoriteBinding): RecyclerView.ViewHolder(binding.root)


}