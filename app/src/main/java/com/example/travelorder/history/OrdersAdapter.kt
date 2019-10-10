package com.example.travelorder.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travelorder.R
import com.example.travelorder.model.Order
import com.example.travelorder.utils.DateUtil
import kotlinx.android.synthetic.main.row_orders_list.view.*

class OrdersAdapter(var ordersList: ArrayList<Order>, val clickListener: (Order) -> Unit) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

//    var ordersList: List<Order> = emptyList()

    fun setNewOrdersList(newOrderList: ArrayList<Order>){
        ordersList.clear()
        ordersList = newOrderList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_orders_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = ordersList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onOrderBind(ordersList[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun onOrderBind(order: Order){
            with(order){
                val purposeText = if (!description.isBlank()){
                    description
                }else{
                    purpose
                }
                itemView.tv_purpose.text = purposeText
                itemView.tv_date.text = DateUtil().getPresentationStringDate(issue_date)
                val locationsString = "$from_location > $to_location"
                itemView.tv_locations.text = locationsString
                if (mileage_end == null){
                    itemView.iv_order_status.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_inprogress))
                }else{
                    itemView.iv_order_status.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_done))
                }

                itemView.setOnClickListener { clickListener(this) }
            }
        }
    }
}