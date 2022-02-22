package com.notes.notes.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.notes.notes.EditActivity
import com.notes.notes.utils.IntentConstance
import com.notes.notes.ListItem
import com.notes.notes.R
import com.notes.notes.database.DbManager
import com.notes.notes.utils.TypeConstance

class ItemsAdapter(listItem: ArrayList<ListItem>, private val context: Context): RecyclerView.Adapter<ItemsAdapter.ItemHolder>() {

    private var listArray = listItem

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return if (viewType == TypeConstance.NORMAL_ITEM) {
            ItemHolder.createItemNormal(parent)
        } else {
            ItemHolder.createItemImportant(parent)
        }
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(listArray[position])
    }

    override fun getItemCount(): Int {
        return listArray.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (listArray[position].check){
            TypeConstance.IMPORTANT_ITEM
        } else {
            TypeConstance.NORMAL_ITEM
        }
    }

    class ItemHolder(view: View, private val context: Context): RecyclerView.ViewHolder(view) {

        private val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        private val tvTime: TextView = view.findViewById(R.id.tvTime)

        fun setData(item: ListItem) {
            tvTitle.text = item.title
            tvTime.text = item.time

            itemView.setOnClickListener {
                val intent = Intent(context, EditActivity::class.java).apply {
                    putExtra(IntentConstance.TITLE_KEY, item.title)
                    putExtra(IntentConstance.DESC_KEY, item.desc)
                    putExtra(IntentConstance.CHECK_KEY, item.check)
                    putExtra(IntentConstance.ID_KEY, item.id)
                }
                context.startActivities(arrayOf(intent))
            }
        }

        companion object {

            fun createItemNormal(parent: ViewGroup): ItemHolder {
                return ItemHolder(LayoutInflater.from(parent.context)
                            .inflate(R.layout.note_item_normal, parent, false), parent.context)
            }

            fun createItemImportant(parent: ViewGroup): ItemHolder{
                return ItemHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.note_item_important, parent, false), parent.context)
            }
        }
}

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(listItems:List<ListItem>){
        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }

    fun deleteFromAdapter(pos:Int,dbManager: DbManager){
        dbManager.deleteFromDb(listArray[pos].id.toString())
        listArray.removeAt(pos)
        notifyItemRangeChanged(0,listArray.size)
        notifyItemRemoved(pos)
    }
}


