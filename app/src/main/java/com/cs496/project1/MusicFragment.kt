package com.cs496.project1

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs496.project1.player.MusicPlayer
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.cs496.project1.player.getInstance
import com.cs496.project1.player.getInstanceSafe
import com.cs496.project1.player.ui.MusicPlayerActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MusicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MusicFragment : Fragment() {
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
        // Inflate the layout for this fragment
        var content = inflater.inflate(R.layout.fragment_music, container, false)
        recyclerview = content.findViewById<RecyclerView>(R.id.recyclerview3)
        recyclerview.layoutManager = LinearLayoutManager(inflater.context)
        val button = content.findViewById<ExtendedFloatingActionButton>(R.id.floatingActionButton)
        button.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                val intent = Intent(recyclerview.context, MusicListActivity::class.java)
                startActivityForResult(intent, 10)
            }
        })

        val status = ContextCompat.checkSelfPermission(inflater.context, "android.permission.READ_EXTERNAL_STORAGE")
        if(status == PackageManager.PERMISSION_GRANTED) {
            Log.d("test", "permission granted")
            initializeMusicData()
        } else {
            Log.d("test", "permission denied")
            requestPermissions(arrayOf<String>("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.RECORD_AUDIO"), 100)
        }

        return content
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 10 && resultCode == RESULT_OK) {
            //Toast.makeText(recyclerview.context, data!!.getStringExtra("filePath"), Toast.LENGTH_SHORT).show()
            getInstance().add(data!!.getStringExtra("filePath")!!)
            initializeMusicData()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MusicFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MusicFragment().apply {
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
            initializeMusicData()
        } else {
            Log.d("test", "permission denied")
        }
    }

    private fun initializeMusicData() {

        val mList = getInstance().musicList
        var music_list = mList.toTypedArray()

        var myAdapter = RecycleAdapter3(music_list)
        recyclerview.adapter = myAdapter
        myAdapter.setOnItemClickListener(object: RecycleAdapter3.OnItemClickListener{
            override fun onItemClick(position: Int) {
                // Play Selected Music
                getInstance().play(position)
                val intent = Intent(recyclerview.context, MusicPlayerActivity::class.java)
                startActivity(intent)
            }
        })
    }

}