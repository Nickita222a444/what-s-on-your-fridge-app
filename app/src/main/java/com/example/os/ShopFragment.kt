package com.example.os

import android.content.ContentValues
import android.database.Cursor
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.*
import android.widget.*
import android.widget.AdapterView.AdapterContextMenuInfo
import androidx.core.view.size
import androidx.fragment.app.Fragment
import com.example.os.adapter.MyListAdapter


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShopFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShopFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var list:ListView
    lateinit var product:EditText
    var saved:Boolean = true
    private fun showList(list: ListView) {
        var db=DBHelper(requireContext()).readableDatabase
        var cursor: Cursor =db.rawQuery("SELECT product, count FROM buy",null)
        cursor.moveToFirst()
        val items:MutableList<String> = mutableListOf()
        while (!cursor.isAfterLast) {
            var item = ""
            item += cursor.getString(0) + " " + cursor.getString(1)
            items.add(item)
            cursor.moveToNext()

        }
        val adapter = MyListAdapter(requireContext(), items.toTypedArray())
        list.adapter = adapter

        db = DBHelper(requireContext()).readableDatabase
        var str:String
        cursor =db.rawQuery("SELECT bought FROM buy",null)
        cursor.moveToFirst()
        var i = 0

        while(!cursor.isAfterLast) {
            str = cursor.getString(0)
            if(str == "1") {
                list.performItemClick(list.adapter.getView(i, null, null), i, list.adapter.getItemId(i))
            }
            cursor.moveToNext()
            i++
        }
        adapter.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button: Button = view.findViewById(R.id.add_button)
        product = view.findViewById(R.id.prod)
        val count: EditText = view.findViewById(R.id.count)
        list = view.findViewById(R.id.prod_list)

        registerForContextMenu(list)

        var db = DBHelper(requireContext()).readableDatabase
        var cursor: Cursor =db.rawQuery("SELECT product, count FROM buy",null)
        cursor.moveToFirst()
        val items:MutableList<String> = mutableListOf()
        while (!cursor.isAfterLast) {
            var item = ""
            item += cursor.getString(0) + " " + cursor.getString(1)
            items.add(item)
            cursor.moveToNext()
        }
        list.adapter = MyListAdapter(requireContext(), items.toTypedArray())

        button.setOnClickListener {
            saved = true
            val str:String = product.text.toString()
            val str2:String=count.text.toString()
            val db=DBHelper(requireContext()).writableDatabase
            val it = ContentValues()
            product.setText("")
            count.setText("")
            it.put("product", str)
            it.put("count", str2)
            it.put("bought", 0)
            db.execSQL("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='buy'")
            db.insert("buy", "product, count, bought", it)
            db.close()
            showList(list)
            saved = false
        }

        db = DBHelper(requireContext()).readableDatabase
        var str:String
        cursor =db.rawQuery("SELECT bought FROM buy",null)
        cursor.moveToFirst()
        var i = 0

        while(!cursor.isAfterLast) {
            str = cursor.getString(0)
            if(str == "1") {
                list.performItemClick(list.getChildAt(i), i, list.getItemIdAtPosition(i))
            }
            cursor.moveToNext()
            i++
        }
        saved = false

        list.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, v: View, position: Int, id: Long) {
                val title:CheckedTextView = v.findViewById(R.id.text)
                var db = DBHelper(requireContext()).readableDatabase
                val cursor = db.rawQuery("SELECT bought FROM buy where id = " + (position + 1), null)
                cursor.moveToFirst()
                val bought = cursor.getString(0)
                db.close()
                db = DBHelper(requireContext()).writableDatabase
                val cont = ContentValues()

                if(bought == "0") {
                    cont.put("bought", 1)
                    title.setTextColor(Color.parseColor("#606366"))
                    title.paintFlags = title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                else if ((bought == "1") && !saved){
                    cont.put("bought", 0)
                    title.setTextColor(Color.parseColor("#202020"))
                    title.paintFlags = 0
                }
                else if ((bought == "1") && saved){
                    cont.put("bought", 1)
                    title.setTextColor(Color.parseColor("#606366"))
                    title.paintFlags = title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }

                db.update("buy", cont, "id = ?", arrayOf((position + 1).toString()))
                cont.clear()
                db.close()
            }
        }
    }

    val IDM_DEL = 101
    val IDM_DEL_ALL = 103

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu.add(0, IDM_DEL, 0, "Удалить")
        menu.add(0, IDM_DEL_ALL, 0, "Удалить всё")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val size:Int = list.size
        val info = item.menuInfo as AdapterContextMenuInfo
        when(item.itemId) {
            IDM_DEL -> {
                saved = true
                val db=DBHelper(requireContext()).writableDatabase
                val pos:Int = info.position + 1
                db.execSQL(" DELETE FROM buy WHERE id = $pos")
                val cont = ContentValues()
                for(i in (pos + 1).. size) {
                    cont.put("id", i - 1)
                    db.update("buy", cont, "id = ?", arrayOf(i.toString()))
                    cont.clear()
                }
                db.close()
                showList(list)
                saved = false
            }
            IDM_DEL_ALL -> {
                val db=DBHelper(requireContext()).writableDatabase
                db.execSQL("DELETE FROM buy")
                db.execSQL("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='buy'")
                db.close()
                val items:MutableList<String> = mutableListOf()
                val adapter:ArrayAdapter<String> = ArrayAdapter(requireContext(),
                    R.layout.shop_list_item, items)
                list.adapter = adapter
            }
        }
        return super.onContextItemSelected(item)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ShopFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShopFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}