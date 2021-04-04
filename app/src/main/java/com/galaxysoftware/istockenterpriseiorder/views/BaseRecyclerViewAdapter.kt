package com.galaxysoftware.istockenterpriseiorder.views


import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.galaxysoftware.istockenterpriseiorder.R

public class BaseRecyclerViewAdapter<T>(@LayoutRes val layoutId: Int,private val bindViewHolder : (T, View) -> Unit) :
        RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder<T>>() {

    private var items : MutableList<T> =  mutableListOf()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T>{
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = layoutInflater.inflate(layoutId,
                        parent, false)

        return BaseViewHolder(view,bindViewHolder)
    }

    override fun getItemCount(): Int = items.size

    fun setItem(_items: MutableList<T>){
        items = _items
        notifyDataSetChanged()
    }

    fun removeItem(_position : Int){
        items.removeAt(_position)
        notifyDataSetChanged()
    }

    fun getItem(_index : Int):T{
       return items[_index]
    }

    fun getIndex(_item : T): Int{
        return items.indexOf(_item)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bind(items[position])
    }


    class BaseViewHolder<T>(private val view: View ,val bindViewHolder: (T, View) -> Unit) :
            RecyclerView.ViewHolder(view) {
        fun bind(item: T) = bindViewHolder(item,view)

    }
}

class RecyclerItemTouchHelper<T>(dragDirs: Int, swipeDirs: Int, private val listener: (RecyclerView.ViewHolder?,Int,Int)-> Unit) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            val foregroundView: View = (viewHolder as BaseRecyclerViewAdapter.BaseViewHolder<T>).itemView.findViewById(R.id.frontView)
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(foregroundView)
        }
    }

    override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val foregroundView: View = (viewHolder as BaseRecyclerViewAdapter.BaseViewHolder<T>).itemView.findViewById(R.id.frontView)
        ItemTouchHelper.Callback.getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val foregroundView: View =(viewHolder as BaseRecyclerViewAdapter.BaseViewHolder<T>).itemView.findViewById(R.id.frontView)
        ItemTouchHelper.Callback.getDefaultUIUtil().clearView(foregroundView)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val foregroundView: View = (viewHolder as BaseRecyclerViewAdapter.BaseViewHolder<T>).itemView.findViewById(R.id.frontView)
        ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener(viewHolder,direction,viewHolder.adapterPosition)
    }
    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

}
