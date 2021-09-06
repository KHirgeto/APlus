package com.rafael.apluse.fragments

import android.content.SharedPreferences
import android.graphics.Color
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
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rafael.apluse.adapters.ClassAdapter
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rafael.apluse.adapters.AddTaskAdapter
import com.rafael.apluse.classes.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Date
import kotlin.collections.ArrayList
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import java.text.DateFormat
import java.time.Month
import java.time.Year


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
    private lateinit var subTasks: ArrayList<SubTask>
    private lateinit var btnCreateTask: Button
    private lateinit var rvAddTask: RecyclerView
    private lateinit var classRVAdapter: AddTaskAdapter
    private lateinit var etName: TextInputLayout
    private lateinit var etDesc: TextInputLayout
    private lateinit var etSubTask: TextInputLayout
    private lateinit var chipGroup: ChipGroup



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
        etSubTask = view?.findViewById(R.id.etSubTaskAddTask)!!
        chipGroup = view?.findViewById(R.id.SubTaskChipGroup)!!
        subTasks = ArrayList()

        tinyDB = TinyDB(activity)

       // val items = listOf("Option 1", "Option 2", "Option 3", "Option 4")

        getDataAndCallAdapter()
        //val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
      // (menu.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        btnSelectDueDate.setOnClickListener {
            pickeDate()
        }

        etSubTask.setEndIconOnClickListener {
            val task = etSubTask.editText?.text.toString().trim()
            if(task.isNotEmpty())
            {
                val subTask = SubTask(task,false)
                subTasks.add(subTask)
                saveSubTask(subTasks)
                addNewChip(task)
                //add to chip
            }
            else
            {
                Toast.makeText(activity,"Pleas enter your task before adding to the list",Toast.LENGTH_SHORT).show()
            }
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
              //  Toast.makeText(activity,date, Toast.LENGTH_SHORT).show()
              //  Log.d("DDDDDDDDDDDDDDD", date)
//                val formatter = SimpleDateFormat("Mmm dd, yyyy", Locale.ENGLISH)
//                val dateTime: Date = formatter.parse(taskDueDate)!!
//                Log.d("DDDDDDDDDDDDDDDCCCCC", date)

                val task = Task(etName,etDesc,className,taskDueDate,null, getSubTask())
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

    private fun saveSubTask(classList: ArrayList<SubTask>) {
        val sharedPreference: SharedPreferences? = activity?.getSharedPreferences("sharedPreferencesSubTasks",
            AppCompatActivity.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor? = sharedPreference?.edit()
        val gson = Gson()
        val json : String = gson.toJson(classList)
        editor?.putString("subTaskList",json)
        editor?.apply()
    }
    fun getSubTask(): ArrayList<SubTask>? {
        val sharedPreference: SharedPreferences? = activity?.getSharedPreferences("sharedPreferencesSubTasks",
            AppCompatActivity.MODE_PRIVATE
        )
        val gson = Gson()
        val json: String? = sharedPreference?.getString("subTaskList", null)
        val type = object : TypeToken<java.util.ArrayList<SubTask?>?>() {}.type
        return gson.fromJson(json, type)
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


            val targetFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
            val originalFormat: DateFormat = SimpleDateFormat("dd-mm-yyyy")
//            val datee: Date? = targetFormat.parse(materialDatePicker.headerText)
            tinyDB.putString("AVDATE",date)
         //   tinyDB.putObject("FORMATED_DATE", datee)
          //  Log.d("KKKKKKKKKKK", datee.toString())
      //      Toast.makeText(activity,datee.toString(),Toast.LENGTH_LONG).show()
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
        classRVAdapter =  AddTaskAdapter(activity,localClassList)
        //val marginLayoutParams = MarginLayoutParams(rvAddTask.layoutParams)
        //marginLayoutParams.setMargins(20, 0, 20, 0)
        //rvAddTask.layoutParams = marginLayoutParams
        rvAddTask.adapter = classRVAdapter

    }
    private fun addNewChip(task: String) {
        //val keyword: String = this.editTextKeyword.getText().toString()
        if (task == null || task.isEmpty()) {
            Toast.makeText(activity, "Please enter the keyword!", Toast.LENGTH_LONG).show()
            return
        }
        try {
            val inflater = LayoutInflater.from(activity)

            // Create a Chip from Layout.
            val newChip =
                inflater.inflate(R.layout.layout_chip_entry, this.chipGroup, false) as Chip
            newChip.text = task
            newChip.isCloseIconVisible = false
            newChip.setTextColor(Color.parseColor("#ffffff"))
            newChip.setChipBackgroundColorResource(R.color.primary)

            //
            // Other methods:
            //
            // newChip.setCloseIconVisible(true);
            // newChip.setCloseIconResource(R.drawable.your_icon);
            // newChip.setChipIconResource(R.drawable.your_icon);
            // newChip.setChipBackgroundColorResource(R.color.red);
            // newChip.setTextAppearanceResource(R.style.ChipTextStyle);
            // newChip.setElevation(15);
            this.chipGroup.addView(newChip)

            // Set Listener for the Chip:
//            newChip.setOnCheckedChangeListener { buttonView, isChecked ->
//                handleChipCheckChanged(
//                    buttonView as Chip,
//                    isChecked
//                )
//            }
            // newChip.setOnCloseIconClickListener { v -> handleChipCloseIconClicked(v as Chip) }
            etSubTask.editText?.setText("")
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(activity, "Error: " + e.message, Toast.LENGTH_LONG).show()
        }
    }

}