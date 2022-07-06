package com.example.projectnailsschedule

import android.app.DatePickerDialog
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.projectnailsschedule.databinding.ActivityAppointmentBinding
import java.util.*


/**
 * Методы для взаимодействия с записью:
 * Редактировать запись, добавить запись
 * */

class Appointment : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var db: SQLiteDatabase
    private val addTitle = "Добавить"
    private val editTitle = "Редактировать"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = DatabaseHelper(applicationContext)
        db = databaseHelper.writableDatabase

        // Добавляем ClickListener на кнопки
        binding.addEditButton.setOnClickListener {
            when (binding.addEditButton.text.toString()) {
                // Если на кнопке написано Добавить - вызвать метод по добавлению строки
                addTitle -> addRow()
                // Если на кнопке написано Редактировать - вызвать метод по редактированию строки
                editTitle -> editIdQuery()
            }
        }
        binding.cancelButton.setOnClickListener {
            cancelButton()
        }
        binding.dayEditText.setOnClickListener {
            selectDate()
        }
        // В зависимости от содержания интента выполняем метод "Редактировать"/установить дату
        if (intent.getStringExtra("appointmentExtra") != null) {
            binding.dayEditText.setText(intent.getStringExtra("appointmentExtra"))
        } else {
            editIdFields()
        }
    }

    private fun addRow() {
        /** Внести значения полей активности в БД */
        // Собрать данные из полей в переменные и
        val fields = arrayListOf(
            binding.dayEditText.text.toString(),
            binding.timeEditText.text.toString(),
            binding.procedureEditText.text.toString(),
            binding.nameEditText.text.toString(),
            binding.phoneEditText.text.toString(),
            binding.miscEditText.text.toString()
        )
        databaseHelper.addRow(fields, db)
        finish()
    }

    private fun editIdFields() {
        /** Заполнить поля актуальными значениями */
        // Получаем список для заполнения полей из интента
        val extraArray = intent.getStringArrayListExtra("appointmentExtra")

        // Устанавливаем актуальные значения в поля для редактирования
        with(binding) {
            dayEditText.setText(extraArray!![1])
            timeEditText.setText(extraArray[2])
            procedureEditText.setText(extraArray[3])
            nameEditText.setText(extraArray[4])
            phoneEditText.setText(extraArray[5])
            miscEditText.setText(extraArray[6])
            addEditButton.text = "Редактировать"
        }
    }

    private fun editIdQuery() {
        /** Передать в метод БД информацию для обновления */
        // Получаем id строки из интента и передаем
        val id = intent.getStringArrayListExtra("appointmentExtra")?.get(0)?.toString()
        val extraArrayQuery = arrayListOf(
            id!!,
            binding.dayEditText.text.toString(),
            binding.timeEditText.text.toString(),
            binding.procedureEditText.text.toString(),
            binding.nameEditText.text.toString(),
            binding.phoneEditText.text.toString(),
            binding.miscEditText.text.toString(),
        )
        databaseHelper.editId(extraArrayQuery, db)
        finish()
    }

    private fun cancelButton() {
        /** Кнопка Отмены */
        finish()
    }

    private fun selectDate() {
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // TODO: не выводить клавиатуру при нажатии на EditText 
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH) + 1

        val datePickerDialog = DatePickerDialog(
            this, { _, year, monthOfYear, dayOfMonth ->
                val dateActivity = DateActivity()
                val date = dateActivity.dateConverter("$dayOfMonth.${monthOfYear + 1}.$year")
                binding.dayEditText.setText(date)
            },
            year, month, day
        )
        
        datePickerDialog.show()
    }
}