package com.codepath.nationalparks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.nationalparks.R.id

class NationalParksRecyclerViewAdapter(
    private val parks: List<NationalPark>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<NationalParksRecyclerViewAdapter.ParkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_national_park, parent, false)
        return ParkViewHolder(view)
    }

    inner class ParkViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var mItem: NationalPark? = null
        val mParkName: TextView = mView.findViewById(id.park_name) as TextView
        val mParkLocation: TextView = mView.findViewById(id.park_location) as TextView
        val mParkDescription: TextView = mView.findViewById(id.park_description) as TextView
        val mParkImage: ImageView = mView.findViewById(id.park_image) as ImageView

        override fun toString(): String {
            return mParkName.toString() + " '" + mParkDescription.text + "'"
        }
    }

    override fun onBindViewHolder(holder: ParkViewHolder, position: Int) {
        val park = parks[position]

        holder.mItem = park
        holder.mParkName.text = park.name
        holder.mParkLocation.text = park.location
        holder.mParkDescription.text = park.description

        val imageUrl = park.imageUrl
        Glide.with(holder.mView)
            .load(imageUrl)
            .centerInside()
            .into(holder.mParkImage)

        holder.mView.setOnClickListener {
            holder.mItem?.let { park ->
                mListener?.onItemClick(park)
            }
        }
    }

    override fun getItemCount(): Int {
        return parks.size
    }
}