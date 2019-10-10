package com.example.travelorder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.travelorder.model.RegistrationUser
import com.example.travelorder.model.User
import com.example.travelorder.utils.Constants
import com.example.travelorder.utils.DateUtil
import com.example.travelorder.utils.PreferencesUtil
import com.example.travelorder.utils.SnackBarUtil
import com.example.travelorder.viewModels.OrdersViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.LocalDateTime

class LoginFragment: Fragment() {

    private lateinit var ordersViewModel: OrdersViewModel
    private var preferences: PreferencesUtil? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
//            ordersViewModel = ViewModelProviders.of(this).get(OrdersViewModel::class.java)
            ordersViewModel = ViewModelProvider(it).get(OrdersViewModel::class.java)
        }
        (activity as MainActivity).supportActionBar?.title = getString(R.string.login)

        preferences = PreferencesUtil(context!!)

        if (preferences?.getString(Constants.USER_CODE).isNullOrBlank()) {
            initViews()
        }else{
            findNavController().navigate(R.id.action_loginFragment_to_mainHistoryFragment)
        }
    }

    private fun initViews(){
        initObservable()
        bt_login_fragment.setOnClickListener {
            if (validate()){
                //findNavController().navigate(R.id.action_loginFragment_to_mainHistoryFragment)  //Here for navigation testing
                //ordersViewModel.postRegistrationData(createRegistrationUser())
                //testCall()
                ordersViewModel.getUser(createRegistrationUser())
            }
        }
    }

    private fun testCall(){
        ordersViewModel.firstTodoTest.observe(this, Observer { todoTest ->
            et_email_login_fragment.setText(todoTest.title)
        })
    }

    private fun validate(): Boolean{
        var isValid = true
        if (et_email_login_fragment.text.isNullOrBlank()){
            isValid = false
            et_email_login_fragment.error = getString(R.string.required_field)
        }
        if (et_code_login_fragment.text.isNullOrBlank()){
            isValid = false
            et_code_login_fragment.error = getString(R.string.required_field)
        }
        return isValid
    }

    private fun createRegistrationUser(): RegistrationUser{
        return RegistrationUser(
            et_email_login_fragment.text.toString(),
            et_code_login_fragment.text.toString()
        )
    }

    private fun initObservable(){
        ordersViewModel.user.observe(this, Observer { user ->
            if(!user.token.isBlank()){
//                GlobalScope.launch {
//                    saveInPreferences(user)
//                }.invokeOnCompletion { findNavController().navigate(R.id.action_loginFragment_to_mainHistoryFragment) }
                saveInPreferences(user)
                findNavController().navigate(R.id.action_loginFragment_to_mainHistoryFragment)
            }else{
                SnackBarUtil().makeErrorSnackBar(et_code_login_fragment, getString(R.string.login_error))
            }
        })
    }

    private fun saveInPreferences(user: User){

        preferences?.saveInt(Constants.USER_ID, user.id)
        preferences?.saveString(Constants.USER_NAME_PREF, user.name)
        preferences?.saveString(Constants.USER_SURNAME_PREF, user.surname)
        preferences?.saveString(Constants.USER_WORKPLACE_PREF, user.workplace)
        preferences?.saveString(Constants.USER_EMAIL, user.email)
        preferences?.saveString(Constants.TOKEN, user.token)
        preferences?.saveString(Constants.TOKEN_DATE, DateUtil().getStringDateFromLocalDateTimeObject(LocalDateTime.now()))
        preferences?.saveString(Constants.USER_CODE, et_code_login_fragment.text.toString())
        preferences?.saveString(Constants.USER_ADDRESS, user.address)
        preferences?.saveString(Constants.USER_COMPANY, user.company_name)
        preferences?.saveString(Constants.USER_COMPANY_ADDRESS, user.company_address)
        ordersViewModel.resetOrderRepository()
    }
}