package com.example.mywechat.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mywechat.databinding.FragmentLoginBinding
import com.example.mywechat.databinding.FragmentSignupBinding
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Url

object SignupApi{
    const val BASE_URL:String = "8.140.133.34:7264"
    const val LOGIN_URL:String = "/user/logon"
}

data class UserBean(
        val username: String,
        val password: String
)

interface UserApiService{

    @POST
    @FormUrlEncoded
    fun login(@Url string: String, @Field("phone") mobile:String, @Field("pwd")pwd: String): Call<UserBean>

}

class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.signupButtonClick.setOnClickListener{

        }

    }
}