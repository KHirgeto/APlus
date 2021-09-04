package com.rafael.apluse.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rafael.apluse.R
import com.rafael.apluse.adapters.ClassAdapter
import com.rafael.apluse.adapters.TaskAdapter
import com.rafael.apluse.classes.*
import android.content.Intent
import android.net.Uri
import android.content.pm.PackageManager
import android.graphics.Color


class ClassPageFragment : Fragment(R.layout.fragment_class_page) {

    private lateinit var tvCName: TextView
    private lateinit var tvPName: TextView
    private lateinit var taskRV: RecyclerView
    private lateinit var classTaskAdapter: TaskAdapter
    private lateinit var btnMail: CardView
    private lateinit var btnCanvas: CardView
    private lateinit var btnMyGoucher: CardView
    private lateinit var classInfoBGCV: CardView
    private lateinit var btnBack: LinearLayout
    private lateinit var tinyDB: TinyDB
    private lateinit var localClassList:ArrayList<StudentClass>
    private lateinit var dateList:ArrayList<Date>
    private lateinit var dateListString:ArrayList<String>
    private lateinit var localTaskList:ArrayList<Task>
    private lateinit var listView: ListView
    private val mFireStore = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().uid

    override fun onStart() {
        super.onStart()

        tvCName  = view?.findViewById(R.id.tvCPageCName)!!
        tvPName = view?.findViewById(R.id.tvCPageProName)!!
        taskRV = view?.findViewById(R.id.CPageRV)!!
        btnMail = view?.findViewById(R.id.btnClassPageMail)!!
        btnCanvas = view?.findViewById(R.id.btnClassPageCanvas)!!
        btnMyGoucher = view?.findViewById(R.id.btnClassPageMyGoucher)!!
        listView = view?.findViewById(R.id.dateListView)!!
        btnBack = view?.findViewById(R.id.btnClassPageBack)!!
        classInfoBGCV = view?.findViewById(R.id.classPageClassInfoCV)!!

        tinyDB = TinyDB(activity)

        val clickedClassName = tinyDB.getString("RVOnClickClassName")
        val clickedClassProName = tinyDB.getString("RVOnClickProName")
      //  val clickedClassLocation = tinyDB.getString("RVOnClickClassLocation")
    //    val clickedClassPhoneNumber = tinyDB.getString("RVOnClickClassPhoneNumber")
        val clickedClassEmail = tinyDB.getString("RVOnClickClassEmail")
        val clickedDateList = tinyDB.getListString("DateStringList")
        val classBGColor = tinyDB.getString("ClickedClassBGColor")

        showData(clickedClassName,clickedClassProName, clickedDateList,classBGColor)

        btnBack.setOnClickListener {
            val fragment: Fragment = HomeFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }


        btnMail.setOnClickListener {
            if(clickedClassEmail.isNotEmpty()||clickedClassEmail != null)
            {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "plain/text"
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(clickedClassEmail))
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject")
                intent.putExtra(Intent.EXTRA_TEXT, "mail body")
                startActivity(Intent.createChooser(intent, ""))

            }
            else
            {

//                val outlookIntent = Intent()
//                outlookIntent.action = Intent.ACTION_VIEW
//                outlookIntent.setClassName(
//                    "com.microsoft.office.outlook",
//                    "com.rafael.apluse.fragments.ClassPageFragment"
//                )
//                startActivity(outlookIntent)


                if(activity?.let { it1 -> isPackageInstalled("com.microsoft.office.outlook", it1.packageManager) } == true)
                {
                    var i = Intent(Intent.ACTION_MAIN)
                    val managerclock: PackageManager? = activity?.packageManager
                    i = managerclock?.getLaunchIntentForPackage("com.microsoft.office.outlook")!!
                    i!!.addCategory(Intent.CATEGORY_LAUNCHER)
                    startActivity(i)
                }
                else
                {
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.addCategory(Intent.CATEGORY_BROWSABLE)
                    intent.data = Uri.parse("https://outlook.com/")
                    startActivity(intent)
                }




            }
        }

        btnCanvas.setOnClickListener {
            if(activity?.let { it1 -> isPackageInstalled("com.instructure.candroid", it1.packageManager) } == true)
            {
                var i = Intent(Intent.ACTION_MAIN)
                val managerclock: PackageManager? = activity?.packageManager
                i = managerclock?.getLaunchIntentForPackage("com.instructure.candroid")!!
                i!!.addCategory(Intent.CATEGORY_LAUNCHER)
                startActivity(i)
            }
            else
            {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                intent.data = Uri.parse("https://canvas.goucher.edu/")
                startActivity(intent)
            }
        }

        btnMyGoucher.setOnClickListener {

                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                intent.data = Uri.parse("https://hercules.goucher.edu/selfservice/Home.aspx")
                startActivity(intent)

        }

    }
    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun showData(clickedClassName: String?, clickedProName: String?, clickedDateList: ArrayList<String>?, classBgColor: String?) {
        Toast.makeText(activity, "TTTTTTTTTTTTTTTTT$clickedClassName",Toast.LENGTH_SHORT).show()

        tvCName.text = clickedClassName
        tvPName.text = clickedProName
        classInfoBGCV.setCardBackgroundColor(Color.parseColor(classBgColor))
        if (clickedDateList != null) {
            updateList(clickedDateList)
        }
        getDataAndUpdate(clickedClassName)


    }

    private fun getDataAndUpdate(clickedClassName: String?) {

        //Toast.makeText(activity, "TTTTTTTTTTTTTTTTT$clickedClassName",Toast.LENGTH_SHORT).show()

        localClassList = ArrayList()
        localTaskList = ArrayList()
        dateList = ArrayList()
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
//                        val name = "Hello "+ students[i].name
                        //Add the students class list from FBDB to classList and save it.
                        if(students[i].classList == null)
                        {
                            Toast.makeText(activity,"You haven't added your class yet, please add your class", Toast.LENGTH_SHORT).show()
                        }
                        else
                        {
                            localClassList.addAll(students[i].classList)
                          //  localTaskList.addAll(students[i].taskList)

                            for (i in students[i].taskList)
                            {
                                if (i.className.equals("\""+clickedClassName+"\""))
                                {
                                     localTaskList.add(i)
                                }
                            }
//                            for (i in localClassList)
//                            {
//                                if (i.className.equals(clickedClassName))
//                                {
//                                    tvCName.text = i.className
//                                    tvCName.setText(i.className)
//                                    tvPName.text = i.proName
//                                    dateList.addAll(i.date)
//                                    // updateList(dateList)
//                                }
//                            }

                            upDateRV(localTaskList, localClassList)
                        }


                    }
                }
            }

    }

    private fun upDateRV(localTaskList: ArrayList<Task>, localClassList: ArrayList<StudentClass>) {




        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        taskRV.layoutManager = layoutManager
        classTaskAdapter =  TaskAdapter(activity,localTaskList, localClassList)

        taskRV.adapter = classTaskAdapter

    }

    private fun updateList(dateList: ArrayList<String>) {

//        dateListString = ArrayList()
//
//        for (i in dateList)
//        {
//            dateListString.add(i.date)
//        }
        val adapter =
            activity?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1,dateList ) }
        listView.adapter = adapter

    }

}