package com.rafael.apluse.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import cn.iwgang.countdownview.CountdownView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rafael.apluse.R
import com.rafael.apluse.classes.StudentClass
import com.rafael.apluse.classes.SubTask
import com.rafael.apluse.classes.TinyDB
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TaskViewFragment : Fragment(R.layout.fragment_task_view) {

    private lateinit var tvCName: TextView
    private lateinit var tvDDay: TextView
    private lateinit var tvTName: TextView
    private lateinit var tvTDesc: TextView
    private lateinit var subTaskListView: ListView
    private lateinit var btnDelete: CardView
    private lateinit var btnDone: CardView
    private lateinit var btnBack: LinearLayout
    private lateinit var countDown: CountdownView
    private lateinit var tinyDB: TinyDB
    private lateinit var subTaskAdapter: ArrayAdapter<String>


    override fun onStart() {
        super.onStart()

        tvCName = view?.findViewById(R.id.tvCNameTaskView)!!
        tvDDay = view?.findViewById(R.id.tvDDayTaskView)!!
        tvTName = view?.findViewById(R.id.tvTNameTaskView)!!
        tvTDesc = view?.findViewById(R.id.tvTDescTaskView)!!
        subTaskListView = view?.findViewById(R.id.lvSubTaskTaskView)!!
        btnDelete = view?.findViewById(R.id.btnDeleteTaskView)!!
        btnDone = view?.findViewById(R.id.btnDoneTaskView)!!
        btnBack = view?.findViewById(R.id.btnBackTaskView)!!
        countDown = view?.findViewById(R.id.countdownView)!!
        tinyDB = TinyDB(activity)

        //GetData
        var className  = tinyDB.getString("currentTaskCName")
        val taskName = tinyDB.getString("currentTaskTName")
        val taskDesc = tinyDB.getString("currentTaskTDesc")
        val taskDDay = tinyDB.getString("currentTaskTDDay")
        val taskDDateString = tinyDB.getString("currentTaskTDDay")
        val taskDDate: Date? = tinyDB.getObject("currentTaskTDDate",Date::class.java)

       // val date: Date? = DateFormat.getInstance().parse(taskDDate)
        val currentTime = Calendar.getInstance().time
        val subTasks = getSubTasks()

        showData(className, taskName, taskDesc, taskDDay, subTasks)




        //val formatter = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        //val dateTime: Date = formatter.parse(taskDDay)!!
        //CountDown
        val sdf = SimpleDateFormat("dd-mm-yyyy HH:mm:ss")
       // val sdf = SimpleDateFormat("dd MMM yyyy")
      // val countDownDDate = sdf.format(taskDDay)
      //  val time: Long = dateTime.time
        val countDate = "7-11-2022 00:00:00"
        val now = Date()
        try {
            //formatting from string to Date
            val date: Date? = sdf.parse(countDate)
           // parseDateToddMMyyyy(taskDDay)
            val currentTime: Long = now.time
            val deadlineDate: Long = date!!.time
            val countDownToDDay: Long = deadlineDate - currentTime
            countDown.start(countDownToDDay)
        }catch (e : ParseException){
            e.printStackTrace()
        }


        //ListView
        btnBack.setOnClickListener {
            val fragment: Fragment = HomeFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }






    }
    fun getDaysDifference(fromDate: Date?, toDate: Date?): Long {
        return if (fromDate == null || toDate == null) 0 else ((toDate.time - fromDate.time) / (1000 * 60 * 60 * 24))
    }
    private fun showData(className: String?, taskName: String?, taskDesc: String?, taskDDay: String?, subTasks: ArrayList<SubTask>?) {

        val stringSubTasks = ArrayList<String>()
        if (subTasks != null) {
            for(t in subTasks)
            {
                stringSubTasks.add(t.subTask)
            }
        }


        //Show task info

        tvCName.text = className
        tvTName.text = taskName
        tvTDesc.text = taskDesc
        tvDDay.text = taskDDay

        //Show list
        Toast.makeText(activity,stringSubTasks.toString(),Toast.LENGTH_SHORT).show()

        Log.d("AAAAAAAAAAAAAAA", stringSubTasks.toString())

        Log.d("TNTNTNTNTNTNTN" ,stringSubTasks.toString())
        val adapter =
            activity?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1,stringSubTasks ) }
        subTaskListView.adapter = adapter


//        subTaskAdapter = view?.context?.let { ArrayAdapter(it,R.layout.list_item,stringSubTasks) }!!
//        subTaskListView.adapter = subTaskAdapter;

    }


    private fun getSubTasks(): ArrayList<SubTask>? {
        val sharedPreference: SharedPreferences? = activity?.getSharedPreferences("sharedPreferencesSubTask",
            AppCompatActivity.MODE_PRIVATE
        )
        val gson = Gson()
        val json: String? = sharedPreference?.getString("SubTasks", null)
        val type = object : TypeToken<java.util.ArrayList<SubTask?>?>() {}.type
        return gson.fromJson(json, type)
    }

//    fun parseDateToddMMyyyy(time: String): String {
//       // "7-11-2022 00:00:00"
//        val inputPattern = "dd MMM yyyy"
//        val outputPattern = "dd-mm-yyyy HH:mm:ss"
//        val inputFormat = SimpleDateFormat(inputPattern)
//        val outputFormat = SimpleDateFormat(outputPattern)
//        var date: Date? = null
//        var str: String? = null
//        try {
//            date = inputFormat.parse(time)
//            str = outputFormat.format(date)
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//        return str
//    }

}