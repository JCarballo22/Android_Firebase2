package com.example.android_firebase2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    private lateinit var etEmail:EditText
    private lateinit var etPass:EditText
    private lateinit var btnRegistrar:Button
    private lateinit var btnLogin:Button
    private lateinit var btnGoogle:Button

    private lateinit var llAutenticar:LinearLayout
    private val GOOGLE_SING_IN = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etEmail = findViewById(R.id.et_Email)
        etPass = findViewById(R.id.et_Pass)
        btnRegistrar = findViewById(R.id.btn_Registro)
        btnLogin = findViewById(R.id.btn_Login)
        btnGoogle = findViewById(R.id.btn_Google)
        llAutenticar = findViewById(R.id.ll_autenticar)

        ejecutarAnalitica()
        setup()
        sesion()
    }

    fun ejecutarAnalitica(){
        val analicis = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("mensaje","Integración de firebase completa")
        analicis.logEvent("InitScreen",bundle)
    }

    override fun onStart() {
        super.onStart()
        llAutenticar.visibility = View.VISIBLE
    }

    private fun sesion(){
        val sPreferencias:SharedPreferences = getSharedPreferences(
            getString(R.string.prefs_file), Context.MODE_PRIVATE
        )
        val email:String? = sPreferencias.getString("email",null)
        val proveedor:String? = sPreferencias.getString("proveedor",null)
        if(email !=null && proveedor != null){
            llAutenticar.visibility = View.INVISIBLE
            mostrarPrincipal(email,TipoProveedor.valueOf(proveedor))
        }
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
                            mostrarPrincipal(it.result?.user?.email?:"",TipoProveedor.BASICO)
                        }else{
                            mostrarAlerta()
                        }
                    }
            }
        }
        btnLogin.setOnClickListener {
            if (etEmail.text.isNotEmpty() && etPass.text.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                        etEmail.text.toString(),
                        etPass.text.toString()).addOnCompleteListener {
                            if(it.isSuccessful){
                                mostrarPrincipal(it.result?.user?.email?:"",TipoProveedor.BASICO)
                            }else{
                                mostrarAlerta()
                            }
                    }
            }
        }
        btnGoogle.setOnClickListener {
            val googleConf:GoogleSignInOptions =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail().build()
            val googleCliente: GoogleSignInClient = GoogleSignIn.getClient(this,googleConf)
            googleCliente.signOut()
            startActivityForResult(googleCliente.signInIntent,GOOGLE_SING_IN)
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

    fun mostrarPrincipal(email:String,proveedor:TipoProveedor){
        val ventana: Intent = Intent(this,PrincipalActivity::class.java).apply {
            putExtra("email",email)
            putExtra("proveedor",proveedor.name.toString())
        }
        startActivity(ventana)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GOOGLE_SING_IN){
            val tarea = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val miCuenta = tarea.getResult(ApiException::class.java)
                if(miCuenta != null){
                    val credencial = GoogleAuthProvider.getCredential(
                        miCuenta.idToken, null
                    )
                    FirebaseAuth.getInstance().signInWithCredential(credencial).addOnCompleteListener {
                        if(it.isSuccessful){
                            mostrarPrincipal(miCuenta.email?:"",TipoProveedor.GOOGLE)
                        }else{
                            mostrarAlerta()
                        }
                    }
                }
            }catch (e:ApiException){
                mostrarAlerta()
            }
        }
    }

}

enum class TipoProveedor{
    BASICO,
    GOOGLE
}