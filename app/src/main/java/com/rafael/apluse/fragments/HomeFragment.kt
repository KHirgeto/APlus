package com.rafael.apluse.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rafael.apluse.R
import com.rafael.apluse.adapters.ClassAdapter
import com.rafael.apluse.adapters.TaskAdapter
import com.rafael.apluse.classes.Student
import com.rafael.apluse.classes.StudentClass
import com.rafael.apluse.classes.Task
import com.rafael.apluse.classes.TinyDB
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var addFAB: FloatingActionButton
    private lateinit var clearFAB: FloatingActionButton
    private lateinit var addTaskFAB: ExtendedFloatingActionButton
    private lateinit var addClassFAB: ExtendedFloatingActionButton
    private lateinit var classRV: RecyclerView
    private lateinit var userName: TextView
    private lateinit var taskRV: RecyclerView
    private lateinit var classRVAdapter: ClassAdapter
    private lateinit var taskRVAdapter: TaskAdapter
    private val mFireStore = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().uid
    private lateinit var tinyDB: TinyDB
    private lateinit var localClassList: ArrayList<StudentClass>
    private lateinit var localTaskList: ArrayList<Task>
    private lateinit var fiveDayTasks: ArrayList<Task>

    override fun onStart() {
        super.onStart()

        addFAB = view?.findViewById(R.id.addFAB)!!
        clearFAB = view?.findViewById(R.id.clearFAB)!!
        addTaskFAB = view?.findViewById(R.id.addTaskFAB)!!
        addClassFAB = view?.findViewById(R.id.addClassFAB)!!
        classRV = view?.findViewById(R.id.classRV)!!
        taskRV = view?.findViewById(R.id.taskRV)!!
        userName = view?.findViewById(R.id.tvHomeFragUserName)!!

        tinyDB = TinyDB(activity)
        localClassList = ArrayList()
        localTaskList = ArrayList()


        addFAB.visibility = View.VISIBLE
        clearFAB.visibility = View.GONE
        addTaskFAB.visibility = View.GONE
        addClassFAB.visibility = View.GONE

        addFAB.setOnClickListener {
            addFAB.visibility = View.GONE
            clearFAB.visibility = View.VISIBLE
            addTaskFAB.visibility = View.VISIBLE
            addClassFAB.visibility = View.VISIBLE
        }
        clearFAB.setOnClickListener {
            addFAB.visibility = View.VISIBLE
            clearFAB.visibility = View.GONE
            addTaskFAB.visibility = View.GONE
            addClassFAB.visibility = View.GONE
        }
        addTaskFAB.setOnClickListener {
            val fragment: Fragment = AddTaskFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        addClassFAB.setOnClickListener {
            val fragment: Fragment = AddClassFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        getDataAndCallRV()
        getTaskDataAndCallRV()
    }

    private fun getTaskDataAndCallRV() {
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
                        if(students[i].taskList == null )
                        {
                            Toast.makeText(activity,"You haven't added your class yet, please add your class", Toast.LENGTH_SHORT).show()
                        }
                        else
                        {
                          //  localClassList.addAll(students[i].classList)
                            localTaskList.addAll(students[i].taskList)
                            upDateTaskRV(localTaskList, localClassList)
                        }
                    }
                }
            }    }

    private fun getDataAndCallRV() {

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
                        val name = "Hello "+ students[i].name
                        userName.text = name
                        //Add the students class list from FBDB to classList and save it.
                        if(students[i].classList == null)
                        {
                            Toast.makeText(activity,"You haven't added your class yet, please add your class", Toast.LENGTH_SHORT).show()
                        }
                        else
                        {
                            localClassList.addAll(students[i].classList)
                            upDateRV(localClassList)
                        }


                    }
                }
            }
    }

    private fun upDateRV(localClassList: ArrayList<StudentClass>) {
        val layoutManager = GridLayoutManager(activity,2)
        //val layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
        classRV.layoutManager = layoutManager
        classRVAdapter =  ClassAdapter(activity,localClassList)

        classRV.adapter = classRVAdapter

    }
    private fun upDateTaskRV(localTakList: ArrayList<Task>, localClassList: ArrayList<StudentClass>) {
       // val layoutManager = GridLayoutManager(activity,2)
        fiveDayTasks = ArrayList()
        val currentTime = Calendar.getInstance().time


        for(i in localTakList.size-1 downTo 0 step 1)
        {
            val dueDate = localTaskList[i].dueDate
            val timeLeft = getDaysDifference(currentTime,dueDate)

            if (timeLeft <= 5)
            {
                fiveDayTasks.add(localTakList[i])
                if (i == 0)
                {
                    if(fiveDayTasks == null || fiveDayTasks.size == 0||fiveDayTasks.isEmpty())
                    {
                        val layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL, false)
                        taskRV.layoutManager = layoutManager
                        taskRVAdapter =  TaskAdapter(activity,localTakList, localClassList)
                        taskRV.adapter = taskRVAdapter
                    }
                    else
                    {
                        val layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL, false)
                        taskRV.layoutManager = layoutManager
                        taskRVAdapter =  TaskAdapter(activity,fiveDayTasks, localClassList)

                        taskRV.adapter = taskRVAdapter
                    }

                }
            }
        }


//        val layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL, false)
//        taskRV.layoutManager = layoutManager
//        taskRVAdapter =  TaskAdapter(activity,localTakList, localClassList)
//
//        taskRV.adapter = taskRVAdapter

    }

    fun getDaysDifference(fromDate: Date?, toDate: Date?): Long {
        return if (fromDate == null || toDate == null) 0 else ((toDate.time - fromDate.time) / (1000 * 60 * 60 * 24))
    }

}