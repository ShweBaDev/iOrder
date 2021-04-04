package com.galaxysoftware.istockenterpriseiorder.views.ui.items


import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.CircleCropTransformation
import com.galaxysoftware.istockenterpriseiorder.R
import com.galaxysoftware.istockenterpriseiorder.dao.SqlUtilities
import com.galaxysoftware.istockenterpriseiorder.databinding.FragmentItemsBinding
import com.galaxysoftware.istockenterpriseiorder.models.ShoppingCartItem
import com.galaxysoftware.istockenterpriseiorder.models.UsrCode
import com.galaxysoftware.istockenterpriseiorder.utils.Utility.hideProgressBar
import com.galaxysoftware.istockenterpriseiorder.utils.Utility.showProgressBar
import com.galaxysoftware.istockenterpriseiorder.views.BaseRecyclerViewAdapter
import com.galaxysoftware.istockenterpriseiorder.views.ui.itemdetail.ItemDetailActivity


class ItemFragment : Fragment() {

    lateinit  var orgCodeList:MutableList<UsrCode>
    lateinit  var filteredCodeList:MutableList<UsrCode>

    lateinit var cartItemList :MutableList<ShoppingCartItem>

    private lateinit var itemRecyclerView: RecyclerView

    private lateinit var itemRecyclerAdapter: BaseRecyclerViewAdapter<UsrCode>

    private lateinit var binding: FragmentItemsBinding

    lateinit var itemViewModel: ItemViewModel

    lateinit var sqlUtilities: SqlUtilities

    lateinit var viewItemClick: (user: UsrCode) -> Unit

    lateinit var getCartItemCount:() -> Unit

    private  var selectedItemIndex : Int = -9

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        sqlUtilities = SqlUtilities(container?.context!!)

        itemViewModel = ViewModelProvider(this).get(ItemViewModel::class.java)



        binding = FragmentItemsBinding.inflate(layoutInflater)
        val view = binding.root


        itemRecyclerView = binding.itemsRecyclerView
        itemRecyclerView.layoutManager = LinearLayoutManager(container?.context!!)

        orgCodeList = mutableListOf<UsrCode>()
        filteredCodeList = mutableListOf()
        cartItemList = mutableListOf()


        var toolbar: Toolbar = activity!!.findViewById<Toolbar>(R.id.toolbar)
        val searchView : SearchView = toolbar.findViewById(R.id.txtSearchView)

        searchView.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(queryText: String?): Boolean {
                if(queryText.isNullOrEmpty()){
                    itemRecyclerAdapter.setItem(orgCodeList)
                }
                else{
                   filteredCodeList.clear()
                    val searchText = queryText.toLowerCase()
                    orgCodeList.forEach{
                        if(it.usrcode.toLowerCase().startsWith(searchText) || it.description!!.toLowerCase().contains(searchText)){
                            filteredCodeList.add(it)
                        }
                    }
                    itemRecyclerAdapter.setItem(filteredCodeList)
                }
                return true
            }
        })

        getCartItems()
        setAdapter()
        setData()
        return view
    }


    fun setFunction(callback: () -> Unit) {
        getCartItemCount = callback
    }

    private fun setAdapter() {

        itemRecyclerAdapter = BaseRecyclerViewAdapter(R.layout.list_item_usrcode) { item, view -> bindViewHolder(item, view) }
        itemRecyclerView.adapter = itemRecyclerAdapter

    }

    private fun setData(){
        context?.showProgressBar()
        itemViewModel.loadData().observe(this, Observer<List<UsrCode>>{ usrCodeList ->

            orgCodeList.addAll(usrCodeList)
            itemRecyclerAdapter.setItem(usrCodeList.toMutableList())
            hideProgressBar()
        })
    }

    private fun bindViewHolder(usrCode: UsrCode, view: View) {
        var textitemname = view.findViewById<TextView>(R.id.item_name)
        var textitemcode = view.findViewById<TextView>(R.id.item_code)

        var imgstar = view.findViewById<ImageView>(R.id.imgStar)


        var imageView = view.findViewById<ImageView>(R.id.imageView)

        imageView.load(usrCode.imageurl) {
            crossfade(true)
            placeholder(R.drawable.ic_default_image_24)
            error(R.drawable.ic_default_image_24)
            transformations(CircleCropTransformation())
        }

        textitemname.text = usrCode.description
        textitemcode.text = usrCode.usrcode


        if(cartItemList.count() >0 ){
            if(cartItemList.filter { it.usrCode == usrCode.usrcode }.toList().count() > 0){
                imgstar.visibility = View.VISIBLE
            }
            else
            {
                imgstar.visibility = View.INVISIBLE
            }
        }else{
            imgstar.visibility = View.INVISIBLE
        }


        view.setOnClickListener {
            selectedItemIndex = itemRecyclerAdapter.getIndex(usrCode)
            showItemDetail(usrCode)
        }
    }

    private fun showItemDetail(code: UsrCode){

        val intent = Intent(activity, ItemDetailActivity::class.java)
        intent.putExtra("code",code.usrcode)
        startActivity(intent)
    }


    override fun onResume() {
        super.onResume()
        getCartItems()
        getCartItemCount()
    }

    private fun getCartItems(){
        sqlUtilities.getCartItems()?.observe(this, Observer {
            cartItemList.clear()
            cartItemList.addAll(it)

            if(it.count() == 0){
                itemRecyclerAdapter.notifyItemRangeChanged(0,20)
            }

            if(selectedItemIndex != -9){
                itemRecyclerAdapter.notifyItemChanged(selectedItemIndex)
            }
        })
    }
}