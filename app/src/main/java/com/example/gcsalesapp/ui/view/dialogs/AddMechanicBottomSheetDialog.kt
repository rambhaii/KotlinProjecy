package com.example.gcsalesapp.ui.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.Nullable
import com.example.gcsalesapp.R
import com.example.gcsalesapp.adapters.MechanicTypeAdapter
import com.example.gcsalesapp.data.response.MechanicType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.add_mechanic_buttom_sheet_dialog.*
import kotlinx.android.synthetic.main.add_mechanic_buttom_sheet_dialog.view.*
import kotlinx.android.synthetic.main.buttom_sheet_dialog.view.*


class AddMechanicBottomSheetDialog(val mechanicTypeList: List<MechanicType>,val callBackListener: CallBackListener) : BottomSheetDialogFragment() {

    private lateinit var mechanicTypeAdapter:MechanicTypeAdapter

    var MechanicVehicleTypeID=0
    var MechanicVehicleTypeName=""

    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(
            R.layout.add_mechanic_buttom_sheet_dialog,
            container, false
        )

        mechanicTypeAdapter = MechanicTypeAdapter(requireContext(),mechanicTypeList)

        v.mech_type_spinner.apply {
            adapter = mechanicTypeAdapter
        }

        v.mech_type_spinner.setOnItemSelectedListener(
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    // It returns the clicked item.
                    val clickedItem: MechanicType =
                        parent.getItemAtPosition(position) as MechanicType
                    MechanicVehicleTypeID = clickedItem.MechanicVehicleTypeID
                    MechanicVehicleTypeName = clickedItem.MechanicVehicleTypeName
//                    Toast.makeText(this@MainActivity, "$name selected", Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            })

        v.cancel_img.setOnClickListener {
            dismiss()
        }

        v.mech_submit_btn.setOnClickListener {
            val bundle=Bundle()
            bundle.putString("first_name",mech_first_name_txt.text.toString().trim())
            bundle.putString("last_name",mech_last_name_txt.text.toString().trim())
            bundle.putString("phone",mech_phone_txt.text.toString().trim())
            bundle.putString("pin",mech_pin_txt.text.toString().trim())
            bundle.putInt("MechanicVehicleTypeID",MechanicVehicleTypeID)
            bundle.putString("MechanicVehicleTypeName",MechanicVehicleTypeName)
            callBackListener.onCallBack(bundle)
            dismiss()
        }

        return v
    }

}