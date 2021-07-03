package dev.fummicc1.lit.snacker.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dev.fummicc1.lit.snacker.R
import dev.fummicc1.lit.snacker.databinding.ViewSnackTagChipBinding
import dev.fummicc1.lit.snacker.models.SnackPresentable
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SnackListAdapter(val context: Context) : RecyclerView.Adapter<SnackListAdapter.ViewHolder>(), CoroutineScope {

    private val snackList: MutableList<SnackPresentable> = mutableListOf()
    private var listener: EventListener? = null

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.IO

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView = view.findViewById<TextView>(R.id.snackItemTitleTextView)
        val chipGroups = view.findViewById<ChipGroup>(R.id.snackTagChipGroup)
        val priorityBackgroundView = view.findViewById<SnackPriorityIndicatorView>(R.id.priorityBackgroundView)
        val rootView = view.findViewById<ConstraintLayout>(R.id.snackItemRooView)
    }

    override fun getItemCount(): Int = snackList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val snack = snackList[position]
        holder.titleTextView.text = snack.title
        holder.rootView.setOnClickListener {
            listener?.onSelectSnack(snack)
        }
        holder.chipGroups.removeAllViews()
        for (tag in snack.tags) {
            val layoutInflater = LayoutInflater.from(context)
            val chip = ViewSnackTagChipBinding.inflate(layoutInflater, null, false).apply {
                this.tagName = tag.name
            }.root as? Chip
            if (chip == null) {
                continue
            }
            chip.isCheckable = false
            holder.chipGroups.addView(chip)
        }

        holder.priorityBackgroundView.priority = snack.priority

        // Handle LongPress Gesture.
        holder.rootView.setOnLongClickListener {
            listener?.onShowOptionMenu(snack)
            false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_snack, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    fun injectSnackList(list: List<SnackPresentable>) {
        launch {
            val result = DiffUtil.calculateDiff(RecyclerDiffCallback(this@SnackListAdapter.snackList, list))
            snackList.clear()
            snackList.addAll(list)
            withContext(Dispatchers.Main) {
                result.dispatchUpdatesTo(this@SnackListAdapter)
            }
        }
    }

    fun configureEventListner(listener: EventListener) {
        this.listener = listener
    }

    interface EventListener {
        fun onSelectSnack(snack: SnackPresentable)
        fun onShowOptionMenu(snack: SnackPresentable)
    }
}