package com.example.grow_.fragment1.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.navigation.Navigation
import com.example.grow_.R
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.grow_.databinding.TopCurrencyLayoutBinding
import com.example.grow_.fragment1.MarketFragmentDirections
import com.example.grow_.models.CryptoCurrency
import java.security.CryptoPrimitive

class TopMarketAdapter (var context: Context, val list : List<CryptoCurrency> ): RecyclerView.Adapter<TopMarketAdapter.TopMarketViewHolder>() {

    inner class TopMarketViewHolder(view : View) : RecyclerView.ViewHolder(view){
         var binding = TopCurrencyLayoutBinding.bind(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopMarketViewHolder {
        return TopMarketViewHolder(LayoutInflater.from(context).inflate(R.layout.top_currency_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TopMarketViewHolder, position: Int) {
        val item = list[position]

        holder.binding.topCurrencyNameTextView.text = item.name

        Glide.with(context).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/" + item.id +".png"
     ).thumbnail(Glide.with(context).load(R.drawable.spinner))
            .into(holder.binding.topCurrencyImageView)

        if (item.quotes!![0].percentChange24h>0){

            holder.binding.topCurrencyChangeTextView.setTextColor(context.resources.getColor(R.color.green))
            holder.binding.topCurrencyChangeTextView.text = "+ ${String.format("%.02f", item.quotes[0].percentChange24h)} %"

        } else {

            holder.binding.topCurrencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.topCurrencyChangeTextView.text = "${String.format("%.02f", item.quotes[0].percentChange24h)} %"

        }

        holder.itemView.setOnClickListener{
            Navigation.findNavController(it).navigate(
                MarketFragmentDirections.actionMarketFragmentToDetailsFragment(item)
            )
        }
    }
}