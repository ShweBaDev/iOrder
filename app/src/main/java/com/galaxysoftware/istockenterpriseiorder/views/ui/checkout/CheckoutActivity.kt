package com.galaxysoftware.istockenterpriseiorder.views.ui.checkout

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.galaxysoftware.istockenterpriseiorder.BaseActivity
import com.galaxysoftware.istockenterpriseiorder.R
import com.galaxysoftware.istockenterpriseiorder.databinding.ActivityCheckoutBinding
import com.galaxysoftware.istockenterpriseiorder.models.ShoppingCartItem
import com.galaxysoftware.istockenterpriseiorder.models.VoucherId
import com.galaxysoftware.istockenterpriseiorder.utils.Utility
import com.galaxysoftware.istockenterpriseiorder.utils.Utility.showProgressBar
import com.galaxysoftware.istockenterpriseiorder.views.BaseRecyclerViewAdapter
import com.galaxysoftware.istockenterpriseiorder.views.RecyclerItemTouchHelper
import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormat


class CheckoutActivity : BaseActivity() {

    lateinit var binding: ActivityCheckoutBinding
    lateinit var checkoutViewModel: CheckoutViewModel
    lateinit var checkoutRecyclerView: RecyclerView
    private lateinit var checkoutRecyclerAdapter: BaseRecyclerViewAdapter<ShoppingCartItem>


    lateinit var docId: String
    lateinit var currentdocId: MutableLiveData<List<VoucherId>>
    lateinit var generateddocId: MutableLiveData<VoucherId>

    private var decimalPriceFormatter = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        val vmFactory = CheckoutViewModelFactory(application)
        checkoutViewModel = ViewModelProvider(this, vmFactory).get(CheckoutViewModel::class.java)


        checkoutRecyclerView = binding.checkoutRecyclerView
        checkoutRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        checkoutRecyclerView.itemAnimator = DefaultItemAnimator()
        checkoutRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )




        setAdapter()
        setData()
        bindUI()

        val view = binding.root
        setContentView(view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        checkoutViewModel.cardItems?.observe(this, Observer { it ->

            binding.txtTotalQty.text = it.sumByDouble { it?.unitqty ?: 1.0 }.toString()
            binding.txtTotalAmount.text = decimalPriceFormatter.format(it.sumByDouble {
                (it?.saleprice ?: 0.0) * (it?.unitqty ?: 1.0)
            })
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        finish()
    }

    fun setAdapter() {

        checkoutRecyclerAdapter =
            BaseRecyclerViewAdapter(R.layout.list_item_checkout) { item, view ->
                bindViewHolder(
                    item,
                    view
                )
            }
        checkoutRecyclerView.adapter = checkoutRecyclerAdapter

        val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
            RecyclerItemTouchHelper<ShoppingCartItem>(
                0,
                ItemTouchHelper.LEFT
            ) { viewHolder, direction, position -> onSwiped(viewHolder, direction, position) }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(checkoutRecyclerView)

    }

    fun setData() {
        showProgressBar()
        checkoutViewModel.loadData()?.observe(this, Observer {
            checkoutRecyclerAdapter.setItem(it.toMutableList())
            Utility.hideProgressBar()
        })

    }

    fun bindUI() {
        currentdocId = MutableLiveData()
        generateddocId = MutableLiveData()

        getCurrentDocId()

        currentdocId.observe(this, Observer {
            if (it.count() == 0) {
                checkoutViewModel.getDocId("INV-").observe(this, Observer {
                    generateddocId.value = it
                })
            } else {

                docId = it[0].voucherno
                binding.txtVouNo.text = docId
            }
        })

        generateddocId.observe(this, Observer {
            sqlUtilities.saveDocId(it)
            docId = it.voucherno
            binding.txtVouNo.text = docId
        })

        binding.btnAddOrder.setOnClickListener {
            confirmOrder()
        }
    }

    private fun getCurrentDocId() {

        sqlUtilities.getDocId()?.observe(this, Observer {
            currentdocId.value = it
        })
    }

    private fun confirmOrder() {
        sqlUtilities.deleteCartItems()
        sqlUtilities.deleteDocId()
        finish()
    }

    private fun bindViewHolder(checkoutItem: ShoppingCartItem, view: View) {

        var txtdescription = view.findViewById<TextView>(R.id.txtDescription)
        var txtamount = view.findViewById<TextView>(R.id.txtAmount)
        var txtQty = view.findViewById<TextView>(R.id.txtQty)
        var txtPrice = view.findViewById<TextView>(R.id.txtPrice)
        var txtRemark = view.findViewById<TextView>(R.id.txtRemark)

        var saleprice: Double? = checkoutItem.saleprice ?: 0.0
        var qty: Double? = checkoutItem.unitqty ?: 1.0
        var amount = saleprice?.times(qty!!)

        txtdescription.text = checkoutItem.description
        txtamount.text = decimalPriceFormatter.format(amount)
        txtPrice.text = decimalPriceFormatter.format(saleprice)
        txtQty.text = qty.toString() + "  x  "
        if (checkoutItem.remark != null && checkoutItem.remark != "")
            txtRemark.text = checkoutItem.remark


        view.setOnClickListener {
            detailDialog(checkoutItem)
        }
    }

    private fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {

        val removeItem = checkoutRecyclerAdapter.getItem(position)
        checkoutRecyclerAdapter.removeItem(position)
        sqlUtilities.deleteCartItem(removeItem)

        checkoutViewModel.cardItems.apply {
            this?.value?.toMutableList()?.forEach {
                if (it.sr == removeItem.sr) {
                    this?.value?.toMutableList()?.remove(it)
                }
            }
        }
    }

    //added by ZYP
    private fun detailDialog(updateItem: ShoppingCartItem) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(updateItem.description)
        val detailLayout: View = layoutInflater.inflate(
            R.layout.dialog_checkout,
            null
        )
        builder.setView(detailLayout)
        val dialog: AlertDialog = builder.create()

        val txtQty: TextView = detailLayout.findViewById(R.id.dialog_txtQty)
        txtQty.text = updateItem.unitqty.toString()

        val btnPlus: Button = detailLayout.findViewById(R.id.btn_plus)
        btnPlus.setOnClickListener {
            var qty: Double = txtQty.text.toString().toDouble()
            txtQty.text = (qty + 1).toString()
        }

        val btnMinus: Button = detailLayout.findViewById(R.id.btn_minus)
        btnMinus.setOnClickListener {
            var qty: Double = txtQty.text.toString().toDouble()
            if (qty > 1.0)
                txtQty.text = (qty - 1).toString()
        }

        val txtRemark: TextInputEditText = detailLayout.findViewById(R.id.dialog_txtRemark)
        if (updateItem.remark != null && updateItem.remark != "")
            txtRemark.setText(updateItem.remark.toString())

        val btnOk: Button = detailLayout.findViewById(R.id.btn_ok)
        btnOk.setOnClickListener {
            checkoutViewModel.cardItems.apply {
                this?.value?.toMutableList()?.forEach {
                    if (it.sr == updateItem.sr) {
                        it.unitqty = txtQty.text.toString().toDouble()
                        it.remark = txtRemark.text.toString()

                    }
                }

                this?.value?.toMutableList().let {
                    if (it != null) {
                        checkoutRecyclerAdapter.setItem(it)
                        binding.txtTotalQty.text = it.sumByDouble { it?.unitqty ?: 1.0 }.toString()
                        binding.txtTotalAmount.text = decimalPriceFormatter.format(it.sumByDouble {
                            (it?.saleprice ?: 0.0) * (it?.unitqty ?: 1.0)
                        })
                    }
                }
            }

            dialog.dismiss()

        }

        val btnCancel: Button = detailLayout.findViewById(R.id.btn_cancel)
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}