package com.example.os.adapter

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView

import com.example.os.EdaData
import com.example.os.R
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.InputStream
import java.io.InputStreamReader


class RecyclerAdapter(var mList: List<EdaData>) :
    RecyclerView.Adapter<RecyclerAdapter.RecipeViewHolder>() {

    interface OnStateClickListener{
        fun onStateClick(state:EdaData, position:Int)
    }
    lateinit var onClickListener:OnStateClickListener
    lateinit var view:View
    lateinit var card : CardView

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image : ImageView = itemView.findViewById(R.id.ImageView)
        val title : TextView = itemView.findViewById(R.id.TextView)
    }

    fun setFilteredList(mList: List<EdaData>){
        this.mList = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.eda_item, parent , false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        var fileName:String = mList[position].imagePath

        var inputStream: InputStream = view.context.assets.open("images/$fileName")
        val drawable:Drawable = Drawable.createFromStream(inputStream, null)!!
        holder.image.setImageDrawable(drawable)
        holder.image.scaleType = ImageView.ScaleType.FIT_XY

        fileName = mList[position].recipePath
        inputStream = view.context.assets.open("recipes/$fileName")
        var recipe = mList[position].title + "\n\n"
        var bufferStream = BufferedReader(InputStreamReader(inputStream))

        bufferStream.lines().forEach { recipe += (it + "\n") }

        holder.title.text = recipe
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}