package dev.fummicc1.lit.snacker.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.fummicc1.lit.snacker.R
import dev.fummicc1.lit.snacker.models.AssociatedSnack
import dev.fummicc1.lit.snacker.models.SnackPresentable
import dev.fummicc1.lit.snacker.services.HTMLClient
import dev.fummicc1.lit.snacker.services.RemoteImageLoader
import kotlinx.android.synthetic.main.item_associated_snack.view.*
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class AssociatedSnackListAdapter(val context: Context) :
    RecyclerView.Adapter<AssociatedSnackListAdapter.ViewHolder>(), CoroutineScope {
    private val job: Job = Job()
    private var snackList: List<AssociatedSnack> = listOf()
    private var listener: EventListener? = null
    private val imageLoader: RemoteImageLoader = RemoteImageLoader(Picasso.get())
    private val htmlClient: HTMLClient = HTMLClient()

    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView = view.findViewById<TextView>(R.id.snackItemTitleTextView)
        val thumbnailImageView = view.findViewById<ImageView>(R.id.thumbnailImageView)
        val rootView = view.findViewById<ConstraintLayout>(R.id.snackItemRooView)
    }

    override fun getItemCount(): Int = snackList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val snack = snackList[position]
        holder.titleTextView.text = snack.title
        holder.rootView.setOnClickListener {
            listener?.onSelectSnack(snack)
        }
        holder.titleTextView.setLines(1)

        holder.thumbnailImageView.apply {

            launch {

                var thumbnailUrl = snack.thumbnailUrl
                if (thumbnailUrl == null) {
                    thumbnailUrl = htmlClient.getOGPImageUrl(snack.url)
                }
                withContext(Dispatchers.Main) {
                    imageLoader.loadImageIntoImageView(
                        thumbnailUrl ?: "",
                        this@apply,
                        {
                        this@apply.visibility = View.VISIBLE
                        },
                        { exception ->
                            this@apply.visibility = View.VISIBLE
                            this@apply.setImageResource(R.mipmap.ic_launcher)
                        })
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_associated_snack, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    fun injectSnackList(list: List<AssociatedSnack>) {
        snackList = list
        notifyDataSetChanged()
    }

    fun configureEventListner(listener: EventListener) {
        this.listener = listener
    }

    interface EventListener {
        fun onSelectSnack(snack: AssociatedSnack)
    }
}