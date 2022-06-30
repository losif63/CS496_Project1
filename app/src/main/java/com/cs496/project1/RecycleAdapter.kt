package com.example.contentprovidertest

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cs496.project1.R
import contacts.core.entities.Contact
import contacts.core.util.phoneList

class RecycleAdapter (private val dataSet: Array<Contact>) :
    RecyclerView.Adapter<RecycleAdapter.ViewHolder>()
{
    /** Provides a reference to the views for each data item.  */
    public class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        private val container: View
        internal val emailTitle: TextView
        internal val emailPreview: TextView
        internal val imageView : ImageView

        init {
//            container = view.findViewById(R.id.list_view_item_container)
            emailTitle = view.findViewById(R.id.email_title)
            emailPreview = view.findViewById(R.id.email_preview)
            imageView = view.findViewById(R.id.sender_icon)
        }
    }

    //METHOD

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.emailTitle.text = dataSet[position].displayNamePrimary
        val ps = dataSet[position].phoneList()
        if (ps.isEmpty()){
            holder.emailPreview.text = "No number"
        } else {
            holder.emailPreview.text = ps[0].number!!
        }
        if(dataSet[position].photoUri != null)
            holder.imageView.setImageURI(dataSet[position].photoUri)

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }


}