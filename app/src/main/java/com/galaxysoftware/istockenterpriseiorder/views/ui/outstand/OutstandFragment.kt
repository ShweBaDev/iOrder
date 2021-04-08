package com.galaxysoftware.istockenterpriseiorder.views.ui.outstand

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.galaxysoftware.istockenterpriseiorder.R
import com.galaxysoftware.istockenterpriseiorder.dao.SqlUtilities
import com.galaxysoftware.istockenterpriseiorder.databinding.FragmentOutstandBinding
import com.galaxysoftware.istockenterpriseiorder.models.CustOutstand
import com.galaxysoftware.istockenterpriseiorder.utils.Utility
import com.galaxysoftware.istockenterpriseiorder.utils.Utility.showProgressBar
import com.galaxysoftware.istockenterpriseiorder.views.BaseRecyclerViewAdapter
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.SimpleDateFormat


class OutstandFragment : Fragment() {
  private lateinit var outstandViewModel: OutstandViewModel
    private lateinit var oustandRecyclerView: RecyclerView
    private lateinit var outstandRecyclerAdapter: BaseRecyclerViewAdapter<CustOutstand>
    lateinit var sqlUtilities: SqlUtilities
    private lateinit var binding: FragmentOutstandBinding
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    var currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance()
    var decimalFormatSymbols:DecimalFormatSymbols = (currencyFormat as DecimalFormat).decimalFormatSymbols



    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        sqlUtilities = SqlUtilities(container?.context!!)
        outstandViewModel = ViewModelProvider(this).get(OutstandViewModel::class.java)

        binding = FragmentOutstandBinding.inflate(layoutInflater)
        val view = binding.root

        oustandRecyclerView= binding.outstandRecyclerView
        oustandRecyclerView.layoutManager = LinearLayoutManager(container?.context!!)
        setAdapter()
        setData()

        val textView: TextView =binding.txtTotalamount
        outstandViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text =outstandViewModel.CurrencyFormat(it.toDouble())
        })

        return view

    }


    private fun setAdapter() {

        outstandRecyclerAdapter = BaseRecyclerViewAdapter(R.layout.outstand_list) { item, view -> bindViewHolder(item, view) }
        oustandRecyclerView.adapter = outstandRecyclerAdapter

    }

    private fun setData(){
        context?.showProgressBar()
        outstandViewModel.loadData().observe(this, Observer<List<CustOutstand>> { CusOustlistList ->
            outstandViewModel.totalresult = CusOustlistList.toMutableList()[0].amount.toString()
            outstandRecyclerAdapter.setItem(CusOustlistList.toMutableList())
            Utility.hideProgressBar()
        })

    }

    private fun bindViewHolder(custOutstand: CustOutstand, view: View) {

        var textdate = view.findViewById<TextView>(R.id.txt_date)
        var textinvoicetype = view.findViewById<TextView>(R.id.txt_invoicetype)
        var textdocid = view.findViewById<TextView>(R.id.txt_invoiceno)
        var textamount = view.findViewById<TextView>(R.id.txt_amount)

        textdate.text = dateFormat.format(custOutstand.date).toString()
        textinvoicetype.text = custOutstand.invoicetype
        textdocid.text = custOutstand.docid
        textamount.text = outstandViewModel.CurrencyFormat(custOutstand.amount)

    }

}