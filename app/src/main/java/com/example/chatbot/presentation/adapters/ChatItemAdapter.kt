import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatbot.R
import com.example.chatbot.data.model.Chat
import java.util.*

class ChatItemAdapter(private val chatList: List<Chat>,
                      private val onDeleteChat: (String) -> Unit,
                      private val onChatClick: (String) -> Unit

) :
    RecyclerView.Adapter<ChatItemAdapter.ChatItemViewHolder>() {

    inner class ChatItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.tv_tittle)
        val deleteImageView: ImageView = itemView.findViewById(R.id.iv_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_history, parent, false)
        return ChatItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        val chat = chatList[position]
        Log.d("ChatItemAdapter", "onBindViewHolder: position=$position, lastMessage=${chat.lastMessage}")
        holder.titleTextView.text = chat.lastMessage?.let { capitalizeFirstLetter(it) }
        holder.deleteImageView.setOnClickListener{
            chat.chatId?.let { chatId ->
                onDeleteChat(chatId)
            }
        }
        holder.itemView.setOnClickListener{
            chat.chatId?.let { chatId ->
                onChatClick(chatId)
            }
        }
    }
    private fun capitalizeFirstLetter(text: String): String {
        if(text.isEmpty()) return text
        return text.substring(0, 1).uppercase(Locale.getDefault()) + text.substring(1)
    }

    override fun getItemCount() = chatList.size
}
