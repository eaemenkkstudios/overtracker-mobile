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
import kotlinx.android.synthetic.main.chat_message.view.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.Message

class ChatAdapter(private val context: Context): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private var dataSet = ArrayList<Message>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = dataSet[position]
        holder.msg.text = message.message

        if(message.sender) {
            holder.cornerRight.visibility = View.VISIBLE
            holder.cornerLeft.visibility = View.INVISIBLE
            holder.chatBubble.setCardBackgroundColor(context.getColor(R.color.colorDetail))
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.gravity = Gravity.END
            params.weight = 1.0f
            params.marginEnd = 35
            params.marginStart = 10
            params.topMargin = 10
            params.bottomMargin = 10
            holder.chatBubble.layoutParams = params
        }  else {
            holder.cornerRight.visibility = View.INVISIBLE
            holder.cornerLeft.visibility = View.VISIBLE
            holder.chatBubble.setCardBackgroundColor(context.getColor(R.color.colorPrimary))
        }
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