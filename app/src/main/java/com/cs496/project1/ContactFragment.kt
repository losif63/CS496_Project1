package com.cs496.project1

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contentprovidertest.RecycleAdapter
import com.example.contentprovidertest.RecycleAdapter2
import contacts.core.Contacts
import contacts.core.ContactsFields
import contacts.core.asc
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var recyclerview: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var content = inflater.inflate(R.layout.fragment_contact, container, false)
        recyclerview = content.findViewById<RecyclerView>(R.id.recycleView)
        recyclerview.layoutManager = LinearLayoutManager(inflater.context)

        val status = ContextCompat.checkSelfPermission(inflater.context, "android.permission.READ_CONTACTS")
        if(status == PackageManager.PERMISSION_GRANTED) {
            Log.d("test", "permission granted1")
            initializeContactData()
        } else {
            Log.d("test", "permission denied1")
            requestPermissions(arrayOf<String>("android.permission.READ_CONTACTS"), 100)
        }

        return content
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ContactFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContactFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("test", "permission granted2")
            initializeContactData()
        } else {
            Log.d("test", "permission denied2")

        }
    }

    private fun initializeContactData() {
        var contacts = Contacts(requireContext()).query().orderBy(
            ContactsFields.DisplayNamePrimary.asc()
        ).find()
        var myAdapter = RecycleAdapter(contacts.toTypedArray())
        recyclerview.adapter = myAdapter
        myAdapter.setOnItemClickListener(object: RecycleAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent : Intent = Intent(Intent.ACTION_VIEW)
                val id = contacts[position].id
                intent.data = Uri.parse(ContactsContract.Contacts.CONTENT_URI.toString() + "/" + id)
                startActivity(intent)
            }
        })
    }
}