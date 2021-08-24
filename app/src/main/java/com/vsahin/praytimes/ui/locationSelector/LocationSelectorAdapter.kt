package com.vsahin.praytimes.ui.locationSelector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vsahin.praytimes.R
import com.vsahin.praytimes.data.uiModel.LocationUIModel

private const val HEADER = 0
private const val ITEM = 1

class LocationSelectorAdapter(
    private val headerText: String? = null,
    private val locations: List<LocationUIModel> = listOf(),
    private val locationClickListener: LocationClickListener? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val headerText: TextView = view.findViewById(R.id.headerText)

        fun bind(locationType: String) {
            headerText.text = locationType
        }
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val locationText: TextView = view.findViewById(R.id.locationText)

        fun bind(location: String) {
            locationText.text = location
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) HEADER else ITEM
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER -> HeaderViewHolder(
                LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.item_location_list_header, viewGroup, false)
            )
            else -> {
                val viewHolder = ItemViewHolder(
                    LayoutInflater.from(viewGroup.context)
                        .inflate(R.layout.item_location_row, viewGroup, false)
                )

                viewHolder.itemView.setOnClickListener {
                    locationClickListener?.onLocationClick(viewHolder.bindingAdapterPosition - 1)
                }

                ITEM
                return viewHolder
            }
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            HEADER -> {
                (viewHolder as HeaderViewHolder).bind(headerText ?: "")
            }
            ITEM -> {
                (viewHolder as ItemViewHolder).bind(locations[position - 1].name)
            }
        }
    }

    override fun getItemCount() = locations.size + 1

    interface LocationClickListener {
        fun onLocationClick(index: Int)
    }
}