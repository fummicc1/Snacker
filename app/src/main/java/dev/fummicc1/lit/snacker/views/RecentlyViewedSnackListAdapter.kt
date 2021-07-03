package dev.fummicc1.lit.snacker.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.fummicc1.lit.snacker.R
import dev.fummicc1.lit.snacker.models.RecentlyViewedSnackPresentable
import dev.fummicc1.lit.snacker.models.SnackPresentable
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RecentlyViewedSnackListAdapter(val context: Context, private val handler: Handler) :
    RecyclerView.Adapter<RecentlyViewedSnackListAdapter.ViewHolder>(), CoroutineScope {

    private val items: MutableList<RecentlyViewedSnackPresentable> = mutableListOf()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.IO

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView = view.findViewById<TextView>(R.id.snackItemTitleTextView)
        val priorityBackgroundView = view.findViewById<SnackPriorityIndicatorView>(R.id.priorityBackgroundView)
        val rootView = view.findViewById<ConstraintLayout>(R.id.snackItemRooView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_snack, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.apply {
            titleTextView.text = item.title
            priorityBackgroundView.visibility = View.INVISIBLE

            rootView.setOnClickListener {
                handler.onClick(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface Handler {
        fun onClick(item: RecentlyViewedSnackPresentable)
    }

    fun injectItems(items: List<RecentlyViewedSnackPresentable>) {
        launch {
            val result = DiffUtil.calculateDiff(RecyclerDiffCallback(this@RecentlyViewedSnackListAdapter.items, items))
            this@RecentlyViewedSnackListAdapter.items.clear()
            this@RecentlyViewedSnackListAdapter.items.addAll(items)
            withContext(Dispatchers.Main) {
                result.dispatchUpdatesTo(this@RecentlyViewedSnackListAdapter)
            }
        }
    }
}