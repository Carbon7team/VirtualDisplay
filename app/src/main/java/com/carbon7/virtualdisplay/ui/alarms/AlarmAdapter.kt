package com.carbon7.virtualdisplay.ui.alarms

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.ListItemAlarmBinding
import com.carbon7.virtualdisplay.model.Alarm

class AlarmAdapter (private var alarms:List<Alarm>) :
    RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val binding= ListItemAlarmBinding.bind(view)

        val lblAlarms = binding.lblAlarms
    }

    fun swap(newAlarms: List<Alarm>){
        alarms=newAlarms
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.list_item_alarm,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ctx=holder.lblAlarms.context
        val a = alarms[position]
        val name = ctx.getString(ctx.resources.getIdentifier(a.code,"string", ctx.packageName))

        holder.lblAlarms.text= "%s: %s".format(a.code, name)
        holder.lblAlarms.setTextColor(when(a.level){
            Alarm.Level.NONE -> Color.GRAY
            Alarm.Level.WARNING -> Color.rgb(255,165,0)
            Alarm.Level.CRITICAL -> Color.RED
        })
    }

    override fun getItemCount() = alarms.size


}