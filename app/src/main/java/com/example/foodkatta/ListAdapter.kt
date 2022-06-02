package com.example.foodkatta
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginTop
import org.w3c.dom.Text

class ListAdapter(val context:Context,val list:ArrayList<MyData>):BaseAdapter() {

    override fun getCount(): Int {
        //TODO("Not yet implemented")
        return list.size

    }

    override fun getItem(p0: Int): Any {
    //TODO("Not yet implemented")

        return p0
    }

    override fun getItemId(p0: Int): Long {
        // TODO("Not yet implemented")
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
    //TODO("Not yet implemented")
        val view:View=LayoutInflater.from(context).inflate(R.layout.row_layout,p2,false)

        val name =view.findViewById<TextView>(R.id.pname)
        val mobile = view.findViewById<TextView>(R.id.pmobile)
//        val balance = view.findViewById<TextView>(R.id.pbalance)
//        val group = view.findViewById<TextView>(R.id.pgroup)
//        val paid = view.findViewById<TextView>(R.id.ppaid)
//        val nameid = view.findViewById<TextView>(R.id.pnameid)
//        val id = view.findViewById<TextView>(R.id.pid)

        name.text=list[p0].name.toString()
        mobile.text=list[p0].contact.toString()
//        balance.text=list[p0].balance.toString()
//        group.text=list[p0].group.toString()
//        nameid.text=list[p0].nameId.toString()
//        paid.text=list[p0].paid.toString()
//        id.text=list[p0].id.toString()
        return  view
    }
}