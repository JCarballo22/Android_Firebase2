package com.example.android_firebase2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class PrincipalActivity : AppCompatActivity() {
    private lateinit var tvEmail:TextView
    private lateinit var tvProveedor:TextView
    private lateinit var btnCerrar:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        tvEmail = findViewById(R.id.tv_Email)
        tvProveedor = findViewById(R.id.tv_Proveedor)
        btnCerrar = findViewById(R.id.btn_Cerrar)


        val bundle:Bundle? = intent.extras
        val email:String?= bundle?.getString("email")
        val proveedor:String?=bundle?.getString("proveedor")

        setup(email?:"",proveedor?:"")

    }

    private fun setup(email:String,proveedor:String){
        title = "Principal"
        tvEmail.text = email
        tvProveedor.text = proveedor

        btnCerrar.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }


}