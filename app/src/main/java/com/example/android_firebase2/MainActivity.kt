package com.example.android_firebase2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var etEmail:EditText
    private lateinit var etPass:EditText
    private lateinit var btnRegistrar:Button
    private lateinit var btnLogin:Button
    private lateinit var btnGoogle:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etEmail = findViewById(R.id.et_Email)
        etPass = findViewById(R.id.et_Pass)
        btnRegistrar = findViewById(R.id.btn_Registro)
        btnLogin = findViewById(R.id.btn_Login)
        btnGoogle = findViewById(R.id.btn_Google)

        setup()
    }

    fun setup(){
        title = "Autenticación"
        btnRegistrar.setOnClickListener{
            if(etEmail.text.isNotEmpty() && etPass.text.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(
                        etEmail.text.toString(),
                        etPass.text.toString()
                    ).addOnCompleteListener{
                        if(it.isSuccessful){

                        }else{
                            mostrarAlerta()
                        }
                    }
            }
        }
    }

    fun mostrarAlerta(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error de autienticado de usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog:AlertDialog = builder.create()
        dialog.show()
    }
}