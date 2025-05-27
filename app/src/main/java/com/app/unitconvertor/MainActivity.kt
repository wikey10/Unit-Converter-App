package com.app.unitconvertor

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import com.app.unitconvertor.ui.theme.UnitConvertorTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme = isSystemInDarkTheme()
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setStatusBarColor(
                    color = Color.Blue,
                    darkIcons = !isDarkTheme
                )
            }
            UnitConvertorTheme {
                Scaffold(

                    modifier = Modifier.fillMaxSize()) { innerPadding ->
                   UnitConvertor(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}



@Preview
@Composable
fun UnitConvertor(modifier: Modifier=Modifier){
    val context = LocalContext.current

    var inputValue by remember { mutableStateOf("") }
    var outputValue by remember { mutableStateOf("") }
    var inputUnit by remember { mutableStateOf("Meters") }
    var outputUnit by remember { mutableStateOf("Meters") }
    var isExpanded by remember { mutableStateOf(false) }
    var oExpanded by remember { mutableStateOf(false) }

    val conversionFactor = remember { mutableStateOf(1.00) }
    val oConversionFactor = remember { mutableStateOf(1.00) }

    var isError by remember { mutableStateOf(false) }


    fun convertUnit(){

        //? : - elvis operator (Modern if statement)

        val inputValueDouble = inputValue.toDoubleOrNull()?:0.0
        val result = (inputValueDouble * conversionFactor.value * 100.0 / oConversionFactor.value).roundToInt() /100.0
        outputValue = result.toString()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ) {
       Text("Unit Controller", modifier =   Modifier.align(alignment = Alignment.Start),
           style = MaterialTheme.typography.headlineLarge,
           fontFamily = FontFamily.Monospace
       )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = inputValue,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = isError,
            supportingText = {
                if(isError){
                    Text("Only numbers allowed", color = MaterialTheme.colorScheme.error)
                }
            },
            label = {
                Text("Enter",
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = FontFamily.Monospace)
            },
            onValueChange = {
                inputValue=it
                if(it.isDigitsOnly() || it.matches(Regex("^\\d*\\.?\\d*$"))){
                    isError = false
                    convertUnit()
                }
                else{
                    isError=true
                }
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            ) {
            Box(
                contentAlignment = Alignment.Center
            ){
                Button(onClick = {
                    isExpanded = !isExpanded
                }) {
                    Text(inputUnit,
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = FontFamily.Monospace
                        )
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown")
                }
                DropdownMenu(expanded = isExpanded, onDismissRequest = {
                    isExpanded=false
                }){
                    DropdownMenuItem(
                        text = { Text("Centi-Meters",
                            style = MaterialTheme.typography.labelMedium,
                            fontFamily = FontFamily.Monospace
                            )},
                        onClick = {
                            inputUnit = "Centi-Meters"
                            isExpanded=false
                            conversionFactor.value = 0.01
                            convertUnit()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Meters",
                            style = MaterialTheme.typography.labelMedium,
                            fontFamily = FontFamily.Monospace
                            )},
                        onClick = {
                            inputUnit = "Meters"
                            isExpanded=false
                            conversionFactor.value = 1.0
                            convertUnit()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Ft", style = MaterialTheme.typography.labelMedium,
                            fontFamily = FontFamily.Monospace)},
                        onClick = {
                            inputUnit = "Ft"
                            isExpanded=false
                            conversionFactor.value = 0.3048
                            convertUnit()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Milli-Meters",
                            style = MaterialTheme.typography.labelMedium,
                            fontFamily = FontFamily.Monospace
                            )},
                        onClick = {
                            inputUnit ="Milli-Meters"
                            isExpanded=false
                            conversionFactor.value = 0.01
                            convertUnit()
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(){
                Button(onClick = {
                    oExpanded = !oExpanded
                }) {
                    Text(outputUnit,style = MaterialTheme.typography.titleMedium,
                        fontFamily = FontFamily.Monospace)
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown")
                    DropdownMenu(expanded = oExpanded, onDismissRequest = {
                        oExpanded=false
                    }){
                        DropdownMenuItem(
                            text = { Text("Centi-Meters",style = MaterialTheme.typography.labelMedium,
                                fontFamily = FontFamily.Monospace)},
                            onClick = {
                                outputUnit = "Centi-Meters"
                                oExpanded=false
                                oConversionFactor.value = 0.01
                                convertUnit()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Meters",style = MaterialTheme.typography.labelMedium,
                                fontFamily = FontFamily.Monospace)},
                            onClick = {
                                outputUnit = "Meters"
                                oExpanded=false
                                oConversionFactor.value = 1.0
                                convertUnit()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Ft",style = MaterialTheme.typography.labelMedium,
                                fontFamily = FontFamily.Monospace)},
                            onClick = {
                                outputUnit = "Ft"
                                oExpanded=false
                                oConversionFactor.value = 0.3048
                                convertUnit()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Milli-Meters",style = MaterialTheme.typography.labelMedium,
                                fontFamily = FontFamily.Monospace)},
                            onClick = {
                                outputUnit = "Milli-Meters"
                                oExpanded=false
                                oConversionFactor.value = 0.001
                                convertUnit()
                            }
                        )
                    }

                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text("Result- $outputValue ${outputUnit}",modifier =   Modifier.align(alignment = Alignment.Start),
            style = MaterialTheme.typography.titleMedium,
            fontFamily = FontFamily.Monospace
            )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UnitConvertorTheme {
        UnitConvertor()
    }
}