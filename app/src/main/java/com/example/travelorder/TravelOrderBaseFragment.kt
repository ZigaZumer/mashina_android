package com.example.travelorder

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.travelorder.utils.Constants
import com.example.travelorder.utils.DateUtil
import com.example.travelorder.utils.PreferencesUtil
import com.example.travelorder.utils.SnackBarUtil
import com.example.travelorder.viewModels.OrdersViewModel
import kotlinx.android.synthetic.main.fragment_travel_order_base.*
import org.joda.time.*
import androidx.navigation.fragment.findNavController
import com.example.travelorder.model.*


class TravelOrderBaseFragment: Fragment(), AdapterView.OnItemSelectedListener, View.OnClickListener {

    private lateinit var ordersViewModel: OrdersViewModel
    var preferences: PreferencesUtil? = null
    private var departureDateTime: LocalDateTime = LocalDateTime.now()
    private var arrivalDateTime: LocalDateTime = LocalDateTime.now()
    private var selectedPurpose: TravelPurpose? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_travel_order_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            ordersViewModel = ViewModelProvider(it).get(OrdersViewModel::class.java)
        }
        (activity as MainActivity).supportActionBar?.title = getString(R.string.single_travel_title)

        preferences = PreferencesUtil(context!!)

        initViews()
        getNeededData()
    }

    private fun initViews(){
        val today = LocalDateTime.now()
        val singleTravelTitle = if (ordersViewModel.selectedOrder == null){
            "${ordersViewModel.travelOrdersList.value?.get(0)?.id?.plus(1)} / ${DateUtil().getPresentationDateFromObject(today)}"
        }else{
            "${ordersViewModel.selectedOrder!!.id} / ${DateUtil().getPresentationDateFromString(ordersViewModel.selectedOrder!!.issue_date)}"
        }
        tv_number_and_date.text = singleTravelTitle
        tv_firm_base_fragment.text = "${preferences?.getString(Constants.USER_COMPANY)}"
        tv_firm_address_base_fragment.text = "${preferences?.getString(Constants.USER_COMPANY_ADDRESS)}"
        val driverWorkplaceTitle = "${preferences?.getString(Constants.USER_NAME_PREF)} ${preferences?.getString(Constants.USER_SURNAME_PREF)} / ${preferences?.getString(Constants.USER_WORKPLACE_PREF)}"
        tv_driver_workplace.text = driverWorkplaceTitle
        tv_driver_address.text = "${preferences?.getString(Constants.USER_ADDRESS)}"

        setSpinners()

        val presentationDateTimeDeparture = if (ordersViewModel.selectedOrder == null){
            departureDateTime = today
            "${DateUtil().getPresentationDateFromObject(today)}/${today.hourOfDay}:${today.minuteOfHour}"
        }else{
            val depDate = DateUtil().getLocalDateObjectFromString(ordersViewModel.selectedOrder!!.estimated_departure)
            departureDateTime = depDate
            "${DateUtil().getPresentationDateFromObject(depDate)}/${depDate.hourOfDay}:${depDate.minuteOfHour}"
        }

        val presentationDateTimeArrival = if (ordersViewModel.selectedOrder == null){
            arrivalDateTime = today
            "${DateUtil().getPresentationDateFromObject(today)}/${today.hourOfDay}:${today.minuteOfHour}"
        }else{
            val depDate = DateUtil().getLocalDateObjectFromString(ordersViewModel.selectedOrder!!.estimated_arrival)
            arrivalDateTime = depDate
            "${DateUtil().getPresentationDateFromObject(depDate)}/${depDate.hourOfDay}:${depDate.minuteOfHour}"
        }
        tv_departure_date_time.text = presentationDateTimeDeparture
        tv_arrival_date_time.text = presentationDateTimeArrival

        setTimeDifference()

        ordersViewModel.selectedOrder?.let {
            et_purpose_description.setText(it.description)
        }

        iv_calendar_departure.setOnClickListener(this)
        iv_calendar_arrival.setOnClickListener(this)

        setRoutsAndMileageIfEdit()

        setTripButton()
    }

    private fun setTripButton(){
        if (ordersViewModel.selectedOrder != null){
            if (ordersViewModel.selectedOrder!!.mileage_end == null){
                bt_single_travel.text = getString(R.string.finish_trip)
            }else{
                bt_single_travel.gone()
            }
        }else{
            bt_single_travel.text = getString(R.string.start_trip)
        }
        bt_single_travel.setOnClickListener(this)
    }

    private fun setRoutsAndMileageIfEdit(){
        val selectedTravel = ordersViewModel.selectedOrder
        selectedTravel?.let {
            et_location_from.setText(it.from_location)
            et_start_km.setText(it.mileage_start.toString())
            et_location_to.setText(it.to_location)
            it.mileage_end?.let { mileageEnd ->
                et_end_km.setText(mileageEnd.toString())
            }
        }
    }

    private fun setSpinners(){
        ordersViewModel.getVehicles()
        ordersViewModel.vehiclesList.observe(this, Observer { vehicles ->
            val vehiclesArray = ArrayList(vehicles)
            vehiclesArray.add(0, Vehicle(getString(R.string.select)))
            val vehicleAdapter = ArrayAdapter<Vehicle>(context!!, android.R.layout.simple_spinner_dropdown_item, vehiclesArray)
            spin_vehicle_registration.adapter = vehicleAdapter
//            spin_vehicle_registration.onItemSelectedListener
            val selectedCarId = if (ordersViewModel.selectedOrder != null){
                ordersViewModel.selectedOrder!!.vehicle_registration
            }else{
                preferences?.getString(Constants.SELECTED_VEHICLE_REGISTRATION)
            }
            if (!selectedCarId.isNullOrBlank()){
                val index = getVehicleIndexByRegistration(vehiclesArray, selectedCarId)
                index?.let {
                    spin_vehicle_registration.setSelection(index)
                }
            }
        })
        ordersViewModel.getPurposes()
        ordersViewModel.purposesList.observe(this, Observer { purposes ->
            val purposesArray = ArrayList(purposes)
            purposesArray.add(0, TravelPurpose(getString(R.string.select)))
            val purposeAdapter = ArrayAdapter<TravelPurpose>(context!!, android.R.layout.simple_spinner_dropdown_item, purposesArray)
            spin_purpose.adapter = purposeAdapter
//            spin_purpose.onItemSelectedListener
            val selectedPurposeId = ordersViewModel.selectedOrder?.purpose
            if (!selectedPurposeId.isNullOrBlank()){
                val index = getPurposeIndexByPurpose(purposesArray, selectedPurposeId)
                index?.let {
                    spin_purpose.setSelection(it)
                }
            }
        })
        ordersViewModel.getEmployees()
        ordersViewModel.employeeList.observe(this, Observer { employees ->
            val employeesArray = ArrayList(employees)
            employeesArray.add(0, Employee(getString(R.string.select)))
            val employeeAdapter = ArrayAdapter<Employee>(context!!, android.R.layout.simple_spinner_dropdown_item, employeesArray)
            spin_vehicle_sender.adapter = employeeAdapter
            val selectedEmployee = ordersViewModel.selectedOrder
//            if (!selectedEmployee.isN)
        })
    }

    private fun getNeededData(){

    }

    private fun getVehicleIndexByRegistration(vehicles: ArrayList<Vehicle>, registration: String): Int?{
        val index = vehicles.indexOf(vehicles.find { it.vehicle_registration == registration })
        return if (index > 0){
            index
        }else{
            null
        }
    }

    private fun getPurposeIndexByPurpose(purposes: ArrayList<TravelPurpose>, purpose: String): Int?{
        val index = purposes.indexOf(purposes.find { it.purpose == purpose })
        return if (index > 0){
            index
        }else{
            null
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        if (view?.id == spin_vehicle_registration.id && position > 0){
//            preferences?.saveString(Constants.SELECTED_VEHICLE_REGISTRATION, (parent?.getItemAtPosition(position) as Vehicle).vehicle_registration)
//            preferences?.saveInt(Constants.SELECTED_VEHICLE_ID, (parent?.getItemAtPosition(position) as Vehicle).id)
//        }
//
//        if (view?.id == spin_purpose.id && position > 0){
//            selectedPurpose = spin_purpose.selectedItem as TravelPurpose
//        }
    }

    override fun onClick(view: View?) {
        if (view?.id == iv_calendar_departure.id){
            val today = LocalDateTime.now()
            DatePickerDialog(context!!,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

                    TimePickerDialog(context!!,
                        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            departureDateTime = LocalDateTime(year,monthOfYear, dayOfMonth, hourOfDay, minute)
                            val dateTimeText = "${DateUtil().getPresentationDateFromObject(departureDateTime)}/${departureDateTime.hourOfDay}:${departureDateTime.minuteOfHour}"
                            if (dateDifferenceCheck()) {
                                tv_departure_date_time.text = (dateTimeText)
                            }
                        }, today.hourOfDay, today.minuteOfHour, true).show()

                }, today.year, today.monthOfYear, today.dayOfMonth
            ).show()
        }
        if (view?.id == iv_calendar_arrival.id){
            val today = LocalDateTime.now()
            DatePickerDialog(context!!,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

                    TimePickerDialog(context!!,
                        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            arrivalDateTime = LocalDateTime(year,monthOfYear, dayOfMonth, hourOfDay, minute)
                            val dateTimeText = "${DateUtil().getPresentationDateFromObject(arrivalDateTime)}/${arrivalDateTime.hourOfDay}:${arrivalDateTime.minuteOfHour}"
                            if (dateDifferenceCheck()) {
                                tv_arrival_date_time.text = (dateTimeText)
                            }
                        }, today.hourOfDay, today.minuteOfHour, true).show()

                }, today.year, today.monthOfYear, today.dayOfMonth
            ).show()
        }
        if (view?.id == bt_single_travel.id){
            if (ordersViewModel.selectedOrder != null){
                if (checkMileage()){
                    val openOrder = createOpenOrder(et_end_km.text.toString().toInt())
//                    val closeOrder = CloseOrder(et_end_km.text.toString().toInt())
                    ordersViewModel.finishNewTravelOrder(ordersViewModel.selectedOrder!!.id, openOrder)
                    ordersViewModel.closedOrderResponseObject.observe(this, Observer { generalResponse ->
                        SnackBarUtil().makeSuccsessSnackBar(et_location_from, generalResponse.message!!)
                        et_location_from.postDelayed(Runnable {
                            findNavController().navigate(R.id.action_travelOrderBaseFragment_to_mainHistoryFragment)
                        }, 5000)
                    })
                }
            }else{
                if (validate()){
                    preferences?.saveString(Constants.SELECTED_VEHICLE_REGISTRATION, (spin_vehicle_registration.selectedItem as Vehicle).vehicle_registration)
                    preferences?.saveInt(Constants.SELECTED_VEHICLE_ID, (spin_vehicle_registration.selectedItem as Vehicle).id)
                    ordersViewModel.startNewTravelOrder(createOpenOrder())
                    ordersViewModel.openOrderResponseObject.observe(this, Observer { generalResponse ->
                        SnackBarUtil().makeSuccsessSnackBar(et_location_from, generalResponse.message!!)
                        et_location_from.postDelayed(Runnable {
                            findNavController().navigate(R.id.action_travelOrderBaseFragment_to_mainHistoryFragment)
                        }, 5000)
                    })
                }
            }
        }
    }

    private fun validate(): Boolean{
        var isValid = true

        if ((spin_purpose.selectedItem as TravelPurpose).id == null){
            isValid = false
            SnackBarUtil().makeErrorSnackBar(et_location_to, getString(R.string.purpose_error))
        }
        if (et_location_from.text.isNullOrBlank()){
            isValid = false
            et_location_from.error = getString(R.string.location_error)
        }
        if (et_location_to.text.isNullOrBlank()){
            isValid = false
            et_location_to.error = getString(R.string.location_error)
        }
        if ((spin_vehicle_registration.selectedItem as Vehicle).id == null){
            isValid = false
            SnackBarUtil().makeErrorSnackBar(et_location_to, getString(R.string.vehicle_error))
        }
        if (et_purpose_description.text.isNullOrBlank()){
            isValid = false
            et_purpose_description.error = getString(R.string.description_error)
        }
        return isValid
    }

    private fun checkMileage(): Boolean{
        var isOk = true

        if (et_start_km.text.isNullOrBlank()){
            isOk = false
            et_start_km.error = getString(R.string.required_field)
        }
        if (et_end_km.text.isNullOrBlank()){
            isOk = false
            et_end_km.error = getString(R.string.required_field)
        }
        if (!et_end_km.text.isNullOrBlank() && !et_start_km.text.isNullOrBlank() && et_end_km.text.toString().toInt() <= et_start_km.text.toString().toInt()){
            isOk = false
            SnackBarUtil().makeErrorSnackBar(et_end_km, getString(R.string.mileage_error))
        }
        return isOk
    }

    private fun createOpenOrder(mileageEnd: Int? = null): OpenOrder{
        val orderDate = if (ordersViewModel.selectedOrder == null){
            LocalDateTime.now().toString()
        }else{
            ordersViewModel.selectedOrder!!.issue_date
        }

        return OpenOrder(
            orderDate,
            preferences?.getInt(Constants.USER_ID)!!,
            (spin_purpose.selectedItem as TravelPurpose).id!!,
            et_location_from.text.toString(),
            et_location_to.text.toString(),
            departureDateTime.toString(),
            arrivalDateTime.toString(),
            preferences?.getInt(Constants.SELECTED_VEHICLE_ID)!!,
            et_purpose_description.text.toString(),
            mileageEnd
        )
    }

    private fun dateDifferenceCheck(): Boolean{
        return if (arrivalDateTime.isBefore(departureDateTime)){
            false
        }else{
            setTimeDifference()
            true
        }
    }

    private fun setTimeDifference() {
        val duratMillis = arrivalDateTime.toDateTime(DateTimeZone.UTC).millis.minus(departureDateTime.toDateTime(
            DateTimeZone.UTC).millis)
        val period = Period(duratMillis, PeriodType.dayTime())
        val diffDays = period.days
        val diffHours = period.hours
        val diffMinutes = period.minutes
        val diffText =
            "$diffDays ${getString(R.string.days)} $diffHours ${getString(R.string.hours)} $diffMinutes ${getString(
                R.string.minutes
            )}"
        tv_duration_time.text = diffText
    }
}