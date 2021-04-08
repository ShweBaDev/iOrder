package com.galaxysoftware.istockenterpriseiorder.views.ui.itemdetail


import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.galaxysoftware.istockenterpriseiorder.BaseActivity
import com.galaxysoftware.istockenterpriseiorder.R
import com.galaxysoftware.istockenterpriseiorder.dao.SqlUtilities
import com.galaxysoftware.istockenterpriseiorder.databinding.ActivityItemDetailBinding
import com.galaxysoftware.istockenterpriseiorder.models.ShoppingCartItem
import com.galaxysoftware.istockenterpriseiorder.models.UnitCode
import com.galaxysoftware.istockenterpriseiorder.models.UsrCode
import com.galaxysoftware.istockenterpriseiorder.models.VoucherId
import com.galaxysoftware.istockenterpriseiorder.utils.Utility.hideProgressBar
import com.galaxysoftware.istockenterpriseiorder.utils.Utility.showProgressBar
import com.galaxysoftware.istockenterpriseiorder.views.BaseRecyclerViewAdapter
import com.google.android.material.chip.Chip
import okhttp3.internal.wait
import java.text.DecimalFormat


class ItemDetailActivity : BaseActivity() {

    private lateinit var unitRecyclerView: RecyclerView

    private lateinit var unitRecyclerAdapter: BaseRecyclerViewAdapter<UnitCode>

    private lateinit var binding: ActivityItemDetailBinding

    lateinit var itemDetailViewModel: ItemDetailViewModel

    private  lateinit var usrCode : String

    private lateinit var selectedItem : MutableLiveData<UsrCode>

    private var decimalPriceFormatter = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        itemDetailViewModel = ViewModelProvider(this).get(ItemDetailViewModel::class.java)
        val view = binding.root



        usrCode = intent.extras?.get("code").toString()

        unitRecyclerView = binding.unitRecyclerview
        unitRecyclerView.itemAnimator =DefaultItemAnimator()



        setUnitAdapter()
        getData(usrCode)
        bindUI()
        setContentView(view)

        supportActionBar?.hide()
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return true
//    }
//
//    override fun onBackPressed() {
//       finish()
//    }


    private fun getData(usrcode : String){
        selectedItem = MutableLiveData()


        showProgressBar()
        itemDetailViewModel.loadData(usrcode).observe(this, Observer { UsrCode ->
            selectedItem.value = UsrCode
            hideProgressBar()

        })


    }

    private fun bindUI(){

        binding.btnDiscard.setOnClickListener {
            finish()
        }

        binding.btnAddtoCart.setOnClickListener {
            saveCartItem()
        }

        binding.plusQty.setOnClickListener {
            increaseQty()
        }

        binding.minusQty.setOnClickListener {
            decreaseQty()
        }

        selectedItem.observe(this, Observer { it ->
            binding.description.text = it.description
            binding.price.text = decimalPriceFormatter.format(it.saleprice)
            binding.qty.text = "1"

            it.units.let {
                binding.unitName.text = it.filter { unitCode -> unitCode.unittype == 1  }.first().shortdesc
            }

            binding.amount.text = decimalPriceFormatter.format(it.saleprice)


            var imageurl : String? = "https://picsum.photos/id/237/200/300"
            binding.itemImage.load(it.imageurl) {
                placeholder(R.drawable.ic_default_image_24)
                error(R.drawable.ic_default_image_24)
            }


            unitRecyclerAdapter.setItem(it.units.toList().sortedBy { it.unittype }.toMutableList())
        })
    }

    private fun setUnitAdapter() {

        unitRecyclerAdapter = BaseRecyclerViewAdapter(R.layout.item_unit) { item, view -> bindUnitViewHolder(item, view) }
        unitRecyclerView.adapter = unitRecyclerAdapter

    }

    private fun bindUnitViewHolder(unitCode: UnitCode, view: View) {
        var btnUnit = view.findViewById<Chip>(R.id.btnUnit)
        btnUnit.text = unitCode.shortdesc

        btnUnit.setOnClickListener{
            changeUnitPrice(unitCode)
        }
    }

    private fun changeUnitPrice(unitCode: UnitCode){
        binding.price.text = unitCode.saleprice.toString()
        binding.unitName.text = unitCode.shortdesc.toString()

        val unitQty : Double = itemDetailViewModel.selectedcaritem.unitqty?:0.0
        val salePrice : Double = unitCode.saleprice ?: 0.0

        binding.amount.text = decimalPriceFormatter.format (salePrice * unitQty )

        itemDetailViewModel.selectedcaritem.unittypeid = unitCode.unittype
        itemDetailViewModel.selectedcaritem.saleprice = unitCode.saleprice

    }

    private fun saveCartItem(){
        sqlUtilities.saveCartItem(itemDetailViewModel.selectedcaritem)
        finish()
    }

    private fun increaseQty(){

        var increasedQty : Double = itemDetailViewModel.selectedcaritem.unitqty?.plus(1.0)?:0.0

        itemDetailViewModel.selectedcaritem.unitqty = increasedQty
        binding.qty.text = itemDetailViewModel.selectedcaritem.unitqty.toString()


        var itemPrice: Double = itemDetailViewModel.selectedcaritem.saleprice?:0.0


        val itemAmount: Double =  (itemPrice * increasedQty)
        binding.amount.text = decimalPriceFormatter.format(itemAmount)
    }
    private fun decreaseQty(){

        if(itemDetailViewModel.selectedcaritem.unitqty!! > 1.0)
        {
            var decreasedQty : Double = itemDetailViewModel.selectedcaritem.unitqty?.minus(1.0)?:0.0

            itemDetailViewModel.selectedcaritem.unitqty = decreasedQty
            binding.qty.text = itemDetailViewModel.selectedcaritem.unitqty.toString()


            var itemPrice: Double = itemDetailViewModel.selectedcaritem.saleprice?:0.0


            val itemAmount: Double =  (itemPrice * decreasedQty)
            binding.amount.text = decimalPriceFormatter.format(itemAmount)
        }

    }

}