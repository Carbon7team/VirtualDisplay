package com.carbon7.virtualdisplay.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carbon7.virtualdisplay.BuildConfig
import com.carbon7.virtualdisplay.model.HttpException
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine



class LoginViewModel : ViewModel() {

    //Pair msg, isError
    private val _loginMsg = MutableLiveData<Pair<String, Boolean>>()
    val loginMsg: LiveData<Pair<String, Boolean>> = _loginMsg




    private var userId : String? = null
    private var token : String? = null

    /**
     * Try to login to the server
     * @param username
     * @param password
     */
    fun login(username: String, password: String, onSuccess: (uid: String,tok: String)->Unit) = viewModelScope.launch{
        try {
            val (u, t) = loginRequest(username, password)
            userId = u
            token = t

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

    /**
     * Make a login request to the server
     * @param username
     * @param password
     *
     * @return a pair containing the userId and the token
     *
     * @throws HttpException threw if the request give result other than 2xx
     * @throws IOException  threw if the request doesn't give response
     */
    private suspend fun loginRequest(username: String, password: String) = suspendCoroutine<Pair<String,String>> { cont ->
        val req = Request.Builder()
            .url("http://${BuildConfig.SERVER_ADDRESS}:4000/loginUser")
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