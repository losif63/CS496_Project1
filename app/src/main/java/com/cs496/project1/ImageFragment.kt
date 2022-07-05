package com.cs496.project1

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.CodeBoy.MediaFacer.MediaFacer
import com.CodeBoy.MediaFacer.PictureGet
import com.CodeBoy.MediaFacer.mediaHolders.pictureContent

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageFragment : Fragment() {
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
        var content = inflater.inflate(R.layout.fragment_image, container, false)
        recyclerview = content.findViewById<RecyclerView>(R.id.recyclerview2)
        recyclerview.layoutManager = GridLayoutManager(inflater.context, 3)

        val status = ContextCompat.checkSelfPermission(inflater.context, "android.permission.READ_CONTACTS")
        val status2 = ContextCompat.checkSelfPermission(inflater.context, "android.permission.READ_EXTERNAL_STORAGE")
        val status3 = ContextCompat.checkSelfPermission(inflater.context, "android.permission.RECORD_AUDIO")
        if(status == PackageManager.PERMISSION_GRANTED && status2 == PackageManager.PERMISSION_GRANTED && status3 == PackageManager.PERMISSION_GRANTED) {
            Log.d("test", "permission granted1")
            initializeImageData()
        } else {
            Log.d("test", "permission denied1")
            requestPermissions(arrayOf<String>("android.permission.READ_CONTACTS", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.RECORD_AUDIO"), 100)
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
         * @return A new instance of fragment ImageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ImageFragment().apply {
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
            Log.d("test", "permission granted")
            initializeImageData()
        } else {
            Log.d("test", "permission denied")
        }
    }

    private fun initializeImageData() {
        var allPhotos : ArrayList<pictureContent> = MediaFacer.withPictureContex(recyclerview.context).getAllPictureContents(
            PictureGet.externalContentUri)
        var photoList = allPhotos.toTypedArray()

        var myAdapter = RecycleAdapter2(photoList)
        recyclerview.adapter = myAdapter
        myAdapter.setOnItemClickListener(object: RecycleAdapter2.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                val photoURI = photoList[position].assertFileStringUri.toString()
                intent.setDataAndType(Uri.parse(photoURI), "image/*")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(intent)
            }
        })
    }
}