package com.rafael.apluse.fragments

import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rafael.apluse.R
import com.rafael.apluse.classes.Student
import com.rafael.apluse.classes.StudentClass
import com.rafael.apluse.classes.TinyDB
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rafael.apluse.adapters.ClassAdapter
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import com.rafael.apluse.classes.Task
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class AddTaskFragment : Fragment(R.layout.fragment_add_task) {

    private lateinit var btnBack: LinearLayout
    private lateinit var btnSelectDueDate: CardView
    private lateinit var tvSelectDate: TextView
    private lateinit var tinyDB: TinyDB
    private lateinit var taskList: ArrayList<Task>
    private lateinit var localTaskList: ArrayList<Task>

    //private lateinit var menu: TextInputLayout
    private val mFireStore = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().uid
    private lateinit var localClassList: ArrayList<StudentClass>
    private lateinit var btnCreateTask: Button
    private lateinit var rvAddTask: RecyclerView
    private lateinit var classRVAdapter: ClassAdapter
    private lateinit var etName: TextInputLayout
    private lateinit var etDesc: TextInputLayout



    override fun onStart() {
        super.onStart()

        btnBack = view?.findViewById(R.id.btnAddTaskBack)!!
        btnSelectDueDate = view?.findViewById(R.id.btnSelectDueDateAddTask)!!
        tvSelectDate = view?.findViewById(R.id.tvSelectDueDate)!!
        //menu = view?.findViewById(R.id.menu)!!
        btnCreateTask = view?.findViewById(R.id.btnCreateTask)!!
        rvAddTask = view?.findViewById(R.id.rvAddTask)!!
        etName = view?.findViewById(R.id.etTaskName)!!
        etDesc = view?.findViewById(R.id.etTaskDescription)!!


        tinyDB = TinyDB(activity)

       // val items = listOf("Option 1", "Option 2", "Option 3", "Option 4")

        getDataAndCallAdapter()
        //val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
      // (menu.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        btnSelectDueDate.setOnClickListener {
            pickeDate()
        }

        btnCreateTask.setOnClickListener {
            val etName = etName.editText?.text.toString().trim()
            val etDesc = etDesc.editText?.text.toString().trim()
            val className = tinyDB.getString("RVOnClickClassName")
            val taskDueDate = tinyDB.getString("taskDueDate")
            val date = tinyDB.getString("AVDATE")

            if(etName.isEmpty()||etDesc.isEmpty()||className.isEmpty()||taskDueDate.isEmpty())
            {
                Toast.makeText(activity,"Please fill every box and select class and due date", Toast.LENGTH_LONG).show()
            }
            else
            {
                val formatter = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
                val dateTime: Date = formatter.parse(date)!!
                val task = Task(etName,etDesc,className,taskDueDate,dateTime)
                getTaskListAndUpdate(task)

            }
        }





        btnBack.setOnClickListener {
            val fragment: Fragment = HomeFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    private fun getTaskListAndUpdate(task: Task)
    {
        //localClassList = ArrayList()
        localTaskList = ArrayList()
        mFireStore.collection("students")
            .get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                val students: ArrayList<Student> = ArrayList()
                //val uid = FirebaseAuth.getInstance().uid
                for(document in queryDocumentSnapshots)
                {
                    val c = document.toObject(Student::class.java)
                    students.add(c)
                }


                for(i in students.size-1 downTo 0 step 1)
                {
                    if(students[i].uid == uid)
                    {
                        //Add the students class list from FBDB to classList and save it.
                       // localClassList.addAll(students[i].classList)
                        if(students[i].taskList == null)
                        {
                            Toast.makeText(activity,"You haven't added your task yet, please add your task", Toast.LENGTH_SHORT).show()
                            localTaskList.add(task)
                            uploadTask(localTaskList)
                        }
                        else
                        {
                            localTaskList.addAll(students[i].taskList)
                            localTaskList.add(task)
                            uploadTask(localTaskList)
                        }

//                        for(i in localClassList.size-1 downTo 1 step 1)
//                        {
//                            if(localClassList[i].equals(task.className))
//                            {
//                                getPreviousData(localClassList,i,task)
//                            }
//                        }

                    }
                }
            }
    }
    private fun getPreviousData(newTask: Task) {


        //Get data and store it in the classList
//        for(i in classList)
//        {
//            if (i.className.equals(newTask.className))
//            {
//                taskList.addAll(i.tasks)
//                taskList.add(newTask)
//                uploadTask(position,taskList)
//            }
//        }

        taskList = ArrayList()


    }
    private fun uploadTask(taskList: ArrayList<Task>) {

       // taskList = ArrayList()
       // Log.d("TTTTTTTTTTTRTTTTTTTTTTT","${position.toString()} ${taskList.toString()}")

        if (uid != null) {

            mFireStore.collection("students").document(uid)
                .update(mapOf(
                    "taskList" to taskList
                )).addOnSuccessListener {
                    Toast.makeText(activity,"Task Added",Toast.LENGTH_SHORT).show()
                    val fragment: Fragment = HomeFragment()
                    val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                    val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.flFragment, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }
                .addOnFailureListener{ e ->
                    Log.e(this.javaClass.simpleName,"Error while adding class",e)
                }
        }

    }

    private fun openTimePicker() {
        val isSystem24Hour = is24HourFormat(activity)
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Due Time")
            .build()
        activity?.let { picker.show(it.supportFragmentManager, "tag") }

        picker.addOnPositiveButtonClickListener {
            // call back code
            val pickedHour = picker.hour
            val pickedMinute = picker.minute
            var time: String = ""

            val formattedTime: String = when {
                pickedHour > 12 -> {
                    if (pickedMinute < 10) {
                        "${picker.hour - 12}:0${picker.minute} pm"
                    } else {
                        "${picker.hour - 12}:${picker.minute} pm"
                    }
                }
                pickedHour == 12 -> {
                    if (pickedMinute < 10) {
                        "${picker.hour}:0${picker.minute} pm"
                    } else {
                        "${picker.hour}:${picker.minute} pm"
                    }
                }
                pickedHour == 0 -> {
                    if (pickedMinute < 10) {
                        "${picker.hour + 12}:0${picker.minute} am"
                    } else {
                        "${picker.hour + 12}:${picker.minute} am"
                    }
                }
                else -> {
                    if (pickedMinute < 10) {
                        "${picker.hour}:0${picker.minute} am"
                    } else {
                        "${picker.hour}:${picker.minute} am"
                    }
                }
            }

            // then update the preview TextView
            time = formattedTime
            tinyDB.putString("AVTIME",time)

           // tvSelectDate.text = time + " "+tvSelectDate.text
            val dueDate = time + " "+tvSelectDate.text
            tinyDB.putString("taskDueDate",dueDate)
            //pickeDate()
        }
        picker.addOnNegativeButtonClickListener {
            Toast.makeText(activity,"Please pick a time in order to add task",Toast.LENGTH_LONG).show()
        }
        picker.addOnCancelListener {
            // call back code
        }
        picker.addOnDismissListener {
        }
    }

    private fun pickeDate() {
        var date: String = ""
        val materialDateBuilder: MaterialDatePicker.Builder<*> =
            MaterialDatePicker.Builder.datePicker()
        materialDateBuilder.setTitleText("Due Date")

        val  materialDatePicker = materialDateBuilder.build()
        activity?.let { materialDatePicker.show(it.supportFragmentManager, "MATERIAL_DATE_PICKER") };

        materialDatePicker.addOnPositiveButtonClickListener {
            // selected date
            date = materialDatePicker.headerText
            tinyDB.putString("AVDATE",date)
            tvSelectDate.text = date
            openTimePicker()
        }
        materialDatePicker.addOnNegativeButtonClickListener {
            Toast.makeText(activity,"Please pick a date in order to send a message",Toast.LENGTH_LONG).show()

        }
        materialDatePicker.addOnCancelListener {
            // Respond to cancel button click.
        }
        materialDatePicker.addOnDismissListener {
            // Respond to dismiss events.

        }


    }

    private fun getDataAndCallAdapter() {

        localClassList = ArrayList()
        mFireStore.collection("students")
            .get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                val students: ArrayList<Student> = ArrayList()
                //val uid = FirebaseAuth.getInstance().uid
                for(document in queryDocumentSnapshots)
                {
                    val c = document.toObject(Student::class.java)
                    students.add(c)
                }


                for(i in students.size-1 downTo 0 step 1)
                {
                    if(students[i].uid == uid)
                    {
                        //Add the students class list from FBDB to classList and save it.
                        //localClassList.addAll(listOf(students[i].classList[i].className))
                        localClassList.addAll(students[i].classList)
                        upDateRV(localClassList)
                    }
                }
            }

    }
    private fun upDateRV(localClassList: ArrayList<StudentClass>) {
        //val layoutManager = GridLayoutManager(activity,2)
        val layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL, false)
        rvAddTask.layoutManager = layoutManager
        classRVAdapter =  ClassAdapter(activity,localClassList)
        //val marginLayoutParams = MarginLayoutParams(rvAddTask.layoutParams)
        //marginLayoutParams.setMargins(20, 0, 20, 0)
        //rvAddTask.layoutParams = marginLayoutParams
        rvAddTask.adapter = classRVAdapter

    }


}