package com.roman.ac602

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roman.ac602.ui.theme.AC602Theme
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AC602Theme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Content()
                }
            }
        }
    }
}
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Content() {
    var showDialog by remember { mutableStateOf(true) }
    var texto by remember { mutableStateOf("") }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // AlertDialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = {
                Text("Contenido")
            },
            text = {
                Text(loadFileContent(context))
            },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text("OK")
                    }
                }
            })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = texto,
            onValueChange = { texto = it },
            label = { Text("Texto") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (texto.isNotBlank()) {
                    saveToFile(context, texto)
                    keyboardController?.hide()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }
}
// Function to load file content
fun loadFileContent(context: Context): String {
    return try {
        val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDirectory, "archivo.txt")
        file.readText()
    } catch (e: Exception) {
        showToast(context, "Error al cargar el contenido del archivo")
        ""
    }
}
fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun saveToFile(context:Context, text: String) {
    try {
        val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDirectory, "archivo.txt")
        // Abrir el archivo en modo de anexado
        val escritorArchivo = FileWriter(file, true)
        val escritorBufferizado = BufferedWriter(escritorArchivo)
        // Escribir el nuevo texto y agregar un salto de línea
        escritorBufferizado.write(text)
        escritorBufferizado.newLine()
        // Cerrar el escritor
        escritorBufferizado.close()
        showToast(context, "Texto guardado correctamente")
    } catch (e: Exception) {
        showToast(context, "Error al guardar el texto")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainActivity() {
    AC602Theme {
        Content()
    }
}
