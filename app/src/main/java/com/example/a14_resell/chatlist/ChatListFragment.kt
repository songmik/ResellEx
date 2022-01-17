package com.example.a14_resell.chatlist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a14_resell.DBKey.Companion.CHILD_CHAT
import com.example.a14_resell.DBKey.Companion.DB_USERS
import com.example.a14_resell.R
import com.example.a14_resell.chatdetail.ChatRoomActivity
import com.example.a14_resell.databinding.FragmentChatlistBinding
import com.example.a14_resell.home.ArticleAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatListFragment : Fragment(R.layout.fragment_chatlist) {

    private var binding: FragmentChatlistBinding? = null
    private val chatRoomList = mutableListOf<ChatListItem>()
    private lateinit var chatListAdapter: ChatListAdapter
    private val auth : FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentChatListBinding = FragmentChatlistBinding.bind(view)
        binding = fragmentChatListBinding

        chatListAdapter = ChatListAdapter(onItemClicked = { chatRoom ->
            context?.let {
                val intent = Intent(it, ChatRoomActivity::class.java)
                intent.putExtra("chatKey", chatRoom.key)
                startActivity(intent)
            }


        })

        chatRoomList.clear()

        fragmentChatListBinding.chatListRecyclerView.adapter = chatListAdapter
        fragmentChatListBinding.chatListRecyclerView.layoutManager = LinearLayoutManager(context)

        if(auth.currentUser == null){
            return
        }
        val chatDB = Firebase.database.reference.child(DB_USERS).child(auth.currentUser!!.uid).child(CHILD_CHAT)
        chatDB.addListenerForSingleValueEvent(object: ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val model = it.getValue(ChatListItem::class.java)
                    model ?: return

                    chatRoomList.add(model)
                }
                chatListAdapter.submitList(chatRoomList)
                chatListAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        chatListAdapter.notifyDataSetChanged()
    }
}