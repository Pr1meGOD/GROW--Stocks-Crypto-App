package com.example.grow_.fragment1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.grow_.R
import com.example.grow_.databinding.FragmentDetailsBinding
import com.example.grow_.models.CryptoCurrency
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DetailsFragment : Fragment() {

    lateinit var binding : FragmentDetailsBinding
    private val item : DetailsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(layoutInflater)

        val data : CryptoCurrency = item.data!!

        setUpDetails(data)

        loadChart(data)

        setButtonOnClick(data)

        addToWatchList(data)


        return binding.root
    }

    var watchList : ArrayList<String>? = null
    var watchListisChecked = false

    private fun addToWatchList(data: CryptoCurrency) {
        readData()

        watchListisChecked = if (watchList!!.contains(data.symbol)){
            binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
            true
        } else{
            binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
            false
        }
        binding.addWatchlistButton.setOnClickListener {
            watchListisChecked =
                if (!watchListisChecked){

                    if (!watchList!!.contains(data.symbol)){
                        watchList!!.add(data.symbol)

                    }
                    storeData()
                    binding.addWatchlistButton.setImageResource(R.drawable.ic_star)

                   true
                } else {
                    binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
                    watchList!!.remove(data.symbol)
                    storeData()
                    false
                }

        }


    }

    private fun storeData(){
        val SharedPreferences = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE)
        val editor = SharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(watchList)
        editor.putString("watchlist", json)
        editor.apply()
    }


    private fun readData() {
        val SharedPreferences = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = SharedPreferences.getString("watchlist", ArrayList<String>().toString())
        val type = object : TypeToken<ArrayList<String>>(){}.type
        watchList = gson.fromJson(json, type)
    }

    private fun setButtonOnClick(data: CryptoCurrency) {

        val oneMonth = binding.button
        val oneWeek = binding.button1
        val oneDay = binding.button2
        val fourHour = binding.button3
        val oneHour = binding.button4
        val fifteenMinute = binding.button5

        val clickListener = View.OnClickListener {
            when (it.id){
                fifteenMinute.id -> loadChartData(it, "15", item, oneMonth, oneHour, oneWeek, oneDay, fourHour)
                oneHour.id -> loadChartData(it, "1H", item, oneMonth, fifteenMinute, oneWeek, oneDay, fourHour)
                fourHour.id -> loadChartData(it, "4H", item, oneMonth, oneHour, oneWeek, oneDay, fifteenMinute)
                oneDay.id -> loadChartData(it, "D", item, oneMonth, oneHour, oneWeek, fifteenMinute, fourHour)
                oneWeek.id -> loadChartData(it, "W", item, oneMonth, oneHour, fifteenMinute, oneDay, fourHour)
                oneMonth.id -> loadChartData(it, "M", item, fifteenMinute, oneHour, oneWeek, oneDay, fourHour)

            }
        }
        fifteenMinute.setOnClickListener(clickListener)
        oneHour.setOnClickListener(clickListener)
        fourHour.setOnClickListener(clickListener)
        oneDay.setOnClickListener(clickListener)
        oneWeek.setOnClickListener(clickListener)
        oneMonth.setOnClickListener(clickListener)
    }

    private fun loadChartData(
        it: View?,
        s: String,
        item: DetailsFragmentArgs,
        oneMonth: AppCompatButton,
        oneHour: AppCompatButton,
        oneWeek: AppCompatButton,
        oneDay: AppCompatButton,
        fourHour: AppCompatButton
    ) {

        disableButton(oneMonth, oneHour, oneWeek, oneDay, fourHour)
        it!!.setBackgroundResource(R.drawable.active_button)
        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        binding.detaillChartWebView.loadUrl(

            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + item.data!!.symbol
                .toString() + "USD&interval="+s+"&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )
    }
    private fun disableButton(oneDay: AppCompatButton, oneMonth: AppCompatButton, oneWeek: AppCompatButton, fourHour: AppCompatButton, oneHour: AppCompatButton) {
        oneDay.background = null
        oneMonth.background = null
        oneWeek.background = null
        fourHour.background = null
        oneHour.background = null
    }
    private fun loadChart(item: CryptoCurrency) {
        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        binding.detaillChartWebView.loadUrl(

            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + item.symbol
                .toString() + "USD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"

        )
    }

    private fun setUpDetails(data: CryptoCurrency) {
           binding.detailSymbolTextView.text = data.symbol


        Glide.with(requireContext()).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/" + data.id +".png"
        ).thumbnail(Glide.with(requireContext()).load(R.drawable.spinner))
            .into(binding.detailImageView)


        binding.detailPriceTextView.text = "${String.format("$%.4f", data.quotes[0].price)}"

        if (data.quotes!![0].percentChange24h>0){

            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.green))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_up)
            binding.detailChangeTextView.text = "+ ${String.format("%.02f", data.quotes[0].percentChange24h)} %"

        } else {

            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.red))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_down)
            binding.detailChangeTextView.text = "${String.format("%.02f", data.quotes[0].percentChange24h)} %"

        }

    }
}