package com.carbon7.virtualdisplay.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carbon7.virtualdisplay.model.HttpException
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine



class LoginViewModel : ViewModel() {
    private val ip = "192.168.11.156"

    //Pair msg, isError
    private val _loginMsg = MutableLiveData<Pair<String, Boolean>>()
    val loginMsg: LiveData<Pair<String, Boolean>> = _loginMsg




    private var userId : String? = null
    private var token : String? = null

    fun loginAndReqCall(username: String, password: String, onSuccess: (uid: String,tok: String)->Unit) = viewModelScope.launch{
        try {
            val (u, t) = login(username, password)
            userId = u
            token = t


            //val initSoc = IO.socket("http://$ip:4000")
            //initSoc.connect()
            //register(initSoc, userId!!)
            //requestCall(initSoc)
            _loginMsg.postValue(Pair("Logged in", false))
            onSuccess(userId!!, token!!)

        }catch (e: HttpException){
            Log.d("MyApp",e.toString())
            _loginMsg.postValue(Pair(when(e.code){
                401 -> "Bad credentials"
                404 -> "Error in the connection with server"
                else -> "Unknown error"
            }, true))
        }catch (e: Exception){
            Log.d("MyApp",e.toString())
        }
    }

    private suspend fun login(username: String, password: String) = suspendCoroutine<Pair<String,String>> { cont ->
        val req = Request.Builder()
            .url("http://$ip:4000/loginUser")
            .post(FormBody.Builder()
                .add("username",username)
                .add("password",password).build())
            .build()

        OkHttpClient().newCall(req).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                cont.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use { res ->
                    if(!res.isSuccessful)
                        cont.resumeWithException(HttpException(res.code(),res.message()))

                    else{
                        Log.d("OkHttp", res.toString())
                        val json = JSONObject(res.body()!!.string())
                        Log.d("OkHttp", json.toString())
                        cont.resume(Pair(json.getString("user"),json.getString("token")))
                    }
                }
            }
        })
    }



}