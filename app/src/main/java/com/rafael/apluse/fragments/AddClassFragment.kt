package com.rafael.apluse.fragments

import android.content.SharedPreferences
import android.graphics.Color
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rafael.apluse.R
import com.rafael.apluse.classes.Date
import com.rafael.apluse.classes.Student
import com.rafael.apluse.classes.StudentClass
import com.rafael.apluse.classes.TinyDB


class AddClassFragment : Fragment(R.layout.fragment_add_class) {

    private lateinit var btnBack: LinearLayout
    private lateinit var etClassName: TextInputLayout
    private lateinit var etProName: TextInputLayout
    private lateinit var btnCreateClass: Button
    private lateinit var tinyDB: TinyDB
    private lateinit var dates: ArrayList<Date>
    private lateinit var classList: ArrayList<StudentClass>
    private lateinit var allClassList: ArrayList<StudentClass>


    private lateinit var monCV: CardView
    private lateinit var tueCV: CardView
    private lateinit var wedCV: CardView
    private lateinit var thuCV: CardView
    private lateinit var friCV: CardView
    private lateinit var satCV: CardView
    private lateinit var sunCV: CardView

    private lateinit var tvMonCV: TextView
    private lateinit var tvTueCV: TextView
    private lateinit var tvWedCV: TextView
    private lateinit var tvThuCV: TextView
    private lateinit var tvFriCV: TextView
    private lateinit var tvSatCV: TextView
    private lateinit var tvSunCV: TextView

    private lateinit var blueCV: CardView
    private lateinit var yellowCV: CardView
    private lateinit var purpleCV: CardView
    private lateinit var redCV: CardView
    private lateinit var cyanCV: CardView
    private lateinit var greenCV: CardView

    private val mFireStore = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().uid

    override fun onStart() {
        super.onStart()

        btnBack = view?.findViewById(R.id.btnAddClassBack)!!
        btnCreateClass = view?.findViewById(R.id.btnCreateClass)!!
        etClassName = view?.findViewById(R.id.etClassName)!!
        etProName = view?.findViewById(R.id.etProName)!!
        tinyDB = TinyDB(activity)

        monCV = view?.findViewById(R.id.monCV)!!
        tueCV = view?.findViewById(R.id.tueCV)!!
        wedCV = view?.findViewById(R.id.wedCV)!!
        thuCV = view?.findViewById(R.id.thuCV)!!
        friCV = view?.findViewById(R.id.friCV)!!
        satCV = view?.findViewById(R.id.satCV)!!
        sunCV = view?.findViewById(R.id.sunCV)!!

        tvMonCV = view?.findViewById(R.id.tvMonCV)!!
        tvTueCV = view?.findViewById(R.id.tvTueCV)!!
        tvWedCV = view?.findViewById(R.id.tvWedCV)!!
        tvThuCV = view?.findViewById(R.id.tvThuCV)!!
        tvFriCV = view?.findViewById(R.id.tvFriCV)!!
        tvSatCV = view?.findViewById(R.id.tvSatCV)!!
        tvSunCV = view?.findViewById(R.id.tvSunCV)!!

        blueCV    = view?.findViewById(R.id.cvLightBlue)!!
        yellowCV  = view?.findViewById(R.id.cvLightYellow)!!
        purpleCV = view?.findViewById(R.id.cvLightPur)!!
        redCV = view?.findViewById(R.id.cvLightRed)!!
        cyanCV = view?.findViewById(R.id.cvLightCyan)!!
        greenCV = view?.findViewById(R.id.cvLightGreen)!!

        dates = ArrayList()

        btnCreateClass.setOnClickListener {



            val CName = etClassName.editText?.text.toString().trim()
            val PName = etProName.editText?.text.toString().trim()
            val CColor = tinyDB.getString("ClassColor")
            val newClass: StudentClass

            if(CName.isNotEmpty() && PName.isNotEmpty()&& CColor.isNotEmpty()&& dates.isNotEmpty())
            {

              //  allClassList = ArrayList()
                newClass = StudentClass(CName,PName,dates,CColor,null)
                //classList.add(newClass)
                updateListWithPreviousDate(newClass)
                //getClassList()?.let { it1 -> allClassList.addAll(it1) }
                //allClassList.add(newClass)
//                addClass(classList)

            }
            else
            {
                Toast.makeText(activity,"Error: Fill in every box and pick your class date and color",Toast.LENGTH_LONG).show()
            }
        }


//        lightBlue">#8197F5</color>
//        lightYellow">#F5CD81</color>
//        lightPurple">#B281F5</color>
//        lightRed">#F58181</color>
//        lightCyan">#81D1F5</color>
//        lightGreen">#81F5A4</color>
//        primary">#1400FF</color>
        blueCV  .setOnClickListener {
            Toast.makeText(activity,"Blue",Toast.LENGTH_SHORT).show()
            storeColor("#8197F5")
        }
        yellowCV.setOnClickListener {
            Toast.makeText(activity,"Yellow",Toast.LENGTH_SHORT).show()

            storeColor("#F5CD81")

        }
        purpleCV.setOnClickListener {
            Toast.makeText(activity,"Purple",Toast.LENGTH_SHORT).show()

            storeColor("#B281F5")

        }
        redCV.setOnClickListener {
            Toast.makeText(activity,"Red",Toast.LENGTH_SHORT).show()

            storeColor("#F58181")

        }
        cyanCV .setOnClickListener {
            Toast.makeText(activity,"Cyan",Toast.LENGTH_SHORT).show()

            storeColor("#81D1F5")

        }
        greenCV.setOnClickListener {
            Toast.makeText(activity,"Green",Toast.LENGTH_SHORT).show()

            storeColor("#81F5A4")

        }


        monCV.setOnClickListener {
            tueCV.setCardBackgroundColor(Color.BLUE)
            openTimePicker("StartTime", "Mon")
            openTimePicker("EndTime","Mon")
        }
        tueCV.setOnClickListener {
            tueCV.setBackgroundColor(Color.BLUE)
            openTimePicker("StartTime","Tue")
            openTimePicker("EndTime","Tue")
        }
        wedCV.setOnClickListener {
            tueCV.setBackgroundColor(Color.BLUE)
            openTimePicker("StartTime","Wed")
            openTimePicker("EndTime","Wed")
        }
        thuCV.setOnClickListener {
            tueCV.setBackgroundColor(Color.BLUE)
            openTimePicker("StartTime","Thu")
            openTimePicker("EndTime","Thu")
        }
        friCV.setOnClickListener {
            tueCV.setBackgroundColor(Color.BLUE)
            openTimePicker("StartTime","Fri")
            openTimePicker("EndTime","Fri")
        }
        satCV.setOnClickListener {
            tueCV.setBackgroundColor(Color.BLUE)
            openTimePicker("StartTime","Sat")
            openTimePicker("EndTime","Sat")
        }
        sunCV.setOnClickListener {
            tueCV.setBackgroundColor(Color.BLUE)
            openTimePicker("StartTime","Sun")
            openTimePicker("EndTime","Sun")
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

    private fun updateListWithPreviousDate(newClass: StudentClass) {


        //Get data and store it in the classList
        classList = ArrayList()

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
                       // classList.addAll(students[i].classList)


                    //    classList.add(newClass)
                       // saveClassList(classList)
                    //    addClass(classList)
                        if(students[i].classList == null)
                        {
                            Toast.makeText(activity,"Adding your first class", Toast.LENGTH_SHORT).show()
                            classList.add(newClass)
                            // saveClassList(classList)
                            addClass(classList)
                        }
                        else
                        {
                            classList.addAll(students[i].classList)
                            classList.add(newClass)
                            // saveClassList(classList)
                            addClass(classList)
                        }


                    }
                }
            }

    }


    private fun saveClassList(classList: ArrayList<StudentClass>) {
        val sharedPreference: SharedPreferences? = activity?.getSharedPreferences("sharedPreferencesClass",
            AppCompatActivity.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor? = sharedPreference?.edit()
        val gson = Gson()
        val json : String = gson.toJson(classList)
        editor?.putString("classList",json)
        editor?.apply()
    }
    fun getClassList(): ArrayList<StudentClass>? {
        val sharedPreference: SharedPreferences? = activity?.getSharedPreferences("sharedPreferencesClass",
            AppCompatActivity.MODE_PRIVATE
        )
        val gson = Gson()
        val json: String? = sharedPreference?.getString("classList", null)
        val type = object : TypeToken<java.util.ArrayList<StudentClass?>?>() {}.type
        return gson.fromJson(json, type)
    }
    private fun addClass(newClass: ArrayList<StudentClass>) {

        Log.d("CCCCCCCCCCCCCCC", newClass.toString())

        if (uid != null) {
            mFireStore.collection("students").document(uid)
                .update(mapOf(
                    "classList" to newClass
                )).addOnSuccessListener {
                    Toast.makeText(activity,"Class Added",Toast.LENGTH_SHORT).show()
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

    private fun storeColor(color: String) {
        tinyDB.putString("ClassColor",color)
    }

    private fun openTimePicker(timeOf: String, day: String) {
        val isSystem24Hour = DateFormat.is24HourFormat(activity)
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Class Time")
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

            if(timeOf == "StartTime")
            {
                when (day) {
                    "Mon" -> {
                        tinyDB.putString("MonStartTime",time)

                        tvMonCV.setTextColor(Color.BLUE)
                        monCV.setBackgroundColor(Color.parseColor("#1400FF"))
                        //cardview.setBackgroundColor(Color.parseColor("#EAEDED"))

                    }
                    "Tue" -> {
                        tinyDB.putString("TueStartTime",time)
                        tvTueCV.setTextColor(Color.BLUE)
                        tueCV.setBackgroundColor(Color.parseColor("#1400FF"))


                    }
                    "Wed" -> {
                        tinyDB.putString("WedStartTime",time)
                        tvWedCV.setTextColor(Color.BLUE)
                        wedCV.setBackgroundColor(Color.parseColor("#1400FF"))

                    }
                    "Thu" -> {
                        tinyDB.putString("ThuStartTime",time)
                        tvThuCV.setTextColor(Color.BLUE)
                        thuCV.setBackgroundColor(Color.parseColor("#1400FF"))
                    }
                    "Fri" -> {
                        tinyDB.putString("FriStartTime",time)
                        tvFriCV.setTextColor(Color.BLUE)
                        friCV.setBackgroundColor(Color.parseColor("#1400FF"))
                    }
                    "Sat" -> {
                        tinyDB.putString("SatStartTime",time)
                        tvSatCV.setTextColor(Color.BLUE)
                        satCV.setBackgroundColor(Color.parseColor("#1400FF"))
                    }
                    "Sun" -> {
                        tinyDB.putString("SunStartTime",time)
                        tvSunCV.setTextColor(Color.BLUE)
                        sunCV.setBackgroundColor(Color.parseColor("#1400FF"))
                    }
                }

            }
            else if(timeOf == "EndTime")
            {

                when (day) {
                    "Mon" -> {
                       // tinyDB.putString("MonEndTime",time)
                        tvMonCV.setTextColor(Color.WHITE)
                        val date = Date("Mon",tinyDB.getString("MonStartTime")+"-"+time)
                        dates.add(date)
                        monCV.setBackgroundColor(Color.parseColor("#1400FF"))

                    }
                    "Tue" -> {
                        tinyDB.putString("TueEndTime",time)
                        tvTueCV.setTextColor(Color.WHITE)
                        val date = Date("Tue",tinyDB.getString("TueStartTime")+"-"+time)
                        dates.add(date)
                    }
                    "Wed" -> {
                        tinyDB.putString("WedEndTime",time)
                        tvWedCV.setTextColor(Color.WHITE)
                        val date = Date("Wed",tinyDB.getString("WedStartTime")+"-"+time)
                        dates.add(date)
                        wedCV.setBackgroundColor(Color.parseColor("#1400FF"))
                    }
                    "Thu" -> {
                        tinyDB.putString("ThuEndTime",time)
                        tvThuCV.setTextColor(Color.WHITE)
                        val date = Date("Thu",tinyDB.getString("ThuStartTime")+"-"+time)
                        dates.add(date)
                        thuCV.setBackgroundColor(Color.parseColor("#1400FF"))
                    }
                    "Fri" -> {
                        tinyDB.putString("FriEndTime",time)
                        tvFriCV.setTextColor(Color.WHITE)
                        val date = Date("Fri",tinyDB.getString("FriStartTime")+"-"+time)
                        dates.add(date)
                        friCV.setBackgroundColor(Color.parseColor("#1400FF"))
                    }
                    "Sat" -> {
                        tinyDB.putString("SatEndTime",time)
                        tvSatCV.setTextColor(Color.WHITE)
                        val date = Date("Sat",tinyDB.getString("SatStartTime")+"-"+time)
                        dates.add(date)
                        satCV.setBackgroundColor(Color.parseColor("#1400FF"))

                    }
                    "Sun" -> {
                        tinyDB.putString("SunEndTime",time)
                        tvSunCV.setTextColor(Color.WHITE)
                        val date = Date("Sun",tinyDB.getString("SunStartTime")+"-"+time)
                        dates.add(date)
                        sunCV.setBackgroundColor(Color.parseColor("#1400FF"))
                    }
                }

            }
            else
            {
                Toast.makeText(activity,"Issue picking time, please reach us and tell us what happened.",Toast.LENGTH_SHORT).show()
            }

           // tvSelectDate.text = time + " "+tvSelectDate.text
            //pickeDate()
        }
        picker.addOnNegativeButtonClickListener {
            Toast.makeText(activity,"Please pick a time in order to add task", Toast.LENGTH_LONG).show()
        }
        picker.addOnCancelListener {
            // call back code
        }
        picker.addOnDismissListener {
        }
    }

}