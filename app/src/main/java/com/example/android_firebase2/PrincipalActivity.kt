package com.example.android_firebase2

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PrincipalActivity : AppCompatActivity() {
    private lateinit var tvEmail:TextView
    private lateinit var tvProveedor:TextView
    private lateinit var btnCerrar:Button
    private lateinit var etTelefono:EditText
    private lateinit var etMunicipio:EditText
    private lateinit var btnGuardar:Button
    private lateinit var btnConsultar:Button
    private lateinit var btnBorrar:Button

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        tvEmail = findViewById(R.id.tv_Email)
        tvProveedor = findViewById(R.id.tv_Proveedor)
        etTelefono = findViewById(R.id.et_Telefono)
        etMunicipio = findViewById(R.id.et_Municipio)
        btnCerrar = findViewById(R.id.btn_Cerrar)
        btnGuardar = findViewById(R.id.btn_Guardar)
        btnConsultar = findViewById(R.id.btn_Consultar)
        btnBorrar = findViewById(R.id.btn_Borrar)



        val bundle:Bundle? = intent.extras
        val email:String?= bundle?.getString("email")
        val proveedor:String?=bundle?.getString("proveedor")

        setup(email?:"",proveedor?:"")

        val sPreferencias:SharedPreferences.Editor? = getSharedPreferences(getString(R.string.prefs_file),
            Context.MODE_PRIVATE).edit()
        sPreferencias?.putString("email",email)
        sPreferencias?.putString("proveedor",proveedor)
        sPreferencias?.apply()

    }

    private fun setup(email:String,proveedor:String){
        title = "Principal"
        tvEmail.text = email
        tvProveedor.text = proveedor

        btnCerrar.setOnClickListener {
            val sPreferencias:SharedPreferences.Editor =
                getSharedPreferences(getString(R.string.prefs_file),
                Context.MODE_PRIVATE).edit()
            sPreferencias.clear()
            sPreferencias.apply()
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

        btnGuardar.setOnClickListener {
            db.collection("usuarios").document(email).set(
                hashMapOf("proveedor" to proveedor,
                "telefono" to etTelefono.text.toString(),
                "Municipio" to etMunicipio.text.toString())
            )

        }
        btnConsultar.setOnClickListener {
            db.collection("usuarios").document(email).get().addOnSuccessListener {
                etTelefono.setText(it.get("telefono") as String?)
                etMunicipio.setText(it.get("Municipio") as String?)
            }

        }
        btnBorrar.setOnClickListener {
            db.collection("usuarios").document(email).delete()
        }
    }


}