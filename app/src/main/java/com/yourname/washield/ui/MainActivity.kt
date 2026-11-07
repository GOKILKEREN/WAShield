package com.yourname.washield.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yourname.washield.data.db.AppDatabase
import com.yourname.washield.data.db.ThreatLogEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = AppDatabase.get(this).threatLogDao()
        setContent {
            MaterialTheme {
                var page by remember { mutableStateOf("Dashboard") }
                val logs by dao.getAll().collectAsState(initial = emptyList())
                Scaffold(topBar = {
                    TopAppBar(title = { Text("WaShield · $page") })
                }) { padding ->
                    Column(Modifier.padding(padding).padding(16.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Dashboard", "Logs", "Settings", "About").forEach {
                                TextButton(onClick = { page = it }) { Text(it) }
                            }
                        }
                        when (page) {
                            "Dashboard" -> Dashboard(logs, onClear = {
                                kotlinx.coroutines.GlobalScope.launch { dao.deleteAll() }
                            })
                            "Logs" -> LogList(logs)
                            "Settings" -> SettingsPage()
                            else -> AboutPage()
                        }
                    }
                }
            }
        }
    }
}

@Composable private fun Dashboard(logs: List<ThreatLogEntity>, onClear: () -> Unit) {
    val today = logs.count { it.timestamp >= System.currentTimeMillis() - 24L * 60 * 60 * 1000 }
    Text("Status: modul menunggu proses WhatsApp dari ShizukuPosed")
    Spacer(Modifier.height(12.dp))
    Text("Log tercatat: ${logs.size}")
    Text("24 jam terakhir: $today")
    Text("CRITICAL: ${logs.count { it.severity == "CRITICAL" }}")
    Text("HIGH: ${logs.count { it.severity == "HIGH" }}")
    Spacer(Modifier.height(12.dp))
    Button(onClick = onClear) { Text("Bersihkan Log") }
    Spacer(Modifier.height(12.dp))
    Text("20 log terbaru", style = MaterialTheme.typography.titleMedium)
    LogList(logs.take(20))
}

@Composable private fun LogList(logs: List<ThreatLogEntity>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(logs, key = { it.id }) { log ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("${log.severity} · ${log.ruleTriggered}", style = MaterialTheme.typography.titleSmall)
                    Text(log.packageName)
                    Text(log.preview)
                    Text(log.actionTaken, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable private fun SettingsPage() {
    val rules = listOf(
        "text_length", "zero_width", "rtl_override", "null_byte", "control_chars",
        "emoji_flood", "base64_blob", "vcard_suspicious", "unicode_private_use",
        "repeat_char", "long_url", "payload_size"
    )
    var enabled by remember { mutableStateOf(setOf("zero_width", "rtl_override", "null_byte", "base64_blob", "vcard_suspicious", "payload_size")) }
    Text("Deteksi aktif (pengaturan UI lokal; sinkronisasi hook menyusul)")
    LazyColumn {
        items(rules) { rule ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(rule)
                Switch(checked = rule in enabled, onCheckedChange = {
                    enabled = if (it) enabled + rule else enabled - rule
                })
            }
        }
    }
}

@Composable private fun AboutPage() {
    Text("WaShield 0.1.0")
    Text("Modul defensif untuk deteksi pola payload mencurigakan.")
    Text("Tidak menjamin deteksi semua ancaman atau kompatibilitas semua versi WhatsApp.")
    Text("Buka WhatsApp melalui ShizukuPosed agar hook dapat dimuat.")
}
