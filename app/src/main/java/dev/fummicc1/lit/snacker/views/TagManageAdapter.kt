package dev.fummicc1.lit.snacker.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.fummicc1.lit.snacker.R
import dev.fummicc1.lit.snacker.models.SnackTagKindPresentable
import kotlinx.android.synthetic.main.view_item_tag_manage.view.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class TagManageAdapter(private val context: Context, private val handler: Handler) :
    RecyclerView.Adapter<TagManageAdapter.ViewHolder>(), CoroutineScope {

    private val tagKinds: MutableList<SnackTagKindPresentable> = mutableListOf()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.IO

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tagTextView = view.findViewById<TextView>(R.id.tagTextView)
        val tagToggleButton = view.findViewById<ToggleButton>(R.id.tagManageToggleButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.view_item_tag_manage, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val tag = tagKinds[position]

        holder.apply {
            tagTextView.text = tag.name
            tagToggleButton.isChecked = tag.isActive

            if (tag.isActive) {
                tagTextView.setTextColor(ContextCompat.getColor(context, R.color.black))
            } else {
                tagTextView.setTextColor(ContextCompat.getColor(context, R.color.light_black))
            }

            tagToggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
                handler.onSwitchActivateToggle(tag, isChecked)

                if (isChecked) {
                    tagTextView.setTextColor(ContextCompat.getColor(context, R.color.black))
                } else {
                    tagTextView.setTextColor(ContextCompat.getColor(context, R.color.light_black))
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return tagKinds.size
    }

    fun injectTags(tagKinds: List<SnackTagKindPresentable>) {
        launch {
            val result = DiffUtil.calculateDiff(
                RecyclerDiffCallback(
                    this@TagManageAdapter.tagKinds,
                    tagKinds
                )
            )
            this@TagManageAdapter.tagKinds.clear()
            this@TagManageAdapter.tagKinds.addAll(tagKinds)
            withContext(Dispatchers.Main) {
                result.dispatchUpdatesTo(this@TagManageAdapter)
            }
        }
    }

    interface Handler {
        fun onSwitchActivateToggle(tagKind: SnackTagKindPresentable, isActive: Boolean)
    }
}