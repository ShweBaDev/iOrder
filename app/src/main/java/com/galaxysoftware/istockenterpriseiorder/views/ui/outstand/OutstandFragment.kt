package com.galaxysoftware.istockenterpriseiorder.views.ui.outstand

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.galaxysoftware.istockenterpriseiorder.R


class OutstandFragment : Fragment() {
  private lateinit var outstandViewModel: OutstandViewModel
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        outstandViewModel=ViewModelProvider(this).get(OutstandViewModel::class.java)
        val root=inflater.inflate(R.layout.fragment_outstand,container,false)
        val textView :TextView=root.findViewById(R.id.text_outstand)
        outstandViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

}