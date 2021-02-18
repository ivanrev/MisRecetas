package com.ivanr.misrecetas.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.ui.det_Notas.NotasViewModel

class FragmentDetNotas : Fragment() {

    private lateinit var notasViewModel: NotasViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        notasViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_det_notas, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        notasViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentUltimas.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            FragmentDetNotas().apply {
            }
    }
}