package com.example.meal_management

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.meal_management.ui.theme.Purple40
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import java.lang.Exception
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Shopping_Add(navController: NavController) {
    val currentDate = MutableStateFlow(LocalDate.now())
    val dte = currentDate.value.dayOfMonth.toString() + " " + currentDate.value.month

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    var mymessId2 by remember { mutableStateOf("") }
    val db = FirebaseFirestore.getInstance()
    val usersCollection = user?.let { db.collection("users").document(it.uid) }
    usersCollection?.get()?.addOnSuccessListener { documentSnapshot ->
        if (documentSnapshot.exists()) {

            mymessId2 = documentSnapshot.getString("messId2").toString()
        }
    }?.addOnFailureListener { e ->
    }
    // UI Design
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    titleContentColor = Color.White,
                ),
                title = {
                    Text("Add Shopping")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Localized description",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        ScrollContent(innerPadding)
        val gradientColors = listOf(
            Color(0, 153, 76),
            Color(188, 255, 192)
        )
        val brush = Brush.linearGradient(
            colors = gradientColors
        )

        var name by remember { mutableStateOf("") }
        var moneyAmount by remember { mutableStateOf("") }
        var itemS by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = brush)
                .padding(top = 65.dp)
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color(249, 239, 219), RoundedCornerShape(4.dp))
                    .padding(5.dp)
            ) {
                TextField(
                    value = moneyAmount,
                    onValueChange = {
                        moneyAmount = it
                    },
                    label = { Text("Enter Today's Expenditure") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                )
                TextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    label = { Text("Enter Name") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                )
                TextField(
                    value = itemS,
                    onValueChange = {
                        itemS = it
                    },
                    label = { Text("Enter Items") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                )
                val context = LocalContext.current
                Button(
                    onClick = {
                        if (!isValidInteger(moneyAmount)) {
                            Toast.makeText(context, "Enter Valid Amount", Toast.LENGTH_SHORT).show()
                        } else {
                            try {
                                val auth = FirebaseAuth.getInstance()
                                addshophistory(mymessId2, name, moneyAmount, itemS,dte)
                                updateTotalShop(mymessId2, moneyAmount)
                                navController.navigateUp()
                            } catch (e: Exception) {
                                Toast.makeText(context, "An error occurred.Try again", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    colors = ButtonDefaults.buttonColors(Color(0, 153, 76)),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("Add")
                }
            }
        }
    }
}

fun updateTotalShop(userEmail: String? = null, newTotalshop: String) {
    // Get previous shopping amount
    var totalshop = ""
    val db = FirebaseFirestore.getInstance()
    db.collection("homes").get().addOnSuccessListener { result ->
        for (document in result) {
            val ans = document.data
            if (ans["homId2"] == userEmail) {
                totalshop = ans["totalShopping"].toString()
                val usersCollection = db.collection("homes")
                usersCollection.whereEqualTo("homId2", userEmail)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val documentId = document.id
                            usersCollection.document(documentId)
                                .update("totalShopping", (totalshop.toInt() + newTotalshop.toInt()).toString())
                                .addOnSuccessListener {
                                }
                                .addOnFailureListener { e ->
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                    }
            }
        }
    }.addOnFailureListener { exception ->
    }

    // Update shopping of mess
    if(totalshop!="" && isValidInteger(totalshop)){

    }

}

fun addshophistory(
    messId: String? = null, name: String? = null,
    amount: String? = null,
    itemS: String? = null,
    dte:String? = null
) {
    val ref = FirebaseDatabase.getInstance().getReference("shopping_history")
    var person = Shop_His(messId, name, amount, itemS,dte)
    val key = ref.push().key
    key?.let {
        val refe = ref.child(it)
        refe.setValue(person)
    }
}
