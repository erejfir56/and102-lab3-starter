package com.codepath.nationalparks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.codepath.nationalparks.NationalPark
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONArray

private const val API_KEY = "lhEhCt8XJrc4URNLNbkCCHu7bGmXEJu2k22qjlnk"

class NationalParksFragment : Fragment(), OnListFragmentInteractionListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_national_parks_list, container, false)
        val progressBar = view.findViewById<View>(R.id.progress) as ContentLoadingProgressBar
        val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        val context = view.context

        recyclerView.layoutManager = LinearLayoutManager(context)

        updateAdapter(progressBar, recyclerView)
        return view
    }

    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.show()

        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = API_KEY

        client[
            "https://developer.nps.gov/api/v1/parks",
            params,
            object : JsonHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers,
                    json: JsonHttpResponseHandler.JSON
                ) {
                    progressBar.hide()

                    val dataJSON = json.jsonObject.get("data") as JSONArray
                    val parksRawJSON = dataJSON.toString()

                    val gson = Gson()
                    val arrayParkType = object : TypeToken<List<NationalPark>>() {}.type
                    val models: List<NationalPark> = gson.fromJson(parksRawJSON, arrayParkType)

                    recyclerView.adapter = NationalParksRecyclerViewAdapter(models, this@NationalParksFragment)

                    Log.d("NationalParksFragment", "response successful")
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    errorResponse: String,
                    t: Throwable?
                ) {
                    progressBar.hide()

                    t?.message?.let {
                        Log.e("NationalParksFragment", errorResponse)
                    }
                }
            }
        ]
    }

    override fun onItemClick(item: NationalPark) {
        Toast.makeText(context, "Park Name: " + item.name, Toast.LENGTH_LONG).show()
    }
}