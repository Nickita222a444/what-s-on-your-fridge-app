package com.example.os

import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.os.adapter.RecyclerAdapter


class ChecklistFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var list:ListView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_checklist, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list = view.findViewById(R.id.listView)
        val button: Button = view.findViewById(R.id.submit)
        val backButton: Button = view.findViewById(R.id.backButton)
        var mList: ArrayList<EdaData>
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.visibility = View.INVISIBLE

        backButton.visibility = View.INVISIBLE
        val db = DBHelper(requireContext()).readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT name FROM ingredient", null)
        cursor.moveToFirst()
        val items: MutableList<String> = mutableListOf()
        while (!cursor.isAfterLast) {
            var item = ""
            item += cursor.getString(0)
            items.add(item)
            cursor.moveToNext()
        }
        adapter = ArrayAdapter(requireContext(), R.layout.shop_list_item, items)
        list.adapter = adapter

        var ing = ""

        list.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, v: View, position: Int, id: Long) {
                val child: CheckedTextView = v.findViewById(R.id.text)
                if (ing != "") {
                    ing += ","
                }
                ing += "\""
                ing += child.text
                ing += "\""
            }
        }

        button.setOnClickListener {
            val db = DBHelper(requireContext()).readableDatabase
            var cursor = db.rawQuery("SELECT dishes FROM ingredient_dish WHERE ingredient_id IN (SELECT id FROM ingredient WHERE name IN ($ing))", null)
            cursor.moveToFirst()
            var str = ""
            while(!cursor.isAfterLast) {
                str += cursor.getString(0)
                str += " "
                cursor.moveToNext()
            }
            ing = ""

            var arr = str.split(" ")
            arr = arr.dropLast(1)
            val unique_arr = arr.toSet()

            str = ""
            for (i in unique_arr) {
                if (str != "") {
                    str += ","
                }
                str += i
            }

            cursor = db.rawQuery("SELECT name, recipe, picture FROM dish WHERE id IN ($str)", null)
            cursor.moveToFirst()

            mList = ArrayList()
            while(!cursor.isAfterLast) {
                mList.add(EdaData(cursor.getString(0), cursor.getString(1), cursor.getString(2)))
                cursor.moveToNext()
            }
            list.visibility = View.INVISIBLE
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.visibility = View.VISIBLE
            val lAdapter = RecyclerAdapter(mList)
            recyclerView.adapter = lAdapter
            button.visibility = View.INVISIBLE
            backButton.visibility = View.VISIBLE
        }

        backButton.setOnClickListener{
            backButton.visibility = View.INVISIBLE
            button.visibility = View.VISIBLE
            recyclerView.visibility = View.INVISIBLE
            list.visibility = View.VISIBLE
            list.adapter = adapter
        }
    }
}

