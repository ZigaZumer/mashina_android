package com.example.travelorder.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.travelorder.MainActivity
import com.example.travelorder.R
import com.example.travelorder.gone
import com.example.travelorder.model.Order
import com.example.travelorder.model.RegistrationUser
import com.example.travelorder.model.User
import com.example.travelorder.utils.Constants
import com.example.travelorder.utils.DateUtil
import com.example.travelorder.utils.PreferencesUtil
import com.example.travelorder.utils.SnackBarUtil
import com.example.travelorder.viewModels.OrdersViewModel
import com.example.travelorder.visible
import kotlinx.android.synthetic.main.fragment_main_history.*
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime

class MainHistoryFragment: Fragment() {

    private lateinit var ordersViewModel: OrdersViewModel
    private var ordersAdapter: OrdersAdapter? = null
    var preferences: PreferencesUtil? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            ordersViewModel = ViewModelProvider(it).get(OrdersViewModel::class.java)
        }
        (activity as MainActivity).supportActionBar?.title = getString(R.string.history_title)

        preferences = PreferencesUtil(context!!)
        initViews()
        makeAppropriateNetworkCalls()
    }

//    override fun onResume() {
//        super.onResume()
//        makeAppropriateNetworkCalls()
//    }

    private fun initViews(){
        ordersAdapter = OrdersAdapter(ArrayList()) { order -> openOrder(order)}
        rv_travel_order_history.setHasFixedSize(true)
        rv_travel_order_history.adapter = ordersAdapter

        fab_add_travel_order.setOnClickListener {
            makeAppropriateNetworkCalls(true)
        }
    }

    private fun openOrder(order: Order){
        ordersViewModel.selectedOrder = order
        findNavController().navigate(R.id.action_mainHistoryFragment_to_travelOrderBaseFragment)
    }

    private fun makeAppropriateNetworkCalls(newOrder: Boolean = false){
        if (isDailyToken()){
            if(!newOrder) {
                preferences?.getString(Constants.TOKEN)
                ordersViewModel.getTravelOrdersHistory()
                ordersViewModel.travelOrdersList.observe(this, Observer { travelOrdersList ->
                    if (travelOrdersList.isNullOrEmpty()) {
                        tv_empty_list_main_history.visible()
                        rv_travel_order_history.gone()
                    } else {
                        ordersAdapter?.setNewOrdersList(ArrayList(travelOrdersList))
                        tv_empty_list_main_history.gone()
                        rv_travel_order_history.visible()
                    }
                })
            }else{
                ordersViewModel.selectedOrder = null
                findNavController().navigate(R.id.action_mainHistoryFragment_to_travelOrderBaseFragment)
            }
        }else{
            ordersViewModel.getUser(createRegistrationUser())
            ordersViewModel.user.observe(this, Observer { user ->
                if(!user.token.isBlank()){
                    saveTokenInPreferences(user)
                    makeAppropriateNetworkCalls(newOrder)
                }else{
                    SnackBarUtil().makeErrorSnackBar(rv_travel_order_history, getString(R.string.token_error))
                }
            })
        }
    }

    private fun isDailyToken(): Boolean{
        return DateUtil().getLocalDateObjectFromString(preferences?.getString(Constants.TOKEN_DATE)!!).toLocalDate() == LocalDate.now()
    }

    private fun createRegistrationUser(): RegistrationUser{
        return RegistrationUser(
            preferences?.getString(Constants.USER_EMAIL),
            preferences?.getString(Constants.USER_CODE)
        )
    }

    private fun saveTokenInPreferences(user: User){
        preferences?.saveString(Constants.TOKEN, user.token)
        preferences?.saveString(Constants.TOKEN_DATE, DateUtil().getStringDateFromLocalDateTimeObject(LocalDateTime.now()))
    }
}