package studios.eaemenkk.overtracker.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_chat.*
import studios.eaemenkk.overtracker.R
import studios.eaemenkk.overtracker.domain.Message
import studios.eaemenkk.overtracker.view.adapter.ChatAdapter
import studios.eaemenkk.overtracker.viewmodel.ChatViewModel

class ChatActivity: AppCompatActivity() {
    private val layoutManager = LinearLayoutManager(this)
    private val viewModel: ChatViewModel by lazy {
        ViewModelProvider(this).get(ChatViewModel::class.java)
    }
    private val adapter = ChatAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        bnvFeed.selectedItemId = R.id.btChat
        bnvFeed.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.btGlobal -> {
                    val intent = Intent("GLOBAL_FEED")
                        .addCategory("GLOBAL_FEED")
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
                R.id.btLocal -> {
                    val intent = Intent("LOCAL_FEED")
                        .addCategory("LOCAL_FEED")
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
                R.id.btFollowing -> {
                    val intent = Intent("FOLLOWED_PLAYERS")
                        .addCategory("FOLLOWED_PLAYERS")
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
                R.id.btHeroes -> {
                    val intent = Intent("HERO_LIST")
                        .addCategory("HERO_LIST")
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
            }
            return@setOnNavigationItemSelectedListener false
        }
        btnSendMessage.setOnClickListener { sendMessageToBot() }
        configureRecyclerView()
    }

    private fun configureRecyclerView() {
        rvChatMessages.adapter = adapter
        rvChatMessages.layoutManager = layoutManager
        layoutManager.stackFromEnd = true
        viewModel.incomingMsg.observe(this, Observer { msg -> addMessage(msg) })
    }

    private fun sendMessageToBot() {
        val message = etChatMessage.text.toString()
        addMessage(Message(message, System.currentTimeMillis(), true))
        etChatMessage.editableText.clear()

        viewModel.sendMessageToBot(message)
    }

    private fun addMessage(msg: Message) {
        adapter.addMessage(msg)
        rvChatMessages.smoothScrollToPosition(adapter.itemCount - 1)
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, 0)
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(0, 0)
    }
}