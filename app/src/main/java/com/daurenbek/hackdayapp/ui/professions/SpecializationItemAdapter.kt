package com.daurenbek.hackdayapp.ui.professions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.daurenbek.hackdayapp.R
import com.daurenbek.hackdayapp.network.SpecializationModel

class SpecializationItemAdapter(
    private val fragment: Fragment,
    private val dataset: List<SpecializationModel>?
) : RecyclerView.Adapter<SpecializationItemAdapter.SpecializationItemViewHolder>() {

    class SpecializationItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val frameId: Int = View.generateViewId()
        val specializationItem: ConstraintLayout = view.findViewById(R.id.specialization_item_container) as ConstraintLayout
        init {
            specializationItem.id = frameId
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SpecializationItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.specialization_item, parent, false)

        return SpecializationItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: SpecializationItemViewHolder, position: Int) {
        holder.specializationItem.let {
            it.findViewById<TextView>(R.id.specialization_name).text = dataset?.get(position)?.name

            it.findViewById<ImageView>(R.id.more_btn).setOnClickListener {

            }
        }
    }

    override fun getItemCount(): Int {
        if (dataset != null) {
            return dataset.size
        } else
            return 0
    }
}