package com.example.projectnailsschedule.ui.calendar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.DateActivity
import com.example.projectnailsschedule.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {

    //
    private var _binding: FragmentCalendarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.e("LifeCycle", "CalendarFragment created")
        // Создаем переменную ViewModel
        val calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val calendarView: CalendarView = binding.calendar
        calendarView.setOnDateChangeListener { calendarView, i, i2, i3 ->
            val dd = i3
            val mm = i2 + 1
            val yy = i

            // Сформировать дату в нормальном формате
            val day = String.format("$dd.$mm.$yy")
            calendarViewModel.calendarChanged(day)

            // Because Fragment is NOT of Context type, you'll need to call the parent Activity
            // Передаем дату и запускаем активность по Дате
            val intent = Intent (activity, DateActivity::class.java)
            intent.putExtra("day", day)
            activity?.startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        Log.e("LifeCycle", "CalendarFragment onDestroy")
        super.onDestroyView()
        _binding = null
    }
}



