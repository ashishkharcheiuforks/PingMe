package com.cnx.pingme

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cnx.pingme.api.MessageModel
import com.cnx.pingme.chat.ChatAdapter
import com.cnx.pingme.chat.ChatViewModel
import com.cnx.pingme.dependencyInjection.injectViewModel
import com.cnx.pingme.utils.*
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject



class MainActivity : AppCompatActivity(),  HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment>  = dispatchingAndroidInjector

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var chatViewModel: ChatViewModel
    private  var userSession : String = SESSION_BOB
    private lateinit var chatRVAdapter : ChatAdapter

    private var messages = ArrayList<MessageModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        chatViewModel = injectViewModel(viewModelFactory)
        chatViewModel.userSessionLd.postValue(userSession)
        getChats()
        setSupportActionBar(toolbar)
        toolbar.title = userSession

        setUpNavigation()

        rvMsg.layoutManager = LinearLayoutManager(this)
        chatRVAdapter = ChatAdapter()
        rvMsg.adapter = chatRVAdapter

        fabSend.setOnClickListener {

            if(!TextUtils.isEmpty(etMessage.editableText)) {

                val message = MessageModel(userSession,
                    CHATBOT_ID, USER_NAME,"",
                    etMessage.editableText.toString(),true)

                etMessage.setText("")
                sendAndReceiveMsg(message)
            }
        }


    }

    private fun setUpNavigation() {

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        var actionBarToggle = ActionBarDrawerToggle(this
            ,drawerLayout , toolbar, R.string.opened, R.string.closed)

        drawerLayout.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()

        nav_view.setNavigationItemSelectedListener {

            drawerLayout.closeDrawers()

            when (it.itemId) {

                R.id.tom -> { userSession = SESSION_TOM }

                R.id.bob -> { userSession = SESSION_BOB }

                R.id.harry -> { userSession = SESSION_HARRY }

                R.id.mark -> { userSession = SESSION_MARK }

            }

            chatViewModel.userSessionLd.postValue(userSession)
            toolbar.title = userSession

            return@setNavigationItemSelectedListener  true
        }

    }


    private fun setupChat() {

        chatViewModel.getUserSession()?.observe(this, Observer {

            Log.d("session","changed with value $it")
             userSession = it
             toolbar.title = userSession
             getChats()
        })
    }



    private fun sendAndReceiveMsg(messageModel: MessageModel) {

        Log.d("SendAndReceive"," in Activity ${messageModel.userSession}")
        chatViewModel.sendAndReceiveChat(messageModel)

    }

    private fun getChats() {


//        this.userSession= userSession
        chatViewModel.chatList.observe(this, Observer {

            Log.d("value changed","$it")


            chatRVAdapter.submitList(it)

            Log.d("count","${it.size}")
            rvMsg.smoothScrollToPosition(it.size -1)


        })

          chatViewModel.userSessionLd.observe(this, Observer {
            userSession = it
        })
    }

}
