package com.notes.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.notes.notes.database.DbManager
import com.notes.notes.databinding.ActivityEditBinding
import com.notes.notes.utils.IntentConstance
import com.notes.notes.utils.TypeConstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private val dbManager = DbManager(this)
    var isEditState = false
    var id = 0
    var itemType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntents()
    }

    fun saveNote(view: android.view.View) = with(binding){
        val currentTitle = edTitle.text.toString()
        val currentDesc = edDescription.text.toString()
        val checkBoxState = checkBox.isChecked.toString()
            if (currentTitle.isNotEmpty() && currentDesc.isNotEmpty()){
                CoroutineScope(Dispatchers.Main).launch {
                    if (isEditState){
                        dbManager.updateItem(currentTitle, currentDesc, checkBoxState, id, getCurrentTime())
                    } else {
                        dbManager.insetToDb(currentTitle, currentDesc, checkBoxState, getCurrentTime())
                    }
                    finish()
                }
            } else {
                Toast.makeText(this@EditActivity, R.string.error, Toast.LENGTH_SHORT).show()
            }
    }

    fun onBack(view: android.view.View){
        finish()
    }

    fun editNote(view: android.view.View) = with(binding){
        edTitle.isEnabled = true
        edDescription.isEnabled = true
        checkBox.isEnabled = true
        btBack.visibility = View.GONE
        btEdit.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        dbManager.openDb()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbManager.closeDb()
    }

    private fun getIntents() = with(binding){
        btEdit.visibility = View.GONE
        val i = intent

        if (i.getStringExtra(IntentConstance.TITLE_KEY) != null){
            edTitle.setText(i.getStringExtra(IntentConstance.TITLE_KEY))
            isEditState = true
            edTitle.isEnabled = false
            edDescription.isEnabled = false
            checkBox.isEnabled = false
            btEdit.visibility = View.VISIBLE
            edDescription.setText(i.getStringExtra(IntentConstance.DESC_KEY))
            checkBox.isChecked = i.getBooleanExtra(IntentConstance.CHECK_KEY, false)
            id = i.getIntExtra(IntentConstance.ID_KEY, 0)
        }
    }

    private fun getCurrentTime(): String{
        val time = Calendar.getInstance().time
        val formaterTime = SimpleDateFormat("dd.MM.yy   kk:mm", Locale.getDefault())
        return formaterTime.format(time)
    }
}