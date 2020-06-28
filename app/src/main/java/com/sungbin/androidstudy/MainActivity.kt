package com.sungbin.androidstudy

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sungbin.recyclerviewadaptermaker.library.AdapterHelper
import com.sungbin.recyclerviewadaptermaker.library.options.Divider
import com.sungbin.recyclerviewadaptermaker.library.options.Option
import com.sungbin.sungbintool.PermissionUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PermissionUtils.request(this, "TODO 데이터를 저장하기 위해 해당 권한이 필요합니다.",
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        )

        fab_add.setOnClickListener {
            val layout = LayoutInflater.from(applicationContext).inflate(R.layout.layout_todo_add, null)
            val editText = layout.findViewById<EditText>(R.id.et_title)
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("할 일 추가")
            dialog.setView(layout)
            dialog.setPositiveButton("완료") { _, _ ->
                DataUtils.save(editText.text.toString(), false)
                Toast.makeText(applicationContext, "추가 완료", Toast.LENGTH_SHORT).show()
                setAdapter()
            }
            dialog.show()
        }

        setAdapter()
        rv_list.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
    }

    private fun setAdapter(){
        AdapterHelper
            .with(rv_list)
            .bindLayout(R.layout.layout_list)
            .addViewBindListener { item, view, position ->
                val name = item[position].toString().replace(".txt", "")
                val textView = view.findViewById<TextView>(R.id.tv_name)
                textView.text = name
                val checkBox = view.findViewById<CheckBox>(R.id.cb_checkbox)
                if(DataUtils.getIsChecked(name)) {
                    checkBox.isChecked = true
                    textView.setTextColor(Color.parseColor("#9e9e9e"))
                    textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    DataUtils.save(name, isChecked)
                    if(isChecked) {
                        textView.setTextColor(Color.parseColor("#9e9e9e"))
                        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    }
                    else {
                        textView.setTextColor(Color.parseColor("#000000"))
                        textView.paintFlags = 0
                    }
                }
            }
            .addOption(Option(Divider(LinearLayout.VERTICAL), null))
            .create(DataUtils.getTodoList() ?: ArrayList<String>())
    }
}