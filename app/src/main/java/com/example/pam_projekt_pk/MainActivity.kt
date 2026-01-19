package com.example.pam_projekt_pk

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material.icons.filled.ArrowBack

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getDatabase(this)

        setContent {

            MaterialTheme(
                colorScheme = MaterialTheme.colorScheme.copy(
                    primary = Color(0xFF6200EE),
                    secondary = Color(0xFF03DAC5)
                )
            ) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "calculator") {
                        composable("calculator") { CalculatorScreen(navController, db) }
                        composable("history") { HistoryScreen(navController, db) }
                    }
                }
            }
        }
    }
}


fun analyzeBmi(bmi: Float, context: Context): Pair<String, Color> {
    return when {
        bmi < 18.5 -> context.getString(R.string.bmi_underweight) to Color(0xFF2196F3)
        bmi < 25.0 -> context.getString(R.string.bmi_normal) to Color(0xFF4CAF50)
        bmi < 30.0 -> context.getString(R.string.bmi_overweight) to Color(0xFFFF9800)
        else -> context.getString(R.string.bmi_obesity) to Color(0xFFF44336)
    }
}


@Composable
fun CalculatorScreen(navController: NavController, db: AppDatabase) {
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))


        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text(stringResource(id = R.string.weight_hint)) },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )

                Spacer(modifier = Modifier.height(16.dp))


                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text(stringResource(id = R.string.height_hint)) },
                    leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))


        Button(
            onClick = {
                val w = weight.replace(",", ".").toFloatOrNull()
                val h = height.replace(",", ".").toFloatOrNull()

                if (w != null && h != null && h > 0) {
                    val hInMeters = h / 100
                    val bmi = w / (hInMeters * hInMeters)

                    val (category, _) = analyzeBmi(bmi, context)

                    val formattedBmi = "%.2f".format(bmi)
                    val fullResult = "$formattedBmi\n($category)"

                    val date = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date())

                    scope.launch {
                        db.bmiDao().insert(BmiRecord(
                            weight = weight,
                            height = height,
                            bmiResult = fullResult,
                            date = date
                        ))
                    }


                    Toast.makeText(context, "BMI: $formattedBmi - $category", Toast.LENGTH_LONG).show()

                    weight = ""
                    height = ""
                } else {
                    Toast.makeText(context, "Błąd! Sprawdź dane.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Text(stringResource(id = R.string.calculate_btn), fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedButton(
            onClick = { navController.navigate("history") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.history_btn))
        }
    }
}

@Composable
fun HistoryScreen(navController: NavController, db: AppDatabase) {
    val historyList by db.bmiDao().getAllHistory().collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = stringResource(id = R.string.history_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }


        if (historyList.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(id = R.string.history_empty), color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(historyList) { record ->

                    val color = when {
                        record.bmiResult.contains("NIEDOWAGA") || record.bmiResult.contains("Underweight") -> Color(0xFF2196F3)
                        record.bmiResult.contains("PRAWIDŁOWA") || record.bmiResult.contains("Normal") -> Color(0xFF4CAF50)
                        record.bmiResult.contains("NADWAGA") || record.bmiResult.contains("Overweight") -> Color(0xFFFF9800)
                        else -> Color(0xFFF44336)
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = record.date, style = MaterialTheme.typography.labelSmall)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Waga: ${record.weight} kg | Wzrost: ${record.height} cm", style = MaterialTheme.typography.bodyMedium)
                            }

                            Text(
                                text = record.bmiResult,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = color,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))


        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {

            Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(id = R.string.back_btn))
        }
    }
}