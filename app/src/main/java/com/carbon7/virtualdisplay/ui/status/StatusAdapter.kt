package com.carbon7.virtualdisplay.ui.status

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.ListItemStatusBinding
import com.carbon7.virtualdisplay.model.Status

class StatusAdapter(context: Context, objects: List<Status>) :
    ArrayAdapter<Status>(context, R.layout.list_item_status, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ListItemStatusBinding
        val status = getItem(position)
        var convertView = convertView

        if(convertView==null) {
            Log.d("MyApp","ConvertView == NULL")
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            binding = ListItemStatusBinding.inflate(inflater,parent,false)
            convertView=binding.root
            //    LayoutInflater.from(context).inflate(R.layout.list_item_status, parent, false)
        }else
            binding= ListItemStatusBinding.bind(convertView)

        if (status != null) {
            binding.lblStatus.text = status.code+ ": "+ context.getString(status.name)
            binding.lblStatus.setTextColor( if(status.isActive) Color.CYAN else Color.GRAY)
        }

        return convertView
    }
}