package dev.fummicc1.lit.snacker.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import dev.fummicc1.lit.snacker.R
import dev.fummicc1.lit.snacker.models.SnackTagKindPresentable
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class EditSnackTagAdapter(val context: Context): RecyclerView.Adapter<EditSnackTagAdapter.ViewHolder>(), CoroutineScope {

    private var activeTagKinds: List<SnackTagKindPresentable> = listOf()
    private var allTagKinds: List<SnackTagKindPresentable> = listOf()
    private var handler: Handler? = null

    override val coroutineContext: CoroutineContext = Job() + Dispatchers.IO

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val root = view.findViewById<ConstraintLayout>(R.id.editSnackTagBottomSheetItemRootView)
        val checkImageView: ImageView = view.findViewById(R.id.editSnackTagCheckImageView)
        val textView = view.findViewById<TextView>(R.id.editSnackTagKindItemTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_item_edit_snack_tag, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = allTagKinds[position]
        val isChecked = activeTagKinds.contains(item)
        if (isChecked) {
            holder.checkImageView.visibility = View.VISIBLE
        } else {
            holder.checkImageView.visibility = View.INVISIBLE
        }

        holder.textView.text = item.name

        holder.root.setOnClickListener {
            handler?.onTap(item)
        }
    }

    override fun getItemCount(): Int {
        return allTagKinds.size
    }

    fun updateAllTagKindItems(allTagKinds: List<SnackTagKindPresentable>) {
        this.allTagKinds = allTagKinds
        notifyDataSetChanged()
    }

    fun updateActiveItems(activeTagKinds: List<SnackTagKindPresentable>) {
        this.activeTagKinds = activeTagKinds
        notifyDataSetChanged()
    }

    fun updateHandler(handler: Handler) {
        this.handler = handler
    }

    interface Handler {
        fun onTap(snackTagKindPresentable: SnackTagKindPresentable)
    }
}