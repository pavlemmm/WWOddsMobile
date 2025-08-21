package com.pavle.wwoddsmobile.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pavle.wwoddsmobile.ui.AuthViewModel

@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onAccountCreated: () -> Unit,
    vm: AuthViewModel = viewModel()
) {
    val s = vm.ui

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Create account", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = s.firstName, onValueChange = vm::onFirstName,
            label = { Text("First name") }, singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = s.lastName, onValueChange = vm::onLastName,
            label = { Text("Last name") }, singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = s.email, onValueChange = vm::onEmailChange,
            label = { Text("Email") }, singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = s.password, onValueChange = vm::onPasswordChange,
            label = { Text("Password") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = s.confirm, onValueChange = vm::onConfirmChange,
            label = { Text("Confirm password") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (s.error != null) {
            Spacer(Modifier.height(8.dp))
            Text(s.error, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(20.dp))
        Button(
            onClick = { vm.register(onSuccess = onAccountCreated) },
            enabled = !s.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (s.isLoading)
                CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
            else
                Text("Create account")
        }

        TextButton(onClick = onBack) { Text("Back to sign in") }
    }
}

@Composable
private fun RegionRow(label: String, code: String, checked: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("$label ($code)")
        Checkbox(checked = checked, onCheckedChange = { onToggle() })
    }
}
