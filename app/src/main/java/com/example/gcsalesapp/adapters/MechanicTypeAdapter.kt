package com.example.gcsalesapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.gcsalesapp.R
import com.example.gcsalesapp.data.response.MechanicType
import kotlinx.android.synthetic.main.view_mechanic_type_spinner_item.view.*

class MechanicTypeAdapter(val ctx: Context, val mechanicTypeList: List<MechanicType>) :
    ArrayAdapter<MechanicType>(ctx, 0, mechanicTypeList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, view: View?, parent: ViewGroup): View {
        lateinit var convertView: View
        if (view == null) {
            convertView = LayoutInflater.from(getContext())
                .inflate(R.layout.view_mechanic_type_spinner_item, parent, false);
        } else {
            convertView = view
        }

        val mechanicType = getItem(position)

        if (mechanicType != null) {
            convertView.mech_type_txt.text = mechanicType.MechanicVehicleTypeName
        }

        return convertView
    }

}