package studios.eaemenkk.overtracker.view.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import io.noties.markwon.Markwon
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.picasso.PicassoImagesPlugin
import kotlinx.android.synthetic.main.chat_message.view.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.Message

class ChatAdapter(private val context: Context): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private var dataSet = ArrayList<Message>()
    private val markdown = Markwon.builder(context)
        .usePlugin(PicassoImagesPlugin.create(context))
        .usePlugin(PicassoImagesPlugin.create(Picasso.get()))
        .usePlugin(PicassoImagesPlugin.create(object: PicassoImagesPlugin.PicassoStore {
            override fun cancel(drawable: AsyncDrawable) {
                Picasso.get()
                    .cancelTag(drawable)
            }

            override fun load(drawable: AsyncDrawable): RequestCreator {
                return Picasso.get()
                    .load(drawable.destination)
                    .tag(drawable)
            }
        }))
        .build()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = dataSet[position]

        if(message.sender) {
            holder.cornerRight.visibility = View.VISIBLE
            holder.cornerLeft.visibility = View.GONE
            holder.chatBubble.setCardBackgroundColor(context.getColor(R.color.colorDetail))
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.gravity = Gravity.END
            params.weight = 1.0f
            params.marginEnd = 35
            params.marginStart = 35
            params.topMargin = 10
            params.bottomMargin = 10
            holder.chatBubble.layoutParams = params
        }  else {
            holder.cornerRight.visibility = View.GONE
            holder.cornerLeft.visibility = View.VISIBLE
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.gravity = Gravity.START
            params.weight = 1.0f
            params.marginEnd = 35
            params.marginStart = 35
            params.topMargin = 10
            params.bottomMargin = 10
            holder.chatBubble.layoutParams = params
            holder.chatBubble.setCardBackgroundColor(context.getColor(R.color.colorPrimary))
        }

        markdown.setMarkdown(holder.msg, message.message)
    }

    fun addMessage(message: Message) {
        dataSet.add(message)
        notifyDataSetChanged()
    }

    fun setMessages(messages: ArrayList<Message>) {
        dataSet = messages
        notifyDataSetChanged()
    }

    class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val msg: TextView = itemView.tvMessage
        val cornerRight: ImageView = itemView.ivCornerRight
        val cornerLeft: ImageView = itemView.ivCornerLeft
        val chatBubble: CardView = itemView.cvMessage
    }

}